package slieb.closureutils.dependencies;


import slieb.closureutils.resources.Resource;

public interface DependencyParser {
    public DependencyNode parse(Resource resource);
}
