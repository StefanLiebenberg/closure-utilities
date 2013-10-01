package com.github.stefanliebenberg.internal;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DependencyCalculator<S extends BaseSourceFile>
        implements IDependencyCalculator<S> {

    public DependencyCalculator(final Collection<S> dependencies) {
        this.dependencies = ImmutableSet.copyOf(dependencies);
    }

    public DependencyCalculator(
            final ImmutableCollection<S> dependencies) {
        this.dependencies = dependencies;
    }


    private final Function<S, String> BASE_TRANSFORMER =
            new Function<S, String>() {
                @Nullable
                @Override
                public String apply(@Nullable final S sourceFile) {
                    if (sourceFile != null) {
                        return sourceFile.getSourceLocation().toString();
                    } else {
                        return null;
                    }
                }
            };

    private void throwNothingProvides(final String namespace)
            throws DependencyException {
        DependencyException.throwNothingProvides(namespace);
    }

    private void throwCircularError(final String namespace,
                                    final S provider,
                                    final Collection<S> parents)
            throws DependencyException {
        final String providerName =
                BASE_TRANSFORMER.apply(provider);
        final Collection<String> strings = Collections2.transform(parents,
                BASE_TRANSFORMER);
        final String[] parentNames =
                strings.toArray(new String[strings.size()]);
        DependencyException.
                throwCircularError(namespace, providerName, parentNames);
    }


    private ImmutableCollection<S> dependencies;

    private S getProviderOf(final String namespace) {
        for (S dependency : dependencies) {
            if (dependency.getProvidedNamespaces().contains(namespace)) {
                return dependency;
            }
        }
        return null;
    }

    private List<S> resolveDependencies(final String namespace,
                                        final List<S> dependencies,
                                        final Collection<S> parents)
            throws DependencyException {
        final S provider = getProviderOf(namespace);

        if (provider == null) {
            throwNothingProvides(namespace);
            return null;
        }

        if (parents.contains(provider)) {
            throwCircularError(namespace, provider, parents);
        }

        if (!dependencies.contains(provider)) {
            parents.add(provider);
            for (String requireStatement : provider.getRequiredNamespaces()) {
                resolveDependencies(requireStatement, dependencies,
                        parents);
            }
            parents.remove(provider);
            dependencies.add(provider);
        }
        return dependencies;
    }

    private void addDependencies(final String entryPoint,
                                 final List<S> nodes)
            throws DependencyException {
        for (S node : resolveDependencies(entryPoint, nodes,
                new HashSet<S>())) {
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }
    }

    public List<S> getDependencyList(final List<String> entryPoints)
            throws DependencyException {
        final List<S> nodes = new ArrayList<S>();
        for (String entryPoint : entryPoints) {
            addDependencies(entryPoint, nodes);
        }
        return nodes;
    }

    public List<S> getDependencyList(final String entryPoint)
            throws DependencyException {
        final List<S> nodes = new ArrayList<S>();
        addDependencies(entryPoint, nodes);
        return nodes;
    }
}
