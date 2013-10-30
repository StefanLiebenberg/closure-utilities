package org.stefanl.closure_utilities.stylesheets;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;
import org.stefanl.closure_utilities.internal.*;
import org.stefanl.closure_utilities.utilities.FS;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class AbstractGssBuilder
        extends AbstractBuilder<iGssBuildOption>
        implements BuilderInterface, GssBuilderInterface {

    protected static final String GSS_EXT = "gss";

    protected static final Function<File, GssSourceFile> FILE_TO_GSS =
            BaseSourceFile.getTransformFunction(GssSourceFile.class);

    protected static final GssDependencyParser dependencyParser =
            new GssDependencyParser();

    protected static final ImageUrlProcessor imageUrlProcessor =
            new ImageUrlProcessor();

    protected ImmutableSet<File> sourceFiles;

    protected File generatedStylesheet;

    protected File generatedRenameMap;

    protected ImmutableList<File> resolvedSourceFiles;

    protected AbstractGssBuilder() { }

    protected AbstractGssBuilder(@Nonnull final iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    public abstract void scan() throws Exception;

    public abstract void parse() throws Exception;

    public abstract void compile() throws Exception;

    @Override
    public void reset() {
        super.reset();
        sourceFiles = null;
        generatedStylesheet = null;
        generatedRenameMap = null;
        resolvedSourceFiles = null;
    }

    @Nonnull
    protected DependencyLoader<GssSourceFile> getDependencyLoader(
            @Nonnull GssDependencyParser parser,
            @Nonnull Collection<File> sourceFiles)
            throws IOException, ReflectiveOperationException {
        return new DependencyLoader<GssSourceFile>(parser, sourceFiles) {
            @Override
            protected GssSourceFile createDependency(
                    @Nonnull final File input) {
                return FILE_TO_GSS.apply(input);
            }
        };
    }

    @Nullable
    protected String getBasePath(@Nullable URI assetsUri,
                                 @Nullable File assetsDirectory,
                                 @Nonnull File outputDirectory) {

        if (assetsUri != null) {
            return assetsUri.toString();
        }

        if (assetsDirectory != null) {
            // TODO, should consider a location map here.
            return FS.getRelative(assetsDirectory, outputDirectory);
        }

        return null;
    }

    @Nonnull
    protected File getTemporaryFile() throws IOException {
        return FS.getTempFile("css_", "pass1");
    }

    @Nonnull
    protected ImmutableSet<File> findSourceFiles(
            @Nonnull final ImmutableCollection<File> directories)
            throws IOException {
        final ImmutableSet.Builder<File> builder = new ImmutableSet.Builder<>();
        for (File sourceFile : FS.find(directories, GSS_EXT)) {
            builder.add(sourceFile.getAbsoluteFile());
        }
        return builder.build();
    }

    /**
     * @param sourceFiles
     * @param outputFile
     * @param renameMap
     * @param productionBoolean
     * @param debugBoolean
     * @throws org.stefanl.closure_utilities.internal.BuildException
     *          SideEffects:
     *          If it builds a rename map it will set
     *          generateRenameMap.
     */
    protected void compileSourceFiles(
            @Nullable final List<File> sourceFiles,
            @Nonnull final File outputFile,
            @Nullable final File renameMap,
            @Nonnull final Boolean productionBoolean,
            @Nonnull final Boolean debugBoolean)
            throws BuildException {

        if (sourceFiles == null || sourceFiles.isEmpty()) {
            throw new BuildException("No input files specified.");
        }

        final ImmutableList.Builder<String> arguments =
                new ImmutableList.Builder<>();

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

        FS.ensureDirectory(outputFile);
        arguments.add("--output-file");
        arguments.add(FS.FILE_TO_FILEPATH.apply(outputFile));

        arguments.addAll(Immuter.list(sourceFiles, FS.FILE_TO_FILEPATH));
        ClosureCommandLineCompiler.main(Immuter.stringArray(arguments.build()));
    }

    @Nullable
    protected File compileInternal(
            @Nullable List<File> resolvedFiles,
            @Nullable File outputFile,
            @Nullable File renameMap,
            @Nonnull Boolean shouldCompile,
            @Nonnull Boolean shouldDebug,
            @Nullable URI assetUri,
            @Nullable File assetDir)
            throws IOException, BuildException {
        if (resolvedFiles != null && !resolvedFiles.isEmpty()) {
            File tempFile = getTemporaryFile();
            compileSourceFiles(
                    resolvedFiles,
                    tempFile,
                    renameMap,
                    shouldCompile,
                    shouldDebug);
            return parseFunctionsFromCss(tempFile, outputFile, assetUri,
                    assetDir);
        } else {
            return null;
        }
    }

    @Nullable
    protected File parseFunctionsFromCss(
            @Nonnull File tempFile,
            @Nullable File outputFile,
            @Nullable URI assetUri,
            @Nullable File assetDir)
            throws IOException {
        if (outputFile != null) {
            if (!outputFile.isAbsolute()) {
                outputFile = outputFile.getAbsoluteFile();
            }
            final String content = FS.read(tempFile);
            final String base = getBasePath(assetUri, assetDir,
                    outputFile.getParentFile());
            FS.write(outputFile, parseCssFunctions(content, base));
            return outputFile;
        } else {
            return null;
        }
    }

    @Nullable
    protected ImmutableSet<File> scanInternal(
            @Nullable final ImmutableCollection<File> directories)
            throws IOException {
        if (directories != null && !directories.isEmpty()) {
            return findSourceFiles(directories);
        } else {
            return null;
        }
    }

    @Nullable
    protected ImmutableList<File> parseInternal(
            @Nullable final Set<File> files,
            @Nullable final List<String> entryPoints,
            @Nonnull final GssDependencyParser parser)
            throws DependencyException, IOException,
            ReflectiveOperationException {
        if (entryPoints != null && files != null) {
            final DependencyLoader<GssSourceFile> dependencyLoader =
                    getDependencyLoader(parser, files);
            return dependencyLoader.getDependenciesFor(entryPoints);
        } else {
            return null;
        }
    }

    @Nonnull
    protected String parseCssFunctions(
            @Nonnull final String inputContent,
            @Nullable final String base) {
        return imageUrlProcessor.processString(inputContent, base);
    }

    @Override
    public void buildInternal() throws Exception {
        scan();
        parse();
        compile();
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
        if (entryPoints == null || entryPoints.isEmpty()) {
            throw new BuildOptionsException(UNSPECIFIED_ENTRY_POINTS);
        }

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
    public ImmutableList<File> getResolvedSourceFiles() {
        return resolvedSourceFiles;
    }


}
