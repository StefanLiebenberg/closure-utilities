package slieb.closureutils.dependencies;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static slieb.closureutils.dependencies.DependencyCalculator
        .resolveDependencies;

public class DependencyScanner {

    private final ResourceProvider resourceProvider;

    private final DependencyParser dependencyParser;

    public DependencyScanner(ResourceProvider resourceProvider,
                             DependencyParser dependencyParser) {
        this.resourceProvider = checkNotNull(resourceProvider,
                "Resource provider is null");
        this.dependencyParser = checkNotNull(dependencyParser,
                "Dependency parser is null");
    }

    public List<DependencyNode> getDependencies(String entryPoint) {
        return getDependencies(newArrayList(entryPoint));
    }

    public DependencyTree getDependencyTree() {
        ImmutableSet.Builder<DependencyNode> setBuilder = new ImmutableSet
                .Builder<>();
        for (Resource resource : checkNotNull(resourceProvider.getResources()
        )) {
            setBuilder.add(dependencyParser.parse(resource));
        }
        return new DependencyTree(setBuilder.build());
    }

    public ImmutableList<DependencyNode> getDependencies(List<String>
                                                                 entryPoints) {
        DependencyTree tree = getDependencyTree();
        List<DependencyNode> dependenciesList = newArrayList();
        for (String namespace : entryPoints) {
            List<DependencyNode> parentsList = newArrayList();
            resolveDependencies(tree, namespace, dependenciesList,
                    parentsList);
        }
        return ImmutableList.copyOf(dependenciesList);
    }
}
