package org.stefanl.closure_utilities.internal;


public interface IBuilder {

    public void checkOptions() throws InvalidBuildOptionsException;

    public void build() throws BuildException;

    public void reset();
}

