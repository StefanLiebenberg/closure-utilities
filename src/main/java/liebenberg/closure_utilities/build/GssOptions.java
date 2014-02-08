package liebenberg.closure_utilities.build;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import liebenberg.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;


public class GssOptions {

    private Boolean shouldCalculateDependencies = true;

    private Boolean shouldGenerateForProduction = false;

    private Boolean shouldGenerateForDebug = true;

    private ImmutableList<File> sourceFiles;

    private ImmutableCollection<File> sourceDirectories;

    private ImmutableList<String> entryPoints;

    private File renameMap;

    private File assetsDirectory;

    private URI assetsUri;

    private File outputFile;

    public void setOutputFile(@Nullable final File outputFile) {
        this.outputFile = outputFile;
    }

    public void setAssetsDirectory(@Nullable final File assetsDirectory) {
        this.assetsDirectory = assetsDirectory;
    }

    public void setSourceFiles(@Nullable final List<File> sourceFiles) {
        if (sourceFiles != null) {
            this.sourceFiles = Immuter.list(sourceFiles);
        } else {
            this.sourceFiles = null;
        }
    }

    public void setSourceFiles(@Nullable final ImmutableList<File>
                                       sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public void setRenameMap(@Nullable final File renameMap) {
        this.renameMap = renameMap;
    }

    public void setShouldGenerateForProduction(
            final Boolean shouldGenerateForProduction) {
        this.shouldGenerateForProduction = shouldGenerateForProduction;
    }

    public void setShouldGenerateForDebug(
            @Nonnull final Boolean shouldGenerateForDebug) {
        this.shouldGenerateForDebug = shouldGenerateForDebug;
    }

    @Nullable
    public File getOutputFile() {
        return outputFile;
    }

    @Nullable
    public ImmutableList<File> getSourceFiles() {
        return sourceFiles;
    }


    @Nullable
    public File getAssetsDirectory() {
        return assetsDirectory;
    }

    @Nullable
    public File getRenameMap() {
        return renameMap;
    }

    @Nonnull
    public Boolean getShouldGenerateForProduction() {
        return shouldGenerateForProduction;
    }


    @Nonnull
    public Boolean getShouldGenerateForDebug() {
        return shouldGenerateForDebug;
    }

    @Nonnull
    public Boolean getShouldCalculateDependencies() {
        return shouldCalculateDependencies;
    }

    public void setShouldCalculateDependencies(
            @Nonnull final Boolean shouldCalculateDependencies) {
        this.shouldCalculateDependencies = shouldCalculateDependencies;
    }

    @Nullable
    public ImmutableCollection<File> getSourceDirectories() {
        return sourceDirectories;
    }

    public void setSourceDirectories(
            @Nullable final Collection<File> sourceDirectories) {
        if (sourceDirectories != null) {
            this.sourceDirectories = Immuter.set(sourceDirectories);
        } else {
            this.sourceDirectories = null;
        }
    }

    public void setSourceDirectories(
            @Nullable final ImmutableCollection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    @Nullable
    public ImmutableList<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(
            @Nullable final List<String> entryPoints) {
        if (entryPoints != null) {
            this.entryPoints = Immuter.list(entryPoints);
        } else {
            this.entryPoints = null;
        }
    }

    public void setEntryPoints(
            @Nullable final ImmutableList<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    @Nullable
    public URI getAssetsUri() {
        return assetsUri;
    }

    public void setAssetsUri(@Nullable final URI assetsUri) {
        this.assetsUri = assetsUri;
    }
}
