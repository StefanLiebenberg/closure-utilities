package org.stefanl.closure_utilities.javascript;

import org.stefanl.closure_utilities.internal.BaseSourceFile;

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

    /**
     * @return Returns true if this file is the goog/base.js file.
     */
    public Boolean getIsBaseFile() {
        return isBaseFile;
    }

    /**
     * @param isBaseFile
     */
    public void setIsBaseFile(final Boolean isBaseFile) {
        this.isBaseFile = isBaseFile;
    }

}
