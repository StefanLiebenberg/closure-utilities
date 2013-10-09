package com.github.stefanliebenberg.closure;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class ClosureBuildOptions {
    private File cssClassRenameMap;
    private File assetsDirectory;
    private Collection<File> gssSourceDirectories;
    private List<String> gssEntryPoints;
    private Collection<File> javascriptSourceDirectories;
    private List<String> javascriptEntryPoints;
    private Collection<File> soySourceDirectories;
    private File soyOutputDirectory;
    private File outputDirectory;
    private Boolean shouldCompile;
    private Boolean shouldDebug;
    private Boolean shouldInline;

    public ClosureBuildOptions() {
        shouldDebug = true;
        shouldInline = false;
        shouldCompile = false;
    }

    @Nullable
    public Collection<File> getGssSourceDirectories() {
        return gssSourceDirectories;
    }

    public void setGssSourceDirectories(
            @Nullable final Collection<File> gssSourceDirectories) {
        this.gssSourceDirectories = gssSourceDirectories;
    }

    @Nullable
    public List<String> getGssEntryPoints() {
        return gssEntryPoints;
    }

    public void setGssEntryPoints(@Nullable final List<String> gssEntryPoints) {
        this.gssEntryPoints = gssEntryPoints;
    }

    @Nullable
    public Collection<File> getJavascriptSourceDirectories() {
        return javascriptSourceDirectories;
    }

    public void setJavascriptSourceDirectories(
            @Nullable final Collection<File> javascriptSourceDirectories) {
        this.javascriptSourceDirectories = javascriptSourceDirectories;
    }

    @Nullable
    public List<String> getJavascriptEntryPoints() {
        return javascriptEntryPoints;
    }

    public void setJavascriptEntryPoints(@Nullable final List<String>
                                                 javascriptEntryPoints) {
        this.javascriptEntryPoints = javascriptEntryPoints;
    }

    @Nullable
    public Collection<File> getSoySourceDirectories() {
        return soySourceDirectories;
    }

    public void setSoySourceDirectories(
            @Nullable final Collection<File> soySourceDirectories) {
        this.soySourceDirectories = soySourceDirectories;
    }

    @Nullable
    public File getSoyOutputDirectory() {
        return soyOutputDirectory;
    }

    public void setSoyOutputDirectory(
            @Nonnull final File soyOutputDirectory) {
        this.soyOutputDirectory = soyOutputDirectory;
    }

    @Nullable
    public File getOutputDirectory() {
        return outputDirectory;
    }


    public void setOutputDirectory(
            @Nullable final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Nonnull
    public Boolean getShouldCompile() {
        return shouldCompile;
    }


    public void setShouldCompile(
            @Nonnull final Boolean shouldCompile) {
        this.shouldCompile = shouldCompile;
    }

    @Nonnull
    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    @Nonnull
    public Boolean getShouldInline() {
        return shouldInline;
    }

    public void setShouldInline(@Nonnull final Boolean shouldInline) {
        this.shouldInline = shouldInline;
    }


    public void setShouldDebug(@Nonnull final Boolean shouldDebug) {
        this.shouldDebug = shouldDebug;
    }

    @Nullable
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    public void setAssetsDirectory(@Nullable final File assetsDirectory) {
        this.assetsDirectory = assetsDirectory;
    }

    @Nullable
    public File getCssClassRenameMap() {
        return cssClassRenameMap;
    }

    public void setCssClassRenameMap(
            @Nullable final File cssClassRenameMap) {
        this.cssClassRenameMap = cssClassRenameMap;
    }
}
