package slieb.closureutils.dependencies;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import slieb.closureutils.resources.Resource;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;

@Immutable
public class DependencyNode {
    private final Resource resource;

    private final ImmutableSet<String> provides, requires;

    private final ImmutableMap<String, Boolean> flags;

    public DependencyNode(Resource resource, ImmutableSet<String> provides,
                          ImmutableSet<String> requires, ImmutableMap<String,
            Boolean> flags) {
        this.resource = resource;
        this.provides = provides;
        this.requires = requires;
        this.flags = flags;
    }

    public Resource getResource() {
        return resource;
    }

    public Collection<String> getProvides() {
        return provides;
    }

    public Collection<String> getRequires() {
        return requires;
    }

    public Boolean getFlag(String name) {
        return flags.containsKey(name) ? flags.get(name) : false;
    }

    public static class Builder {
        public final Resource resource;

        protected final ImmutableMap.Builder<String,
                Boolean> flags = new ImmutableMap.Builder<>();


        protected final ImmutableSet.Builder<String>
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

        public Builder setFlag(String flagName, Boolean value) {
            flags.put(flagName, value);
            return this;
        }

        public DependencyNode build() {
            return new DependencyNode(resource, providesBuilder.build(),
                    requiresBuilder.build(), flags.build());
        }
    }
}
