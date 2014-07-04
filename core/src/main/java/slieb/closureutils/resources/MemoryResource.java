package slieb.closureutils.resources;

import java.io.*;
import java.net.URI;

// todo This class could cause concurrency headaches.
public class MemoryResource implements Resource {

    private CharArrayWriter writer;

    private final URI uri;

    public MemoryResource(URI uri) {
        this.uri = uri;
    }

    public MemoryResource() {
        this.uri = null;
    }

    @Override
    public Reader getReader() throws IOException {
        return new CharArrayReader(writer.toCharArray());
    }

    @Override
    public Writer getWriter() throws IOException {
        if (writer != null) {
            writer.close();
        }
        return writer = new CharArrayWriter();
    }

    @Override
    public URI getUri() {
        return uri;
    }
}
