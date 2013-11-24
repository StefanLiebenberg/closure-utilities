package liebenberg.closure_utilities.stylesheets;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.apache.commons.codec.digest.DigestUtils;

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
public class AdvancedGssBuilder extends AbstractGssBuilder {

    private final Map<File, String> cachedChecksums = new Hashtable<>();

    private final Map<File, String> cachedResolvedChecksums = new
            Hashtable<>();

    private final List<String> cachedEntryPoints = new ArrayList<>();

    private final List<File> cachedResolvedFiles = new ArrayList<>();

    public AdvancedGssBuilder() {}

    @Override
    public void scan(@Nonnull final GssOptions options,
                     @Nonnull final InternalResults internalResults)
            throws Exception {
        final ImmutableCollection<File> sourceDirectories =
                options.getSourceDirectories();
        internalResults.sourceFiles = scanInternal(sourceDirectories);
    }

    @Nonnull
    private String getChecksum(@Nonnull final File inputFile) throws
            IOException {
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
    public void parse(@Nonnull GssOptions options,
                      @Nonnull InternalResults internalResults)
            throws Exception {
        final ImmutableList<String> entryPoints = options.getEntryPoints();
        Boolean entryPointsChanged = false, sourceFilesChanged = false;

        if (!cachedEntryPoints.equals(entryPoints)) {
            entryPointsChanged = true;
            cachedEntryPoints.clear();
            if (entryPoints != null) {
                cachedEntryPoints.addAll(entryPoints);
            }
        }

        final Map<File, String> checksums =
                getChecksums(internalResults.sourceFiles);
        if (!cachedChecksums.equals(checksums)) {
            sourceFilesChanged = true;
            cachedChecksums.clear();
            if (checksums != null) {
                cachedChecksums.putAll(checksums);
            }
        }

        if (entryPointsChanged || sourceFilesChanged) {
            internalResults.resolvedSourceFiles =
                    parseInternal(internalResults.sourceFiles,
                            entryPoints, dependencyParser);
        }
    }

    @Override
    public void compile(@Nonnull final GssOptions options,
                        @Nonnull final InternalResults internalResults)
            throws Exception {

        Boolean configHasChanged = false, resolvedFilesHaveChanged = false;
        if (!cachedResolvedFiles.equals(internalResults.resolvedSourceFiles)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedFiles.clear();
            cachedResolvedFiles.addAll(internalResults.resolvedSourceFiles);
        }

        final Map<File, String> resolvedChecksums = new Hashtable<>();
        for (File resolvedFile : internalResults.resolvedSourceFiles) {
            resolvedChecksums.put(resolvedFile,
                    cachedChecksums.get(resolvedFile));
        }

        if (!cachedResolvedChecksums.equals(resolvedChecksums)) {
            resolvedFilesHaveChanged = true;
            cachedResolvedChecksums.clear();
            cachedResolvedChecksums.putAll(resolvedChecksums);
        }

        if (configHasChanged || resolvedFilesHaveChanged) {
            final File outputFile = options.getOutputFile();
            final File renameMap = options.getRenameMap();
            final Boolean shouldCompile =
                    options.getShouldGenerateForProduction();
            final Boolean shouldDebug =
                    options.getShouldGenerateForDebug();
            final URI assetUri = options.getAssetsUri();
            final File assetDir = options.getAssetsDirectory();
            internalResults.generatedStylesheet =
                    compileInternal(internalResults.resolvedSourceFiles,
                            outputFile, renameMap, shouldCompile, shouldDebug,
                            assetUri, assetDir);
            if (internalResults.generatedStylesheet != null) {
                internalResults.generatedRenameMap = renameMap;
            } else {
                internalResults.generatedRenameMap = null;
            }
        }
    }


}
