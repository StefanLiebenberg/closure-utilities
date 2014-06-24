package slieb.closure.build.gss;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import slieb.blendercss.BlendOptions;
import slieb.blendercss.Blender;
import slieb.closure.build.internal.AbstractBuilder;
import slieb.closure.build.internal.BuildException;
import slieb.closure.build.internal.SourceFileBase;
import slieb.closure.dependency.stylesheets.StylesheetDependencyParser;
import slieb.closure.dependency.stylesheets.StylesheetResourceProvider;
import slieb.closure.html.models.Stylesheet;
import slieb.closure.internal.DependencyException;
import slieb.closure.internal.DependencyLoader;
import slieb.closure.internal.GssDependencyParser;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static slieb.blendercss.Loader.getInjector;

@Immutable
public abstract class AbstractGssBuilder
        extends AbstractBuilder<GssOptions, GssResult> {

    protected static final Function<File, GssSourceFile> FILE_TO_GSS =
            SourceFileBase.getTransformFunction(GssSourceFile.class);




    public abstract void scan(
            @Nonnull final GssOptions options,
            @Nonnull final InternalData internalData)
            throws Exception;

    public abstract void parse(
            @Nonnull final GssOptions options,
            @Nonnull final InternalData internalData)
            throws Exception;

    public abstract void compile(
            @Nonnull final GssOptions options,
            @Nonnull final InternalData internalData)
            throws Exception;



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
    protected ImmutableSet<File> findSourceFiles(
            @Nonnull final Collection<File> directories)
            throws IOException {
        return findSourceFiles(directories, "gss", "sass", "scss");
        // later we can add css support aswell.
    }


    private Blender blender;

    private synchronized Blender getBlender() {
        if (blender == null) {
            blender = getInjector(Files.createTempDir()).getInstance(Blender.class);
        }
        return blender;
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
            try {
                getBlender()
                        .compile(resolvedFiles, outputFile,
                                new BlendOptions.Builder()
                                        .setOutputCssRenameMap(renameMap)
                                        .setShouldCompile(shouldCompile)
                                        .setShouldDebug(shouldDebug)
                                        .setImagesPath(assetUri != null ? assetUri.getPath() : null)
                                        .build());
            } catch (IOException e) {
                throw new BuildException(e);
            }

            return outputFile;
        } else {
            return null;
        }
    }


    @Nullable
    protected ImmutableSet<File> scanInternal(
            @Nullable final Collection<File> directories) throws IOException {

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
    @Override
    protected GssResult buildInternal(
            @Nonnull final GssOptions options)
            throws Exception {
        final InternalData internalData = new InternalData();



        StylesheetResourceProvider dependecyResourceProvider  =
                new StylesheetResourceProvider();


        scan(options, internalData);
        parse(options, internalData);
        compile(options, internalData);
        return internalData.toResults();
    }


    private final static String UNSPECIFIED_OUTPUT_FILE =
            "Gss Output file is unspecified.";

    private final static String UNSPECIFIED_ENTRY_POINTS =
            "Gss Entry points are unspecified.";

    private final static String UNSPECIFIED_SOURCES =
            "Gss sources are unspecified.";


    @Override
    public void checkOptions(@Nonnull GssOptions options)
            throws BuildException {
        LOGGER.info("Checking gss options");

        final File outputFile = options.getOutputFile();
        if (outputFile == null) {
            throw new BuildException(UNSPECIFIED_OUTPUT_FILE);
        }

        final Collection<File> sourceDirectories =
                options.getSourceDirectories();
        final Boolean sourceDirectoriesAreSpecified =
                sourceDirectories != null && !sourceDirectories.isEmpty();
        final Collection<File> sourceFiles =
                options.getSourceFiles();
        final Boolean sourceFilesAreSpecified =
                sourceFiles != null && !sourceFiles.isEmpty();
        if (!sourceDirectoriesAreSpecified && !sourceFilesAreSpecified) {
            throw new BuildException(UNSPECIFIED_SOURCES);
        }

        final Collection<String> entryPoints = options.getEntryPoints();
        if (entryPoints == null || entryPoints.isEmpty()) {
            throw new BuildException(UNSPECIFIED_ENTRY_POINTS);
        }
    }

    public static class InternalData {
        public ImmutableList<File> resolvedSourceFiles;
        public ImmutableSet<File> sourceFiles;
        public File generatedRenameMap;
        public File generatedStylesheet;

        public GssResult toResults() {
            return new GssResult(sourceFiles, generatedStylesheet,
                    generatedRenameMap, resolvedSourceFiles);
        }
    }

}
