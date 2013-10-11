package org.stefanl.closure_utilities.utilities;


import com.google.common.base.Function;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class FsTool {

    @Nonnull
    public static Path getRelativePath(
            @Nonnull final Path path,
            @Nonnull final Path base) {
        return base.toAbsolutePath().relativize(path.toAbsolutePath());
    }

    @Nonnull
    public static Path getRelativePath(
            @Nonnull final File path,
            @Nonnull final File base) {
        return getRelativePath(path.toPath(), base.toPath());
    }

    @Nonnull
    public static String getRelative(@Nonnull final File path,
                                     @Nonnull final File base) {
        return getRelativePath(path, base).toString();
    }

    public static void ensureDirectory(@Nonnull final File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void ensureDirectoryFor(@Nonnull final File outputFile) {
        ensureDirectory(outputFile.getParentFile());
    }

    public static void collectFiles(@Nonnull final File directory,
                                    @Nonnull final Collection<File> files,
                                    @Nonnull final String... extensions) {
        if (directory.exists()) {
            final Iterator<File> iterator =
                    FileUtils.iterateFiles(directory, extensions, true);
            while (iterator.hasNext()) {
                files.add(iterator.next());
            }
        }
    }

    public static void collectFiles(
            @Nonnull final Collection<File> directories,
            @Nonnull final Collection<File> files,
            @Nonnull final String... extensions) {
        for (File directory : directories) {
            collectFiles(directory, files, extensions);
        }
    }

    @Nonnull
    public static Collection<File> find(
            @Nonnull final File directory,
            @Nonnull final String... extensions) {
        final Collection<File> files = new HashSet<File>();
        collectFiles(directory, files, extensions);
        return files;
    }

    @Nonnull
    public static Collection<File> find(
            @Nonnull final Collection<File> directories,
            @Nonnull final String... extensions) {
        final Collection<File> files = new HashSet<File>();
        collectFiles(directories, files, extensions);
        return files;
    }

    @Nonnull
    public static String read(
            @Nonnull final File inputFile)
            throws IOException {
        try (final FileInputStream inputStream = new FileInputStream
                (inputFile)) {
            return IOUtils.toString(inputStream);
        }
    }


    public static void write(
            @Nonnull final File output,
            @Nonnull final String content)
            throws IOException {
        ensureDirectoryFor(output);
        try (final FileWriter writer = new FileWriter(output)) {
            writer.write(content);
        }
    }

    static void copy(
            @Nonnull final File inputFile,
            @Nonnull final File outputFile)
            throws IOException {
        try (final InputStream istream = new FileInputStream(inputFile);
             final OutputStream ostream = new FileOutputStream(outputFile)) {
            IOUtils.copy(istream, ostream);
        }
    }

    @Nonnull
    public static File getTempFile(
            @Nonnull final String prefix,
            @Nonnull final String affix)
            throws IOException {
        File tempFile = File.createTempFile(prefix, affix);
        ensureDirectoryFor(tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Nonnull
    public static File getTempDirectory()
            throws IOException {
        final File directory = Files.createTempDir();
        directory.deleteOnExit();
        return directory;
    }

    public final static Function<URL, String> URL_TO_FILEPATH = new
            Function<URL, String>() {
                public String apply(final URL url) {
                    if (url != null) {
                        return url.getPath();
                    } else {
                        return null;
                    }
                }
            };

    public final static Function<File, String> FILE_TO_FILEPATH =
            new Function<File, String>() {
                @Nullable
                @Override
                public String apply(@Nullable File file) {
                    if (file != null) {
                        return file.getPath();
                    } else {
                        return null;
                    }
                }
            };

    public final static Function<File, Path> FILE_TO_PATH =
            new Function<File, Path>() {
                @Nullable
                @Override
                public Path apply(@Nullable File file) {
                    if (file != null) {
                        return file.toPath();
                    } else {
                        return null;
                    }
                }
            };


    public final static Function<File, URI> FILE_TO_URI =
            new Function<File, URI>() {
                @Nullable
                @Override
                public URI apply(@Nullable File file) {
                    if (file != null) {
                        return file.toURI();
                    } else {
                        return null;
                    }
                }
            };

}
