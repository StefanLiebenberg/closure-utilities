package slieb.closureutils.gss;


import com.google.common.collect.ImmutableSet;

import java.io.File;

public class GssResult {

    private final ImmutableSet<File> sourceFiles;

    private final File generatedStylesheet;

    private final File generatedRenameMap;

    private final ImmutableList<File> resolvedSourceFiles;

    public GssResult(
            @Nullable final ImmutableSet<File> sourceFiles,
            @Nullable final File generatedStylesheet,
            @Nullable final File generatedRenameMap,
            @Nullable final ImmutableList<File> resolvedSourceFiles) {
        this.sourceFiles = sourceFiles;
        this.generatedStylesheet = generatedStylesheet;
        this.generatedRenameMap = generatedRenameMap;
        this.resolvedSourceFiles = resolvedSourceFiles;
    }

    public ImmutableSet<File> getSourceFiles() {
        return sourceFiles;
    }

    public File getGeneratedStylesheet() {
        return generatedStylesheet;
    }

    public File getGeneratedRenameMap() {
        return generatedRenameMap;
    }

    public ImmutableList<File> getResolvedSourceFiles() {
        return resolvedSourceFiles;
    }
}
