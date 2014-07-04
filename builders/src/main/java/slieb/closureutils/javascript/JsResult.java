package slieb.closureutils.javascript;


import slieb.closureutils.build.BuildResult;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.resources.Resource;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public class JsResult implements BuildResult {

    private final List<DependencyNode> resolvedResources;

    private final Resource outputResource;

    private final Resource baseResource;

    private final Resource depsResource;

    private final Resource definesResource;

    public JsResult(List<DependencyNode> resolvedResources,
                    Resource outputResource, Resource baseResource,
                    Resource depsResource, Resource definesResource) {
        this.resolvedResources = resolvedResources;
        this.outputResource = outputResource;
        this.baseResource = baseResource;
        this.depsResource = depsResource;
        this.definesResource = definesResource;
    }

    public List<DependencyNode> getResolvedResources() {
        return resolvedResources;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public Resource getBaseResource() {
        return baseResource;
    }

    public Resource getDepsResource() {
        return depsResource;
    }

    public Resource getDefinesResource() {
        return definesResource;
    }

    public static class Builder {
        private List<DependencyNode> resolvedResources;

        private Resource outputResource, baseResource, depsResource,
                definesResource;

        public Builder setResolvedResources(List<DependencyNode>
                                                    resolvedResources) {
            this.resolvedResources = resolvedResources;
            return this;
        }

        public Builder setOutputResource(Resource outputResource) {
            this.outputResource = outputResource;
            return this;
        }

        public Builder setBaseResource(Resource baseResource) {
            this.baseResource = baseResource;
            return this;
        }

        public Builder setDepsResource(Resource depsResource) {
            this.depsResource = depsResource;
            return this;
        }

        public Builder setDefinesResource(Resource definesResource) {
            this.definesResource = definesResource;
            return this;
        }

        public JsResult build() {
            return new JsResult(resolvedResources, outputResource,
                    baseResource, depsResource, definesResource);
        }
    }
}
