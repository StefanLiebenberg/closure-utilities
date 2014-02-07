package liebenberg.closure_utilities.javascript;


import com.google.javascript.rhino.head.ScriptableObject;
import liebenberg.closure_utilities.internal.DependencyException;
import liebenberg.closure_utilities.render.DependencyFileRenderer;
import liebenberg.closure_utilities.render.RenderException;
import liebenberg.closure_utilities.rhino.EnvJsRunner;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClosureRunner extends EnvJsRunner {

    private static final ClosureImporter CLOSURE_IMPORTER =
            new ClosureImporter();

    private final Collection<File> sourceDirectories;

    private File baseFile;

    private Set<ClosureSourceFile> closureSourceFileSet = new HashSet<>();


    private static final ClosureDependencyParser parser =
            new ClosureDependencyParser();

    private static final DependencyFileRenderer dependencyFileRenderer =
            new DependencyFileRenderer();

    public ClosureRunner(@Nonnull Collection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    private ClosureSourceFile parseSourceFile(File file) throws IOException {
        ClosureSourceFile closureSourceFile = new ClosureSourceFile(file);
        FileReader fileReader = new FileReader(file);
        parser.parse(closureSourceFile, fileReader);
        fileReader.close();
        return closureSourceFile;
    }

    private void scanDependencies() throws IOException {
        baseFile = null;
        closureSourceFileSet.clear();
        dependencyFileRenderer.reset();
        for (File sourceFile : FS.find(sourceDirectories, "js")) {
            ClosureSourceFile closureSourceFile = parseSourceFile(sourceFile);
            closureSourceFileSet.add(closureSourceFile);
            if (closureSourceFile.getIsBaseFile()) {
                baseFile = sourceFile;
            }
        }
        dependencyFileRenderer.setBasePath(baseFile.getParentFile()
                .getAbsolutePath());
        dependencyFileRenderer.setDependencies(closureSourceFileSet);
    }


    public void initialize() {
        super.initialize();
        try {
            scanDependencies();
            File baseDirectory = baseFile.getParentFile();
            ScriptableObject.putProperty(scope, "CLOSURE_BASE_PATH",
                    baseDirectory.getPath() + "/");
            ScriptableObject.putProperty(scope, "CLOSURE_IMPORT_SCRIPT",
                    CLOSURE_IMPORTER);
            evaluateFile(baseFile);
            evaluateString(dependencyFileRenderer.render());
        } catch (IOException | RenderException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void require(String require)
            throws DependencyException, IOException {
        evaluateString("goog.require('" + require + "');");
    }
}
