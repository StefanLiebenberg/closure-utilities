package org.stefanl.closure_utilities.templates;

import org.stefanl.closure_utilities.utilities.Immuter;
import com.google.common.collect.ImmutableCollection;

import java.io.File;
import java.util.Collection;

public class MultiLocaleSoyBuildOptions extends SoyBuildOptions {

    public Collection<File> soyMessageFiles;


    public void setSoyMessagesFiles(final Collection<File> soyMessageFiles) {
        this.soyMessageFiles = soyMessageFiles;
    }

    public ImmutableCollection<File> getSoyMessageFiles() {
        return Immuter.set(soyMessageFiles);
    }

}
