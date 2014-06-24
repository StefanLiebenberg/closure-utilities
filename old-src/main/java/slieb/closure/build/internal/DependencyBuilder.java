package slieb.closure.build.internal;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import slieb.closure.internal.DependencyCalculator;
import slieb.closure.internal.DependencyOptions;

import javax.annotation.Nonnull;

public class DependencyBuilder<A extends SourceFileBase>
        extends AbstractBuilder<DependencyOptions<A>, ImmutableList<A>> {

    private static final Boolean STRICT_MODE = true;

    private static final String UNSPECIFIED_ENTRY_POINTS =
            "No entry points specified";

    private static final String UNSPECIFIED_SOURCE_FILES =
            "No source files specified";

    @Override
    public void checkOptions(@Nonnull DependencyOptions<A> options)
            throws BuildException {
        if (STRICT_MODE) {

            ImmutableList<String> entryPoints = options.getEntryPoints();
            ImmutableList<A> entryFiles = options.getEntryFiles();
            if ((entryPoints == null || entryPoints.isEmpty()) && (
                    entryFiles == null || entryFiles.isEmpty())) {
                throw new BuildException(UNSPECIFIED_ENTRY_POINTS);
            }

            ImmutableCollection<A> sourceFiles = options.getSourceFiles();
            if (sourceFiles == null || sourceFiles.isEmpty()) {
                throw new BuildException(UNSPECIFIED_SOURCE_FILES);
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
        final ImmutableList<A> entryFiles = buildOptions.getEntryFiles();
        if (sourceFiles != null) {
            DependencyCalculator<A> dependencyCalculator =
                    new DependencyCalculator<>(sourceFiles);
            listBuilder.addAll(dependencyCalculator.getDependencyList
                    (entryFiles, entryPoints));
        }
        return listBuilder.build();
    }
}
