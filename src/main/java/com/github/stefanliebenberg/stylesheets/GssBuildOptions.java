package com.github.stefanliebenberg.stylesheets;


import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;


public class GssBuildOptions {

    private Boolean shouldGenerateForProduction = false;

    private Boolean shouldGenerateForDebug = true;

    private ImmutableList<File> sourceFiles;

    private File renameMap;

    private File assetsDirectory;

    private File outputFile;

    public void setOutputFile(@Nullable final File outputFile) {
        this.outputFile = outputFile;
    }

    public void setAssetsDirectory(@Nullable final File assetsDirectory) {
        this.assetsDirectory = assetsDirectory;
    }

    public void setSourceFiles(@Nullable final ImmutableList<File>
                                       sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public void setSourceFiles(@Nullable final List<File> sourceFiles) {
        setSourceFiles(Immuter.list(sourceFiles));
    }

    public void setRenameMap(@Nullable final File renameMap) {
        this.renameMap = renameMap;
    }

    public void setShouldGenerateForProduction(
            final Boolean shouldGenerateForProduction) {
        this.shouldGenerateForProduction = shouldGenerateForProduction;
    }

    public void setShouldGenerateForDebug(
            @Nonnull final Boolean shouldGenerateForDebug) {
        this.shouldGenerateForDebug = shouldGenerateForDebug;
    }

    @Nullable
    public File getOutputFile() {
        return outputFile;
    }

    @Nullable
    public ImmutableList<File> getSourceFiles() {
        return sourceFiles;
    }

    @Nullable
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    @Nullable
    public File getRenameMap() {
        return renameMap;
    }

    @Nonnull
    public Boolean getShouldGenerateForProduction() {
        return shouldGenerateForProduction;
    }


    @Nonnull
    public Boolean getShouldGenerateForDebug() {
        return shouldGenerateForDebug;
    }

}
