package org.stefanl.closure_utilities.closure;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class ImmutableClosureBuildOptions extends BaseClosureBuildOptions {
    public ImmutableClosureBuildOptions(
            @Nullable final ImmutableCollection<File> soySourceDirectories,
            @Nullable final File cssClassRenameMap,
            @Nullable final File assetsDirectory,
            @Nullable final ImmutableCollection<File> gssSourceDirectories,
            @Nullable final ImmutableList<String> gssEntryPoints,
            @Nullable final ImmutableCollection<File>
                    javascriptSourceDirectories,
            @Nullable final ImmutableList<String> javascriptEntryPoints,
            @Nullable final File soyOutputDirectory,
            @Nullable final File outputDirectory,
            @Nonnull final Boolean shouldCompile,
            @Nonnull final Boolean shouldDebug,
            @Nonnull final Boolean shouldInline,
            @Nonnull final Boolean ignoreGssBuild,
            @Nonnull final Boolean ignoreSoyBuild,
            @Nonnull final Boolean ignoreJsBuild,
            @Nonnull final Boolean ignoreHtmlBuild) {
        this.soySourceDirectories = soySourceDirectories;
        this.cssClassRenameMap = cssClassRenameMap;
        this.assetsDirectory = assetsDirectory;
        this.gssSourceDirectories = gssSourceDirectories;
        this.gssEntryPoints = gssEntryPoints;
        this.javascriptSourceDirectories = javascriptSourceDirectories;
        this.javascriptEntryPoints = javascriptEntryPoints;
        this.soyOutputDirectory = soyOutputDirectory;
        this.outputDirectory = outputDirectory;
        this.shouldCompile = shouldCompile;
        this.shouldDebug = shouldDebug;
        this.shouldInline = shouldInline;
        this.ignoreGssBuild = ignoreGssBuild;
        this.ignoreSoyBuild = ignoreSoyBuild;
        this.ignoreJsBuild = ignoreJsBuild;
        this.ignoreHtmlBuild = ignoreHtmlBuild;
    }
}
