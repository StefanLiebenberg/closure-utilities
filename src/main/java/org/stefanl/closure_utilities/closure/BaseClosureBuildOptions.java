package org.stefanl.closure_utilities.closure;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public abstract class BaseClosureBuildOptions implements iClosureBuildOptions {


    protected ImmutableCollection<File> soySourceDirectories;

    @Nullable
    @Override
    public ImmutableCollection<File> getSoySourceDirectories() {
        return soySourceDirectories;
    }

    protected File cssClassRenameMap;

    @Nullable
    @Override
    public File getCssClassRenameMap() {
        return cssClassRenameMap;
    }

    protected File assetsDirectory;

    @Nullable
    @Override
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    protected ImmutableCollection<File> gssSourceDirectories;

    @Nullable
    @Override
    public ImmutableCollection<File> getGssSourceDirectories() {
        return gssSourceDirectories;
    }

    protected ImmutableList<File> gssExternalStylesheets;

    @Nullable
    @Override
    public ImmutableList<File> getExternalStylesheets() {
        return gssExternalStylesheets;
    }

    protected ImmutableList<String> gssEntryPoints;

    @Nullable
    @Override
    public ImmutableList<String> getGssEntryPoints() {
        return gssEntryPoints;
    }

    protected ImmutableCollection<File> javascriptSourceDirectories;

    @Nullable
    @Override
    public ImmutableCollection<File> getJavascriptSourceDirectories() {
        return javascriptSourceDirectories;
    }

    protected ImmutableList<File> externalJavascriptFiles;

    @Nullable
    @Override
    public ImmutableList<File> getExternalScriptFiles() {
        return externalJavascriptFiles;
    }

    protected ImmutableList<String> javascriptEntryPoints;


    @Nullable
    @Override
    public ImmutableList<String> getJavascriptEntryPoints() {
        return javascriptEntryPoints;
    }

    protected File soyOutputDirectory;

    @Nullable
    @Override
    public File getSoyOutputDirectory() {
        return soyOutputDirectory;
    }


    protected File outputDirectory;

    @Nullable
    @Override
    public File getOutputDirectory() {
        return outputDirectory;
    }


    protected Boolean shouldCompile = false;

    @Nonnull
    @Override
    public Boolean getShouldCompile() {
        return shouldCompile;
    }


    protected Boolean shouldDebug = false;

    @Nonnull
    @Override
    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    @Deprecated
    protected Boolean shouldInline = false;

    @Nonnull
    @Override
    @Deprecated
    public Boolean getShouldInline() {
        return shouldInline;
    }

    protected String htmlContent;

    @Override
    @Nullable
    public String getHtmlContent() {
        return htmlContent;
    }

}
