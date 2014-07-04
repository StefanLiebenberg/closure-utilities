package slieb.closureutils.templates;


import slieb.closureutils.build.BuildResult;
import slieb.closureutils.resources.ResourceProvider;

import javax.annotation.concurrent.Immutable;

@Immutable
public class SoyResult implements BuildResult {

    private final ResourceProvider compiledResources;

    public SoyResult(ResourceProvider compiledResources) {
        this.compiledResources = compiledResources;
    }

    public ResourceProvider getCompiledResources() {
        return compiledResources;
    }

    public static class Builder {
        protected ResourceProvider compiledResources;

        public Builder setCompiledResources(ResourceProvider
                                                    compiledResources) {
            this.compiledResources = compiledResources;
            return this;
        }

        public SoyResult build() {
            return new SoyResult(compiledResources);
        }

    }
}
