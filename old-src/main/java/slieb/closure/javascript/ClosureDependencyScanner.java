package slieb.closure.javascript;


import slieb.closure.build.ClosureSourceFile;
import slieb.closure.internal.DependencyException;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

public class ClosureDependencyScanner {

    protected final Logger LOGGER =
            Logger.getLogger(getClass().getName());

    private static final ClosureDependencyParser parser =
            new ClosureDependencyParser();

    private final Collection<File> sourceDirectories;

    private File baseFile;

    private Collection<ClosureSourceFile> closureSourceFiles =
            new HashSet<>();

    private Collection<File> sourceFiles =
            new HashSet<>();

    public ClosureDependencyScanner(Collection<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public void reset() {
        sourceFiles.clear();
        closureSourceFiles.clear();
        baseFile = null;
    }

    private static final String SCAN_MSG = "Scanning javascript directories.";
    private static final String SCAN_SRC_FILE_MSG = "Source file: %s";
    private static final String SCAN_BASE_FILE_MSG = "Base file: %s";
    private static final String SCAN_SUMMARY_MSG = "Base file: %s";

    public void scan() throws IOException {
        reset();
        LOGGER.info(SCAN_MSG);
        for (File sourceFile : FS.find(sourceDirectories, "js")) {
            LOGGER.finer(String.format(SCAN_SRC_FILE_MSG, sourceFile));
            sourceFiles.add(sourceFile);
            ClosureSourceFile closureSourceFile = parseSourceFile(sourceFile);
            closureSourceFiles.add(closureSourceFile);
            if (closureSourceFile.getIsBaseFile()) {
                baseFile = sourceFile;
                LOGGER.finer(String.format(SCAN_BASE_FILE_MSG, baseFile));
            }
        }
        LOGGER.info(String.format(SCAN_SUMMARY_MSG, closureSourceFiles.size()));
    }

    private ClosureSourceFile parseSourceFile(File file)
            throws IOException {
        final ClosureSourceFile closureSourceFile = new ClosureSourceFile(file);
        try (final FileReader fileReader = new FileReader(file)) {
            parser.parse(closureSourceFile, fileReader);
            fileReader.close();
        }
        return closureSourceFile;
    }

    @Nonnull
    public File getBaseFile() throws DependencyException {
        if (baseFile == null) {
            final StringBuilder msgBuilder = new StringBuilder();

            msgBuilder.append("Cannot find closure library's base.js in any " +
                    "of " +
                    "these directories:\n");
            for (File dir : sourceDirectories) {
                msgBuilder.append("  ");
                if (!dir.exists()) {
                    msgBuilder.append("[MISSING] ");
                }
                msgBuilder.append(dir.getPath() + "\n");
            }
            msgBuilder.append("Please ensure that closure-library is " +
                    "available under the specified source directories.");

            throw new DependencyException(msgBuilder.toString());
        }
        return baseFile;
    }

    public Collection<ClosureSourceFile> getClosureSourceFiles() {
        return closureSourceFiles;
    }

    public Collection<File> getSourceFiles() {
        return sourceFiles;
    }
}
