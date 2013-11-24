package liebenberg.closure_utilities.javascript;

import com.google.common.collect.Sets;
import org.junit.Test;
import liebenberg.closure_utilities.internal.AbstractApplicationTest;

import java.io.File;
import java.util.Collection;

public class TestRunnerTest extends AbstractApplicationTest {
    @Test
    public void testRun() throws Exception {
        final File testFile = getApplicationDirectory("test/javascript/Basic" +
                ".test.js");
        final Collection<File> sourceDirectories = Sets.newHashSet
                (getApplicationDirectory("src/javascript"));
        final TestRunner testRunner = new TestRunner(testFile,
                sourceDirectories);
        testRunner.run();
    }

}
