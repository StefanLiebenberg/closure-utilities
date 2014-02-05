package liebenberg.closure_utilities.javascript;

import com.google.javascript.jscomp.MessageBundle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JsOptions {

    private File outputDependencyFile;
    private File outputDefinesFile;
    private Collection<File> sourceDirectories;
    private List<String> entryPoints;
    private List<File> entryFiles;
    private Map<String, Object> globals;
    private File outputFile;
    private Boolean shouldDebug = true;
    private Boolean shouldCompile = false;
    private MessageBundle messageBundle = null;

    @Nullable
    public Collection<File> getSourceDirectories() {
        return sourceDirectories;
    }

    public void setSourceDirectories(
            @Nullable final Collection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    @Nullable
    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(@Nullable final List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    @Nullable
    public File getOutputDependencyFile() {
        return outputDependencyFile;
    }

    public void setOutputDependencyFile(
            @Nullable final File outputDependencyFile) {
        this.outputDependencyFile = outputDependencyFile;
    }

    @Nullable
    public Map<String, Object> getGlobals() {
        return globals;
    }

    public void setGlobals(@Nullable Map<String, Object> globals) {
        this.globals = globals;
    }

    @Nonnull
    public Boolean getShouldDebug() {
        return shouldDebug;
    }

    public void setShouldDebug(@Nonnull final Boolean shouldDebug) {
        this.shouldDebug = shouldDebug;
    }

    @Nonnull
    public Boolean getShouldCompile() {
        return shouldCompile;
    }

    public void setShouldCompile(@Nonnull final Boolean shouldCompile) {
        this.shouldCompile = shouldCompile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public List<File> getEntryFiles() {
        return entryFiles;
    }

    public void setEntryFiles(List<File> entryFiles) {
        this.entryFiles = entryFiles;
    }

    public File getOutputDefinesFile() {
        return outputDefinesFile;
    }

    public void setOutputDefinesFile(File outputDefinesFile) {
        this.outputDefinesFile = outputDefinesFile;
    }

    @Nullable
    public MessageBundle getMessageBundle() {
        return messageBundle;
    }


    public void setMessageBundle(@Nullable MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }
}
