package liebenberg.closure_utilities.javascript;

import com.google.common.collect.Sets;
import liebenberg.closure_utilities.internal.AbstractApplicationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

public class TestRunnerTest extends AbstractApplicationTest {

    TestRunner testRunner;

    @Before
    public void setup() {
        final Collection<File> sourceDirectories = Sets.newHashSet
                (getApplicationDirectory("src/javascript"));
        testRunner = new TestRunner(sourceDirectories);
        testRunner.initialize();
    }

    @After
    public void tearDown() {
        testRunner.close();
    }

    @Test
    public void testRun() throws Exception {
        final File testFile =
                getApplicationDirectory("test/javascript/Basic.test.js");
        testRunner.run(testFile);
        Assert.assertTrue(testRunner.isSuccess());
    }
}
