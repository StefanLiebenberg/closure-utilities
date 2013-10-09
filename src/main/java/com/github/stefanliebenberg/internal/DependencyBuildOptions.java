package com.github.stefanliebenberg.internal;

import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class DependencyBuildOptions<A extends BaseSourceFile> {

    private Collection<A> sourceFiles;

    private List<String> entryPoints;

    public void setSourceFiles(
            @Nonnull final Collection<A> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public void setEntryPoints(
            @Nonnull final List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    @Nullable
    public ImmutableSet<A> getSourceFiles() {
        return Immuter.set(sourceFiles);
    }

    @Nullable
    public ImmutableList<String> getEntryPoints() {
        return Immuter.list(entryPoints);
    }

}
