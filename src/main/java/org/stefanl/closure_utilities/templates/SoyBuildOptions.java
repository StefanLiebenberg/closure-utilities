package org.stefanl.closure_utilities.templates;


import org.stefanl.closure_utilities.internal.IBuildOptions;
import org.stefanl.closure_utilities.utilities.Immuter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public class SoyBuildOptions implements IBuildOptions {

    private File outputDirectory;

    private Collection<File> sources;

    private Collection<File> sourceDirectories;

    private Map<String, String> globalStringMap;

    private File messageFile;

    public void setOutputDirectory(@Nullable final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSources(@Nullable final Collection<File> sources) {
        this.sources = sources;
    }

    public void setSourceDirectories(@Nullable final Collection<File>
                                             sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public void setMessageFile(@Nullable final File messageFile) {
        this.messageFile = messageFile;
    }

    public void setGlobalStringMap(@Nullable final Map<String,
            String> globalStringMap) {
        this.globalStringMap = globalStringMap;
    }

    @Nullable
    public File getOutputDirectory() {
        return outputDirectory;
    }

    @Nullable
    public ImmutableCollection<File> getSources() {
        if (sources != null) {
            return Immuter.list(sources);
        } else {
            return null;
        }
    }

    @Nullable
    public ImmutableCollection<File> getSourceDirectories() {
        if (sourceDirectories != null) {
            return Immuter.list(sourceDirectories);
        } else {
            return null;
        }
    }

    @Nullable
    public File getMessageFile() {
        return messageFile;
    }

    @Nullable
    public ImmutableMap<String, String> getGlobalStringMap() {
        if (globalStringMap != null) {
            return Immuter.map(globalStringMap);
        } else {
            return null;
        }
    }
}
