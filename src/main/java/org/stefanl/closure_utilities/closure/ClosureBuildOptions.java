package org.stefanl.closure_utilities.closure;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class ClosureBuildOptions
        extends BaseClosureBuildOptions
        implements iClosureBuildOptions {

    public void setGssSourceDirectories(
            @Nullable final Collection<File> gssSourceDirectories) {
        if (gssSourceDirectories != null) {
            this.gssSourceDirectories = Immuter.set(gssSourceDirectories);
        } else {
            this.gssSourceDirectories = null;
        }
    }


    public void setGssSourceDirectories(
            @Nullable final ImmutableCollection<File> gssSourceDirectories) {
        this.gssSourceDirectories = gssSourceDirectories;
    }


    public void setGssEntryPoints(
            @Nullable final List<String> gssEntryPoints) {
        if (gssEntryPoints != null) {
            this.gssEntryPoints = Immuter.list(gssEntryPoints);
        } else {
            this.gssEntryPoints = null;
        }
    }

    public void setGssEntryPoints(
            @Nullable final ImmutableList<String> gssEntryPoints) {
        this.gssEntryPoints = gssEntryPoints;
    }


    public void setJavascriptSourceDirectories(
            @Nullable final Collection<File> javascriptSourceDirectories) {
        if (javascriptSourceDirectories != null) {
            this.javascriptSourceDirectories =
                    Immuter.set(javascriptSourceDirectories);
        } else {
            this.javascriptSourceDirectories = null;
        }
    }


    public void setJavascriptEntryPoints(
            @Nullable final List<String> javascriptEntryPoints) {
        if (javascriptEntryPoints != null) {
            this.javascriptEntryPoints = Immuter.list(javascriptEntryPoints);
        } else {
            this.javascriptSourceDirectories = null;
        }
    }


    public void setSoySourceDirectories(
            @Nullable final Collection<File> soySourceDirectories) {
        if (soySourceDirectories != null) {
            this.soySourceDirectories = Immuter.set(soySourceDirectories);
        } else {
            this.soySourceDirectories = null;
        }
    }


    public void setSoyOutputDirectory(
            @Nonnull final File soyOutputDirectory) {
        this.soyOutputDirectory = soyOutputDirectory;
    }


    public void setOutputDirectory(
            @Nullable final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }


    public void setShouldCompile(
            @Nonnull final Boolean shouldCompile) {
        this.shouldCompile = shouldCompile;
    }


    public void setShouldInline(@Nonnull final Boolean shouldInline) {
        this.shouldInline = shouldInline;
    }


    public void setShouldDebug(@Nonnull final Boolean shouldDebug) {
        this.shouldDebug = shouldDebug;
    }


    public void setAssetsDirectory(@Nullable final File assetsDirectory) {
        this.assetsDirectory = assetsDirectory;
    }


    public void setCssClassRenameMap(
            @Nullable final File cssClassRenameMap) {
        this.cssClassRenameMap = cssClassRenameMap;
    }


    public void setIgnoreGssBuild(@Nonnull final Boolean ignoreGssBuild) {
        this.ignoreGssBuild = ignoreGssBuild;
    }


    public void setIgnoreSoyBuild(@Nonnull final Boolean ignoreSoyBuild) {
        this.ignoreSoyBuild = ignoreSoyBuild;
    }


    public void setIgnoreJsBuild(@Nonnull final Boolean ignoreJsBuild) {
        this.ignoreJsBuild = ignoreJsBuild;
    }


    public void setIgnoreHtmlBuild(@Nonnull final Boolean ignoreHtmlBuild) {
        this.ignoreHtmlBuild = ignoreHtmlBuild;
    }

    public void setIgnoreBuilds() {
        this.setIgnoreGssBuild(true);
        this.setIgnoreJsBuild(true);
        this.setIgnoreSoyBuild(true);
        this.setIgnoreHtmlBuild(true);
    }
}
