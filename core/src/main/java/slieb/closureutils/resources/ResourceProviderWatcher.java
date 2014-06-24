package slieb.closureutils.resources;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceProviderWatcher {

    private final ResourceProvider resourceProvider;

    private final Map<Resource, ResourceWatcher> watcherMap;

    public ResourceProviderWatcher(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        this.watcherMap = new HashMap<>();
    }

    public boolean hasChanged() {
        Boolean changed = false;
        Set<Resource> previousResources = watcherMap.keySet();
        for (Resource resource : resourceProvider.getResources()) {
            if (watcherMap.containsKey(resource)) {
                previousResources.remove(resource);
                ResourceWatcher resourceWatcher = watcherMap.get(resource);
                if (changed) {
                    resourceWatcher.hasChanged();
                } else {
                    changed = resourceWatcher.hasChanged();
                }
            } else {
                changed = true;
                ResourceWatcher resourceWatcher = new ResourceWatcher(resource);
                resourceWatcher.hasChanged();
                watcherMap.put(resource, resourceWatcher);
            }
        }
        for (Resource previousResource : previousResources) {
            watcherMap.remove(previousResource);
            if (!changed) {
                changed = true;
            }
        }

        return changed;
    }
}
