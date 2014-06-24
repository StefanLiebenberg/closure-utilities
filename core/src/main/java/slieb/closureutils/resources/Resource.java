package slieb.closureutils.resources;


import java.io.IOException;
import java.io.Reader;

public interface Resource {

    /**
     * Creates a reader for this resources.
     */
    public Reader getReader() throws IOException;
}
