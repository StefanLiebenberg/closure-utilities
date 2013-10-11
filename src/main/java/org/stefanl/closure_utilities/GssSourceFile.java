package org.stefanl.closure_utilities;

import org.stefanl.closure_utilities.internal.BaseSourceFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class GssSourceFile extends BaseSourceFile {

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
