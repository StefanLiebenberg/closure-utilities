package com.github.stefanliebenberg.javascript;


import com.github.stefanliebenberg.internal.*;
import com.github.stefanliebenberg.render.DependencyFileRenderer;
import com.github.stefanliebenberg.render.RenderException;
import com.github.stefanliebenberg.utilities.FsTool;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class JsBuilder
        extends AbstractBuilder<JsBuildOptions>
        implements IBuilder {

    private final ClosureDependencyParser dependencyParser =
            new ClosureDependencyParser();

    private final DependencyBuilder<ClosureSourceFile> dependencyBuilder =
            new DependencyBuilder<ClosureSourceFile>();

    private static final String JS_EXT = "js";

    private List<File> sourceFiles;

    private List<ClosureSourceFile> closureSourceFiles;

    public void findDependencyFiles()
            throws IOException {
        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        if (sourceDirectories != null) {
            final Collection<File> sourceFiles =
                    FsTool.find(sourceDirectories, JS_EXT);
            closureSourceFiles = new ArrayList<ClosureSourceFile>();
            for (File sourceFile : sourceFiles) {
                ClosureSourceFile closureSourceFile =
                        new ClosureSourceFile(sourceFile);
                dependencyParser.parse(closureSourceFile,
                        FsTool.read(sourceFile));
                closureSourceFiles.add(closureSourceFile);
            }
        }
    }

    private final DependencyFileRenderer dependencyFileRenderer =
            new DependencyFileRenderer();


    public void buildDependenciesFile() throws RenderException, IOException {
        File dependencyFile = buildOptions.getOutputDependencyFile();
        if (dependencyFile != null) {
            FsTool.write(dependencyFile, dependencyFileRenderer
                    .setBasePath(null)
                    .setDependencies(closureSourceFiles)
                    .render());
        }
    }

    public void calculateDependencies()
            throws DependencyException, IOException, BuildException {
        if (buildOptions.getShouldCalculateDependencies()) {
            final List<String> entryPoints = buildOptions.getEntryPoints();
            final Collection<File> srcDirectories = buildOptions
                    .getSourceDirectories();
            if (srcDirectories != null && entryPoints != null) {
                final Collection<File> allSourceFiles =
                        FsTool.find(srcDirectories, JS_EXT);
                final Collection<ClosureSourceFile> dependencies =
                        new HashSet<ClosureSourceFile>();

                for (File sourceFile : allSourceFiles) {
                    ClosureSourceFile closureSourceFile =
                            new ClosureSourceFile(sourceFile);
                    try (Reader fileReader = new FileReader(sourceFile)) {
                        dependencyParser.parse(closureSourceFile, fileReader);
                    }
                    dependencies.add(closureSourceFile);
                }

                final DependencyBuildOptions<ClosureSourceFile>
                        depBuildOptions =
                        new DependencyBuildOptions<ClosureSourceFile>();
                depBuildOptions.setEntryPoints(buildOptions.getEntryPoints());
                depBuildOptions.setSourceFiles(dependencies);
                dependencyBuilder.setBuildOptions(depBuildOptions);
                dependencyBuilder.build();
            }
            sourceFiles = dependencyBuilder.getResolvedFiles();
        } else {
            sourceFiles = buildOptions.getSourceFiles();
        }

    }

    @Override
    public void build() throws BuildException {
        checkOptions();
        try {
            findDependencyFiles();
            buildDependenciesFile();
            calculateDependencies();
            // compileScriptFile();
        } catch (IOException | DependencyException | RenderException
                exception) {
            throwBuildException(exception);
        }
    }

    @Override
    public void reset() {
        super.reset();
        dependencyBuilder.reset();
        dependencyFileRenderer.reset();
        sourceFiles = null;
    }

    @Nullable
    public List<File> getSourceFiles() {
        return sourceFiles;
    }

    public List<ClosureSourceFile> getClosureSourceFiles() {
        return closureSourceFiles;
    }
}
