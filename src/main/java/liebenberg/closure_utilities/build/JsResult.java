package liebenberg.closure_utilities.build;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.List;

@Immutable
public class JsResult {


    public JsResult(
            @Nullable final File baseFile,
            @Nullable final File outputDepsFile,
            @Nullable final File outputDefinesFile,
            @Nullable final File outputFile,
            @Nullable final List<File> scriptFiles) {
        this.baseFile = baseFile;
        this.outputDepsFile = outputDepsFile;
        this.outputDefinesFile = outputDefinesFile;
        this.scriptFiles = scriptFiles;
        this.outputFile = outputFile;
    }

    private final List<File> scriptFiles;

    private final File outputFile;

    private final File baseFile;

    private final File outputDepsFile;

    private final File outputDefinesFile;


    @Nullable
    public List<File> getScriptFiles() {
        return scriptFiles;
    }

    @Nullable
    public File getOutputFile() {
        return outputFile;
    }

    @Nullable
    public File getBaseFile() {
        return baseFile;
    }

    @Nullable
    public File getOutputDepsFile() {
        return outputDepsFile;
    }

    @Nullable
    public File getOutputDefinesFile() {
        return outputDefinesFile;
    }
}
