package org.stefanl.closure_utilities.stylesheets;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;
import org.stefanl.closure_utilities.internal.*;
import org.stefanl.closure_utilities.utilities.FS;
import org.stefanl.closure_utilities.utilities.FS;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GssBuilder
        extends AbstractBuilder<iGssBuildOption>
        implements BuilderInterface {

    public GssBuilder() {}

    public GssBuilder(@Nonnull final iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    private final ImageUrlProcessor imageUrlProcessor =
            new ImageUrlProcessor();


    private static final Function<File, GssSourceFile> FILE_TO_GSS =
            BaseSourceFile.getTransformFunction(GssSourceFile.class);

    private final GssDependencyParser gssDependencyParser =
            new GssDependencyParser();

    private DependencyLoader<GssSourceFile> gssDependencyLoader;

    private File temporaryOutputFile;

    private File generatedStylesheet;

    private File generatedRenameMap;

    private ImmutableList<File> sourceFiles;

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
            throw new BuildException("No input files specified.");
        }

        List<String> arguments = new ArrayList<String>();
        arguments.add("--allow-unrecognized-functions");
        arguments.add("--allow-unrecognized-properties");

        if (renameMap != null) {
            FS.ensureDirectoryFor(renameMap);
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
        FS.ensureDirectory(outputFile);
        arguments.add(FS.FILE_TO_FILEPATH.apply(outputFile));
        arguments.addAll(Immuter.list(sourceFiles, FS.FILE_TO_FILEPATH));
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
            return FS.getRelative(assetsDirectory,
                    outputFile.getParentFile());
        }

        return null;
    }

    public void parseFunctionsFromCss()
            throws IOException {
        final File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            final String content = FS.read(temporaryOutputFile);
            final String base = getBasePath(outputFile);
            FS.write(outputFile, parseCssFunctions(content, base));
            generatedStylesheet = outputFile;
        }
    }


    @Nonnull
    private File getTemporaryFile()
            throws BuildException {
        try {
            return FS.getTempFile("css_", "pass1");
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }


    private void calculateSourceFiles()
            throws DependencyException, IOException, IllegalAccessException,
            InstantiationException {
        final ImmutableCollection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        final ImmutableList<String> entryPoints =
                buildOptions.getEntryPoints();

        if (sourceDirectories != null && entryPoints != null) {
            final Collection<File> allSourceFiles =
                    FS.find(sourceDirectories, "gss");
            gssDependencyLoader =
                    new DependencyLoader<GssSourceFile>(gssDependencyParser,
                            allSourceFiles) {
                        @Override
                        protected GssSourceFile createDependency(File input) {
                            return FILE_TO_GSS.apply(input);
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

    private final static String UNSPECIFIED_OUTPUT_FILE =
            "Gss Output file is unspecified.";

    private final static String UNSPECIFIED_ENTRY_POINTS =
            "Gss Entry points are unspecified.";

    private final static String UNSPECIFIED_SOURCES =
            "Gss sources are unspecified.";


    @Override
    public void checkOptions() throws BuildOptionsException {
        super.checkOptions();

        final File outputFile = buildOptions.getOutputFile();
        if (outputFile == null) {
            throw new BuildOptionsException(UNSPECIFIED_OUTPUT_FILE);
        }

        final Boolean shouldCalculate =
                buildOptions.getShouldCalculateDependencies();

        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        final Boolean sourceDirectoriesAreSpecified =
                sourceDirectories != null && !sourceDirectories.isEmpty();
        final Collection<File> sourceFiles =
                buildOptions.getSourceFiles();
        final Boolean sourceFilesAreSpecified =
                sourceFiles != null && !sourceFiles.isEmpty();
        if (!sourceDirectoriesAreSpecified && !sourceFilesAreSpecified) {
            throw new BuildOptionsException(UNSPECIFIED_SOURCES);
        }


        final Collection<String> entryPoints = buildOptions.getEntryPoints();
        if (shouldCalculate && (entryPoints == null || entryPoints.isEmpty())) {
            throw new BuildOptionsException(UNSPECIFIED_ENTRY_POINTS);
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
