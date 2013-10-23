package org.stefanl.closure_utilities.closure;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class ImmutableClosureBuildOptions extends AbstractClosureBuildOptions {
    public ImmutableClosureBuildOptions(
            @Nullable final ImmutableCollection<File> soySourceDirectories,
            @Nullable final File cssClassRenameMap,
            @Nullable final File assetsDirectory,
            @Nullable final ImmutableCollection<File> gssSourceDirectories,
            @Nullable final ImmutableList<File> externalStylesheets,
            @Nullable final ImmutableList<String> gssEntryPoints,
            @Nullable final ImmutableCollection<File>
                    javascriptSourceDirectories,
            @Nullable final ImmutableList<File> externalScripts,
            @Nullable final ImmutableList<String> javascriptEntryPoints,
            @Nullable final File javascriptDependencyOutputFile,
            @Nullable final File soyOutputDirectory,
            @Nullable final File outputDirectory,
            @Nullable final String htmlContent,
            @Nonnull final Boolean shouldCompile,
            @Nonnull final Boolean shouldDebug,
            @Nonnull final Boolean shouldInline) {
        this.soySourceDirectories = soySourceDirectories;
        this.cssClassRenameMap = cssClassRenameMap;
        this.assetsDirectory = assetsDirectory;
        this.gssSourceDirectories = gssSourceDirectories;
        this.gssExternalStylesheets = externalStylesheets;
        this.gssEntryPoints = gssEntryPoints;
        this.javascriptSourceDirectories = javascriptSourceDirectories;
        this.javascriptDependencyOutputFile = javascriptDependencyOutputFile;
        this.externalJavascriptFiles = externalScripts;
        this.javascriptEntryPoints = javascriptEntryPoints;
        this.soyOutputDirectory = soyOutputDirectory;
        this.outputDirectory = outputDirectory;
        this.htmlContent = htmlContent;
        this.shouldCompile = shouldCompile;
        this.shouldDebug = shouldDebug;
        this.shouldInline = shouldInline;
    }
}
