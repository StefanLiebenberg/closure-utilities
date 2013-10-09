package com.github.stefanliebenberg.closure;


import com.github.stefanliebenberg.html.HtmlBuildOptions;
import com.github.stefanliebenberg.html.HtmlBuilder;
import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.javascript.JsBuildOptions;
import com.github.stefanliebenberg.javascript.JsBuilder;
import com.github.stefanliebenberg.render.DefaultHtmlRenderer;
import com.github.stefanliebenberg.stylesheets.GssBuildOptions;
import com.github.stefanliebenberg.stylesheets.GssBuilder;
import com.github.stefanliebenberg.templates.SoyBuildOptions;
import com.github.stefanliebenberg.templates.SoyBuilder;

import javax.annotation.Nonnull;
import java.io.File;

public class ClosureBuilder
        extends AbstractBuilder<ClosureBuildOptions>
        implements IBuilder {

    public ClosureBuilder() {}

    public ClosureBuilder(@Nonnull final ClosureBuildOptions buildOptions) {
        super(buildOptions);
    }

    private final GssBuilder gssBuilder = new GssBuilder();

    private final SoyBuilder soyBuilder = new SoyBuilder();

    private final JsBuilder jsBuilder = new JsBuilder();

    public final HtmlBuilder htmlBuilder = new HtmlBuilder();

    @Override
    public void reset() {
        super.reset();
        gssBuilder.reset();
        soyBuilder.reset();
        jsBuilder.reset();
    }

    public File getGssOutputFile() {
        return null;
    }

    @Nonnull
    public GssBuildOptions getGssBuildOptions() {
        GssBuildOptions gssBuildOptions = new GssBuildOptions();
        gssBuildOptions.setAssetsDirectory(buildOptions.getAssetsDirectory());
        gssBuildOptions.setShouldCalculateDependencies(true);
        gssBuildOptions.setSourceDirectories(
                buildOptions.getGssSourceDirectories());
        gssBuildOptions.setRenameMap(
                buildOptions.getCssClassRenameMap());
        gssBuildOptions.setShouldGenerateForDebug(
                buildOptions.getShouldDebug());
        gssBuildOptions.setShouldGenerateForProduction(
                buildOptions.getShouldCompile());
        gssBuildOptions.setOutputFile(getGssOutputFile());
        return gssBuildOptions;
    }

    public void buildGss()
            throws BuildException {
        gssBuilder.setBuildOptions(getGssBuildOptions());
        gssBuilder.build();
    }

    @Nonnull
    public SoyBuildOptions getSoyBuildOptions() {
        final SoyBuildOptions soyBuildOptions = new SoyBuildOptions();
        soyBuildOptions.setSourceDirectories(
                buildOptions.getSoySourceDirectories());
        return soyBuildOptions;
    }

    public void buildSoy()
            throws BuildException {
        soyBuilder.setBuildOptions(getSoyBuildOptions());
        soyBuilder.build();
    }

    @Nonnull
    public JsBuildOptions getJsBuildOptions() {
        JsBuildOptions jsBuildOptions = new JsBuildOptions();
        jsBuildOptions.setShouldCalculateDependencies(true);
        jsBuildOptions.setEntryPoints(
                buildOptions.getJavascriptEntryPoints());
        jsBuildOptions.setSourceDirectories(
                buildOptions.getJavascriptSourceDirectories());
        return jsBuildOptions;
    }

    public void buildJs() throws BuildException {
        jsBuilder.setBuildOptions(getJsBuildOptions());
        jsBuilder.build();
    }

    @Nonnull
    public HtmlBuildOptions getHtmlBuildOptions() {
        HtmlBuildOptions htmlBuildOptions = new HtmlBuildOptions();
        htmlBuildOptions.setHtmlRenderer(new DefaultHtmlRenderer());
        htmlBuildOptions.setShouldBuildInline(false);
        htmlBuildOptions.setLocationMap(null);
        htmlBuildOptions.setStylesheetFiles(gssBuilder.getSourceFiles());
        htmlBuildOptions.setJavascriptFiles(jsBuilder.getSourceFiles());
        htmlBuildOptions.setContent(null);
        htmlBuildOptions.setOutputFile(null);
        return htmlBuildOptions;
    }

    public void buildHtml() throws BuildException {
        htmlBuilder.setBuildOptions(getHtmlBuildOptions());
        htmlBuilder.build();
    }

    @Override
    public void build() throws BuildException {
        checkOptions();
        buildGss();
        buildSoy();
        buildJs();
        buildHtml();
    }
}
