package slieb.closureutils.javascript.runtimes;


public class ClosureRunner extends EnvJsRunner {

//    private final ClosureImporter closureImporter;
//
//
//    public ClosureRunner(ClosureImporter closureImporter) {
//        this.closureImporter = closureImporter;
//    }
//
//    private String renderDependencyFile(File baseFile)
//            throws DependencyException, RenderException, IOException {
//        final File baseParentFile = baseFile.getParentFile();
//        return dependencyFileRenderer
//                .setBasePath(baseParentFile.getAbsolutePath())
//                .setDependencies(scanner.getClosureSourceFiles())
//                .render();
//    }
//
//
//    public void initialize() {
//        try {
//            super.initialize();
//            File baseFile = scanner.getBaseFile();
//            File baseDirectory = baseFile.getParentFile();
//            putObject("CLOSURE_BASE_PATH", baseDirectory.getPath() + "/");
//            putObject("CLOSURE_IMPORT_SCRIPT", CLOSURE_IMPORTER);
//            evaluateFile(baseFile);
//            evaluateString(renderDependencyFile(baseFile));
//        } catch (IOException | RenderException | DependencyException
//                exception) {
//            throw new RuntimeException(exception);
//        }
//    }
//
//    public void require(String require) throws IOException {
//        call("goog.require", null, javaToJS(require));
//    }
}
