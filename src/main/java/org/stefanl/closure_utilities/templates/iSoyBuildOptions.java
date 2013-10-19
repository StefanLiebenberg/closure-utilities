package org.stefanl.closure_utilities.templates;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.io.File;

public interface iSoyBuildOptions {

    @Nullable
    public File getOutputDirectory();

    @Nullable
    public File getMessageFile();

    @Nullable
    public ImmutableCollection<File> getSources();

    @Nullable
    public ImmutableCollection<File> getSourceDirectories();

    @Nullable
    public ImmutableMap<String, String> getGlobalStringMap();
}
