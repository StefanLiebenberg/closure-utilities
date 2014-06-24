package slieb.closure.javascript;


import slieb.closure.internal.DependencyException;
import slieb.closure.render.DependencyFileRenderer;
import slieb.closure.render.RenderException;
import slieb.closure.runtimes.EnvRunner;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class ClosureRunner extends EnvRunner {

    private static final ClosureImporter CLOSURE_IMPORTER =
            new ClosureImporter();

    private static final DependencyFileRenderer dependencyFileRenderer =
            new DependencyFileRenderer();

    private ClosureDependencyScanner scanner;

    public ClosureRunner(@Nonnull Collection<File> sourceDirectories) {
        this.scanner = new ClosureDependencyScanner(sourceDirectories);
    }

    private void scanDependencies() throws IOException {
        scanner.scan();
    }

    private String renderDependencyFile(File baseFile)
            throws DependencyException, RenderException, IOException {
        final File baseParentFile = baseFile.getParentFile();
        return dependencyFileRenderer
                .setBasePath(baseParentFile.getAbsolutePath())
                .setDependencies(scanner.getClosureSourceFiles())
                .render();
    }


    public void initialize() {
        try {
            scanDependencies();
            super.initialize();
            File baseFile = scanner.getBaseFile();
            File baseDirectory = baseFile.getParentFile();
            putObject("CLOSURE_BASE_PATH", baseDirectory.getPath() + "/");
            putObject("CLOSURE_IMPORT_SCRIPT", CLOSURE_IMPORTER);
            evaluateFile(baseFile);
            evaluateString(renderDependencyFile(baseFile));
        } catch (IOException | RenderException | DependencyException
                exception) {
            throw new RuntimeException(exception);
        }
    }

    public void require(String require) throws IOException {
        call("goog.require", null, javaToJS(require));
    }
}
