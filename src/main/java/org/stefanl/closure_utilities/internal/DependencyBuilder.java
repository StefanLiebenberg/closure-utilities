package org.stefanl.closure_utilities.internal;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class DependencyBuilder<A extends BaseSourceFile>
        extends AbstractBuilder<DependencyOptions<A>, ImmutableList<A>> {

    private static final Boolean STRICT_MODE = true;

    private static final String UNSPECIFIED_ENTRY_POINTS =
            "No entry points specified";

    private static final String UNSPECIFIED_SOURCE_FILES =
            "No source files specified";

    @Override
    public void checkOptions(@Nonnull DependencyOptions<A> options)
            throws BuildOptionsException {
        if (STRICT_MODE) {
            ImmutableList<String> entryPoints = options.getEntryPoints();
            if (entryPoints == null || entryPoints.isEmpty()) {
                throw new BuildOptionsException(UNSPECIFIED_ENTRY_POINTS);
            }
            ImmutableCollection<A> sourceFiles = options.getSourceFiles();
            if (sourceFiles == null || sourceFiles.isEmpty()) {
                throw new BuildOptionsException(UNSPECIFIED_SOURCE_FILES);
            }
        }
    }

    @Override
    @Nonnull
    public ImmutableList<A> buildInternal(
            @Nonnull final DependencyOptions<A> buildOptions)
            throws Exception {
        ImmutableList.Builder<A> listBuilder = new ImmutableList.Builder<>();
        final ImmutableSet<A> sourceFiles = buildOptions.getSourceFiles();
        final ImmutableList<String> entryPoints = buildOptions.getEntryPoints();
        if (sourceFiles != null && entryPoints != null) {
            DependencyCalculator<A> dependencyCalculator =
                    new DependencyCalculator<>(sourceFiles);
            listBuilder.addAll(dependencyCalculator.getDependencyList
                    (entryPoints));
        }
        return listBuilder.build();
    }
}
