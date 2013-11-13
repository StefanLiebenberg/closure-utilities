package org.stefanl.closure_utilities.internal;

import com.google.common.collect.ImmutableCollection;
import org.stefanl.closure_utilities.utilities.Immuter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class DependencyOptions<A extends BaseSourceFile> {

    private ImmutableCollection<A> sourceFiles;

    private ImmutableList<String> entryPoints;

    public void setSourceFiles(@Nonnull final Collection<A> sourceFiles) {
        if (sourceFiles instanceof ImmutableCollection) {
            this.sourceFiles = (ImmutableCollection<A>) sourceFiles;
        } else {
            this.sourceFiles = Immuter.set(sourceFiles);
        }
    }

    public void setEntryPoints(@Nonnull final List<String> entryPoints) {
        if (entryPoints instanceof ImmutableList) {
            this.entryPoints = (ImmutableList<String>) entryPoints;
        } else {
            this.entryPoints = Immuter.list(entryPoints);
        }
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
