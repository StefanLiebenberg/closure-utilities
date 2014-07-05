package slieb.closureutils.rendering.models;


import slieb.closureutils.resources.Resource;

import static slieb.closureutils.resources.Resources.safeReadResource;

public class Stylesheet {
    private final Resource resource;

    public Stylesheet(Resource resource) {
        this.resource = resource;
    }

    public String getHref() {
        return resource.getUri().toString();
    }

    public String getContent() {
        return safeReadResource(resource);
    }
}
