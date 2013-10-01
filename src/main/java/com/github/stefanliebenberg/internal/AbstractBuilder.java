package com.github.stefanliebenberg.internal;


public abstract class AbstractBuilder
        implements IBuilder {

    public AbstractBuilder() {}

    protected static void throwBuildException(final String message)
            throws BuildException {
        throw new BuildException(message);
    }

    protected static void throwBuildException(final String message,
                                              final Throwable e)
            throws BuildException {
        throw new BuildException(message, e);
    }

    @Override
    public abstract void build()
            throws BuildException;

    @Override
    public abstract void reset();
}

