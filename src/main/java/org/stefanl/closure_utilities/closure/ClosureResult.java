package org.stefanl.closure_utilities.closure;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class ClosureResult {

    private final File generatedStylesheet;

    private final File generatedRenameMap;

    private final File soyOutputDirectory;

    private final File htmlOutputFile;

    private final File jsOutputFile;

    public ClosureResult(
            @Nullable final File generatedStylesheet,
            @Nullable final File generatedRenameMap,
            @Nullable final File soyOutputDirectory,
            @Nullable final File htmlOutputFile,
            @Nullable final File jsOutputFile) {
        this.generatedStylesheet = generatedStylesheet;
        this.generatedRenameMap = generatedRenameMap;
        this.soyOutputDirectory = soyOutputDirectory;
        this.htmlOutputFile = htmlOutputFile;
        this.jsOutputFile = jsOutputFile;
    }


    @Nullable
    public File getGeneratedStylesheet() {
        return generatedStylesheet;
    }

    @Nullable
    public File getGeneratedRenameMap() {
        return generatedRenameMap;
    }

    @Nullable
    public File getHtmlOutputFile() {
        return htmlOutputFile;
    }

    @Nullable
    public File getSoyOutputDirectory() {
        return soyOutputDirectory;
    }

    @Nullable
    public File getJsOutputFile() {
        return jsOutputFile;
    }
}
