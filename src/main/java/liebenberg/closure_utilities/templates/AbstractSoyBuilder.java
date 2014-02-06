package liebenberg.closure_utilities.templates;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.shared.SoyGeneralOptions;
import com.google.template.soy.xliffmsgplugin.XliffMsgPlugin;
import liebenberg.closure_utilities.internal.AbstractBuilder;
import liebenberg.closure_utilities.internal.BuildOptionsException;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractSoyBuilder
        extends AbstractBuilder<SoyOptions, SoyResult> {

    /**
     * The file extension for soy files.
     */
    protected static final String SOY_EXT = "soy";

    /**
     * A plugin used to translate xliffMessages.
     */
    protected static final XliffMsgPlugin xliffMsgPlugin =
            new XliffMsgPlugin();

    protected AbstractSoyBuilder() {}


    @Nonnull
    protected ImmutableSet<File> findSourceFiles(
            @Nonnull final ImmutableCollection<File> directories)
            throws IOException {
        return findSourceFiles(directories, SOY_EXT);
    }


    @Nonnull
    protected SoyFileSet.Builder getSoyBuilder(
            @Nullable final ImmutableMap<String, String> globalStringMap) {
        final SoyFileSet.Builder builder = new SoyFileSet.Builder();
        builder.setCssHandlingScheme(
                SoyGeneralOptions.CssHandlingScheme.BACKEND_SPECIFIC);
        builder.setAllowExternalCalls(false);
        if (globalStringMap != null) {
            builder.setCompileTimeGlobals(globalStringMap);
        }
        return builder;
    }

    @Nonnull
    protected SoyFileSet getSoyFileSet(
            @Nonnull final List<File> files,
            @Nullable final ImmutableMap<String, String> globalStringMap)
            throws IOException {
        final SoyFileSet.Builder builder = getSoyBuilder(globalStringMap);
        for (File file : files) {
            builder.add(FS.read(file), file.getPath());
        }
        return builder.build();
    }

    @Nonnull
    protected SoyJsSrcOptions getJsSrcOptions() {
        SoyJsSrcOptions jsSrcOptions = new SoyJsSrcOptions();
        //options.setShouldDeclareTopLevelNamespaces(true);
        jsSrcOptions.setShouldProvideRequireJsFunctions(true);
        jsSrcOptions.setShouldGenerateJsdoc(true);
        //options.setShouldProvideRequireSoyNamespaces(true);
        //options.setShouldDeclareTopLevelNamespaces(true);
        jsSrcOptions.setCodeStyle(SoyJsSrcOptions.CodeStyle.STRINGBUILDER);
        jsSrcOptions.setShouldProvideBothSoyNamespacesAndJsFunctions(true);
        //jsSrcOptions.setCssHandlingScheme();
        return jsSrcOptions;
    }


    @Nonnull
    protected List<String> compileList(
            @Nonnull final List<File> files,
            @Nullable final ImmutableMap<String, String> globalMap,
            @Nullable final File messageFile)
            throws IOException {
        final SoyFileSet soyFileSet = getSoyFileSet(files, globalMap);
        final SoyJsSrcOptions jsSrcOptions = getJsSrcOptions();
        SoyMsgBundle bundle = null;
        if (messageFile != null && messageFile.exists()) {
            bundle = xliffMsgPlugin.parseTranslatedMsgsFile(FS.read
                    (messageFile));
            jsSrcOptions.setGoogMsgsAreExternal(false);
        } else {
            jsSrcOptions.setGoogMsgsAreExternal(true);
        }
        return soyFileSet.compileToJsSrc(jsSrcOptions, bundle);
    }

    @Nonnull
    protected Hashtable<File, String> compile(
            @Nonnull final Collection<File> sources,
            @Nullable final ImmutableMap<String, String> globalMap,
            @Nullable final File messageFile)
            throws IOException {
        final List<File> fileList = Lists.newArrayList(sources);
        final List<String> strSources =
                compileList(fileList, globalMap, messageFile);
        final Hashtable<File, String> result = new Hashtable<>();
        for (int i = 0; i < fileList.size(); i++) {
            result.put(fileList.get(i), strSources.get(i));
        }
        return result;
    }

    public void compileSoyFiles(
            @Nonnull SoyOptions options,
            @Nonnull InternalData internalResult)
            throws IOException {
        final Collection<File> sourceFiles = new HashSet<File>();
        final ImmutableCollection<File> sources = options.getSources();

        if (sources != null) {
            sourceFiles.addAll(sources);
        }

        final ImmutableCollection<File> sourceDirectories =
                options.getSourceDirectories();
        if (sourceDirectories != null && !sourceDirectories.isEmpty()) {
            sourceFiles.addAll(findSourceFiles(sourceDirectories));
        }

        if (!sourceFiles.isEmpty()) {
            internalResult.compiledSources = compile(sourceFiles,
                    options.getGlobalStringMap(),
                    options.getMessageFile());
        } else {
            internalResult.compiledSources = new Hashtable<>();
        }
    }

    @Nonnull
    @Override
    protected SoyResult buildInternal(@Nonnull SoyOptions options)
            throws Exception {
        final InternalData internalData = new InternalData();
        internalData.generatedFiles = new HashSet<>();
        internalData.outputDirectory = options.getOutputDirectory();
        compileSoyFiles(options, internalData);
        File sourceFile, outputFile;
        String sourceContent;
        for (Map.Entry<File, String> entry :
                internalData.compiledSources.entrySet()) {
            sourceFile = entry.getKey();
            sourceContent = entry.getValue();
            outputFile = new File(internalData.outputDirectory,
                    sourceFile.getPath() + ".js");
            internalData.generatedFiles.add(outputFile);
            FS.write(outputFile, sourceContent);
        }
        return internalData.toResult();
    }

    protected final static String UNSPECIFIED_SOURCES =
            "Soy sources are unspecified";

    @Override
    public void checkOptions(@Nonnull SoyOptions options) throws
            BuildOptionsException {

        final ImmutableCollection<File> srcDirs = options
                .getSourceDirectories();
        final ImmutableCollection<File> sources = options.getSources();

        final Boolean srcDirsIsSpecified =
                srcDirs != null && !srcDirs.isEmpty();
        final Boolean sourcesIsSpecified =
                sources != null && !sources.isEmpty();

        if (!srcDirsIsSpecified && !sourcesIsSpecified) {
            throw new BuildOptionsException(UNSPECIFIED_SOURCES);
        }
    }

    /**
     * Internal data class for soy builders.
     */
    private static class InternalData {

        /**
         * The soy output directory
         */
        private File outputDirectory;

        /**
         * A set of files generated from a soy build
         */
        private HashSet<File> generatedFiles;

        /**
         * A map of input file to compiled strings.
         */
        private Hashtable<File, String> compiledSources;


        /**
         * @return The SoyResult object.
         */
        protected SoyResult toResult() {
            return new SoyResult(outputDirectory, compiledSources,
                    generatedFiles);
        }
    }
}
