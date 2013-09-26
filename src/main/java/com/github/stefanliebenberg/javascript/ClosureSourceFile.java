package com.github.stefanliebenberg.javascript;

import com.github.stefanliebenberg.internal.BaseSourceFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ClosureSourceFile extends BaseSourceFile {

    public ClosureSourceFile(final URI sourceLocation) {
        super(sourceLocation);
    }

    public ClosureSourceFile(final File sourceFile) {
        super(sourceFile);
    }

    public ClosureSourceFile(final String sourcePath)
            throws URISyntaxException {
        super(sourcePath);
    }

    private Boolean isBaseFile = false;

    public Boolean getIsBaseFile() {
        return isBaseFile;
    }

    public void setIsBaseFile(final Boolean isBaseFile) {
        this.isBaseFile = isBaseFile;
    }

}
