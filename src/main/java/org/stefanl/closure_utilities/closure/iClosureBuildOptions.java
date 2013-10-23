package org.stefanl.closure_utilities.closure;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;

public interface iClosureBuildOptions {

    @Nullable
    public ImmutableCollection<File> getGssSourceDirectories();

    @Nullable
    public ImmutableList<File> getExternalStylesheets();

    @Nullable
    public ImmutableList<String> getGssEntryPoints();

    @Nullable
    public ImmutableCollection<File> getJavascriptSourceDirectories();

    @Nullable
    public ImmutableList<File> getExternalScriptFiles();

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
    public URI getAssetsUri();

    @Nullable
    public File getCssClassRenameMap();

    @Nullable
    public File getOutputHtmlFile();

    @Nullable
    public File getJavascriptDependencyOutputFile();

    @Nullable
    public File getOutputStylesheetFile();

    @Nullable
    public String getHtmlContent();
}
