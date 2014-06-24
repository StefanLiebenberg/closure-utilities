package slieb.closureutils.dependencies;


import java.util.Set;

public class DependencyTree {

    private final Set<DependencyNode> dependencyNodes;

    public DependencyTree(Set<DependencyNode> dependencyNodes) {
        this.dependencyNodes = dependencyNodes;
    }

    public DependencyNode getProviderOf(String namespace) {
        for (DependencyNode node : dependencyNodes) {
            if (node.getProvides().contains(namespace)) {
                return node;
            }
        }
        return null;
    }
}
