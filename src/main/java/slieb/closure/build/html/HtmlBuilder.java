package slieb.closure.build.html;

import slieb.closure.build.internal.AbstractBuilder;
import slieb.closure.build.internal.BuildException;
import slieb.closure.render.DefaultHtmlRenderer;
import slieb.closure.render.HtmlRenderer;
import slieb.closure.render.RenderException;
import slieb.closure.render.SoyHtmlRenderer;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class HtmlBuilder
        extends AbstractBuilder<HtmlOptions, HtmlResult> {

    public static HtmlRenderer getHtmlTemplateRenderer(
            Collection<File> sourceDirectories,
            String templateName) {
        return new SoyHtmlRenderer(sourceDirectories, templateName);
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
    public void checkOptions(@Nonnull final HtmlOptions buildOptions)
            throws BuildException {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile == null) {
            throw new BuildException(UNSPECIFIED_OUTPUT_FILE);
        }
    }
}
