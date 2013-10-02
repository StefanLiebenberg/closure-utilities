package com.github.stefanliebenberg.internal;


public abstract class AbstractBuilder<A>
        implements IBuilder {

    public AbstractBuilder() {}

    protected A buildOptions;

    public AbstractBuilder(final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    public void setBuildOptions(final A buildOptions) {
        this.buildOptions = buildOptions;
    }


    @Override
    public abstract void build()
            throws BuildException;

    @Override
    public void reset() {
        buildOptions = null;
    }

    protected static void throwBuildException(final String message)
            throws BuildException {
        throw new BuildException(message);
    }

    protected static void throwBuildException(final String message,
                                              final Throwable e)
            throws BuildException {
        throw new BuildException(message, e);
    }

    protected static void throwBuildException(final Throwable throwable)
            throws BuildException {
        throwBuildException("Build Failed", throwable);
    }

}

