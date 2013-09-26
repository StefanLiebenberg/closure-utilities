package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.BaseSourceFile;

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
