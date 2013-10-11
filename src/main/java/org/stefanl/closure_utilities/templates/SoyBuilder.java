package org.stefanl.closure_utilities.templates;


import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.IBuilder;
import org.stefanl.closure_utilities.utilities.FsTool;
import com.google.common.collect.Lists;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.shared.SoyGeneralOptions;
import com.google.template.soy.xliffmsgplugin.XliffMsgPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SoyBuilder extends AbstractBuilder<SoyBuildOptions>
        implements IBuilder {

    public SoyBuilder() {}

    public SoyBuilder(@Nonnull final SoyBuildOptions buildOptions) {
        super(buildOptions);
    }


    @Override
    public void reset() {
        super.reset();
        compiledSources = null;
        generatedFiles = null;
    }

    private Map<File, String> compiledSources;

    private Collection<File> generatedFiles;

    private final XliffMsgPlugin xliffMsgPlugin = new XliffMsgPlugin();

    @Nonnull
    private SoyFileSet.Builder getSoyBuilder() {
        final SoyFileSet.Builder builder = new SoyFileSet.Builder();
        if (buildOptions.getGlobalStringMap() != null) {
            builder.setCompileTimeGlobals(buildOptions.getGlobalStringMap());
        }
        builder.setCssHandlingScheme(SoyGeneralOptions.CssHandlingScheme
                .BACKEND_SPECIFIC);
        builder.setAllowExternalCalls(false);
        return builder;
    }

    @Nonnull
    private SoyFileSet getSoyFileSet(
            @Nonnull final List<File> files)
            throws IOException {
        final SoyFileSet.Builder builder = getSoyBuilder();
        for (File file : files) {
            builder.add(FsTool.read(file), file.getPath());
        }
        return builder.build();
    }

    @Nonnull
    private SoyJsSrcOptions getJsSrcOptions() {
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
    private List<String> compileList(
            @Nonnull final List<File> files) throws IOException {
        final SoyFileSet soyFileSet = getSoyFileSet(files);
        final SoyJsSrcOptions jsSrcOptions = getJsSrcOptions();
        final File messageFile = buildOptions.getMessageFile();
        SoyMsgBundle bundle = null;
        if (messageFile != null && messageFile.exists()) {
            bundle = xliffMsgPlugin.parseTranslatedMsgsFile(
                    FsTool.read(messageFile));
        }
        return soyFileSet.compileToJsSrc(jsSrcOptions, bundle);
    }

    @Nonnull
    private Map<File, String> compile(
            @Nonnull final Collection<File> sources)
            throws IOException {
        final List<File> fileList = Lists.newArrayList(sources);
        final List<String> strSources = compileList(fileList);
        final Map<File, String> result = new HashMap<File, String>();
        for (int i = 0; i < fileList.size(); i++) {
            result.put(fileList.get(i), strSources.get(i));
        }
        return result;
    }


    public void compileSoyFiles() throws IOException {
        final Collection<File> sourceFiles = new HashSet<File>();
        final Collection<File> sources = buildOptions.getSources();

        if (sources != null) {
            sourceFiles.addAll(sources);
        }

        final Collection<File> sourceDirectories =
                buildOptions.getSourceDirectories();
        if (sourceDirectories != null && !sourceDirectories.isEmpty()) {
            for (File sourceDirectory : sourceDirectories) {
                sourceFiles.addAll(
                        FsTool.find(sourceDirectory, "soy"));
            }
        }
        compiledSources = compile(sourceFiles);
    }


    @Override
    public void buildInternal() throws Exception {

        final File output = buildOptions.getOutputDirectory();
        compileSoyFiles();
        generatedFiles = new HashSet<File>();
        File sourceFile, outputFile;
        String sourceContent;
        for (Map.Entry<File, String> entry : compiledSources.entrySet()) {
            sourceFile = entry.getKey();
            sourceContent = entry.getValue();
            outputFile = new File(output, sourceFile.getPath() + ".js");
            generatedFiles.add(outputFile);
            FsTool.write(outputFile, sourceContent);
        }

    }

    public Map<File, String> getCompiledSources() {
        return compiledSources;
    }

    public Collection<File> getGeneratedFiles() {
        return generatedFiles;
    }
}
