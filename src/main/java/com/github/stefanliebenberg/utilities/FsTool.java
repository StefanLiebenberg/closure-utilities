package com.github.stefanliebenberg.utilities;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

    public static String[] getNames(final String path) {
        return path.split(File.separator);
    }

    public static String getCommonPath(final String... paths) {
        final int length = paths.length;
        String[][] names = new String[length][];
        for (int i = 0; i < length; i++) {
            names[i] = getNames(paths[i]);
        }

        String common = "";
        String[] firstPath = names[0];
        int firstLength = firstPath.length;
        for (int j = 0; j < firstLength; j++) {
            String thisFolder = firstPath[j]; //grab the next folder name in
            // the first path
            boolean allMatched = true; //assume all have matched in case
            // there are no more paths
            for (int i = 1; i < length && allMatched; i++) { //look at the
                // other paths
                String[] current = names[i];
                if (current.length < j) { //if there is no folder here
                    allMatched = false; //no match
                    break; //stop looking because we've gone as far as we can
                }
                //otherwise
                allMatched &= current[j].equals(thisFolder); //check if it
                // matched
            }

            if (allMatched) { //if they all matched this folder name
                common += File.separator + thisFolder;
            } else {//otherwise
                break;//stop looking
            }
        }
        return common;
    }

    public static String getCommonPath(final Collection<String> paths) {
        return getCommonPath(paths.toArray(new String[paths.size()]));
    }

    public final static Function<File, String> fileToPathTransform = new
            Function<File, String>() {
                public String apply(final File file) {
                    return file.getPath();
                }
            };

    public final static Function<URL, String> urlToPathTransform = new
            Function<URL, String>() {
                public String apply(final URL url) {
                    return url.getPath();
                }
            };


    public static File getCommonParent(final Collection<File> files) {
        return new File(getCommonPath(Collections2.transform(files,
                fileToPathTransform)));
    }

    public static File getCommonParentForUrls(final Collection<URL> files) {
        return new File(getCommonPath(Collections2.transform(files,
                urlToPathTransform)));
    }

}
