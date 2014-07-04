package slieb.closureutils.resources;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;

public class StringResource implements Resource {
    private final String string;
    private final URI uri;

    public StringResource(String string, URI uri) {
        this.string = string;
        this.uri = uri;
    }

    @Override
    public Reader getReader() {
        return new StringReader(string);
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public Writer getWriter() throws IOException {
        throw new RuntimeException("StringResource does not implement a " +
                "writer");
    }
}
