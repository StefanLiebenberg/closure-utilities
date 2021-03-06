package slieb.closure.build;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class ClosureResult {

    private final File generatedStylesheet;

    private final File generatedRenameMap;

    private final File soyOutputDirectory;

    private final File htmlOutputFile;

    private final File jsBaseFile;
    private final File jsDepsOutputFile;
    private final File jsDefinesOutputFile;
    private final File jsOutputFile;

    public ClosureResult(
            @Nullable final File generatedStylesheet,
            @Nullable final File generatedRenameMap,
            @Nullable final File soyOutputDirectory,
            @Nullable final File htmlOutputFile,
            @Nullable final File jsBaseFile,
            @Nullable final File jsDepsOutputFile,
            @Nullable final File jsDefinesOutputFile,
            @Nullable final File jsOutputFile) {
        this.generatedStylesheet = generatedStylesheet;
        this.generatedRenameMap = generatedRenameMap;
        this.soyOutputDirectory = soyOutputDirectory;
        this.htmlOutputFile = htmlOutputFile;
        this.jsBaseFile = jsBaseFile;
        this.jsDepsOutputFile = jsDepsOutputFile;
        this.jsDefinesOutputFile = jsDefinesOutputFile;
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

    @Nullable
    public File getJsBaseFile() {
        return jsBaseFile;
    }

    @Nullable
    public File getJsDepsOutputFile() {
        return jsDepsOutputFile;
    }

    @Nullable
    public File getJsDefinesOutputFile() {
        return jsDefinesOutputFile;
    }
}
