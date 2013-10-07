package com.github.stefanliebenberg.closure;


import java.io.File;
import java.util.Collection;
import java.util.List;

public class ClosureBuildOptions {
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

    public Collection<File> getGssSourceDirectories() {
        return gssSourceDirectories;
    }

    public void setGssSourceDirectories(Collection<File> gssSourceDirectories) {
        this.gssSourceDirectories = gssSourceDirectories;
    }

    public List<String> getGssEntryPoints() {
        return gssEntryPoints;
    }

    public void setGssEntryPoints(List<String> gssEntryPoints) {
        this.gssEntryPoints = gssEntryPoints;
    }

    public Collection<File> getJavascriptSourceDirectories() {
        return javascriptSourceDirectories;
    }

    public void setJavascriptSourceDirectories(Collection<File>
                                                       javascriptSourceDirectories) {
        this.javascriptSourceDirectories = javascriptSourceDirectories;
    }

    public List<String> getJavascriptEntryPoints() {
        return javascriptEntryPoints;
    }

    public void setJavascriptEntryPoints(List<String> javascriptEntryPoints) {
        this.javascriptEntryPoints = javascriptEntryPoints;
    }

    public Collection<File> getSoySourceDirectories() {
        return soySourceDirectories;
    }

    public void setSoySourceDirectories(Collection<File> soySourceDirectories) {
        this.soySourceDirectories = soySourceDirectories;
    }

    public File getSoyOutputDirectory() {
        return soyOutputDirectory;
    }

    public void setSoyOutputDirectory(File soyOutputDirectory) {
        this.soyOutputDirectory = soyOutputDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Boolean getShouldCompile() {
        return shouldCompile;
    }

    public void setShouldCompile(Boolean shouldCompile) {
        this.shouldCompile = shouldCompile;
    }

    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    public Boolean getShouldInline() {
        return shouldInline;
    }

    public void setShouldInline(Boolean shouldInline) {
        this.shouldInline = shouldInline;
    }

    public void setShouldDebug(Boolean shouldDebug) {
        this.shouldDebug = shouldDebug;
    }

    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    public void setAssetsDirectory(File assetsDirectory) {
        this.assetsDirectory = assetsDirectory;
    }
}
