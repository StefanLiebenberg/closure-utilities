package org.stefanl.closure_utilities.closure;

import com.google.common.collect.Lists;
import org.stefanl.closure_utilities.html.HtmlBuilder;
import org.stefanl.closure_utilities.html.HtmlOptions;
import org.stefanl.closure_utilities.html.HtmlResult;
import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.BuildOptionsException;
import org.stefanl.closure_utilities.javascript.JsBuilder;
import org.stefanl.closure_utilities.javascript.JsOptions;
import org.stefanl.closure_utilities.javascript.JsResult;
import org.stefanl.closure_utilities.render.DefaultHtmlRenderer;
import org.stefanl.closure_utilities.stylesheets.DefaultGssBuilder;
import org.stefanl.closure_utilities.stylesheets.GssOptions;
import org.stefanl.closure_utilities.stylesheets.GssResult;
import org.stefanl.closure_utilities.templates.DefaultSoyBuilder;
import org.stefanl.closure_utilities.templates.SoyOptions;
import org.stefanl.closure_utilities.templates.SoyResult;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClosureBuilder
        extends AbstractBuilder<ClosureOptions, ClosureResult> {

    private static class InternalData {
        private File generatedStylesheet;
        private File generatedRenameMap;
        private File soyOutputDirectory;
        private File htmlOutputFile;
        private File jsOutputFile;
        private List<File> calculatedScriptFiles;

        @Nonnull
        private ClosureResult toResult() {
            return new ClosureResult(generatedStylesheet,
                    generatedRenameMap, soyOutputDirectory, htmlOutputFile,
                    jsOutputFile);
        }
    }

    public enum BuildCommand {

        ALL, STYLESHEETS, TEMPLATES, JAVASCRIPT, HTML;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        @Nonnull
        public static BuildCommand fromText(String text) {
            return valueOf(text.trim().toUpperCase());
        }
    }

    public ClosureBuilder() {}

    private final DefaultGssBuilder gssBuilder = new DefaultGssBuilder();

    private final DefaultSoyBuilder soyBuilder = new DefaultSoyBuilder();

    private final JsBuilder jsBuilder = new JsBuilder();

    private final HtmlBuilder htmlBuilder = new HtmlBuilder();

    public static final String DEFAULT_STYLESHEET_FILENAME = "style.css";

    @Nonnull
    public File getGssOutputFile(@Nonnull final ClosureOptions options) {
        final File outputStylesheetFile = options.getOutputStylesheetFile();
        if (outputStylesheetFile != null) {
            return outputStylesheetFile;
        } else {
            return new File(options.getOutputDirectory(),
                    DEFAULT_STYLESHEET_FILENAME);
        }
    }


    @Nonnull
    public GssOptions getGssBuildOptions(
            @Nonnull final ClosureOptions options) {
        final GssOptions gssBuildOptions = new GssOptions();
        gssBuildOptions.setShouldCalculateDependencies(true);
        gssBuildOptions.setAssetsDirectory(options.getAssetsDirectory());
        gssBuildOptions.setEntryPoints(options.getGssEntryPoints());
        gssBuildOptions.setSourceDirectories(options.getGssSourceDirectories());
        gssBuildOptions.setRenameMap(options.getCssClassRenameMap());
        gssBuildOptions.setShouldGenerateForDebug(options.getShouldDebug());
        gssBuildOptions.setShouldGenerateForProduction(options
                .getShouldCompile());
        gssBuildOptions.setOutputFile(getGssOutputFile(options));
        gssBuildOptions.setAssetsUri(options.getAssetsUri());
        return gssBuildOptions;
    }

    protected void buildGss(@Nonnull final ClosureOptions options,
                            @Nonnull final InternalData internalData)
            throws BuildException {
        final GssOptions gssOptions = getGssBuildOptions(options);
        final GssResult gssResult = gssBuilder.build(gssOptions);
        internalData.generatedStylesheet = gssResult.getGeneratedStylesheet();
        internalData.generatedRenameMap = gssResult.getGeneratedRenameMap();
    }


    public ClosureResult buildGssOnly(@Nonnull final ClosureOptions options)
            throws BuildException {
        final InternalData internalData = new InternalData();
        buildGss(options, internalData);
        return internalData.toResult();
    }

    public final static String COMPILED_TEMPLATES_DIRECTORY_NAME =
            "compiled-templates";

    @Nonnull
    public File getSoyOutputDirectory(
            @Nonnull final ClosureOptions options) {
        final File soyOutputDirectory = options.getSoyOutputDirectory();
        if (soyOutputDirectory != null) {
            return soyOutputDirectory;
        } else {
            return getOutputFile(options, COMPILED_TEMPLATES_DIRECTORY_NAME);
        }
    }

    @Nonnull
    public File getOutputDirectory(@Nonnull final ClosureOptions
                                           options) {
        final File outputDirectory = options.getOutputDirectory();
        if (outputDirectory != null) {
            return outputDirectory;
        } else {
            throw new NullPointerException(UNSPECIFIED_OUTPUT_DIRECTORY);
        }
    }

    @Nonnull
    private File getOutputFile(
            @Nonnull final ClosureOptions options,
            @Nonnull final String fileName) {
        return new File(getOutputDirectory(options), fileName);
    }

    @Nonnull
    private SoyOptions getSoyBuildOptions(@Nonnull final ClosureOptions
                                                  options) {
        final SoyOptions soyOptions = new SoyOptions();
        soyOptions.setSourceDirectories(options.getSoySourceDirectories());
        soyOptions.setOutputDirectory(getSoyOutputDirectory(options));
        return soyOptions;
    }

    private void buildSoy(@Nonnull final ClosureOptions options,
                          @Nonnull final InternalData internalData)
            throws BuildException {
        final SoyOptions soyOptions = getSoyBuildOptions(options);
        final SoyResult soyResult = soyBuilder.build(soyOptions);
        internalData.soyOutputDirectory = soyResult.getOutputDirectory();
    }

    public ClosureResult buildSoyOnly(@Nonnull final ClosureOptions options)
            throws BuildException {
        final InternalData internalData = new InternalData();
        buildSoy(options, internalData);
        return internalData.toResult();
    }

    @Nonnull
    private JsOptions getJsBuildOptions(@Nonnull final
                                        ClosureOptions options) {
        JsOptions jsOptions = new JsOptions();
        jsOptions.setOutputFile(getOutputFile(options, "script.js"));
        jsOptions.setEntryPoints(options.getJavascriptEntryPoints());
        jsOptions.setSourceDirectories(
                options.getJavascriptSourceDirectories());
        jsOptions.setShouldCompile(options.getShouldCompile());
        jsOptions.setShouldDebug(options.getShouldDebug());
        jsOptions.setOutputDependencyFile(
                options.getJavascriptDependencyOutputFile());
        return jsOptions;
    }

    private void buildJs(@Nonnull final ClosureOptions options,
                         @Nonnull final InternalData internalData)
            throws BuildException {
        final JsOptions jsOptions = getJsBuildOptions(options);
        JsResult jsResult = jsBuilder.build(jsOptions);
        internalData.jsOutputFile = jsResult.getOutputFile();
        internalData.calculatedScriptFiles = jsResult.getScriptFiles();
    }

    public ClosureResult buildJsOnly(@Nonnull final ClosureOptions options)
            throws BuildException {
        final InternalData internalData = new InternalData();
        buildJs(options, internalData);
        return internalData.toResult();
    }

    private final static String DEFAULT_HTML_PAGE_NAME =
            "index.html";

    @Nonnull
    private File getHtmlOutputFile(@Nonnull final ClosureOptions options) {
        final File htmlFile = options.getOutputHtmlFile();
        if (htmlFile != null) {
            return htmlFile;
        } else {
            return getOutputFile(options, DEFAULT_HTML_PAGE_NAME);
        }
    }

    @Nonnull
    private List<File> getStylesheetsForHtmlBuild(
            @Nonnull final ClosureOptions options,
            @Nonnull final InternalData internalData) {
        final List<File> stylesheets = new ArrayList<>();
        final List<File> externalStylesheets =
                options.getExternalStylesheets();
        if (externalStylesheets != null && !externalStylesheets.isEmpty()) {
            stylesheets.addAll(externalStylesheets);
        }

        if (internalData.generatedStylesheet != null) {
            stylesheets.add(internalData.generatedStylesheet);
        }
        return stylesheets;
    }

    @Nonnull
    private List<File> getJavascriptFilesForHtmlBuild(
            @Nonnull final ClosureOptions options,
            @Nonnull final InternalData internalData) {

        final List<File> javascriptFiles = new ArrayList<>();
        final List<File> externalScripts = options.getExternalScriptFiles();
        if (externalScripts != null && !externalScripts.isEmpty()) {
            javascriptFiles.addAll(externalScripts);
        }

        if (options.getShouldCompile()) {
            final File outFile = internalData.jsOutputFile;
            if (outFile != null) {
                javascriptFiles.add(outFile);
            }
        } else {
            final List<File> calculatedSourceFiles =
                    internalData.calculatedScriptFiles;
            // jsBuilder.getScriptsFilesToCompile();
            if (calculatedSourceFiles != null) {
                javascriptFiles.addAll(calculatedSourceFiles);
            }
        }
        return javascriptFiles;
    }

    @Nonnull
    private HtmlOptions getHtmlBuildOptions(
            @Nonnull final ClosureOptions options,
            @Nonnull final InternalData internalData) {
        final HtmlOptions htmlOptions = new HtmlOptions();
        htmlOptions.setOutputFile(getHtmlOutputFile(options));
        htmlOptions.setStylesheetFiles(getStylesheetsForHtmlBuild(options,
                internalData));
        htmlOptions.setJavascriptFiles(getJavascriptFilesForHtmlBuild
                (options, internalData));
        htmlOptions.setHtmlRenderer(new DefaultHtmlRenderer());
        htmlOptions.setContent(options.getHtmlContent());
        htmlOptions.setLocationMap(null);
        htmlOptions.setShouldBuildInline(false);
        return htmlOptions;
    }


    private void buildHtml(@Nonnull final ClosureOptions options,
                           @Nonnull final InternalData internalData)
            throws BuildException {
        final HtmlOptions htmlOptions = getHtmlBuildOptions(options,
                internalData);
        final HtmlResult result = htmlBuilder.build(htmlOptions);
        internalData.htmlOutputFile = result.getGeneratedHtmlFile();
    }

    @Nonnull
    public ClosureResult buildHtmlOnly(@Nonnull final ClosureOptions options)
            throws BuildException {
        final InternalData internalData = new InternalData();
        buildHtml(options, internalData);
        return internalData.toResult();
    }


    @Nonnull
    @Override
    protected ClosureResult buildInternal(@Nonnull ClosureOptions options)
            throws Exception {
        final InternalData internalData = new InternalData();
        buildCommands(Lists.newArrayList(BuildCommand.ALL), options,
                internalData);
        return internalData.toResult();
    }

    protected void buildCommands(
            @Nonnull final Collection<BuildCommand> commands,
            @Nonnull final ClosureOptions options,
            @Nonnull final InternalData data)
            throws BuildException {
        boolean doAll = commands.contains(BuildCommand.ALL);

        if (doAll || commands.contains(BuildCommand.STYLESHEETS)) {
            buildGss(options, data);
        }

        if (doAll || commands.contains(BuildCommand.TEMPLATES)) {
            buildSoy(options, data);
        }

        if (doAll || commands.contains(BuildCommand.JAVASCRIPT)) {
            buildJs(options, data);
        }

        if (doAll || commands.contains(BuildCommand.HTML)) {
            buildHtml(options, data);
        }
    }

    @Nonnull
    public ClosureResult buildCommands(
            @Nonnull final ClosureOptions options,
            @Nonnull final BuildCommand... commands) throws BuildException {
        final InternalData data = new InternalData();
        buildCommands(Lists.newArrayList(commands), options, data);
        return data.toResult();
    }


    private static final String UNSPECIFIED_OUTPUT_DIRECTORY =
            "Closure output directory has not been specified.";

    @Override
    public void checkOptions(@Nonnull ClosureOptions options) throws
            BuildOptionsException {
        final File outputDirectory = options.getOutputDirectory();
        if (outputDirectory == null) {
            throw new BuildOptionsException(UNSPECIFIED_OUTPUT_DIRECTORY);
        }
    }


}
