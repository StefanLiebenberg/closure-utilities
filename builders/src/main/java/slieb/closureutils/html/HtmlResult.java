package slieb.closureutils.html;


import slieb.closureutils.build.BuildResult;
import slieb.closureutils.resources.Resource;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;


@Immutable
public class HtmlResult implements BuildResult {

    private final Resource generatedHtmlResource;

    public HtmlResult(@Nullable final Resource generatedHtmlResource) {
        this.generatedHtmlResource = generatedHtmlResource;
    }

    public Resource getGeneratedHtmlResource() {
        return generatedHtmlResource;
    }

    public static class Builder {
        protected Resource generatedHtmlResource;

        public Builder setGeneratedHtmlResource(Resource generatedHtmlResource) {
            this.generatedHtmlResource = generatedHtmlResource;
            return this;
        }

        public HtmlResult build() {
            return new HtmlResult(generatedHtmlResource);
        }
    }
}
