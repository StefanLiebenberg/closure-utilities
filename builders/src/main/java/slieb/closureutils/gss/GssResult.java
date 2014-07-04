package slieb.closureutils.gss;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import slieb.closureutils.build.BuildResult;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.resources.Resource;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public class GssResult implements BuildResult {

    private final Resource generatedStylesheet, generatedRenameMap;

    private final ImmutableSet<Resource> resources;

    private final ImmutableList<DependencyNode> resolvedNodes;

    public GssResult(@Nullable final Resource generatedStylesheet,
                     @Nullable final Resource generatedRenameMap,
                     @Nullable final ImmutableSet<Resource> resources,
                     @Nullable final ImmutableList<DependencyNode>
                             resolvedNodes) {
        this.generatedStylesheet = generatedStylesheet;
        this.generatedRenameMap = generatedRenameMap;
        this.resources = resources;
        this.resolvedNodes = resolvedNodes;
    }

    public Resource getGeneratedStylesheet() {
        return generatedStylesheet;
    }

    public Resource getGeneratedRenameMap() {
        return generatedRenameMap;
    }

    public ImmutableSet<Resource> getResources() {
        return resources;
    }

    public ImmutableList<DependencyNode> getResolvedNodes() {
        return resolvedNodes;
    }


    public static class Builder {
        protected Resource generatedStylesheet, generatedRenameMap;

        protected ImmutableSet<Resource> resources;

        protected ImmutableList<DependencyNode> resolvedNodes;

        public void setGeneratedStylesheet(Resource generatedStylesheet) {
            this.generatedStylesheet = generatedStylesheet;
        }

        public void setGeneratedRenameMap(Resource generatedRenameMap) {
            this.generatedRenameMap = generatedRenameMap;
        }

        public void setResources(ImmutableSet<Resource> resources) {
            this.resources = resources;
        }

        public void setResolvedNodes(ImmutableList<DependencyNode>
                                             resolvedNodes) {
            this.resolvedNodes = resolvedNodes;
        }

        public GssResult build() {
            return new GssResult(generatedStylesheet, generatedRenameMap,
                    resources, resolvedNodes);
        }
    }
}
