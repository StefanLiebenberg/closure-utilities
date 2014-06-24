package slieb.closureutils.build;


public interface BuilderInterface<A extends BuildOptions, B extends BuildResult> {
    public B build(A options) throws BuildException;
}
