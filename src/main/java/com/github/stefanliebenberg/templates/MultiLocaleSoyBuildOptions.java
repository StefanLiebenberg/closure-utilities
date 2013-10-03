package com.github.stefanliebenberg.templates;

import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.collect.ImmutableCollection;

import java.io.File;
import java.util.Collection;

public class MultiLocaleSoyBuildOptions extends SoyBuildOptions {

    public Collection<File> soyMessageFiles;

    public ImmutableCollection<File> getSoyMessageFiles() {
        return Immuter.set(soyMessageFiles);
    }

    public void setSoyMessagesFiles(final Collection<File> soyMessageFiles) {
        this.soyMessageFiles = soyMessageFiles;
    }
}
