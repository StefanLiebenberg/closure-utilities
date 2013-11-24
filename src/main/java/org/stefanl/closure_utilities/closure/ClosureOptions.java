package org.stefanl.closure_utilities.closure;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;

public class ClosureOptions {

    private File javascriptOutputFile;

    private ImmutableCollection<File> soySourceDirectories;

    @Nullable
    public ImmutableCollection<File> getSoySourceDirectories() {
        return soySourceDirectories;
    }

    private File cssClassRenameMap;

    @Nullable
    public File getCssClassRenameMap() {
        return cssClassRenameMap;
    }

    private File assetsDirectory;

    @Nullable
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    private ImmutableCollection<File> gssSourceDirectories;

    @Nullable
    public ImmutableCollection<File> getGssSourceDirectories() {
        return gssSourceDirectories;
    }

    private ImmutableList<File> gssExternalStylesheets;

    @Nullable
    public ImmutableList<File> getExternalStylesheets() {
        return gssExternalStylesheets;
    }

    private ImmutableList<String> gssEntryPoints;

    @Nullable
    public ImmutableList<String> getGssEntryPoints() {
        return gssEntryPoints;
    }

    private ImmutableCollection<File> javascriptSourceDirectories;

    private ImmutableCollection<File> javascriptTestDirectories;

    @Nullable
    public ImmutableCollection<File> getJavascriptSourceDirectories(
            @Nonnull final Boolean includeTestDirectories) {
        ImmutableCollection.Builder<File> builder =
                new ImmutableSet.Builder<>();
        builder.addAll(javascriptSourceDirectories);
        if (includeTestDirectories) {
            builder.addAll(javascriptTestDirectories);
        }
        return builder.build();
    }

    private ImmutableList<File> externalJavascriptFiles;

    @Nullable
    public ImmutableList<File> getExternalScriptFiles() {
        return externalJavascriptFiles;
    }

    private ImmutableList<String> javascriptEntryPoints;


    @Nullable
    public ImmutableList<String> getJavascriptEntryPoints() {
        return javascriptEntryPoints;
    }

    private ImmutableList<File> javascriptEntryFiles;


    private File soyOutputDirectory;

    @Nullable

    public File getSoyOutputDirectory() {
        return soyOutputDirectory;
    }


    private File outputDirectory;

    @Nullable

    public File getOutputDirectory() {
        return outputDirectory;
    }


    private Boolean shouldCompile = false;

    @Nonnull

    public Boolean getShouldCompile() {
        return shouldCompile;
    }


    private Boolean shouldDebug = false;

    @Nonnull

    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    @Deprecated
    private Boolean shouldInline = false;

    @Nonnull
    @Deprecated
    public Boolean getShouldInline() {
        return shouldInline;
    }

    private String htmlContent;


    @Nullable
    public String getHtmlContent() {
        return htmlContent;
    }

    private File javascriptDependencyOutputFile;

    @Nullable
    public File getJavascriptDependencyOutputFile() {
        return javascriptDependencyOutputFile;
    }

    private File outputHtmlFile;

    @Nullable
    public File getOutputHtmlFile() {
        return outputHtmlFile;
    }


    private File outputStylesheetFile;

    @Nullable
    public File getOutputStylesheetFile() {
        return outputStylesheetFile;
    }

    private URI assetsUri;

    @Nullable
    public URI getAssetsUri() {
        return assetsUri;
    }

    public void setGssSourceDirectories(
            @Nullable final Collection<File> gssSourceDirectories) {
        if (gssSourceDirectories != null) {
            this.gssSourceDirectories = Immuter.set(gssSourceDirectories);
        } else {
            this.gssSourceDirectories = null;
        }
    }

    public void setExternalStylesheets(
            @Nullable final List<File> externalStylesheets) {
        if (externalStylesheets != null) {
            gssExternalStylesheets = Immuter.list(externalStylesheets);
        } else {
            gssExternalStylesheets = null;
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

    public void setExternalScripts(
            @Nullable final Collection<File> externalScripts) {
        if (externalScripts != null) {
            this.externalJavascriptFiles = Immuter.list(externalScripts);
        } else {
            this.externalJavascriptFiles = null;
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

    public void setSoyOutputDirectory(@Nonnull final File soyOutputDirectory) {
        this.soyOutputDirectory = soyOutputDirectory;
    }

    public void setOutputDirectory(@Nullable final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setShouldCompile(@Nonnull final Boolean shouldCompile) {
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

    public void setCssClassRenameMap(@Nullable final File cssClassRenameMap) {
        this.cssClassRenameMap = cssClassRenameMap;
    }

    public void setHtmlContent(@Nullable final String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public void setJavascriptDependencyOutputFile(
            @Nullable final File depsFile) {
        this.javascriptDependencyOutputFile = depsFile;
    }

    public void setOutputStylesheetFile(@Nullable final File cssFile) {
        this.outputStylesheetFile = cssFile;
    }

    public void setJavascriptTestDirectories(
            @Nullable final Collection<File> testDirectories) {
        if (testDirectories != null) {
            this.javascriptTestDirectories = Immuter.set(testDirectories);
        } else {
            this.javascriptTestDirectories = null;
        }
    }

    public File getJavascriptOutputFile() {
        return javascriptOutputFile;
    }

    public void setJavascriptOutputFile(File javascriptOutputFile) {
        this.javascriptOutputFile = javascriptOutputFile;
    }


    public void setOutputHtmlFile(File outputHtmlFile) {
        this.outputHtmlFile = outputHtmlFile;
    }

    public void setAssetsUri(URI assetsUri) {
        this.assetsUri = assetsUri;
    }

    public ImmutableList<File> getJavascriptEntryFiles() {
        return javascriptEntryFiles;
    }

    public void setJavascriptEntryFiles(
            @Nullable List<File> javascriptEntryFiles) {
        if (javascriptEntryFiles != null) {
            this.javascriptEntryFiles = Immuter.list(javascriptEntryFiles);
        } else {
            this.javascriptEntryFiles = null;
        }
    }
}
