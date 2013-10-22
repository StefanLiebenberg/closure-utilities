package org.stefanl.closure_utilities.internal;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public class DependencyBuilder<A extends BaseSourceFile>
        extends AbstractBuilder<DependencyBuildOptions<A>>
        implements BuilderInterface {

    private DependencyCalculator<A> dependencyCalculator;

    private List<A> resolvedSourceFiles;

    private final Function<A, File> SOURCE_TO_FILE =
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

    @Override
    public void buildInternal() throws Exception {
        final ImmutableSet<A> sourceFiles = buildOptions.getSourceFiles();
        final ImmutableList<String> entryPoints = buildOptions.getEntryPoints();
        if (sourceFiles != null && entryPoints != null) {
            dependencyCalculator = new DependencyCalculator<A>(sourceFiles);
            resolvedSourceFiles =
                    dependencyCalculator.getDependencyList(entryPoints);
        }
    }

    @Override
    public void reset() {
        super.reset();
        dependencyCalculator = null;
        resolvedSourceFiles = null;
    }

    @Nullable
    public DependencyCalculator getDependencyCalculator() {
        return dependencyCalculator;
    }

    @Nullable
    public List<A> getResolvedSourceFiles() {
        return resolvedSourceFiles;
    }

    @Nullable
    public List<File> getResolvedFiles() {
        if (resolvedSourceFiles != null) {
            return Lists.transform(resolvedSourceFiles, SOURCE_TO_FILE);
        } else {
            return null;
        }
    }
}
