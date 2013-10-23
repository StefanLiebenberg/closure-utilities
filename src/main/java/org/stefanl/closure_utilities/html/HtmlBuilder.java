package org.stefanl.closure_utilities.html;

import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.BuildOptionsException;
import org.stefanl.closure_utilities.internal.BuilderInterface;
import org.stefanl.closure_utilities.render.DefaultHtmlRenderer;
import org.stefanl.closure_utilities.render.HtmlRenderer;
import org.stefanl.closure_utilities.render.RenderException;
import org.stefanl.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;


public class HtmlBuilder
        extends AbstractBuilder<HtmlBuildOptions>
        implements BuilderInterface {

    private File generatedHtmlFile;

    public HtmlBuilder() {}

    public HtmlBuilder(@Nonnull final HtmlBuildOptions options) {
        super(options);
    }

    @Nonnull
    public static HtmlRenderer getDefaultRenderer() {
        return new DefaultHtmlRenderer();
    }

    @Override
    public void reset() {
        super.reset();
        generatedHtmlFile = null;
    }

    @Nonnull
    public HtmlRenderer getHtmlRenderer() {
        HtmlRenderer htmlRenderer = buildOptions.getHtmlRenderer();
        if (htmlRenderer == null) {
            htmlRenderer = getDefaultRenderer();
        }
        return htmlRenderer;
    }


    @Nonnull
    public String renderHtmlFile(File outputPath)
            throws IOException, RenderException {
        return getHtmlRenderer()
                .setShouldInline(buildOptions.getShouldBuildInline())
                .setContent(buildOptions.getContent())
                .setScripts(buildOptions.getJavascriptFiles())
                .setOutputPath(outputPath)
                .setStylesheets(buildOptions.getStylesheetFiles())
                .render();
    }


    @Override
    public void buildInternal() throws Exception {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            FS.write(outputFile, renderHtmlFile(outputFile));
            generatedHtmlFile = outputFile;
        }
    }

    private final static String UNSPECIFIED_OUTPUT_FILE =
            "Html output file has not been specified.";

    @Override
    public void checkOptions() throws BuildOptionsException {

        super.checkOptions();

        final File outputFile = buildOptions.getOutputFile();
        if (outputFile == null) {
            throw new BuildOptionsException(UNSPECIFIED_OUTPUT_FILE);
        }

    }

    @Nullable
    public File getGeneratedHtmlFile() {
        return generatedHtmlFile;
    }
}
