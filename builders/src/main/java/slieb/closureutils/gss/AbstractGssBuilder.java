package slieb.closureutils.gss;


import slieb.blendercss.BlendOptions;
import slieb.blendercss.Blender;
import slieb.closureutils.build.BuildException;
import slieb.closureutils.build.BuilderInterface;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.dependencies.DependencyScanner;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;
import slieb.closureutils.resources.Resources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.io.Files.createTempDir;
import static java.nio.file.Files.createTempFile;
import static slieb.blendercss.Loader.getInjector;

@Immutable
public abstract class AbstractGssBuilder implements
        BuilderInterface<GssOptions, GssResult> {

    protected final DependencyParser dependencyParser;

    protected AbstractGssBuilder(DependencyParser dependencyParser) {
        this.dependencyParser = dependencyParser;
    }

    protected void buildInternal(GssOptions options, GssResult.Builder builder)
            throws Exception {
        scan(options, builder);
        compile(options, builder);
    }

    private Blender blender;

    private Blender getBlender() {
        if (blender == null) {
            blender = getInjector(createTempDir()).getInstance(Blender.class);
        }
        return blender;
    }

    protected void compileResources(
            @Nullable Resource outputResource,
            @Nullable List<DependencyNode> resources,
            @Nullable Resource renameMapResource,
            @Nonnull Boolean shouldCompile,
            @Nonnull Boolean shouldDebug,
            @Nullable URI assetUri)
            throws IOException, BuildException {

        if (resources != null && !resources.isEmpty()) {
            // Blender Css Does not understand resources, so we improvise.
            // todo, these files will need to be cleaned up.
            File outputFile = createTempFile("resource_", "tmp").toFile();
            outputFile.deleteOnExit();
            File renameMap = createTempFile("resource_", "tmp").toFile();
            renameMap.deleteOnExit();
            List<File> sourceFiles = new ArrayList<>();
            for (DependencyNode node : resources) {
                sourceFiles.add(Resources.copyResourceToTempFile(node
                                .getResource(),
                        ".gss"));
            }
            try {
                getBlender()
                        .compile(sourceFiles, outputFile,
                                new BlendOptions.Builder()
                                        .setOutputPath("")
                                        .setOutputCssRenameMap(renameMap)
                                        .setShouldCompile(shouldCompile)
                                        .setShouldDebug(shouldDebug)
                                        .setImagesPath(assetUri != null ?
                                                assetUri.getPath() : null)
                                        .build());

                // load back into resources.
                Resources.copyResourceFromFile(renameMapResource, renameMap);
                Resources.copyResourceFromFile(outputResource, outputFile);
            } catch (IOException e) {
                throw new BuildException(e);
            }


        }
    }

    protected DependencyScanner getDependencyScanner(
            ResourceProvider resourceProvider) {
        return new DependencyScanner(resourceProvider, dependencyParser);
    }


    protected abstract void scan(
            @Nonnull final GssOptions options,
            @Nonnull final GssResult.Builder builder)
            throws Exception;

    protected abstract void compile(
            @Nonnull final GssOptions options,
            @Nonnull final GssResult.Builder builder)
            throws Exception;


    @Override
    public GssResult build(GssOptions options) throws BuildException {
        try {
            GssResult.Builder builder = new GssResult.Builder();
            buildInternal(options, builder);
            return builder.build();
        } catch (Exception exception) {
            throw new BuildException(exception);
        }
    }
}
