package slieb.closureutils.resources;


import java.net.URI;

public class IterableResourceProvider implements ResourceProvider {

    private final Iterable<Resource> resources;

    private final URI uri;

    public IterableResourceProvider(Iterable<Resource> resources, URI uri) {
        this.resources = resources;
        this.uri = uri;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public Iterable<Resource> getResources() {
        return resources;
    }
}
