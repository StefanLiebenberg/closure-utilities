package liebenberg.closure_utilities.internal;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DependencyCalculator<S extends SourceFileBase>
        implements IDependencyCalculator<S> {

    public DependencyCalculator(
            @Nonnull final Collection<S> dependencies) {
        this.dependencies = ImmutableSet.copyOf(dependencies);
    }

    public DependencyCalculator(
            @Nonnull final ImmutableCollection<S> dependencies) {
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

    @Nonnull
    private DependencyException nothingProvides(
            @Nonnull final String namespace) {
        return new DependencyException("Nothing provides '" + namespace + "'");
    }

    @Nonnull
    private DependencyException circularError(
            @Nonnull final String namespace,
            @Nonnull final Collection<S> parents) {
        final Collection<String> strings =
                Collections2.transform(parents, BASE_TRANSFORMER);
        final String[] parentNames =
                strings.toArray(new String[strings.size()]);
        final StringBuilder message = new StringBuilder();
        message.append("Circular Error detected while trying to import '")
                .append(namespace).append("'.");
        for (String parent : parentNames) {
            message.append("\n   ").append(parent);
        }
        message.append("\n   ").append(namespace);
        return new DependencyException(message.toString());
    }

    private ImmutableCollection<S> dependencies;

    @Nullable
    private S getProviderOf(
            @Nonnull final String namespace) {
        for (S dependency : dependencies) {
            if (dependency.getProvidedNamespaces().contains(namespace)) {
                return dependency;
            }
        }
        return null;
    }

    @Nonnull
    private List<S> resolveDependencies(
            @Nonnull final S provider,
            @Nonnull final List<S> dependencies,
            @Nonnull final Collection<S> parents)
            throws DependencyException {

        if (!dependencies.contains(provider)) {
            parents.add(provider);
            for (String requireStatement : provider.getRequiredNamespaces()) {
                resolveDependencies(requireStatement, dependencies, parents);
            }
            parents.remove(provider);
            dependencies.add(provider);
        }

        return dependencies;
    }


    @Nonnull
    private List<S> resolveDependencies(@Nonnull final String namespace,
                                        @Nonnull final List<S> dependencies,
                                        @Nonnull final Collection<S> parents)
            throws DependencyException {
        final S provider = getProviderOf(namespace);

        if (provider == null) {
            throw nothingProvides(namespace);
        }

        if (parents.contains(provider)) {
            throw circularError(namespace, parents);
        }

        return resolveDependencies(provider, dependencies, parents);
    }

    private void addDependencies(
            @Nonnull final String entryPoint,
            @Nonnull final List<S> nodes)
            throws DependencyException {
        for (S node : resolveDependencies(entryPoint, nodes,
                new HashSet<S>())) {
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }
    }


    private void addDependencies(@Nonnull final S entryFile,
                                 @Nonnull final List<S> nodes)
            throws DependencyException {
        for (S node : resolveDependencies(entryFile, nodes, new HashSet<S>())) {
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
        }
    }

    public List<S> getDependencyList(@Nonnull final List<String> entryPoints)
            throws DependencyException {
        final List<S> nodes = new ArrayList<S>();
        for (String entryPoint : entryPoints) {
            addDependencies(entryPoint, nodes);
        }
        return nodes;
    }

    public List<S> getDependencyList(@Nonnull final String entryPoint)
            throws DependencyException {
        final List<S> nodes = new ArrayList<S>();
        addDependencies(entryPoint, nodes);
        return nodes;
    }

    public List<S> getDependencyList(
            @Nullable final List<S> entryFiles,
            @Nullable final List<String> entryPoints)
            throws DependencyException {
        final List<S> nodes = new ArrayList<S>();
        if (entryFiles != null) {
            for (S entryFile : entryFiles) {
                addDependencies(entryFile, nodes);
            }
        }
        if (entryPoints != null) {
            for (String entryPoint : entryPoints) {
                addDependencies(entryPoint, nodes);
            }
        }
        return nodes;
    }
}
