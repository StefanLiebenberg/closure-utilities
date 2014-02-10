package slieb.closure.javascript;

import slieb.closure.internal.DependencyException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class TestRunner extends ClosureRunner {

    public TestRunner(@Nonnull final Collection<File> sourceDirectories) {
        super(sourceDirectories);
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            require("goog.testing.jsunit");
        } catch (IOException | DependencyException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void run(File testFile) throws IOException {
        evaluateFile(testFile);
        doLoad();
    }

    public Boolean isSuccess() {
        return getBoolean("G_testRunner.isSuccess()");
    }
}
