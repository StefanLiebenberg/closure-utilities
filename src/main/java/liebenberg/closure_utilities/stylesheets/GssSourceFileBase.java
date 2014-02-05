package liebenberg.closure_utilities.stylesheets;

import liebenberg.closure_utilities.internal.SourceFileBase;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class GssSourceFileBase extends SourceFileBase {

    public GssSourceFileBase(URI sourceLocation) {
        super(sourceLocation);
    }

    public GssSourceFileBase(File sourceFile) {
        super(sourceFile);
    }

    public GssSourceFileBase(String sourcePath) throws URISyntaxException {
        super(sourcePath);
    }
}
