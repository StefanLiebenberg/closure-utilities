package org.stefanl.closure_utilities.javascript;


import org.stefanl.closure_utilities.internal.*;
import org.stefanl.closure_utilities.render.DependencyFileRenderer;
import org.stefanl.closure_utilities.render.RenderException;
import org.stefanl.closure_utilities.utilities.FsTool;
import com.google.javascript.jscomp.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

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
    }

    @Nonnull
    private CompilerOptions getCompilerOptions(final File sourceMap) {
        final CompilerOptions compilerOptions = new CompilerOptions();
        if (buildOptions.getShouldDebug()) {
            final CompilationLevel level =
                    CompilationLevel.ADVANCED_OPTIMIZATIONS;
            level.setOptionsForCompilationLevel(compilerOptions);
            level.setTypeBasedOptimizationOptions(compilerOptions);
            compilerOptions.setAggressiveVarCheck(CheckLevel.ERROR);
            compilerOptions.setBrokenClosureRequiresLevel(CheckLevel.ERROR);
            compilerOptions.setCheckGlobalNamesLevel(CheckLevel.ERROR);
            compilerOptions.setCheckGlobalThisLevel(CheckLevel.ERROR);
            compilerOptions.setCheckMissingReturn(CheckLevel.ERROR);
            compilerOptions.setCheckProvides(CheckLevel.ERROR);
            compilerOptions.setCheckRequires(CheckLevel.ERROR);

            compilerOptions.setWarningLevel(DiagnosticGroups.ACCESS_CONTROLS,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .AMBIGUOUS_FUNCTION_DECL, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .DEBUGGER_STATEMENT_PRESENT, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.CHECK_REGEXP,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.CONST,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .CONSTANT_PROPERTY, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.DEPRECATED,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .DUPLICATE_MESSAGE, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.DUPLICATE_VARS,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.ES5_STRICT,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .EXTERNS_VALIDATION, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups.UNDEFINED_NAMES,
                    CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .UNDEFINED_VARIABLES, CheckLevel.ERROR);
            compilerOptions.setWarningLevel(DiagnosticGroups
                    .TYPE_INVALIDATION, CheckLevel.ERROR);

            // we declare many unkown defines.
            compilerOptions.setWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES,
                    CheckLevel.OFF);


            compilerOptions.setCheckTypes(true);
            compilerOptions.setOptimizeArgumentsArray(true);
            compilerOptions.setOptimizeCalls(true);
            compilerOptions.setOptimizeParameters(true);
            compilerOptions.setOptimizeReturns(true);
            compilerOptions.setCheckSymbols(true);
            compilerOptions.setCheckEventfulObjectDisposalPolicy
                    (CheckEventfulObjectDisposal.DisposalCheckingPolicy
                            .AGGRESSIVE);
            compilerOptions.setAggressiveRenaming(true);
        } else {
            final CompilationLevel level =
                    CompilationLevel.SIMPLE_OPTIMIZATIONS;
            level.setOptionsForCompilationLevel(compilerOptions);
            level.setTypeBasedOptimizationOptions(compilerOptions);
            compilerOptions.setPrettyPrint(true);
        }

        final Map<String, Object> globals =
                buildOptions.getGlobals();
        if (globals != null) {
            compilerOptions.setDefineReplacements(globals);
        }

        if (sourceMap != null) {
            compilerOptions.setSourceMapFormat(SourceMap.Format.V3);
            //compilerOptions.setSourceMapDetailLevel(SourceMap.DetailLevel
            // .ALL);
            compilerOptions.setSourceMapDetailLevel(SourceMap.DetailLevel
                    .SYMBOLS);
            compilerOptions.setSourceMapOutputPath(sourceMap.getPath());
        }
        return compilerOptions;
    }

    @Override
    public void buildInternal() throws Exception {
        findDependencyFiles();
        buildDependenciesFile();
        calculateDependencies();
        // compileScriptFile();
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

    @Nullable
    public List<ClosureSourceFile> getClosureSourceFiles() {
        return closureSourceFiles;
    }

    @Override
    public void checkOptions() throws BuildException {
        super.checkOptions();
        checkNotNull(buildOptions.getEntryPoints());
        checkNotNull(buildOptions.getSourceDirectories());
    }
}
