package slieb.closureutils.html;

import slieb.closureutils.build.BuildException;
import slieb.closureutils.build.BuilderInterface;
import slieb.closureutils.rendering.HtmlRenderer;
import slieb.closureutils.rendering.HtmlRendererFactory;
import slieb.closureutils.rendering.RenderException;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.StringResource;

import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class HtmlBuilder implements BuilderInterface<HtmlOptions, HtmlResult> {

    private final HtmlRendererFactory htmlRendererFactory;

    public HtmlBuilder(HtmlRendererFactory htmlRendererFactory) {
        this.htmlRendererFactory = htmlRendererFactory;
    }

    @Override
    public HtmlResult build(HtmlOptions options) throws BuildException {
        if (options == null) {
            throw new IllegalArgumentException();
        }

        Resource outputResource = checkNotNull(options.getOutputResource(),
                "Output resource not specified.");
        URI outputResourceUri = outputResource.getUri();

        List<Resource> scriptResources = checkNotNull(options
                .getScriptResources());
        List<Resource> stylesheetResources = checkNotNull(options
                .getStylesheetResources());
        String content = options.getContent();
        HtmlRenderer renderer = htmlRendererFactory.create();
        HtmlResult.Builder builder = new HtmlResult.Builder();
        try {
            String htmlContent = renderer
                    .setOutputUri(outputResourceUri)
                    .setScripts(scriptResources)
                    .setStylesheets(stylesheetResources)
                    .setContent(content)
                    .render();
            builder.setGeneratedHtmlResource(new StringResource(htmlContent,
                    outputResourceUri));

            return builder.build();
        } catch (RenderException e) {
            throw new BuildException(e);
        }
    }


}
