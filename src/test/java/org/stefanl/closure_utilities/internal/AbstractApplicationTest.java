package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;
import java.io.File;

public class AbstractApplicationTest {
    @Nonnull
    protected File getApplicationDirectory() {
        return new File("src/test/resources/app");
    }

    @Nonnull
    protected File getApplicationDirectory(String path) {
        return new File(getApplicationDirectory(), path);
    }

}
