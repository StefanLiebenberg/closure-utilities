package org.stefanl.closure_utilities.closure;

import org.stefanl.closure_utilities.html.HtmlBuildOptions;
import org.stefanl.closure_utilities.html.HtmlBuilder;
import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.BuilderInterface;
import org.stefanl.closure_utilities.internal.InvalidBuildOptionsException;
import org.stefanl.closure_utilities.javascript.JsBuildOptions;
import org.stefanl.closure_utilities.javascript.JsBuilder;
import org.stefanl.closure_utilities.javascript.JsBuilder;
import org.stefanl.closure_utilities.render.DefaultHtmlRenderer;
import org.stefanl.closure_utilities.stylesheets.GssBuildOptions;
import org.stefanl.closure_utilities.stylesheets.GssBuilder;
import org.stefanl.closure_utilities.stylesheets.GssBuilder;
import org.stefanl.closure_utilities.templates.SoyBuildOptions;
import org.stefanl.closure_utilities.templates.SoyBuilder;
import org.stefanl.closure_utilities.templates.SoyBuilder;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClosureBuilder
        extends AbstractBuilder<iClosureBuildOptions>
        implements BuilderInterface {

    public ClosureBuilder() {}

    public ClosureBuilder(@Nonnull final iClosureBuildOptions
                                  buildOptions) {
        super(buildOptions);
    }

    private final GssBuilder gssBuilder = new GssBuilder();

    private final SoyBuilder soyBuilder = new SoyBuilder();

    private final JsBuilder jsBuilder = new JsBuilder();

    private final HtmlBuilder htmlBuilder = new HtmlBuilder();

    @Override
    public void reset() {
        super.reset();
        gssBuilder.reset();
        soyBuilder.reset();
        jsBuilder.reset();
        htmlBuilder.reset();
    }


    public static final String DEFAULT_STYLESHEET_FILENAME = "style.css";

    public File getGssOutputFile() {
        return new File(buildOptions.getOutputDirectory(),
                DEFAULT_STYLESHEET_FILENAME);
    }

    @Nonnull
    public GssBuildOptions getGssBuildOptions() {

        final GssBuildOptions gssBuildOptions = new GssBuildOptions();

        gssBuildOptions.setShouldCalculateDependencies(true);
        gssBuildOptions.setAssetsDirectory(buildOptions.getAssetsDirectory());
        gssBuildOptions.setEntryPoints(buildOptions.getGssEntryPoints());
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

    public final static String COMPILED_TEMPLATES_DIRECTORY_NAME =
            "compiled-templates";

    @Nonnull
    public File getSoyOutputDirectory() {
        final File soyOutputDirectory = buildOptions.getSoyOutputDirectory();

        if (soyOutputDirectory != null) {
            return soyOutputDirectory;
        } else {
            return getOutputFile(COMPILED_TEMPLATES_DIRECTORY_NAME);
        }
    }

    @Nonnull
    public File getOutputDirectory() {
        final File outputDirectory = buildOptions.getOutputDirectory();
        if (outputDirectory != null) {
            return outputDirectory;
        } else {
            throw new NullPointerException(UNSPECIFIED_OUTPUT_DIRECTORY);
        }
    }

    @Nonnull
    private File getOutputFile(
            @Nonnull final String fileName) {
        return new File(getOutputDirectory(), fileName);
    }

    @Nonnull
    public SoyBuildOptions getSoyBuildOptions() {
        final SoyBuildOptions soyBuildOptions = new SoyBuildOptions();
        soyBuildOptions.setSourceDirectories(
                buildOptions.getSoySourceDirectories());
        soyBuildOptions.setOutputDirectory(getSoyOutputDirectory());
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
        jsBuildOptions.setOutputFile(getOutputFile("script.js"));
        jsBuildOptions.setEntryPoints(buildOptions.getJavascriptEntryPoints());
        jsBuildOptions.setSourceDirectories(
                buildOptions.getJavascriptSourceDirectories());
        jsBuildOptions.setShouldCompile(buildOptions.getShouldCompile());
        jsBuildOptions.setShouldDebug(buildOptions.getShouldDebug());
        return jsBuildOptions;

    }

    public void buildJs() throws BuildException {

        jsBuilder.setBuildOptions(getJsBuildOptions());
        jsBuilder.build();

    }

    public final static String DEFAULT_HTML_PAGE_NAME =
            "index.html";

    @Nonnull
    public File getHtmlOutputFile() {
        return getOutputFile(DEFAULT_HTML_PAGE_NAME);
    }

    @Nonnull
    public List<File> getStylesheets() {
        final List<File> stylesheets = new ArrayList<>();

        final List<File> externalStylesheets =
                buildOptions.getExternalStylesheets();
        if (externalStylesheets != null && !externalStylesheets.isEmpty()) {
            stylesheets.addAll(externalStylesheets);
        }

        final File generatedStylesheet = gssBuilder.getGeneratedStylesheet();
        if (generatedStylesheet != null) {
            stylesheets.add(generatedStylesheet);
        }
        return stylesheets;
    }

    @Nonnull
    public List<File> getJavascriptFiles() {
        final List<File> javascriptFiles = new ArrayList<>();
        final List<File> externalScripts = buildOptions
                .getExternalScriptFiles();
        if (externalScripts != null && !externalScripts.isEmpty()) {
            javascriptFiles.addAll(externalScripts);
        }

        if (buildOptions.getShouldCompile()) {
            final File outFile = jsBuilder.getOutputFile();
            if (outFile != null) {
                javascriptFiles.add(outFile);
            }
        } else {
            final List<File> calculatedSourceFiles = jsBuilder.getSourceFiles();
            if (calculatedSourceFiles != null) {
                javascriptFiles.addAll(calculatedSourceFiles);
            }
        }
        return javascriptFiles;
    }

    @Nonnull
    public HtmlBuildOptions getHtmlBuildOptions() {
        final HtmlBuildOptions htmlBuildOptions = new HtmlBuildOptions();
        htmlBuildOptions.setOutputFile(getHtmlOutputFile());
        htmlBuildOptions.setStylesheetFiles(getStylesheets());
        htmlBuildOptions.setJavascriptFiles(getJavascriptFiles());
        htmlBuildOptions.setHtmlRenderer(new DefaultHtmlRenderer());
        htmlBuildOptions.setContent(buildOptions.getHtmlContent());
        htmlBuildOptions.setLocationMap(null);
        htmlBuildOptions.setShouldBuildInline(false);
        return htmlBuildOptions;
    }


    public void buildHtml() throws BuildException {
        htmlBuilder.setBuildOptions(getHtmlBuildOptions());
        htmlBuilder.build();
    }

    @Override
    public void buildInternal() throws BuildException {
        buildGss();
        buildSoy();
        buildJs();
        buildHtml();
    }


    private static final String UNSPECIFIED_OUTPUT_DIRECTORY =
            "Closure output directory has not been specified.";

    @Override
    public void checkOptions() throws InvalidBuildOptionsException {
        super.checkOptions();
        final File outputDirectory = buildOptions.getOutputDirectory();
        if (outputDirectory == null) {
            throw new InvalidBuildOptionsException
                    (UNSPECIFIED_OUTPUT_DIRECTORY);
        }
    }


}
