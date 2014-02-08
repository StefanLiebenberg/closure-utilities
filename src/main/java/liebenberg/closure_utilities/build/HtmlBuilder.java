package liebenberg.closure_utilities.build;

import liebenberg.closure_utilities.render.DefaultHtmlRenderer;
import liebenberg.closure_utilities.render.HtmlRenderer;
import liebenberg.closure_utilities.render.RenderException;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class HtmlBuilder
        extends AbstractBuilder<HtmlOptions, HtmlResult> {

    private static final String BUILDER_NAME = "[HTML]";

    public HtmlBuilder() {
        super(BUILDER_NAME);
    }

    private static class InternalData {
        private File generatedHtmlFile;

        private HtmlResult toResult() {
            return new HtmlResult(generatedHtmlFile);
        }
    }


    @Nonnull
    public static HtmlRenderer getDefaultRenderer() {
        return new DefaultHtmlRenderer();
    }

    @Nonnull
    public HtmlRenderer getHtmlRenderer(HtmlOptions buildOptions) {
        HtmlRenderer htmlRenderer = buildOptions.getHtmlRenderer();
        if (htmlRenderer == null) {
            htmlRenderer = getDefaultRenderer();
        }
        return htmlRenderer;
    }


    @Nonnull
    public String renderHtmlFile(HtmlOptions buildOptions, File outputPath)
            throws IOException, RenderException {
        return getHtmlRenderer(buildOptions)
                .setShouldInline(buildOptions.getShouldBuildInline())
                .setContent(buildOptions.getContent())
                .setScripts(buildOptions.getJavascriptFiles())
                .setOutputPath(outputPath)
                .setStylesheets(buildOptions.getStylesheetFiles())
                .render();
    }


    @Override
    @Nonnull
    public HtmlResult buildInternal(
            @Nonnull final HtmlOptions buildOptions)
            throws Exception {
        final InternalData internalData = new InternalData();
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            FS.write(outputFile, renderHtmlFile(buildOptions, outputFile));
            internalData.generatedHtmlFile = outputFile;
        }
        return internalData.toResult();
    }

    private final static String UNSPECIFIED_OUTPUT_FILE =
            "Html output file has not been specified.";

    @Override
    public void checkOptions(
            @Nonnull final HtmlOptions buildOptions) throws
            BuildOptionsException {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile == null) {
            throw new BuildOptionsException(UNSPECIFIED_OUTPUT_FILE);
        }
    }
}
