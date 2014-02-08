package liebenberg.closure_utilities.build;

import liebenberg.closure_utilities.build.SourceFileBase;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Represents a javascript closure source file.
 */
public class ClosureSourceFile extends SourceFileBase {

    /**
     * @param sourceLocation Source file url.
     */
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
