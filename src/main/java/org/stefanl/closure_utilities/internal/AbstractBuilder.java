package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractBuilder<A>
        implements IBuilder {

    public AbstractBuilder() {}

    protected A buildOptions;

    public AbstractBuilder(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    public void setBuildOptions(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    protected abstract void buildInternal() throws Exception;

    @Override
    public void build() throws BuildException {
        checkOptions();
        try {
            buildInternal();
        } catch (Exception exception) {

            if (exception instanceof BuildException) {
                // doing this creates a somewhat cleaner
                // stack trace to debug.
                throw (BuildException) exception;
            }

            final String message = "An exception occurred during build";
            throw new BuildException(message, exception);
        }
    }

    @Override
    public void reset() {
        buildOptions = null;
    }

    protected static BuildException buildException(
            @Nonnull final String message) {
        return new BuildException(message);
    }

    protected static BuildException buildException(
            @Nonnull final String message,
            @Nullable final Throwable e) {
        return new BuildException(message, e);
    }

    protected static BuildException buildException(
            @Nonnull final Throwable throwable) {
        return buildException("Build Failed", throwable);
    }

    protected static void checkNonEmptyCollection(
            @Nullable final Collection collection,
            @Nonnull final String message) throws BuildException {
        if (collection == null || collection.isEmpty()) {
            throw buildException(message);
        }
    }

    protected static void checkNonEmptyCollection(
            @Nullable final Collection collection) throws BuildException {
        checkNonEmptyCollection(collection, "Collection cannot be null or " +
                "empty.");
    }


    protected static void checkNotNull(
            @Nullable final Object object,
            @Nonnull final String message)
            throws BuildException {
        if (object == null) {
            throw buildException(message, new NullPointerException());
        }
    }

    protected static void checkNull(
            @Nullable final Object object,
            @Nonnull final String message) throws BuildException {
        if (object != null) {
            throw buildException(message);
        }
    }

    private final String BUILD_OPTIONS_ERROR =
            "Build options have not been set.";

    @Override
    public void checkOptions()
            throws InvalidBuildOptionsException {
        if (buildOptions == null) {
            throw new InvalidBuildOptionsException(
                    BUILD_OPTIONS_ERROR,
                    new NullPointerException());
        }
    }

}

