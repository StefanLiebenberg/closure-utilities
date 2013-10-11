package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.*;
import com.github.stefanliebenberg.utilities.FsTool;
import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.base.Function;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GssBuilder
        extends AbstractBuilder<GssBuildOptions>
        implements IBuilder {

    public GssBuilder() {}

    public GssBuilder(@Nonnull final GssBuildOptions buildOptions) {
        super(buildOptions);
    }

    private final ImageUrlProcessor imageUrlProcessor =
            new ImageUrlProcessor();

    private final static Function<File, GssSourceFile> FILE_TO_GSS_SRC =
            new Function<File, GssSourceFile>() {
                @Nullable
                @Override
                public GssSourceFile apply(@Nullable File input) {
                    if (input != null) {
                        return new GssSourceFile(input);
                    } else {
                        return null;
                    }
                }
            };


    private final static Function<GssSourceFile, File> GSS_SRC_TO_FILE =
            new Function<GssSourceFile, File>() {
                @Nullable
                @Override
                public File apply(@Nullable GssSourceFile input) {
                    if (input != null) {
                        return new File(input.getSourceLocation());
                    } else {
                        return null;
                    }
                }
            };

    private final GssDependencyParser gssDependencyParser =
            new GssDependencyParser();

    private DependencyLoader<GssSourceFile> gssDependencyLoader;

    private File temporaryOutputFile;

    private File generatedStylesheet;

    private File generatedRenameMap;


    private List<File> sourceFiles;

    @Override
    public void reset() {
        super.reset();
        sourceFiles = null;
        temporaryOutputFile = null;
        generatedStylesheet = null;
        generatedRenameMap = null;
        imageUrlProcessor.setImageRoot(null);
    }

    private void compileCssFiles(
            @Nullable final List<File> sourceFiles,
            @Nonnull final File outputFile,
            @Nullable final File renameMap,
            @Nonnull final Boolean productionBoolean,
            @Nonnull final Boolean debugBoolean)
            throws BuildException {

        if (sourceFiles == null || sourceFiles.isEmpty()) {
            throw buildException("No input files specified.");
        }

        List<String> arguments = new ArrayList<String>();
        arguments.add("--allow-unrecognized-functions");
        arguments.add("--allow-unrecognized-properties");

        if (renameMap != null) {
            FsTool.ensureDirectoryFor(renameMap);
            arguments.add("--output-renaming-map");
            arguments.add(renameMap.getPath());
        }

        if (productionBoolean) {
            arguments.add("--output-renaming-map-format");
            arguments.add("CLOSURE_COMPILED");
            if (debugBoolean) {
                arguments.add("--rename");
                arguments.add("DEBUG");
            } else {
                arguments.add("--rename");
                arguments.add("CLOSURE");
            }
        } else {
            arguments.add("--output-renaming-map-format");
            arguments.add("CLOSURE_UNCOMPILED");
            arguments.add("--rename");
            arguments.add("NONE");
        }


        arguments.add("--output-file");
        FsTool.ensureDirectoryFor(outputFile);
        arguments.add(FsTool.FILE_TO_FILEPATH.apply(outputFile));
        arguments.addAll(Immuter.list(sourceFiles, FsTool.FILE_TO_FILEPATH));
        ClosureCommandLineCompiler.main(Immuter.stringArray(arguments));
        generatedRenameMap = renameMap;
    }

    private void compileCssFiles()
            throws BuildException {
        compileCssFiles(
                sourceFiles,
                temporaryOutputFile,
                buildOptions.getRenameMap(),
                buildOptions.getShouldGenerateForProduction(),
                buildOptions.getShouldGenerateForDebug());
    }


    @Nonnull
    public String parseCssFunctions(
            @Nonnull final String inputContent,
            @Nullable final String base) {
        imageUrlProcessor.setImageRoot(base);
        return imageUrlProcessor.processString(inputContent);
    }

    @Nullable
    public String getBasePath(@Nonnull File outputFile) {

        URI assetsUri = buildOptions.getAssetsUri();
        if (assetsUri != null) {
            return assetsUri.toString();
        }

        File assetsDirectory = buildOptions.getAssetsDirectory();
        if (assetsDirectory != null) {
            return FsTool.getRelative(assetsDirectory,
                    outputFile.getParentFile());
        }

        return null;
    }

    public void parseFunctionsFromCss()
            throws IOException {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            final String content = FsTool.read(temporaryOutputFile);
            final String base = getBasePath(outputFile);
            FsTool.write(outputFile, parseCssFunctions(content, base));
            generatedStylesheet = outputFile;
        }
    }


    @Nonnull
    private File getTemporaryFile()
            throws BuildException {
        try {
            return FsTool.getTempFile("css_", "pass1");
        } catch (IOException e) {
            throw buildException(e);
        }
    }


    private void calculateSourceFiles() throws DependencyException,
            IOException, IllegalAccessException, InstantiationException {
        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        final List<String> entryPoints = buildOptions.getEntryPoints();
        if (sourceDirectories != null && entryPoints != null) {
            final Collection<File> allSourceFiles =
                    FsTool.find(sourceDirectories, "gss");
            gssDependencyLoader =
                    new DependencyLoader<GssSourceFile>(gssDependencyParser,
                            allSourceFiles) {
                        @Override
                        protected GssSourceFile createDependency(File input) {
                            return new GssSourceFile(input);
                        }
                    };
            sourceFiles = gssDependencyLoader.getDependenciesFor(entryPoints);
        }
    }

    @Override
    public void buildInternal()
            throws Exception {

        if (buildOptions.getShouldCalculateDependencies()) {
            calculateSourceFiles();
        } else {
            sourceFiles = buildOptions.getSourceFiles();
        }


        temporaryOutputFile = getTemporaryFile();
        compileCssFiles();
        parseFunctionsFromCss();

    }

    @Override
    public void checkOptions() throws BuildException {
        super.checkOptions();

        String failureMessage = "Checking Build Options Failed in GssBuilder," +
                " because ";
        checkNotNull(buildOptions.getOutputFile(),
                failureMessage + "you need to specify a output file");
        if (buildOptions.getShouldCalculateDependencies()) {
            checkNotNull(
                    buildOptions.getSourceDirectories(),
                    failureMessage + "if shouldCalculateDependencies is true," +
                            " then you must specify sourceDirectories.");
            checkNull(buildOptions.getSourceFiles(),
                    failureMessage + "if shouldCalculateDependencies is true," +
                            " then you cannot specify sourceFiles");
        } else {
            checkNull(
                    buildOptions.getSourceDirectories(),
                    failureMessage + "if shouldCalculateDependencies is " +
                            "false, then you cannot specify sourceDirectories" +
                            ".");
            checkNotNull(buildOptions.getSourceFiles(),
                    failureMessage + "if shouldCalculateDependencies is " +
                            "false, then you must specify sourceFiles");
        }


    }


    /**
     * NOTE: this function may be removed later when gss compile is done in
     * memory.
     *
     * @return The temporary file used for outputing the compiled gss.
     */
    @Nullable
    public File getTemporaryOutputFile() {
        return temporaryOutputFile;
    }


    /**
     * @return The rename map file generated by this build.
     */
    @Nullable
    public File getGeneratedRenameMap() {
        return generatedRenameMap;
    }


    /**
     * @return The final generated stylesheet file.
     */
    @Nullable
    public File getGeneratedStylesheet() {
        return generatedStylesheet;
    }


    /**
     * The calculated source files.
     */
    @Nullable
    public List<File> getSourceFiles() {
        return sourceFiles;
    }
}
