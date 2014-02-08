package liebenberg.closure_utilities.soy;


import com.google.common.base.Function;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import org.junit.Assert;
import org.junit.Test;
import liebenberg.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoyDelegateOptimizerTest {

    @Nonnull
    private SourceFile getSourceFile(@Nonnull final String path)
            throws IOException {
        final InputStream inputStream =
                this.getClass().getResourceAsStream(path);
        return SourceFile.fromInputStream(path, inputStream);
    }

    @Nonnull
    private Map<String, String> getSoySources(@Nonnull final URL... soyURI) {
        final SoyFileSet.Builder fileSetBuilder = new SoyFileSet.Builder();
        for (URL url : soyURI) {
            fileSetBuilder.add(url);
        }
        final SoyFileSet set = fileSetBuilder.build();
        final SoyJsSrcOptions soyOptions = new SoyJsSrcOptions();
        final List<String> listOfSources = set.compileToJsSrc(soyOptions, null);
        final Map<String, String> sources = new HashMap<String, String>();
        for (int i = 0; i < soyURI.length; i++) {
            URL url = soyURI[i];
            String content = listOfSources.get(i);
            sources.put(url.toString(), content);
        }
        return sources;
    }

    private static final Function<Map.Entry<String, String>,
            SourceFile> ENTRY_SOURCE_FILE_FUNCTION =
            new Function<Map.Entry<String, String>, SourceFile>() {
                @Nullable
                @Override
                public SourceFile apply(@Nullable Map.Entry<String,
                        String> entry) {
                    if (entry != null) {
                        return SourceFile.fromCode(entry.getKey(),
                                entry.getValue());
                    } else {
                        return null;
                    }
                }
            };

    @Test
    public void testOptimizationPass()
            throws IOException {

        final Map<String, String> soySources = getSoySources(
                getClass().getResource("/app/src/soy/example/example.soy"),
                getClass().getResource("/app/src/soy/example/example_override" +
                        ".soy"));

        final Compiler compiler = new Compiler();
        final CompilerOptions jsOptions = new CompilerOptions();

        final List<SourceFile> externs =
                CommandLineRunner.getDefaultExterns();
        final List<SourceFile> inputs =
                new ArrayList<SourceFile>();
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/base.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/debug/error.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/dom/nodetype.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/string/string.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/asserts/asserts" +
                ".js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/structs" +
                "/inversionmap.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/i18n/graphemebreak" +
                ".js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/format/format.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/array/array.js"));
        inputs.add(getSourceFile
                ("/app/src/javascript/closure-library/closure/goog/useragent/useragent.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/dom/browserfeature" +
                ".js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/dom/tagname.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/dom/classes.js"));
        inputs.add(getSourceFile
                ("/app/src/javascript/closure-library/closure/goog/functions/functions.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/math/math.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/math/coordinate" +
                ".js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/math/size.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/object/object.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/dom/dom.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/i18n/bidiformatter" +
                ".js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/i18n/bidi.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/soy/data.js"));
        inputs.add(getSourceFile("/app/src/javascript/closure-library/closure/goog/soy/soy.js"));
        inputs.add(getSourceFile
                ("/app/src/javascript/closure-library/closure/goog/string/stringbuffer.js"));
        inputs.add(getSourceFile("/app/src/javascript/soyutils_usegoog.js"));
        inputs.addAll(Immuter.list(soySources.entrySet(),
                ENTRY_SOURCE_FILE_FUNCTION));
        inputs.add(SourceFile.fromCode("path:calling",
                "alert(template.example.Foo({Variant: 'RED'}));"));
        final CompilationLevel level = CompilationLevel.ADVANCED_OPTIMIZATIONS;
        level.setDebugOptionsForCompilationLevel(jsOptions);
        level.setOptionsForCompilationLevel(jsOptions);
        level.setTypeBasedOptimizationOptions(jsOptions);

        SoyDelegateOptimizer.addToCompile(compiler, jsOptions);

        final Result result = compiler.compile(externs, inputs, jsOptions);
        final String source = compiler.toSource();
        Assert.assertTrue(result.success);
        Assert.assertFalse(source.contains("[DELETE]"));
        Assert.assertTrue(source.contains("[KEEP]"));
    }

}
