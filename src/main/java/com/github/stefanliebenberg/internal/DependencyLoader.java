package com.github.stefanliebenberg.internal;


import com.github.stefanliebenberg.utilities.FsTool;
import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class DependencyLoader<A extends BaseSourceFile> {

    private final IDependencyParser<A> dependencyParser;

    private final IDependencyCalculator<A> dependencyCalculator;

    private final Collection<A> dependencies;

    public DependencyLoader(
            @Nonnull final IDependencyParser<A> depParser,
            @Nonnull final Collection<File> fileDependencies)
            throws IllegalAccessException, InstantiationException, IOException {
        dependencyParser = depParser;
        dependencies = new HashSet<A>();
        for (File file : fileDependencies) {
            A dep = createDependency(file);
            depParser.parse(dep, FsTool.read(file));
            dependencies.add(dep);
        }

        dependencyCalculator = new DependencyCalculator<A>(dependencies);
    }

    protected abstract A createDependency(File input);

    private final Function<A, File> SRC_TO_FILE =
            new Function<A, File>() {
                @Nullable
                @Override
                public File apply(@Nullable A input) {
                    if (input != null) {
                        return new File(input.getSourceLocation());
                    } else {
                        return null;
                    }
                }
            };


    @Nonnull
    public ImmutableList<File> getDependenciesFor(
            @Nonnull final List<String> entryPoints)
            throws DependencyException {
        return Immuter.list(dependencyCalculator.getDependencyList
                (entryPoints), SRC_TO_FILE);
    }

    @Nonnull
    public ImmutableList<File> getDependenciesFor(
            @Nonnull final String entryPoint)
            throws DependencyException {
        return Immuter.list(dependencyCalculator.getDependencyList
                (entryPoint), SRC_TO_FILE);
    }

}
