package slieb.closureutils.javascript;

import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.dependencies.DependencyTree;

import java.util.Set;

public class JavascriptDependencyTree extends DependencyTree {

    private DependencyNode baseNode;

    public JavascriptDependencyTree(Set<DependencyNode> dependencyNodes) {
        super(dependencyNodes);
    }

    public DependencyNode getBaseNode() {
        return baseNode;
    }
}
