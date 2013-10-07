package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.utilities.FsTool;
import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.css.compiler.commandline.ClosureCommandLineCompiler;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GssBuilder extends AbstractBuilder<GssBuildOptions>
        implements IBuilder {


    public GssBuilder() {}

    public GssBuilder(@Nonnull final GssBuildOptions buildOptions) {
        super(buildOptions);
    }

    private final ImageUrlProcessor imageUrlProcessor =
            new ImageUrlProcessor();


    @Override
    public void reset() {
        super.reset();
        generatedStylesheet = null;
        generatedRenameMap = null;
        imageUrlProcessor.setImageRoot(null);
    }

    private File generatedStylesheet;

    public File getGeneratedStylesheet() {
        return generatedStylesheet;
    }

    private File generatedRenameMap;

    public File getGeneratedRenameMap() {
        return generatedRenameMap;
    }

    private void compileCssFiles(
            @Nonnull final List<File> sourceFiles,
            @Nonnull final File outputFile,
            @Nullable final File renameMap,
            @Nonnull final Boolean productionBoolean,
            @Nonnull final Boolean debugBoolean)
            throws BuildException {

        if (sourceFiles != null && sourceFiles.isEmpty()) {
            throwBuildException("No input files specified.");
        }

        List<String> arguments = new ArrayList<String>();
        arguments.add("--allow-unrecognized-functions");
        arguments.add("--allow-unrecognized-properties");

        if (renameMap != null) {
            FsTool.ensureDirectoryFor(renameMap);
            arguments.add("--output-renaming-map");
            arguments.add(renameMap.getPath());
        }

        if (productionBoolean != null && productionBoolean) {
            arguments.add("--output-renaming-map-format");
            arguments.add("CLOSURE_COMPILED");
            if (debugBoolean != null && debugBoolean) {
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


        arguments.add("--output-file");
        FsTool.ensureDirectoryFor(outputFile);
        arguments.add(FsTool.FILE_TO_FILEPATH.apply(outputFile));
        arguments.addAll(Immuter.list(sourceFiles, FsTool.FILE_TO_FILEPATH));
        ClosureCommandLineCompiler.main(Immuter.stringArray(arguments));
        generatedRenameMap = renameMap;
    }

    private void compileCssFiles(final File outputFile)
            throws BuildException {
        compileCssFiles(
                buildOptions.getSourceFiles(),
                outputFile,
                buildOptions.getRenameMap(),
                buildOptions.getShouldGenerateForProduction(),
                buildOptions.getShouldGenerateForDebug());
    }


    @Nonnull
    public String parseCssFunctions(
            @Nonnull final String inputContent,
            @Nonnull final String base) {
        imageUrlProcessor.setImageRoot(base);
        return imageUrlProcessor.processString(inputContent);
    }

    public void parseFunctionsFromCss(final File inputFile,
                                      final File outputFile)
            throws BuildException {
        try {
            final String content = FsTool.read(inputFile);
            final File assetDirectory = buildOptions.getAssetsDirectory();
            final String base = FsTool.getRelative(assetDirectory,
                    outputFile.getParentFile());
            FsTool.write(outputFile, parseCssFunctions(content, base));
            generatedStylesheet = outputFile;
        } catch (IOException ioException) {
            throwBuildException(ioException);
        }
    }


    private File getTemporaryFile()
            throws BuildException {
        try {
            return FsTool.getTempFile("css_", "pass1");
        } catch (IOException e) {
            throwBuildException(e);
        }
        return null;
    }

    @Override
    public void build()
            throws BuildException {
        if (buildOptions == null) {
            throwBuildException("No build options given");
        }
        final File tempFile = getTemporaryFile();
        final File outputFile = buildOptions.getOutputFile();
        compileCssFiles(tempFile);
        parseFunctionsFromCss(tempFile, outputFile);
    }

}
