package slieb.closure.build.gss;

import slieb.closure.build.internal.SourceFileBase;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class GssSourceFile extends SourceFileBase {

    public GssSourceFile(URI sourceLocation) {
        super(sourceLocation);
    }

    public GssSourceFile(File sourceFile) {
        super(sourceFile);
    }

    public GssSourceFile(String sourcePath) throws URISyntaxException {
        super(sourcePath);
    }
}
