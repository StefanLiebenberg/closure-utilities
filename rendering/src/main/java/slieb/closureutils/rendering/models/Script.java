package slieb.closureutils.rendering.models;


import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.Resources;

public class Script {

    private Resource resource;

    public Script(Resource resource) {
        this.resource = resource;
    }

    public String getSrc() {
        return resource.getUri().toString();
    }

    public String getContent() {
        return Resources.safeReadResource(resource);
    }
}
