package slieb.closureutils.dependencies;


import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;

public class DependencyTree {

    private final ImmutableSet<DependencyNode> dependencyNodes;

    public DependencyTree(Set<DependencyNode> dependencyNodes) {
        this.dependencyNodes = ImmutableSet.copyOf(dependencyNodes);
    }

    public DependencyNode getProviderOf(String namespace) {
        for (DependencyNode node : dependencyNodes) {
            if (node.getProvides().contains(namespace)) {
                return node;
            }
        }
        return null;
    }

    public Collection<DependencyNode> getDependencyNodes() {
        return dependencyNodes;
    }
}
