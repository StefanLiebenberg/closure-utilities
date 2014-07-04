package slieb.closureutils.gss;

import slieb.closureutils.dependencies.DependencyCalculator;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This build tries to cache it's values and skips builds when possible,
 * in future this is meant to go into a loop-type watch task.
 */
public class AdvancedGssBuilder extends AbstractGssBuilder {


    public AdvancedGssBuilder(DependencyParser dependencyParser) {
        super(dependencyParser);
    }


    private final List<String> cachedEntryPoints = new ArrayList<>();

    private final List<DependencyNode> cachedResolvedNodes = new ArrayList<>();

    private final Map<Resource, String> cachedChecksums =
            new Hashtable<>();

    private final Map<DependencyNode, String> cachedResolvedChecksums =
            new Hashtable<>();


    @Override
    public void scan(@Nonnull final GssOptions options,
                     @Nonnull final GssResult.Builder builder)
            throws Exception {
        builder.setResolvedNodes(getDependencyScanner(options
                .getResourceProvider())
                .getDependencies(options.getEntryPoints()));
    }

    @Override
    public void compile(@Nonnull final GssOptions options,
                        @Nonnull final GssResult.Builder resultBuilder)
            throws Exception {


        Boolean configHasChanged = false, resolvedFilesHaveChanged = false;

        if (!cachedResolvedNodes.equals(resultBuilder.resolvedNodes)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedNodes.clear();
            cachedResolvedNodes.addAll(resultBuilder.resolvedNodes);
        }

        final Map<DependencyNode, String> resolvedSums = new Hashtable<>();
        for (DependencyNode node : resultBuilder.resolvedNodes) {
            resolvedSums.put(node, cachedChecksums.get(node.getResource()));
        }

        if (!cachedResolvedChecksums.equals(resolvedSums)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedChecksums.clear();
            cachedResolvedChecksums.putAll(resolvedSums);
        }

        if (configHasChanged || resolvedFilesHaveChanged) {
            final Resource outputFile = options.getOutputResource();
            final Resource renameMap = options.getRenameMapResource();
            final Boolean shouldCompile =
                    options.getShouldGenerateForProduction();
            final Boolean shouldDebug =
                    options.getShouldGenerateForDebug();
            final URI assetUri = options.getAssetsUri();
            compileResources(outputFile, resultBuilder.resolvedNodes,
                    renameMap, shouldCompile, shouldDebug, assetUri);
            resultBuilder.setGeneratedStylesheet(outputFile);
            if (resultBuilder.generatedStylesheet != null) {
                resultBuilder.generatedRenameMap = renameMap;
            } else {
                resultBuilder.generatedRenameMap = null;
            }
        }
    }


}
