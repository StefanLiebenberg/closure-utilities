package liebenberg.closure_utilities.javascript;

import liebenberg.closure_utilities.internal.SourceFileBase;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ClosureSourceFileBase extends SourceFileBase {

    public ClosureSourceFileBase(final URI sourceLocation) {
        super(sourceLocation);
    }

    public ClosureSourceFileBase(final File sourceFile) {
        super(sourceFile);
    }

    public ClosureSourceFileBase(final String sourcePath)
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
