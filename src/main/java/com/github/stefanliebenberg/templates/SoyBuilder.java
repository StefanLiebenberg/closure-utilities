package com.github.stefanliebenberg.templates;


import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.utilities.FsTool;
import com.google.common.collect.Lists;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.shared.SoyGeneralOptions;
import com.google.template.soy.xliffmsgplugin.XliffMsgPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SoyBuilder extends AbstractBuilder<SoyBuildOptions>
        implements IBuilder {

    public SoyBuilder() {}

    public SoyBuilder(final SoyBuildOptions buildOptions) {
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

    private XliffMsgPlugin xliffMsgPlugin = new XliffMsgPlugin();

    private SoyFileSet.Builder getSoyBuilder() {
        SoyFileSet.Builder builder = new SoyFileSet.Builder();
        if (buildOptions.getGlobalStringMap() != null) {
            builder.setCompileTimeGlobals(buildOptions.getGlobalStringMap());
        }
        builder.setCssHandlingScheme(SoyGeneralOptions.CssHandlingScheme
                .BACKEND_SPECIFIC);
        builder.setAllowExternalCalls(false);
        return builder;
    }

    private SoyFileSet getSoyFileSet(final List<File> files) {
        final SoyFileSet.Builder builder = getSoyBuilder();
        for (File file : files) {
            builder.add(FsTool.safeRead(file), file.getPath());
        }
        return builder.build();
    }

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

    private List<String> compileList(List<File> files) {
        SoyFileSet soyFileSet = getSoyFileSet(files);
        SoyJsSrcOptions jsSrcOptions = getJsSrcOptions();
        File messageFile = buildOptions.getMessageFile();
        SoyMsgBundle bundle = null;
        if (messageFile != null && messageFile.exists()) {
            bundle = xliffMsgPlugin.parseTranslatedMsgsFile(FsTool.safeRead
                    (messageFile));
        }
        return soyFileSet.compileToJsSrc(jsSrcOptions, bundle);
    }

    private Map<File, String> compile(Collection<File> sources) {
        List<File> fileList = Lists.newArrayList(sources);
        List<String> strSources = compileList(fileList);
        Map<File, String> result = new HashMap<File, String>();
        for (int i = 0; i < fileList.size(); i++) {
            result.put(fileList.get(i), strSources.get(i));
        }
        return result;
    }


    public void compileSoyFiles() {
        Collection<File> sourceFiles = new HashSet<File>();

        Collection<File> sources = buildOptions.getSources();
        if (sources != null) {
            sourceFiles.addAll(sources);
        }

        Collection<File> sourceDirectories = buildOptions
                .getSourceDirectories();
        if (sourceDirectories != null && !sourceDirectories.isEmpty()) {
            for (File sourceDirectory : sourceDirectories) {
                sourceFiles.addAll(
                        FsTool.find(sourceDirectory, "soy"));
            }
        }
        compiledSources = compile(sourceFiles);
    }


    @Override
    public void build() throws BuildException {
        final File output = buildOptions.getOutputDirectory();
        compileSoyFiles();
        try {
            generatedFiles = new HashSet<File>();
            for (Map.Entry<File, String> entry : compiledSources.entrySet()) {
                File sourceFile = entry.getKey();
                String sourceContent = entry.getValue();

                File outputFile = new File(output, sourceFile.getPath() +
                        ".js");
                generatedFiles.add(outputFile);
                FsTool.write(outputFile, sourceContent);
            }
        } catch (IOException e) {
            throwBuildException(e);
        }
    }

    public Map<File, String> getCompiledSources() {
        return compiledSources;
    }

    public Collection<File> getGeneratedFiles() {
        return generatedFiles;
    }
}
