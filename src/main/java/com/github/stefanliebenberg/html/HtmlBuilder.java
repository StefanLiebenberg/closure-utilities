package com.github.stefanliebenberg.html;

import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.render.DefaultHtmlRenderer;
import com.github.stefanliebenberg.render.HtmlRenderer;
import com.github.stefanliebenberg.render.RenderException;
import com.github.stefanliebenberg.utilities.FsTool;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;


public class HtmlBuilder
        extends AbstractBuilder<HtmlBuildOptions>
        implements IBuilder {

    private File generatedHtmlFile;

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
    public void build() throws BuildException {
        checkOptions();
        try {
            final File outputFile = buildOptions.getOutputFile();
            if (outputFile != null) {
                FsTool.write(outputFile, renderHtmlFile(outputFile));
                generatedHtmlFile = outputFile;
            }
        } catch (IOException | RenderException ioException) {
            throw new BuildException(ioException);
        }
    }

    @Override
    public void checkOptions() throws BuildException {
        super.checkOptions();
        checkNotNull(buildOptions.getOutputFile());
    }

    public File getGeneratedHtmlFile() {
        return generatedHtmlFile;
    }
}
