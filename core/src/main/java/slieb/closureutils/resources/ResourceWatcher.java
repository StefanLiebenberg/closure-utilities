package slieb.closureutils.resources;


public class ResourceWatcher {

    private Resource resource;

    public ResourceWatcher(Resource resource) {
        this.resource = resource;
    }

    public boolean hasChanged() {
        return true;
    }

}
