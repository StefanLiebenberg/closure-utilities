package slieb.closureutils.resources;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

public class ClassResource implements Resource {
    private final Class classObject;
    private final String resourceName;
    private final URI uri;

    public ClassResource(Class classObject, String resourceName, URI uri) {
        this.classObject = classObject;
        this.resourceName = resourceName;
        this.uri = uri;
    }

    public ClassResource(Class classObject, String resourceName)
            throws URISyntaxException {
        this(classObject, resourceName,
                classObject.getResource(resourceName).toURI());
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(classObject.getResourceAsStream
                (resourceName));
    }

    @Override
    public Writer getWriter() throws IOException {
        throw new RuntimeException("Cannot supply a writer to a class " +
                "resource");
    }

    @Override
    public URI getUri() {
        return uri;
    }
}
