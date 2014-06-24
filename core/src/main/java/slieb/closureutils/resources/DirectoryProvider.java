package slieb.closureutils.resources;

import java.io.File;

public class DirectoryProvider implements ResourceProvider {

    private final File directory;

    public DirectoryProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public Iterable<Resource> getResources() {
        throw new RuntimeException();
    }
}
