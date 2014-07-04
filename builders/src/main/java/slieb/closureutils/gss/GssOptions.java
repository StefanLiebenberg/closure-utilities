package slieb.closureutils.gss;


import slieb.closureutils.build.BuildOptions;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import javax.annotation.concurrent.Immutable;
import java.net.URI;
import java.util.List;

@Immutable
public class GssOptions implements BuildOptions {

    private final ResourceProvider resourceProvider;

    private final Boolean
            shouldCalculateDependencies,
            shouldGenerateForProduction,
            shouldGenerateForDebug;

    private final List<String> entryPoints;

    private final Resource renameMapResource, outputResource;

    private final URI assetsUri;

    public GssOptions(ResourceProvider resourceProvider,
                      Boolean shouldCalculateDependencies,
                      Boolean shouldGenerateForProduction,
                      Boolean shouldGenerateForDebug,
                      List<String> entryPoints, Resource renameMapResource,
                      Resource outputResource, URI assetsUri) {
        this.resourceProvider = resourceProvider;
        this.shouldCalculateDependencies = shouldCalculateDependencies;
        this.shouldGenerateForProduction = shouldGenerateForProduction;
        this.shouldGenerateForDebug = shouldGenerateForDebug;
        this.entryPoints = entryPoints;
        this.renameMapResource = renameMapResource;
        this.outputResource = outputResource;
        this.assetsUri = assetsUri;
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public Boolean getShouldCalculateDependencies() {
        return shouldCalculateDependencies;
    }

    public Boolean getShouldGenerateForProduction() {
        return shouldGenerateForProduction;
    }

    public Boolean getShouldGenerateForDebug() {
        return shouldGenerateForDebug;
    }

    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public Resource getRenameMapResource() {
        return renameMapResource;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public URI getAssetsUri() {
        return assetsUri;
    }

    public static class Builder {
        protected ResourceProvider resourceProvider;

        protected Boolean
                shouldCalculateDependencies = true,
                shouldGenerateForProduction = false,
                shouldGenerateForDebug = true;

        protected List<String> entryPoints;

        protected Resource renameMapResource, outputResource;

        protected URI assetsUri;


        public Builder setResourceProvider(ResourceProvider resourceProvider) {
            this.resourceProvider = resourceProvider;
            return this;
        }

        public Builder setShouldCalculateDependencies(Boolean shouldCalculateDependencies) {
            this.shouldCalculateDependencies = shouldCalculateDependencies;
            return this;
        }

        public Builder setShouldGenerateForProduction(Boolean shouldGenerateForProduction) {
            this.shouldGenerateForProduction = shouldGenerateForProduction;
            return this;
        }

        public Builder setShouldGenerateForDebug(Boolean shouldGenerateForDebug) {
            this.shouldGenerateForDebug = shouldGenerateForDebug;
            return this;
        }

        public Builder setEntryPoints(List<String> entryPoints) {
            this.entryPoints = entryPoints;
            return this;
        }

        public Builder setRenameMapResource(Resource renameMapResource) {
            this.renameMapResource = renameMapResource;
            return this;
        }

        public Builder setOutputResource(Resource outputResource) {
            this.outputResource = outputResource;
            return this;
        }

        public Builder setAssetsUri(URI assetsUri) {
            this.assetsUri = assetsUri;
            return this;
        }

        public GssOptions build() {
            return new GssOptions(resourceProvider,
                    shouldCalculateDependencies, shouldGenerateForProduction,
                    shouldGenerateForDebug, entryPoints, renameMapResource,
                    outputResource, assetsUri);
        }
    }
}
