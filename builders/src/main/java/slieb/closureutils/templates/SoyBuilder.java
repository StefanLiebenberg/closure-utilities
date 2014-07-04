package slieb.closureutils.templates;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.xliffmsgplugin.XliffMsgPlugin;
import slieb.closureutils.build.BuildException;
import slieb.closureutils.build.BuilderInterface;
import slieb.closureutils.resources.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.template.soy.shared.SoyGeneralOptions
        .CssHandlingScheme.BACKEND_SPECIFIC;

public class SoyBuilder implements BuilderInterface<SoyOptions, SoyResult> {

    /**
     * A plugin used to translate xliffMessages.
     */
    protected static final XliffMsgPlugin xliffMsgPlugin =
            new XliffMsgPlugin();


    @Override
    public SoyResult build(SoyOptions options) throws BuildException {
        SoyResult.Builder builder = new SoyResult.Builder();

        ResourceProvider resourceProvider = options.getResourceProvider();
        try {
            builder.setCompiledResources(compile(resourceProvider,
                    options.getGlobalStringMap(),
                    options.getMessageFile()));
        } catch (IOException exception) {
            throw new BuildException(exception);
        }

        return builder.build();
    }


    @Nonnull
    protected SoyFileSet.Builder getSoyBuilder(Map<String,
            String> globalStringMap) {
        final SoyFileSet.Builder builder = new SoyFileSet.Builder();
        builder.setCssHandlingScheme(BACKEND_SPECIFIC);
        builder.setAllowExternalCalls(false);
        if (globalStringMap != null) {
            builder.setCompileTimeGlobals(globalStringMap);
        }
        return builder;
    }

    @Nonnull
    protected SoyFileSet getSoyFileSet(
            List<Resource> resources,
            Map<String, String> globalStringMap)
            throws IOException {
        final SoyFileSet.Builder builder = getSoyBuilder(globalStringMap);
        for (Resource resource : resources) {
            builder.add(Resources.readResource(resource), resource.getUri().getPath());
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
            @Nonnull final List<Resource> resources,
            @Nullable final Map<String, String> globalMap,
            @Nullable final Resource mesResource)
            throws IOException {
        final SoyFileSet soyFileSet = getSoyFileSet(resources, globalMap);
        final SoyJsSrcOptions jsSrcOptions = getJsSrcOptions();
        SoyMsgBundle bundle = null;
        if (mesResource != null) {
            bundle = xliffMsgPlugin.parseTranslatedMsgsFile(Resources.readResource
                    (mesResource));
            jsSrcOptions.setGoogMsgsAreExternal(false);
        } else {
            jsSrcOptions.setGoogMsgsAreExternal(true);
        }
        return soyFileSet.compileToJsSrc(jsSrcOptions, bundle);
    }

    @Nonnull
    protected ResourceProvider compile(
            @Nonnull final ResourceProvider resourceProvider,
            @Nullable final Map<String, String> globalMap,
            @Nullable final Resource messageResource)
            throws IOException {
        final List<Resource> resources = newArrayList(resourceProvider
                .getResources());
        final List<String> compilation = compileList(resources, globalMap,
                messageResource);
        final Set<Resource> result = new HashSet<>();
        for (int i = 0, s = resources.size(); i < s; i++) {
            result.add(new StringResource(compilation.get(i),
                    resources.get(i).getUri()));
        }
        return new IterableResourceProvider(result, null);
    }
}
