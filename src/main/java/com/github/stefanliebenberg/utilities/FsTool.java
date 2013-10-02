package com.github.stefanliebenberg.utilities;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class FsTool {

    public static Path getRelativePath(final Path path,
                                       final Path base) {
        return base.toAbsolutePath().relativize(path.toAbsolutePath());
    }

    public static Path getRelativePath(final File path,
                                       final File base) {
        return getRelativePath(base.toPath(), path.toPath());
    }

    public static String getRelative(final File path,
                                     final File base) {
        return getRelativePath(path, base).toString();
    }

    public static void ensureDirectory(final File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void ensureDirectoryFor(final File outputFile) {
        ensureDirectory(outputFile.getParentFile());
    }

    public static void collectFiles(final File directory,
                                    final Collection<File> files,
                                    final String... extensions) {
        if (directory.exists()) {
            final Iterator<File> iterator =
                    FileUtils.iterateFiles(directory, extensions, true);
            while (iterator.hasNext()) {
                files.add(iterator.next());
            }
        }
    }

    public static void collectFiles(
            final Collection<File> directories,
            final Collection<File> files,
            final String... extensions) {
        for (File directory : directories) {
            collectFiles(directory, files, extensions);
        }
    }

    public static Collection<File> find(final File directory,
                                        final String... extensions) {
        final Collection<File> files = new HashSet<File>();
        collectFiles(directory, files, extensions);
        return files;
    }

    public static Collection<File> find(
            final Collection<File> directories,
            final String... extensions) {
        final Collection<File> files = new HashSet<File>();
        collectFiles(directories, files, extensions);
        return files;
    }

    public static String read(final File inputFile)
            throws IOException {
        try (final FileInputStream inputStream = new FileInputStream
                (inputFile)) {
            return IOUtils.toString(inputStream);
        }
    }

    public static String safeRead(final File inputFile) {
        try {
            return read(inputFile);
        } catch (IOException ignored) {
            System.err.println("Error occurred inside of a FsTools.safeRead. " +
                    "ignoring");
            ignored.printStackTrace();
            return null;
        }
    }

    public static void write(final File output,
                             final String content)
            throws IOException {
        ensureDirectoryFor(output);
        try (final FileWriter writer = new FileWriter(output)) {
            writer.write(content);
        }
    }

    public static void safeWrite(final File output,
                                 final String content) {
        try {
            write(output, content);
        } catch (IOException ignored) {
            System.err.println("Error occurred inside of a FsTools.safeWrite." +
                    " ignoring");
            ignored.printStackTrace();
        }
    }

    static void copy(File inputFile, File outputFile)
            throws IOException {
        try (final InputStream istream = new FileInputStream(inputFile);
             final OutputStream ostream = new FileOutputStream(outputFile)) {
            IOUtils.copy(istream, ostream);
        }
    }

    public static File getTempFile(final String prefix,
                                   final String affix)
            throws IOException {
        File tempFile = File.createTempFile(prefix, affix);
        ensureDirectoryFor(tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

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
