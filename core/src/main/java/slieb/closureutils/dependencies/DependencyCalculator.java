package slieb.closureutils.dependencies;


import javax.annotation.Nonnull;
import java.util.List;

import static slieb.closureutils.dependencies.DependencyExceptionHandler.circularDependency;
import static slieb.closureutils.dependencies.DependencyExceptionHandler.nothingProvides;

public final class DependencyCalculator {

    @Nonnull
    public static List<DependencyNode> resolveDependencies(
            @Nonnull DependencyTree tree,
            @Nonnull String namespace,
            @Nonnull List<DependencyNode> dependencies,
            @Nonnull List<DependencyNode> parents) {
        final DependencyNode provider = tree.getProviderOf(namespace);
        if (provider == null) {
            throw nothingProvides(namespace, parents);
        }
        if (parents.contains(provider)) {
            throw circularDependency(namespace, provider, parents);
        }
        if (!dependencies.contains(provider)) {
            parents.add(provider);
            for (String requireNamespace : provider.getRequires()) {
                resolveDependencies(tree, requireNamespace, dependencies, parents);
            }
            parents.remove(provider);
            dependencies.add(provider);
        }
        return dependencies;
    }

}
