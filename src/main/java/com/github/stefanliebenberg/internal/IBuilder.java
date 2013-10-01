package com.github.stefanliebenberg.internal;


public interface IBuilder {

    public void build() throws BuildException;

    public void reset();
}

