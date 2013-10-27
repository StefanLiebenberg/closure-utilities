package org.stefanl.closure_utilities.internal;


import org.apache.commons.io.FileUtils;
import org.stefanl.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;

public abstract class AbstractApplicationTest {

    protected File outputDirectory;

    protected void setUp() throws Exception {
        outputDirectory = FS.getTempDirectory();
    }

    protected void tearDown() throws Exception {
        FileUtils.deleteDirectory(outputDirectory);
        outputDirectory = null;
    }

    @Nonnull
    protected File getApplicationDirectory() {
        return new File("src/test/resources/app");
    }

    @Nonnull
    protected File getApplicationDirectory(String path) {
        return new File(getApplicationDirectory(), path);
    }

}
