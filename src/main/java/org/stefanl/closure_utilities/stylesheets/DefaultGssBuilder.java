package org.stefanl.closure_utilities.stylesheets;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.BuildOptionsException;
import org.stefanl.closure_utilities.internal.DependencyException;
import org.stefanl.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class DefaultGssBuilder
        extends AbstractGssBuilder
        implements GssBuilderInterface {

    public DefaultGssBuilder() {}

    public DefaultGssBuilder(@Nonnull iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    @Override
    public void reset() {
        super.reset();
    }


    public void parseFunctionsFromCss() throws IOException {
        File outputFile = buildOptions.getOutputFile();
        if (outputFile != null) {
            if (!outputFile.isAbsolute()) {
                outputFile = outputFile.getAbsoluteFile();
            }
            final String content = FS.read(temporaryOutputFile);
            final String base = getBasePath(buildOptions.getAssetsUri(),
                    buildOptions.getAssetsDirectory(),
                    outputFile.getParentFile());
            FS.write(outputFile, parseCssFunctions(content, base));
            generatedStylesheet = outputFile;
        }
    }

    /**
     * Collects all the gss files
     */
    public void scan() throws IOException {
        final ImmutableCollection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        if (sourceDirectories != null && !sourceDirectories.isEmpty()) {
            sourceFiles = findSourceFiles(sourceDirectories);
        } else {
            sourceFiles = null;
        }
    }


    /**
     * Parses the found gss files and produces a list of resolved files.
     *
     * @throws IOException
     * @throws ReflectiveOperationException
     * @throws DependencyException
     */
    public void parse()
            throws IOException, ReflectiveOperationException,
            DependencyException {
        final ImmutableList<String> entryPoints = buildOptions.getEntryPoints();
        if (sourceFiles != null && !sourceFiles.isEmpty() &&
                entryPoints != null && !entryPoints.isEmpty()) {
            gssDependencyLoader =
                    getDependencyLoader(dependencyParser, sourceFiles);
            resolvedSourceFiles =
                    gssDependencyLoader.getDependenciesFor(entryPoints);
        } else {
            resolvedSourceFiles = null;
        }
    }


    public void compile() throws IOException, BuildException {
        if (resolvedSourceFiles != null && !resolvedSourceFiles.isEmpty()) {
            temporaryOutputFile = getTemporaryFile();
            final File renameMap = buildOptions.getRenameMap();
            compileSourceFiles(
                    resolvedSourceFiles,
                    temporaryOutputFile,
                    renameMap,
                    buildOptions.getShouldGenerateForProduction(),
                    buildOptions.getShouldGenerateForDebug());
            generatedRenameMap = renameMap;
            parseFunctionsFromCss();
        }
    }

    @Override
    public void buildInternal() throws Exception {
        scan();
        parse();
        compile();
    }

    private final static String UNSPECIFIED_OUTPUT_FILE =
            "Gss Output file is unspecified.";

    private final static String UNSPECIFIED_ENTRY_POINTS =
            "Gss Entry points are unspecified.";

    private final static String UNSPECIFIED_SOURCES =
            "Gss sources are unspecified.";


    @Override
    public void checkOptions() throws BuildOptionsException {
        super.checkOptions();

        final File outputFile = buildOptions.getOutputFile();
        if (outputFile == null) {
            throw new BuildOptionsException(UNSPECIFIED_OUTPUT_FILE);
        }

        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        final Boolean sourceDirectoriesAreSpecified =
                sourceDirectories != null && !sourceDirectories.isEmpty();
        final Collection<File> sourceFiles =
                buildOptions.getSourceFiles();
        final Boolean sourceFilesAreSpecified =
                sourceFiles != null && !sourceFiles.isEmpty();
        if (!sourceDirectoriesAreSpecified && !sourceFilesAreSpecified) {
            throw new BuildOptionsException(UNSPECIFIED_SOURCES);
        }

        final Collection<String> entryPoints = buildOptions.getEntryPoints();
        if (entryPoints == null || entryPoints.isEmpty()) {
            throw new BuildOptionsException(UNSPECIFIED_ENTRY_POINTS);
        }

    }


}
