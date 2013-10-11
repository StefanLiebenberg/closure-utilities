package org.stefanl.closure_utilities.html;

import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.IBuilder;
import org.stefanl.closure_utilities.render.DefaultHtmlRenderer;
import org.stefanl.closure_utilities.render.HtmlRenderer;
import org.stefanl.closure_utilities.render.RenderException;
import org.stefanl.closure_utilities.utilities.FsTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;


public class HtmlBuilder
        extends AbstractBuilder<HtmlBuildOptions>
        implements IBuilder {

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
            FsTool.write(outputFile, renderHtmlFile(outputFile));
            generatedHtmlFile = outputFile;
        }
    }

    @Override
    public void checkOptions() throws BuildException {
        super.checkOptions();
        checkNotNull(buildOptions.getOutputFile());
    }

    @Nullable
    public File getGeneratedHtmlFile() {
        return generatedHtmlFile;
    }
}
