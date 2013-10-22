package org.stefanl.closure_utilities.internal;


public interface BuilderInterface {

    public void checkOptions() throws InvalidBuildOptionsException;

    public void build() throws BuildException;

    public void reset();
}

