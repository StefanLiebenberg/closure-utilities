package com.github.stefanliebenberg.templates;


import com.github.stefanliebenberg.internal.IBuildOptions;
import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class SoyBuildOptions implements IBuildOptions {

    private File outputDirectory;

    private Collection<File> sources;

    private Collection<File> sourceDirectories;

    private Map<String, String> globalStringMap;

    private File messageFile;

    public void setOutputDirectory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSources(final Collection<File> sources) {
        this.sources = sources;
    }

    public void setSourceDirectories(final Collection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public void setMessageFile(final File messageFile) {
        this.messageFile = messageFile;
    }

    public void setGlobalStringMap(final Map<String, String> globalStringMap) {
        this.globalStringMap = globalStringMap;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public ImmutableCollection<File> getSources() {
        return Immuter.list(sources);
    }

    public ImmutableCollection<File> getSourceDirectories() {
        return Immuter.list(sourceDirectories);
    }

    public File getMessageFile() {
        return messageFile;
    }

    public ImmutableMap<String, String> getGlobalStringMap() {
        return Immuter.map(globalStringMap);
    }
}
