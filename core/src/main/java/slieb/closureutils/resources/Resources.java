package slieb.closureutils.resources;


import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.*;
import java.net.URI;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.IOUtils.copy;

public final class Resources {

    private Resources() {}

    public static Resource getResourceByUri(final ResourceProvider provider,
                                            final URI uri) {
        for (Resource resource : provider.getResources()) {
            if (uri.equals(resource.getUri())) {
                return resource;
            }
        }
        return null;
    }

    public static File copyResourceToTempFile(Resource resource,
                                              String ext) throws IOException {
        File resourceFile = createTempFile("resource_", ext).toFile();
        resourceFile.deleteOnExit();
        try (Writer writer = new FileWriter(resourceFile)) {
            copyResourceToWriter(resource, writer);
        }
        return resourceFile;
    }

    public static void copyResourceToFile(Resource resource,
                                          File resourceFile) throws
            IOException {
        try (Writer writer = new FileWriter(resourceFile)) {
            copyResourceToWriter(resource, writer);
        }
    }

    public static void copyResourceFromFile(Resource resource,
                                            File resourceFile) throws
            IOException {
        try (Reader reader = new FileReader(resourceFile)) {
            copyResourceFromReader(resource, reader);
        }
    }

    public static void copyResourceToWriter(Resource resource,
                                            Writer writer) throws IOException {
        try (Reader reader = resource.getReader()) {
            copy(reader, writer);
        }
    }

    public static void copyResourceFromReader(Resource resource,
                                              Reader reader) throws
            IOException {
        try (Writer writer = resource.getWriter()) {
            copy(reader, writer);
        }
    }

    public static InputStream getResourceInputStream(Resource resource)
            throws IOException {
        return new ReaderInputStream(resource.getReader());
    }

    public static OutputStream getResourceOutputStream(Resource resource)
            throws IOException {
        return new WriterOutputStream(resource.getWriter());
    }

    public static String readResource(Resource resource) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            copyResourceToWriter(resource, writer);
            return writer.toString();
        }
    }

    public static String safeReadResource(Resource resource) {
        try {
            return readResource(resource);
        } catch (IOException exception) {
            return null;
        }
    }

    public static void writeResource(Resource resource, String content) throws
            IOException {
        try (StringReader reader = new StringReader(content)) {
            copyResourceFromReader(resource, reader);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    public static void safeWriteResource(Resource resource, String content) {
        try {
            writeResource(resource, content);
        } catch (IOException exception) {
        }
    }
}
