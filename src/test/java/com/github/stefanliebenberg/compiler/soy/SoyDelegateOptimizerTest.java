package com.github.stefanliebenberg.compiler.soy;


import com.github.stefanliebenberg.utilities.Immuter;
import com.google.common.base.Function;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoyDelegateOptimizerTest {

    private SourceFile getSourceFile(String path)
            throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(path);
        return SourceFile.fromInputStream(path, inputStream);
    }

    private Map<String, String> getSoySources (URL... soyURI)  {
        SoyFileSet.Builder fileSetBuilder = new SoyFileSet.Builder();
        for(URL url : soyURI) {
            fileSetBuilder.add(url);
        }
        SoyFileSet set = fileSetBuilder.build();
        SoyJsSrcOptions soyOptions = new SoyJsSrcOptions();
        List<String> listOfSources = set.compileToJsSrc(soyOptions, null);
        Map<String, String> sources = new HashMap<String, String>();
        for(int i = 0; i < soyURI.length; i++) {
            URL url = soyURI[i];
            String content = listOfSources.get(i);
            sources.put(url.toString(), content);
        }
        return sources;
    }

    @Test
    public void testOptimizationPass()
            throws IOException {

        Map<String, String> soySources = getSoySources(
                getClass().getResource("/soy/example.soy"),
                getClass().getResource("/soy/example_override.soy"));

        Compiler compiler = new Compiler();
        CompilerOptions jsOptions = new CompilerOptions();

        List<SourceFile> externs =
                CommandLineRunner.getDefaultExterns();
        List<SourceFile> inputs =
                new ArrayList<SourceFile>();
        inputs.add(getSourceFile("/javascript/goog/base.js"));
        inputs.add(getSourceFile("/javascript/goog/debug/error.js"));
        inputs.add(getSourceFile("/javascript/goog/dom/nodetype.js"));
        inputs.add(getSourceFile("/javascript/goog/string/string.js"));
        inputs.add(getSourceFile("/javascript/goog/asserts/asserts.js"));
        inputs.add(getSourceFile("/javascript/goog/structs/inversionmap.js"));
        inputs.add(getSourceFile("/javascript/goog/i18n/graphemebreak.js"));
        inputs.add(getSourceFile("/javascript/goog/format/format.js"));
        inputs.add(getSourceFile("/javascript/goog/array/array.js"));
        inputs.add(getSourceFile("/javascript/goog/useragent/useragent.js"));
        inputs.add(getSourceFile("/javascript/goog/dom/browserfeature.js"));
        inputs.add(getSourceFile("/javascript/goog/dom/tagname.js"));
        inputs.add(getSourceFile("/javascript/goog/dom/classes.js"));
        inputs.add(getSourceFile("/javascript/goog/functions/functions.js"));
        inputs.add(getSourceFile("/javascript/goog/math/math.js"));
        inputs.add(getSourceFile("/javascript/goog/math/coordinate.js"));
        inputs.add(getSourceFile("/javascript/goog/math/size.js"));
        inputs.add(getSourceFile("/javascript/goog/object/object.js"));
        inputs.add(getSourceFile("/javascript/goog/dom/dom.js"));
        inputs.add(getSourceFile("/javascript/goog/i18n/bidiformatter.js"));
        inputs.add(getSourceFile("/javascript/goog/i18n/bidi.js"));
        inputs.add(getSourceFile("/javascript/goog/soy/data.js"));
        inputs.add(getSourceFile("/javascript/goog/soy/soy.js"));
        inputs.add(getSourceFile("/javascript/goog/string/stringbuffer.js"));
        inputs.add(getSourceFile("/javascript/soyutils_usegoog.js"));

        inputs.addAll(Immuter.list(soySources.entrySet(),
                new Function<Map.Entry<String, String>, SourceFile>() {
            @Nullable
            @Override
            public SourceFile apply(@Nullable Map.Entry<String, String> entry) {
                return SourceFile.fromCode(entry.getKey(), entry.getValue());
            }
        }));

        inputs.add(SourceFile.fromCode("path:calling", "alert(example.Foo({Variant: 'RED'}));"));
        CompilationLevel level = CompilationLevel.ADVANCED_OPTIMIZATIONS;
        level.setDebugOptionsForCompilationLevel(jsOptions);
        level.setOptionsForCompilationLevel(jsOptions);
        level.setTypeBasedOptimizationOptions(jsOptions);

        SoyDelegateOptimizer.addToCompile(compiler, jsOptions);

        Result result = compiler.compile(externs, inputs, jsOptions);


        String source = compiler.toSource();
        //System.err.println(source);

        Assert.assertTrue(result.success);
        Assert.assertFalse(source.contains("[DELETE]"));
        Assert.assertTrue(source.contains("[KEEP]"));


    }

}
