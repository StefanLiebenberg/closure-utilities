package slieb.closureutils.resources;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import static org.apache.commons.io.FileUtils.iterateFiles;

public class DirectoryProvider implements ResourceProvider {

    private final File directory;

    private final URI uri;

    private final String[] extensions;

    public DirectoryProvider(File directory, URI uri, String[] extensions) {
        this.directory = directory;
        this.uri = uri;
        this.extensions = extensions;
    }

    public DirectoryProvider(File directory, String[] extensions) {
        this.directory = directory;
        this.uri = directory.toURI();
        this.extensions = extensions;
    }

    @Override
    public Iterable<Resource> getResources() {
        return new DirectoryIterable(directory, extensions, uri);
    }

    @Override
    public URI getUri() {
        return uri;
    }


    private static class DirectoryIterable implements Iterable<Resource> {
        private final File directory;
        private final String[] extensions;
        private final URI uri;

        private DirectoryIterable(File directory, String[] extensions,
                                  URI uri) {
            this.directory = directory;
            this.extensions = extensions;
            this.uri = uri;
        }

        @Override
        public Iterator<Resource> iterator() {
            final URI directoryURI = directory.toURI();
            final Iterator<File> parentIterator =
                    iterateFiles(directory, extensions, true);
            return new DirectoryIterator(parentIterator, uri, directoryURI);
        }
    }

    private static class DirectoryIterator implements Iterator<Resource> {
        private final Iterator<File> parentIterator;
        private final URI uri;
        private final URI directoryURI;

        private DirectoryIterator(Iterator<File> parentIterator, URI uri,
                                  URI directoryURI) {
            this.parentIterator = parentIterator;
            this.uri = uri;
            this.directoryURI = directoryURI;
        }

        @Override
        public boolean hasNext() {
            return parentIterator.hasNext();
        }

        @Override
        public Resource next() {
            final File file = parentIterator.next();
            final URI resourceURI = uri.resolve(file.toURI().relativize
                    (directoryURI));
            return new FileResource(file, resourceURI);
        }

        @Override
        public void remove() {
            parentIterator.remove();
        }
    }

}
