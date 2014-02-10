package slieb.closure.javascript;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;


public class ClosureRunnerTest {

    private final static File APP_DIRECTORY =
            new File("src/test/resources/app");

    private ClosureRunner closureRunner;

    @Before
    public void setUp() throws Exception {
        Collection<File> sourceDirectories = new HashSet<>();
        sourceDirectories.add(new File(APP_DIRECTORY, "src/javascript"));
        closureRunner = new ClosureRunner(sourceDirectories);
        closureRunner.initialize(); // should load base and deps file.
    }

    @After
    public void tearDown() throws Exception {
        closureRunner.close();
        closureRunner = null;
    }

    @Test
    public void testRequire() throws Exception {
        closureRunner.require("goog.array");
        Assert.assertTrue(closureRunner.getBoolean("goog.isDef(goog.array);"));
        Assert.assertTrue(closureRunner.getBoolean("goog.isFunction(goog" +
                ".array.forEach);"));
    }


}
