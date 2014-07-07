package slieb.closureutils.javascript;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import slieb.closureutils.build.BuildException;
import slieb.closureutils.build.BuilderInterface;
import slieb.closureutils.dependencies.DependencyCalculator;
import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.dependencies.DependencyScanner;
import slieb.closureutils.rendering.DefinesFileRenderer;
import slieb.closureutils.rendering.DependencyFileRenderer;
import slieb.closureutils.rendering.RenderException;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;
import slieb.closureutils.resources.StringResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

public class JsBuilder implements BuilderInterface<JsOptions, JsResult> {

    protected final JavascriptDependencyParser dependencyParser;

    private final DependencyFileRenderer depFileRenderer =
            new DependencyFileRenderer();

    private final DefinesFileRenderer definesFileRenderer =
            new DefinesFileRenderer();

    @Inject
    public JsBuilder(JavascriptDependencyParser dependencyParser) {
        this.dependencyParser = dependencyParser;
    }


    private void buildDefinesFile(@Nonnull JsOptions options,
                                  @Nonnull JsResult.Builder builder)
            throws RenderException, IOException {
        Resource definesResource = new StringResource(definesFileRenderer.render(options.getGlobals()), null);
        builder.setDefinesResource(definesResource);
    }

    private void buildDependenciesFile(@Nonnull JsOptions options,
                                       @Nonnull JsResult.Builder builder)
            throws RenderException, IOException {
        builder.setDepsResource(
                new StringResource(depFileRenderer.render(null), null));
    }

    protected DependencyScanner getDependencyScanner(
            ResourceProvider resourceProvider) {
        return new DependencyScanner(resourceProvider, dependencyParser);
    }

    private void calculateDependencies(@Nonnull JsOptions options,
                                       @Nonnull JsResult.Builder builder)
            throws IOException, BuildException {

        builder.setResolvedResources(
                getDependencyScanner(options.getResourceProvider())
                        .getDependencies(options.getEntryPoints()));

//        final List<String> entryPoints = options.getEntryPoints();
//        final List<File> entryFiles = options.getEntryFiles();
//        if (entryPoints != null || entryFiles != null) {
//            final DependencyOptions<ClosureSourceFile>
//                    depBuildOptions = new DependencyOptions<>();
//            depBuildOptions.setEntryPoints(entryPoints);
//            depBuildOptions.setEntryFiles(builder.closureEntryFiles);
//            depBuildOptions.setSourceFiles(builder.closureSourceFiles);
//            builder.resolvedSourceFiles =
//                    depBuilder.build(depBuildOptions);
//            builder.resolvedFiles =
//                    Immuter.list(builder.resolvedSourceFiles,
//                            SourceFileBase.TO_FILE);
//        }
//
//        final List<File> scriptsFilesToCompile =
//                getScriptFilesToCompile(builder);
//        builder.scriptFiles = scriptsFilesToCompile;

    }

    private void setCompilerOptionsForCompile(
            @Nonnull final CompilerOptions o) {
        final CompilationLevel level =
                CompilationLevel.ADVANCED_OPTIMIZATIONS;
        final CheckLevel err = CheckLevel.ERROR, off = CheckLevel.OFF;
        level.setOptionsForCompilationLevel(o);
        level.setTypeBasedOptimizationOptions(o);
        o.setAggressiveVarCheck(err);
        o.setBrokenClosureRequiresLevel(err);
        o.setCheckGlobalNamesLevel(err);
        o.setCheckGlobalThisLevel(err);
        o.setCheckMissingReturn(err);
        o.setCheckProvides(err);
        o.setCheckRequires(err);
        o.setWarningLevel(DiagnosticGroups.ACCESS_CONTROLS, err);
        o.setWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, err);
        o.setWarningLevel(DiagnosticGroups.DEBUGGER_STATEMENT_PRESENT, err);
        o.setWarningLevel(DiagnosticGroups.CHECK_REGEXP, err);
        o.setWarningLevel(DiagnosticGroups.CHECK_VARIABLES, err);
        o.setWarningLevel(DiagnosticGroups.CONST, err);
        o.setWarningLevel(DiagnosticGroups.CONSTANT_PROPERTY, err);
        o.setWarningLevel(DiagnosticGroups.DEPRECATED, err);
        o.setWarningLevel(DiagnosticGroups.DUPLICATE_MESSAGE, err);
        o.setWarningLevel(DiagnosticGroups.DUPLICATE_VARS, err);
        o.setWarningLevel(DiagnosticGroups.ES5_STRICT, err);
        o.setWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION, err);
        o.setWarningLevel(DiagnosticGroups.UNDEFINED_NAMES, err);
        o.setWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES, err);
        o.setWarningLevel(DiagnosticGroups.TYPE_INVALIDATION, err);
        o.setCheckEventfulObjectDisposalPolicy(
                CheckEventfulObjectDisposal.DisposalCheckingPolicy.AGGRESSIVE);
        // we declare many unkown defines.
        o.setWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES, off);
        o.setCheckTypes(true);
        o.setOptimizeArgumentsArray(true);
        o.setOptimizeCalls(true);
        o.setOptimizeParameters(true);
        o.setOptimizeReturns(true);
        o.setCheckSymbols(true);
        o.setAggressiveRenaming(true);

    }

    private void setCompilerOptionsForDebug(@Nonnull final CompilerOptions o) {
        final CompilationLevel level = CompilationLevel.SIMPLE_OPTIMIZATIONS;
        level.setOptionsForCompilationLevel(o);
        level.setTypeBasedOptimizationOptions(o);
        o.setPrettyPrint(true);
    }

    @Nonnull
    private CompilerOptions getCompilerOptions(
            @Nonnull final JsOptions options,
            @Nullable final File sourceMap) {

        final CompilerOptions cOpts = new CompilerOptions();
//
//        if (!options.getShouldDebug()) {
//            if (options.getVerbose()) {
//                LOGGER.info("Setting compiler options for production!");
//            }
//            setCompilerOptionsForCompile(cOpts);
//        } else {
//            if (options.getVerbose()) {
//                LOGGER.info("Setting compiler options for debug!");
//            }
//            setCompilerOptionsForDebug(cOpts);
//        }
//
//        MessageBundle msgBundle = options.getMessageBundle();
//        if (msgBundle != null) {
//            if (options.getVerbose()) {
//                LOGGER.info("Setting message bundle.");
//            }
//            cOpts.setMessageBundle(msgBundle);
//        }
//
//        final Map<String, Object> globals = options.getGlobals();
//        if (globals != null) {
//            if (options.getVerbose()) {
//                LOGGER.info("Setting globals");
//            }
//            cOpts.setDefineReplacements(globals);
//        }
//
//        if (sourceMap != null) {
//            if (options.getVerbose()) {
//                LOGGER.info("Setting sourceMap output path: " + sourceMap
//                        .getPath());
//            }
//            cOpts.setSourceMapFormat(SourceMap.Format.V3);
//            cOpts.setSourceMapDetailLevel(SourceMap.DetailLevel.ALL);
//            cOpts.setSourceMapOutputPath(sourceMap.getPath());
//        }
        return cOpts;
    }


    private List<SourceFile> getExternFiles() throws IOException {
        final List<SourceFile> externs = new ArrayList<>();
        externs.addAll(CommandLineRunner.getDefaultExterns());
        return externs;
    }

    private List<SourceFile> getCompilerSourceFiles(
            @Nonnull List<File> scriptFilesToCompile) {
        return Lists.transform(scriptFilesToCompile,
                new Function<File, SourceFile>() {
                    @Nullable
                    @Override
                    public SourceFile apply(@Nullable File input) {
                        return SourceFile.fromFile(input);
                    }
                });
    }

    private void addCustomBuildPasses(
            @Nonnull final java.lang.Compiler compiler,
            @Nonnull final CompilerOptions compilerOptions) {
//        SoyDelegateOptimizer.addToCompile(compiler, compilerOptions);
        // todo, add space here for user to specify custom passes.
    }

    private void compileScriptFile(@Nonnull final JsOptions options,
                                   @Nonnull final JsResult.Builder builder)
            throws BuildException, IOException {
        final Compiler compiler = new Compiler();
        final CompilerOptions compilerOptions =
                getCompilerOptions(options, null);
        final List<SourceFile> externs = getExternFiles();
//        final List<SourceFile> sources =
//                getCompilerSourceFiles(builder.scriptFiles);
//        addCustomBuildPasses(compiler, compilerOptions);
//        builder.compilerResult =
//                compiler.compile(externs, sources, compilerOptions);
//        if (builder.compilerResult.success) {
//            final String source = compiler.toSource();
//            builder.outputFile = options.getOutputFile();
//            FS.write(builder.outputFile, source);
//        } else {
//            throw new BuildException("Compilation Failure");
//        }
    }

    @Nonnull
    private ImmutableList<File> getScriptFilesToCompile(
            @Nonnull JsResult.Builder builder) {
        final ImmutableList.Builder<File> toCompileFiles =
                new ImmutableList.Builder<File>();
//        if (builder.baseFile != null) {
//            toCompileFiles.add(builder.baseFile);
//        }
//
//        if (builder.dependencyFile != null) {
//            toCompileFiles.add(builder.dependencyFile);
//        }
//
//        // add rename map
//        // add defines.js
//
//        if (builder.resolvedFiles != null) {
//            toCompileFiles.addAll(builder.resolvedFiles);
//        }
        return toCompileFiles.build();
    }


    private static final String UNSPECIFIED_OUTPUT_FILE =
            "Javascript output file not specified";

    private static final String UNSPECIFIED_ENTRY_POINTS =
            "Javascript entry points have not specified.";

    private static final String UNSPECIFIED_SOURCE_DIRECTORIES =
            "Javascript source directories have not specified.";


    @Nonnull

    protected JsResult buildInternal(@Nonnull JsOptions options)
            throws Exception {
        final JsResult.Builder builder = new JsResult.Builder();


//        if (builder.baseFile == null) {
//            throw new BuildException("Closure Library's base.js cannot be " +
//                    "found. Check that the closure library directory has
// been" +
//                    " added as a source directory.");
//        }

        buildDependenciesFile(options, builder);
        buildDefinesFile(options, builder);
        calculateDependencies(options, builder);
//        if (options.getShouldCompile()) {
//            compileScriptFile(options, builder);
//        }
        return builder.build();
    }

    @Override
    public JsResult build(JsOptions options) throws BuildException {
        try {
            return buildInternal(options);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
