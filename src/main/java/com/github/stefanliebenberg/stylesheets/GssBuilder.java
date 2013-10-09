package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.*;
import com.github.stefanliebenberg.utilities.FsTool;
import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
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
            throwBuildException("No input files specified.");
            return;
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
                buildOptions.getSourceFiles(),
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

    public void parseFunctionsFromCss()
            throws IOException {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            final String content = FsTool.read(temporaryOutputFile);
            final File assetDirectory = buildOptions.getAssetsDirectory();
            final String base =
                    (assetDirectory != null) ?
                            FsTool.getRelative(assetDirectory,
                                    outputFile.getParentFile()) : null;
            FsTool.write(outputFile, parseCssFunctions(content, base));
            generatedStylesheet = outputFile;
        }
    }


    private File getTemporaryFile()
            throws BuildException {
        try {
            return FsTool.getTempFile("css_", "pass1");
        } catch (IOException e) {
            throwBuildException(e);
        }
        return null;
    }


    private void calculateSourceFiles() throws DependencyException {
        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        if (sourceDirectories != null) {
            final Collection<File> allSourceFiles =
                    FsTool.find(sourceDirectories, "gss");
            Collection<GssSourceFile> dependencies =
                    Collections2.transform(allSourceFiles, FILE_TO_GSS_SRC);
            DependencyCalculator<GssSourceFile> dependencyCalculator =
                    new DependencyCalculator<GssSourceFile>(dependencies);
            List<GssSourceFile> gssSourceFiles =
                    dependencyCalculator.getDependencyList(
                            buildOptions.getEntryPoints());
            sourceFiles = Lists.transform(gssSourceFiles, GSS_SRC_TO_FILE);
        }
    }

    @Override
    public void build()
            throws BuildException {
        checkOptions();
        try {
            if (buildOptions.getShouldCalculateDependencies()) {
                calculateSourceFiles();
            } else {
                sourceFiles = buildOptions.getSourceFiles();
            }

            if (sourceFiles == null) {
                throw new BuildException("SourceFiles is null",
                        new NullPointerException());
            }

            temporaryOutputFile = getTemporaryFile();
            compileCssFiles();
            parseFunctionsFromCss();
        } catch (IOException | DependencyException exception) {
            throwBuildException(exception);
        }
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


    @Nullable
    /**
     * NOTE: this function may be removed later when gss compile is done in
     * memory.
     * @return The temporary file used for outputing the compiled gss.
     */
    public File getTemporaryOutputFile() {
        return temporaryOutputFile;
    }

    @Nullable
    /**
     * @return The rename map file generated by this build.
     */
    public File getGeneratedRenameMap() {
        return generatedRenameMap;
    }

    @Nullable
    /**
     * @return The final generated stylesheet file.
     */
    public File getGeneratedStylesheet() {
        return generatedStylesheet;
    }

    @Nullable
    /**
     * The calculated source files.
     */
    public List<File> getSourceFiles() {
        return sourceFiles;
    }
}
