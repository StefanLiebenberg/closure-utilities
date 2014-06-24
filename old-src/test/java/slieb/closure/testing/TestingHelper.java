package slieb.closure.testing;


import java.io.File;

public class TestingHelper {

    public static final File TEST_RESOURCES_DIRECTORY =
            new File("src/test/resources");

    public File getTestResourcesFile(String fileName) {
        return new File(TEST_RESOURCES_DIRECTORY, fileName);
    }
}
