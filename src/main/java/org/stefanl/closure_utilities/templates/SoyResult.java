package org.stefanl.closure_utilities.templates;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.Map;
import java.util.Set;

@Immutable
public class SoyResult {

    private final File outputDirectory;

    private final ImmutableMap<File, String> compiledSources;

    private final ImmutableSet<File> generatedFiles;

    public SoyResult(@Nullable final File outputDirectory,
                     @Nullable final Map<File, String> compiledSources,
                     @Nullable final Set<File> generatedFiles) {
        this.outputDirectory = outputDirectory;
        if (compiledSources != null) {
            this.compiledSources = Immuter.map(compiledSources);
        } else {
            this.compiledSources = null;
        }
        if (generatedFiles != null) {
            this.generatedFiles = Immuter.set(generatedFiles);
        } else {
            this.generatedFiles = null;
        }
    }

    public ImmutableSet<File> getGeneratedFiles() {
        return generatedFiles;
    }

    public ImmutableMap<File, String> getCompiledSources() {
        return compiledSources;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
