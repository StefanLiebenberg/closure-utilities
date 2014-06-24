package slieb.closureutils.resources;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
}
