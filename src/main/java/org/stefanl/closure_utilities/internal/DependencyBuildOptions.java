package org.stefanl.closure_utilities.internal;

import org.stefanl.closure_utilities.utilities.Immuter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
