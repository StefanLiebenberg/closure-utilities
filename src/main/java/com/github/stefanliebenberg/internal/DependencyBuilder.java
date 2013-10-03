package com.github.stefanliebenberg.internal;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;

public class DependencyBuilder<A extends BaseSourceFile>
        extends AbstractBuilder<DependencyBuildOptions<A>>
        implements IBuilder {

    private DependencyCalculator<A> dependencyCalculator;

    private List<A> resolvedSourceFiles;

    @Override
    public void build() throws BuildException {
        try {
            final ImmutableSet<A> sourceFiles = buildOptions.getSourceFiles();
            final ImmutableList<String> entryPoints =
                    buildOptions.getEntryPoints();
            dependencyCalculator = new DependencyCalculator<A>(sourceFiles);
            resolvedSourceFiles =
                    dependencyCalculator.getDependencyList(entryPoints);
        } catch (DependencyException dependencyException) {
            throwBuildException(dependencyException);
        }
    }

    @Override
    public void reset() {
        super.reset();
        dependencyCalculator = null;
        resolvedSourceFiles = null;
    }

    public DependencyCalculator getDependencyCalculator() {
        return dependencyCalculator;
    }

    public List<A> getResolvedSourceFiles() {
        return resolvedSourceFiles;
    }
}
