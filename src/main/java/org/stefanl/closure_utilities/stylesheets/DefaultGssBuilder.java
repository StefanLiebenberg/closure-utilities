package org.stefanl.closure_utilities.stylesheets;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.stefanl.closure_utilities.internal.DependencyException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DefaultGssBuilder
        extends AbstractGssBuilder
        implements GssBuilderInterface {

    public DefaultGssBuilder() {}

    public DefaultGssBuilder(@Nonnull iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    @Override
    public void reset() {
        super.reset();
    }


    @Override
    public void scan() throws Exception {
        final ImmutableCollection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        sourceFiles = scanInternal(sourceDirectories);
    }


    /**
     * Parses the found gss files and produces a list of resolved files.
     *
     * @throws IOException
     * @throws ReflectiveOperationException
     * @throws DependencyException
     */
    @Override
    public void parse() throws Exception {
        final ImmutableList<String> entryPoints = buildOptions.getEntryPoints();
        resolvedSourceFiles =
                parseInternal(sourceFiles, entryPoints, dependencyParser);
    }


    @Override
    public void compile() throws Exception {
        final File outputFile = buildOptions.getOutputFile();
        final File renameMap = buildOptions.getRenameMap();
        final Boolean shouldCompile =
                buildOptions.getShouldGenerateForProduction();
        final Boolean shouldDebug =
                buildOptions.getShouldGenerateForDebug();
        final URI assetUri = buildOptions.getAssetsUri();
        final File assetDir = buildOptions.getAssetsDirectory();
        generatedStylesheet =
                compileInternal(resolvedSourceFiles, outputFile, renameMap,
                        shouldCompile, shouldDebug, assetUri, assetDir);
        if (generatedStylesheet != null) {
            generatedRenameMap = renameMap;
        } else {
            generatedRenameMap = null;
        }
    }


}
