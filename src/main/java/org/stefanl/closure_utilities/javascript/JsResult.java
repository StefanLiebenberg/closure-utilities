package org.stefanl.closure_utilities.javascript;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.List;

@Immutable
public class JsResult {

    public JsResult(@Nullable final File outputFile,
                    @Nullable final List<File> scriptFiles) {
        this.scriptFiles = scriptFiles;
        this.outputFile = outputFile;
    }

    private final List<File> scriptFiles;

    private final File outputFile;

    @Nullable
    public List<File> getScriptFiles() {
        return scriptFiles;
    }

    @Nullable
    public File getOutputFile() {
        return outputFile;
    }
}
