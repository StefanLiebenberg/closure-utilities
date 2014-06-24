package slieb.closure.tools;


import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

public class FS {

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
            IteratorCollector.collect(
                    FileUtils.iterateFiles(directory, extensions, true),
                    files);
        } else {
            System.err.println("[warning] The directory " + directory
                    .getAbsolutePath() + " does not exist.");
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
        final Collection<File> files = new HashSet<>();
        collectFiles(directory, files, extensions);
        return files;
    }

    @Nonnull
    public static HashSet<File> find(
            @Nonnull final Collection<File> directories,
            @Nonnull final String... extensions) {
        final HashSet<File> files = new HashSet<>();
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
            writer.flush();
            writer.close();
        }
    }


    @Nonnull
    public static File getTempDirectory()
            throws IOException {
        final File directory = Files.createTempDir();
        directory.deleteOnExit();
        return directory;
    }

}
