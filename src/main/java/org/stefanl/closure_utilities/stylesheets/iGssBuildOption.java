package org.stefanl.closure_utilities.stylesheets;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;

public interface iGssBuildOption {

    @Nullable
    public File getOutputFile();

    @Nullable
    public ImmutableList<File> getSourceFiles();

    @Nullable
    public File getRenameMap();

    @Nonnull
    public Boolean getShouldGenerateForProduction();

    @Nonnull
    public Boolean getShouldGenerateForDebug();

    @Nullable
    public ImmutableCollection<File> getSourceDirectories();

    @Nullable
    public ImmutableList<String> getEntryPoints();

    @Nullable
    public URI getAssetsUri();

    @Nullable
    public File getAssetsDirectory();
}
