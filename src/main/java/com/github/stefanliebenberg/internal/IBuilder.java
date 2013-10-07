package com.github.stefanliebenberg.internal;


public interface IBuilder {

    public void checkOptions() throws BuildException;

    public void build() throws BuildException;

    public void reset();
}

