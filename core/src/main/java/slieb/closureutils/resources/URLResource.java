package slieb.closureutils.resources;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLResource implements Resource {

    private final URL url;


    public URLResource(URL url) {
        this.url = url;
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(url.openStream());
    }

    @Override
    public Writer getWriter() throws IOException {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public URI getUri() {
        try {
            return url.toURI();
        } catch (URISyntaxException syntaxException) {
            throw new RuntimeException(syntaxException);
        }
    }
}