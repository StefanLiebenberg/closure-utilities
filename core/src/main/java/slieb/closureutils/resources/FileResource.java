package slieb.closureutils.resources;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileResource implements Resource {

    private final File file;

    public FileResource(File file) {
        this.file = file;
    }

    @Override
    public Reader getReader() throws IOException {
        return new FileReader(file);
    }
}
