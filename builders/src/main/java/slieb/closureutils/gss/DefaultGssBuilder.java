package slieb.closureutils.gss;


import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.net.URI;

@Immutable
public class DefaultGssBuilder extends AbstractGssBuilder {

    public DefaultGssBuilder(DependencyParser dependencyParser) {
        super(dependencyParser);
    }


    /**
     * Parses the found gss files and produces a list of resolved files.
     */
    @Override
    public void scan(@Nonnull GssOptions options,
                     @Nonnull GssResult.Builder builder)
            throws Exception {
        builder.setResolvedNodes(
                getDependencyScanner(options.getResourceProvider())
                        .getDependencies(options.getEntryPoints()));
    }


    @Override
    public void compile(@Nonnull GssOptions options,
                        @Nonnull GssResult.Builder builder)
            throws Exception {
        final Resource outputResource = options.getOutputResource();
        final Resource renameMap = options.getRenameMapResource();
        final Boolean shouldCompile =
                options.getShouldGenerateForProduction();
        final Boolean shouldDebug =
                options.getShouldGenerateForDebug();
        final URI assetUri = options.getAssetsUri();
        compileResources(outputResource, builder.resolvedNodes,
                renameMap, shouldCompile, shouldDebug, assetUri);
        if (outputResource != null) {
            builder.setGeneratedStylesheet(outputResource);
            builder.setGeneratedRenameMap(renameMap);
        } else {
            builder.setGeneratedStylesheet(null);
            builder.setGeneratedRenameMap(null);
        }
    }


}
