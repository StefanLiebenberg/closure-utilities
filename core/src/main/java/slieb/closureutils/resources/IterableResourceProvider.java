package slieb.closureutils.resources;


public class IterableResourceProvider implements ResourceProvider {
    private final Iterable<Resource> resources;

    public IterableResourceProvider(Iterable<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public Iterable<Resource> getResources() {
        return resources;
    }
}
