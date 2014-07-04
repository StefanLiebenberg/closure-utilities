package slieb.closureutils.resources;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

public interface Resource {

    /**
     * Creates a reader for this resources.
     */
    public Reader getReader() throws IOException;

    public Writer getWriter() throws IOException;


    public URI getUri();
}
