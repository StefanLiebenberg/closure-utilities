package slieb.closureutils.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;

public class URIReferenceResource implements Resource, Serializable {

    private final Resource resource;

    private final URI uri;

    public URIReferenceResource(Resource resource, URI uri) {
        this.resource = resource;
        this.uri = uri;
    }

    @Override
    public Reader getReader() throws IOException {
        return resource.getReader();
    }

    @Override
    public Writer getWriter() throws IOException {
        return resource.getWriter();
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof URIReferenceResource)) return false;

        URIReferenceResource that = (URIReferenceResource) o;

        if (!resource.equals(that.resource)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + uri.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + getUri().toString() + ")";
    }
}
