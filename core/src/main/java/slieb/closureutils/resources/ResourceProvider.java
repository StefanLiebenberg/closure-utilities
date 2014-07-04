package slieb.closureutils.resources;

import java.net.URI;

public interface ResourceProvider {

    public URI getUri();

    public Iterable<Resource> getResources();
}
