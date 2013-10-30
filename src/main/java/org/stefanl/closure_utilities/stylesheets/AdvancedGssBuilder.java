package org.stefanl.closure_utilities.stylesheets;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.apache.commons.codec.digest.DigestUtils;
import org.stefanl.closure_utilities.internal.BuilderInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * This build tries to cache it's values and skips builds when possible,
 * in future this is meant to go into a loop-type watch task.
 */
public class AdvancedGssBuilder
        extends AbstractGssBuilder
        implements BuilderInterface, GssBuilderInterface {

    public AdvancedGssBuilder() {}

    public AdvancedGssBuilder(@Nonnull final iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    private final Map<File, String> cachedChecksums = new Hashtable<>();

    private final Map<File, String> cachedResolvedChecksums = new Hashtable<>();

    private final List<String> cachedEntryPoints = new ArrayList<>();

    private final List<File> cachedResolvedFiles = new ArrayList<>();

    @Override
    public void reset() {
        super.reset();
        cachedChecksums.clear();
        cachedEntryPoints.clear();
        cachedResolvedFiles.clear();
        cachedResolvedChecksums.clear();
    }

    @Override
    public void scan() throws Exception {
        final ImmutableCollection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        sourceFiles = scanInternal(sourceDirectories);
    }

    @Nonnull
    private String getChecksum(@Nonnull File inputFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            return DigestUtils.md5Hex(inputStream);
        }
    }

    @Nullable
    private Map<File, String> getChecksums(
            @Nullable final Collection<File> files)
            throws IOException {
        if (files != null) {
            final Map<File, String> sums = new Hashtable<>();
            for (File file : files) {
                sums.put(file, getChecksum(file));
            }
            return sums;
        } else {
            return null;
        }
    }

    @Override
    public void parse() throws Exception {

        final ImmutableList<String> currentEntryPoints =
                buildOptions.getEntryPoints();

        Boolean entryPointsChanged = false;
        if (!cachedEntryPoints.equals(currentEntryPoints)) {
            entryPointsChanged = true;
            cachedEntryPoints.clear();
            if (currentEntryPoints != null) {
                cachedEntryPoints.addAll(currentEntryPoints);
            }
        }

        Boolean sourceFilesChanged = false;
        final Map<File, String> currentChecksums = getChecksums(sourceFiles);
        if (!cachedChecksums.equals(currentChecksums)) {
            sourceFilesChanged = true;
            cachedChecksums.clear();
            if (currentChecksums != null) {
                cachedChecksums.putAll(currentChecksums);
            }
        }

        if (entryPointsChanged || sourceFilesChanged) {
            resolvedSourceFiles =
                    parseInternal(sourceFiles, currentEntryPoints,
                            dependencyParser);
        }
    }

    @Override
    public void compile() throws Exception {

        Boolean configHasChanged = false;

        Boolean resolvedFilesHaveChanged = false;

        if (!cachedResolvedFiles.equals(resolvedSourceFiles)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedFiles.clear();
            cachedResolvedFiles.addAll(resolvedSourceFiles);
        }

        final Map<File, String> resolvedChecksums = new Hashtable<>();
        for (File resolvedFile : resolvedSourceFiles) {
            resolvedChecksums.put(resolvedFile,
                    cachedChecksums.get(resolvedFile));
        }

        if (!cachedResolvedChecksums.equals(resolvedChecksums)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedChecksums.clear();
            cachedResolvedChecksums.putAll(resolvedChecksums);
        }

        if (configHasChanged || resolvedFilesHaveChanged) {
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


}
