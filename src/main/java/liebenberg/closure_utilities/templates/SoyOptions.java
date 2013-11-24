package liebenberg.closure_utilities.templates;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import liebenberg.closure_utilities.utilities.Immuter;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public class SoyOptions {

    private File outputDirectory;

    private ImmutableCollection<File> sources;

    private ImmutableCollection<File> sourceDirectories;

    private ImmutableMap<String, String> globalStringMap;

    private File messageFile;

    public void setOutputDirectory(@Nullable final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSources(@Nullable final Collection<File> sources) {
        if (sources != null) {
            this.sources = Immuter.set(sources);
        } else {
            this.sources = null;
        }
    }

    public void setSources(@Nullable final ImmutableCollection<File> sources) {
        this.sources = sources;
    }

    public void setSourceDirectories(
            @Nullable final Collection<File> sourceDirectories) {
        if (sourceDirectories != null) {
            this.sourceDirectories = Immuter.set(sourceDirectories);
        } else {
            this.sourceDirectories = null;
        }
    }

    public void setSourceDirectories(
            @Nullable final ImmutableCollection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public void setMessageFile(@Nullable final File messageFile) {
        this.messageFile = messageFile;
    }

    public void setGlobalStringMap(
            @Nullable final Map<String, String> globalStringMap) {
        if (globalStringMap != null) {
            this.globalStringMap = Immuter.map(globalStringMap);
        } else {
            this.globalStringMap = null;
        }
    }

    public void setGlobalStringMap(
            @Nullable final ImmutableMap<String, String> globalStringMap) {
        this.globalStringMap = globalStringMap;
    }

    @Nullable
    public File getOutputDirectory() {
        return outputDirectory;
    }

    @Nullable
    public ImmutableCollection<File> getSources() {
        return sources;
    }

    @Nullable
    public ImmutableCollection<File> getSourceDirectories() {
        return sourceDirectories;
    }

    @Nullable
    public File getMessageFile() {
        return messageFile;
    }

    @Nullable
    public ImmutableMap<String, String> getGlobalStringMap() {
        return globalStringMap;
    }
}
