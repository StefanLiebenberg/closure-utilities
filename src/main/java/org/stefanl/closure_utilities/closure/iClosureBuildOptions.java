package org.stefanl.closure_utilities.closure;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public interface iClosureBuildOptions {

    @Nullable
    public ImmutableCollection<File> getGssSourceDirectories();

    @Nullable
    public ImmutableList<String> getGssEntryPoints();

    @Nullable
    public ImmutableCollection<File> getJavascriptSourceDirectories();

    @Nullable
    public ImmutableList<String> getJavascriptEntryPoints();

    @Nullable
    public ImmutableCollection<File> getSoySourceDirectories();

    @Nullable
    public File getSoyOutputDirectory();

    @Nullable
    public File getOutputDirectory();

    @Nonnull
    public Boolean getShouldCompile();


    @Nonnull
    public Boolean getShouldDebug();

    @Nonnull
    @Deprecated
    public Boolean getShouldInline();

    @Nullable
    public File getAssetsDirectory();

    @Nullable
    public File getCssClassRenameMap();

    @Nonnull
    public Boolean getIgnoreGssBuild();

    @Nonnull
    public Boolean getIgnoreSoyBuild();

    @Nonnull
    public Boolean getIgnoreJsBuild();

    @Nonnull
    public Boolean getIgnoreHtmlBuild();

    @Nonnull
    public Boolean getIgnoreBuilds();
}
