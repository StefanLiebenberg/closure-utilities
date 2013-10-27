package org.stefanl.closure_utilities.stylesheets;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;
import org.apache.commons.codec.digest.DigestUtils;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.BuildOptionsException;
import org.stefanl.closure_utilities.internal.BuilderInterface;
import org.stefanl.closure_utilities.internal.DependencyException;
import org.stefanl.closure_utilities.utilities.FS;
import org.stefanl.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class AdvancedGssBuilder
        extends AbstractGssBuilder
        implements BuilderInterface, GssBuilderInterface {

    public AdvancedGssBuilder() {}

    public AdvancedGssBuilder(@Nonnull final iGssBuildOption buildOptions) {
        super(buildOptions);
    }

    private ImmutableList<String> previousEntryPoints;


    private final Map<File, String> sourceToMD5Map = new Hashtable<>();

    private final Map<File, String> resolvedMd5 = new Hashtable<>();


    @Override
    public void reset() {
        super.reset();
        sourceToMD5Map.clear();
        resolvedMd5.clear();
        previousEntryPoints = null;
    }

    /**
     * @param sourceFiles
     * @param outputFile
     * @param renameMap
     * @param productionBoolean
     * @param debugBoolean
     * @throws BuildException SideEffects:
     *                        If it builds a rename map it will set
     *                        generateRenameMap.
     */
    private void compileCssFiles(
            @Nullable final List<File> sourceFiles,
            @Nonnull final File outputFile,
            @Nullable final File renameMap,
            @Nonnull final Boolean productionBoolean,
            @Nonnull final Boolean debugBoolean)
            throws BuildException {

        if (sourceFiles == null || sourceFiles.isEmpty()) {
            throw new BuildException("No input files specified.");
        }

        final List<String> arguments = new ArrayList<>();
        arguments.add("--allow-unrecognized-functions");
        arguments.add("--allow-unrecognized-properties");

        if (renameMap != null) {
            FS.ensureDirectoryFor(renameMap);
            arguments.add("--output-renaming-map");
            arguments.add(renameMap.getPath());
        }

        if (productionBoolean) {
            arguments.add("--output-renaming-map-format");
            arguments.add("CLOSURE_COMPILED");
            if (debugBoolean) {
                arguments.add("--rename");
                arguments.add("DEBUG");
            } else {
                arguments.add("--rename");
                arguments.add("CLOSURE");
            }
        } else {
            arguments.add("--output-renaming-map-format");
            arguments.add("CLOSURE_UNCOMPILED");
            arguments.add("--rename");
            arguments.add("NONE");
        }


        FS.ensureDirectory(outputFile);
        arguments.add("--output-file");
        arguments.add(FS.FILE_TO_FILEPATH.apply(outputFile));

        arguments.addAll(Immuter.list(sourceFiles, FS.FILE_TO_FILEPATH));
        ClosureCommandLineCompiler.main(Immuter.stringArray(arguments));
        generatedRenameMap = renameMap;
    }

    private void compileCssFiles()
            throws BuildException {
        compileCssFiles(
                resolvedSourceFiles,
                temporaryOutputFile,
                buildOptions.getRenameMap(),
                buildOptions.getShouldGenerateForProduction(),
                buildOptions.getShouldGenerateForDebug());
    }


    public void parseFunctionsFromCss()
            throws IOException {
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


    private static final String GSS_EXT = "gss";


    public void scan() throws IOException {
        final ImmutableCollection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        if (sourceDirectories != null && !sourceDirectories.isEmpty()) {
            sourceFiles = findSourceFiles(sourceDirectories);
        } else {
            sourceFiles = null;
        }
    }

    @Nonnull
    private String getChecksum(@Nonnull File inputFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            return DigestUtils.md5Hex(inputStream);
        }
    }


    @Nonnull
    private Boolean sourceFilesOrEntryPointsHaveChanged(
            @Nonnull Collection<File> sourceFiles,
            @Nonnull ImmutableList<String> entryPoints) throws IOException {
        Boolean changed = false;
        final Set<File> notIncluded = Sets.newHashSet(sourceToMD5Map.keySet());
        for (File sourceFile : sourceFiles) {
            Boolean encounteredBefore = sourceToMD5Map.containsKey(sourceFile);
            String currentValue = getChecksum(sourceFile);
            if (encounteredBefore) {
                notIncluded.remove(sourceFile);
                String savedValue = sourceToMD5Map.get(sourceFile);
                if (!currentValue.equals(savedValue)) {
                    changed = true;
                    sourceToMD5Map.put(sourceFile, currentValue);
                }
            } else {
                sourceToMD5Map.put(sourceFile, currentValue);
                changed = true;
            }
        }

        for (File missingFile : notIncluded) {
            changed = true;
            sourceToMD5Map.remove(missingFile);
        }

        if (previousEntryPoints == null ||
                !previousEntryPoints.equals(entryPoints)) {
            changed = true;
        }

        previousEntryPoints = entryPoints;

        return changed;
    }


    public void parse()
            throws IOException, ReflectiveOperationException,
            DependencyException {
        final ImmutableList<String> entryPoints = buildOptions.getEntryPoints();
        if (sourceFiles != null && entryPoints != null &&
                !sourceFiles.isEmpty() && !entryPoints.isEmpty()) {
            if (sourceFilesOrEntryPointsHaveChanged(sourceFiles, entryPoints)) {
                gssDependencyLoader =
                        getDependencyLoader(dependencyParser, sourceFiles);
                resolvedSourceFiles =
                        gssDependencyLoader.getDependenciesFor(entryPoints);
            }
        } else {
            resolvedSourceFiles = null;
        }
    }


    @Nonnull
    public Boolean resolvedFilesHaveChanged(
            @Nonnull ImmutableList<File> resolvedFiles) {
        Boolean changed = false;
        Collection<File> notFound = Lists.newArrayList(resolvedFiles);

        for (File resolvedFile : resolvedFiles) {

            // this happened due to absolute path mismatch.
            if (!sourceToMD5Map.containsKey(resolvedFile)) {
                log("Cannot find " + resolvedFile.getPath(), LogLevel.WARN);
                throw new RuntimeException("Missing file");
            }

            Boolean hasSeen = resolvedMd5.containsKey(resolvedFile);
            String currentValue = sourceToMD5Map.get(resolvedFile);
            if (hasSeen) {
                notFound.remove(resolvedFile);
                String previousValue = resolvedMd5.get(resolvedFile);
                if (!currentValue.equals(previousValue)) {
                    changed = true;
                }
            } else {
                changed = true;
                resolvedMd5.put(resolvedFile, currentValue);
            }
        }

        for (File missingFile : notFound) {
            changed = true;
            resolvedMd5.remove(missingFile);
        }
        return changed;
    }


    public Boolean compileConfigHasChanged() {
        return true;
    }

    public Boolean needsCompile() {
        return compileConfigHasChanged() &&
                resolvedFilesHaveChanged(resolvedSourceFiles);
    }


    public void compile() throws IOException, BuildException {
        if (resolvedSourceFiles != null && !resolvedSourceFiles.isEmpty()) {
            if (resolvedFilesHaveChanged(resolvedSourceFiles)) {
                log("resolved files have changed. \ncompiling gss files.");
                temporaryOutputFile = getTemporaryFile();
                compileCssFiles();
                parseFunctionsFromCss();
            }
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
