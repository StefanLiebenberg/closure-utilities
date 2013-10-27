package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;

public interface BuilderInterface<A> {

    public void checkOptions() throws BuildOptionsException;

    public void build() throws BuildException;

    public void reset();
}

