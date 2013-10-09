package com.github.stefanliebenberg.internal;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.management.InstanceAlreadyExistsException;

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


    @Override
    public abstract void build()
            throws BuildException;

    @Override
    public void reset() {
        buildOptions = null;
    }

    protected static void throwBuildException(@Nonnull final String message)
            throws BuildException {
        throw new BuildException(message);
    }

    protected static void throwBuildException(@Nonnull final String message,
                                              @Nullable final Throwable e)
            throws BuildException {
        throw new BuildException(message, e);
    }

    protected static void throwBuildException(
            @Nonnull final Throwable throwable)
            throws BuildException {
        throwBuildException("Build Failed", throwable);
    }


    protected static void checkNotNull(
            @Nullable final Object object,
            @Nonnull final String message)
            throws BuildException {
        if (object == null) {
            throwBuildException(message, new NullPointerException());
        }
    }

    protected static void checkNull(
            @Nullable final Object object,
            @Nonnull final String message) throws BuildException {
        if (object != null) {
            throwBuildException(message);
        }
    }


    protected static void checkNotNull(@Nullable final Object object)
            throws BuildException {
        checkNotNull(object, "Object should not be null");
    }

    protected static void checkNull(@Nullable final Object object)
            throws BuildException {
        checkNull(object, "Object should be null");
    }

    @Override
    public void checkOptions()
            throws BuildException {
        checkNotNull(buildOptions, "Build options should not be null");
    }

}

