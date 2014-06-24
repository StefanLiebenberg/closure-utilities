package slieb.closureutils.dependencies;


import com.google.common.collect.ImmutableSet;
import slieb.closureutils.resources.Resource;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;


public class DependencyNode {
    private final Resource resource;

    private final ImmutableSet<String> provides, requires;

    public DependencyNode(Resource resource, Set<String> provides, Set<String> requires) {
        this.resource = resource;
        this.provides = copyOf(provides);
        this.requires = copyOf(requires);
    }

    public Resource getResource() {
        return resource;
    }

    public ImmutableSet<String> getProvides() {
        return provides;
    }

    public ImmutableSet<String> getRequires() {
        return requires;
    }

    public static class Builder {
        private final Resource resource;

        private final ImmutableSet.Builder<String>
                providesBuilder = new ImmutableSet.Builder<>(),
                requiresBuilder = new ImmutableSet.Builder<>();

        public Builder(Resource resource) {
            this.resource = resource;
        }

        public Builder requires(String namespace) {
            requiresBuilder.add(namespace);
            return this;
        }

        public Builder provides(String namespace) {
            providesBuilder.add(namespace);
            return this;
        }

        public DependencyNode build() {
            return new DependencyNode(resource, providesBuilder.build(), requiresBuilder.build());
        }


    }
}
