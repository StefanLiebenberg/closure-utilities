package slieb.closure.internal;


import slieb.closure.build.internal.SourceFileBase;

import java.util.Collection;

public class DependencyExceptionHandler {

    private static final String NOTHING_PROVIDES_FORMAT =
            "Nothing provides %s.";

    public DependencyException nothingProvides(final String requiredClass) {
        return new DependencyException(
                String.format(NOTHING_PROVIDES_FORMAT, requiredClass));
    }

    private static final String CIRCULAR_DEPENDENCIES_FORMAT =
            "Circular Error detected while trying to import %s. " +
                    "The parents are:%s";

    public DependencyException
    circularDependencies(final String requiredClass,
                         final Collection<? extends SourceFileBase>
                                 parents) {

        StringBuilder parentStack = new StringBuilder();
        for (SourceFileBase sourceFileBase : parents) {
            parentStack
                    .append("\n  ")
                    .append(sourceFileBase.getSourceLocation());
        }

        return new DependencyException(
                String.format(CIRCULAR_DEPENDENCIES_FORMAT, requiredClass,
                        parentStack));
    }

}
