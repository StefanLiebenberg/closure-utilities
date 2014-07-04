package slieb.closureutils.resources;


import java.io.*;
import java.net.URI;

public class FileResource implements Resource {

    private final File file;
    private final URI uri;

    public FileResource(File file, URI uri) {
        this.file = file;
        this.uri = uri;
    }

    public FileResource(File file) {
        this(file, file.toURI());
    }

    @Override
    public Reader getReader() throws IOException {
        return new FileReader(file);
    }

    @Override
    public Writer getWriter() throws IOException {
        return new FileWriter(file);
    }

    @Override
    public URI getUri() {
        return uri;
    }
}
