package liebenberg.closure_utilities.javascript;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;


public class TestRunner extends ClosureRunner {

    public TestRunner(@Nonnull final Collection<File> sourceDirectories) {
        super(sourceDirectories);
    }

    public Boolean run(File testFile) throws Exception {
        evaluateFile(testFile);
        doLoad();
        return getBoolean("G_testRunner.isSuccess()");
    }
}
