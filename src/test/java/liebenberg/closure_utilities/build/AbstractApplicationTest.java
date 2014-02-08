package liebenberg.closure_utilities.build;


import liebenberg.closure_utilities.utilities.FS;
import org.apache.commons.io.FileUtils;

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

    public static final File APPLICATION_DIRECTORY =
            new File("src/test/resources/app");

    @Nonnull
    protected File getApplicationDirectory() {
        return APPLICATION_DIRECTORY;
    }

    @Nonnull
    protected File getApplicationDirectory(String path) {
        return new File(getApplicationDirectory(), path);
    }
}
