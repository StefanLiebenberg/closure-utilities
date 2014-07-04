package slieb.closureutils.resources;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

public class SimpleResource implements Resource {

    private final URI uri;
    private final Reader reader;
    private final Writer writer;

    public SimpleResource(URI uri, Reader reader, Writer writer) {
        this.uri = uri;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public Writer getWriter() throws IOException {
        return writer;
    }

    @Override
    public Reader getReader() throws IOException {
        return reader;
    }
}
