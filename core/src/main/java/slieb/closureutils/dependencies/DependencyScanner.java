package slieb.closureutils.dependencies;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class DependencyScanner {

    private final ResourceProvider resourceProvider;

    private final DependencyParser dependencyParser;

    private final DependencyCalculator dependencyCalculator;

    public DependencyScanner(ResourceProvider resourceProvider, DependencyParser dependencyParser, DependencyCalculator dependencyCalculator) {
        this.resourceProvider = resourceProvider;
        this.dependencyParser = dependencyParser;
        this.dependencyCalculator = dependencyCalculator;
    }

    public List<DependencyNode> getDependencies(String entryPoint) {
        return getDependencies(newArrayList(entryPoint));
    }

    public DependencyTree getDependencyTree() {
        ImmutableSet.Builder<DependencyNode> setBuilder = new ImmutableSet.Builder<>();
        for (Resource resource : resourceProvider.getResources()) {
            setBuilder.add(dependencyParser.parse(resource));
        }
        return new DependencyTree(setBuilder.build());
    }

    public ImmutableList<DependencyNode> getDependencies(List<String> entryPoints) {
        DependencyTree tree = getDependencyTree();
        ArrayList<DependencyNode> dependenciesList = new ArrayList<>();
        for (String namespace : entryPoints) {
            dependencyCalculator.resolve(tree, namespace, dependenciesList, Sets.<DependencyNode>newHashSet());
        }
        return ImmutableList.copyOf(dependenciesList);
    }
}
