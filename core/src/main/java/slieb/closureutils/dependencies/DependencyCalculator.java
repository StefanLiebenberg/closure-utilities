package slieb.closureutils.dependencies;


import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;

public class DependencyCalculator {

    public List<DependencyNode> resolve(DependencyTree tree, DependencyNode provider, List<DependencyNode> dependencies, Collection<DependencyNode> parents) {
        if (!dependencies.contains(provider)) {
            parents.add(provider);
            for (String namespace : provider.getRequires()) {
                resolve(tree, namespace, dependencies, parents);
            }
            parents.remove(provider);
            dependencies.add(provider);
        }
        return dependencies;
    }


    public List<DependencyNode> resolve(DependencyTree tree, String namespace, List<DependencyNode> dependencies,
                                        Collection<DependencyNode> parents) {
        final DependencyNode provider = tree.getProviderOf(namespace);
        checkState(provider != null, format("Nothing provides %s", namespace));
        checkState(!parents.contains(provider), format("Circular Dependency at %s", namespace));
        return resolve(tree, provider, dependencies, parents);
    }

}
