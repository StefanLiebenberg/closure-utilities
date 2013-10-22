package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;

public abstract class AbstractBuilder<A>
        implements BuilderInterface {


    protected A buildOptions;

    public AbstractBuilder() {}

    public AbstractBuilder(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    public void setBuildOptions(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    protected abstract void buildInternal() throws Exception;

    public static final String BUILD_EXCEPTION_MESSAGE =
            "An exception occured during the build.";

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
            throw new BuildException(BUILD_EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public void reset() {
        buildOptions = null;
    }


    private static final String BUILD_OPTIONS_ERROR =
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

