package liebenberg.closure_utilities.build;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.net.URI;

@Immutable
public class DefaultGssBuilder extends AbstractGssBuilder {

    public DefaultGssBuilder() {}

    @Override
    public void scan(@Nonnull GssOptions options,
                     @Nonnull InternalData internalData)
            throws Exception {
        final ImmutableCollection<File> sourceDirectories =
                options.getSourceDirectories();
        internalData.sourceFiles = scanInternal(sourceDirectories);
    }


    /**
     * Parses the found gss files and produces a list of resolved files.
     */
    @Override
    public void parse(@Nonnull GssOptions options,
                      @Nonnull InternalData internalData)
            throws Exception {
        final ImmutableList<String> entryPoints = options.getEntryPoints();
        internalData.resolvedSourceFiles =
                parseInternal(internalData.sourceFiles, entryPoints,
                        dependencyParser);
    }


    @Override
    public void compile(@Nonnull GssOptions options,
                        @Nonnull InternalData internalData)
            throws Exception {
        final File outputFile = options.getOutputFile();
        final File renameMap = options.getRenameMap();
        final Boolean shouldCompile =
                options.getShouldGenerateForProduction();
        final Boolean shouldDebug =
                options.getShouldGenerateForDebug();
        final URI assetUri = options.getAssetsUri();
        final File assetDir = options.getAssetsDirectory();
        internalData.generatedStylesheet =
                compileInternal(internalData.resolvedSourceFiles,
                        outputFile, renameMap, shouldCompile, shouldDebug,
                        assetUri, assetDir);
        if (internalData.generatedStylesheet != null) {
            internalData.generatedRenameMap = renameMap;
        } else {
            internalData.generatedRenameMap = null;
        }
    }


}
