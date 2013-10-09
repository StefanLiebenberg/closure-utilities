package com.github.stefanliebenberg.javascript;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class JsBuildOptions {

    private Boolean shouldCalculateDependencies = true;
    private File outputDependencyFile;
    private Collection<File> sourceDirectories;
    private List<File> sourceFiles;
    private List<String> entryPoints;

    @Nullable
    public Collection<File> getSourceDirectories() {
        return sourceDirectories;
    }

    public void setSourceDirectories(
            @Nullable final Collection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }


    @Nullable
    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(@Nullable final List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }


    @Nonnull
    public Boolean getShouldCalculateDependencies() {
        return shouldCalculateDependencies;
    }

    public void setShouldCalculateDependencies(
            @Nonnull Boolean shouldCalculateDependencies) {
        this.shouldCalculateDependencies = shouldCalculateDependencies;
    }

    @Nullable
    public List<File> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(
            @Nullable final List<File> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    @Nullable
    public File getOutputDependencyFile() {
        return outputDependencyFile;
    }

    public void setOutputDependencyFile(
            @Nullable final File outputDependencyFile) {
        this.outputDependencyFile = outputDependencyFile;
    }
}
