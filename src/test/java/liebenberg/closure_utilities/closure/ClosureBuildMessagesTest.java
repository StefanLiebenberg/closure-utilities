package liebenberg.closure_utilities.closure;

import com.google.javascript.jscomp.MessageBundle;
import com.google.javascript.jscomp.XtbMessageBundle;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.ContextFactory;
import com.google.javascript.rhino.head.ScriptableObject;
import com.google.javascript.rhino.head.tools.shell.Global;
import liebenberg.closure_utilities.internal.AbstractBuildTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests feature for templates to ignore xlif file, and for it to used at
 * compile time instead.
 */
public class ClosureBuildMessagesTest
        extends AbstractBuildTest<ClosureBuilder, ClosureOptions,
        ClosureResult> {

    public ClosureBuildMessagesTest() throws
            IllegalAccessException,
            InstantiationException {
        super(ClosureBuilder.class, ClosureOptions.class);
    }


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        builderOptions.setOutputDirectory(outputDirectory);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private static final liebenberg.closure_utilities.rhino.Console console =
            new liebenberg.closure_utilities.rhino.Console();

    @Test
    public void testVanillaTranslations() throws Exception {
        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("company.greeting");
        builderOptions.setJavascriptEntryPoints(entryPoints);
        builderOptions.setJavascriptSourceDirectories
                (getJavascriptSourceDirectories());
        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(false);
        ClosureResult result = builder.buildJsOnly(builderOptions);

        final Context context = new ContextFactory().enterContext();
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_3);

        //scope = context.initStandardObjects();
        final Global scope = new Global(context);
        ScriptableObject.putProperty(scope, "console",
                Context.javaToJS(console, scope));
        final InputStream inputStream = getClass().getResourceAsStream("/env" +
                ".rhino.js");
        final InputStreamReader inputStreamReader = new InputStreamReader
                (inputStream);
        context.evaluateReader(scope, inputStreamReader, "/env/rhino.js", 0,
                null);

        FileReader fileReader;
        File baseFile = result.getJsBaseFile();
        Assert.assertNotNull(baseFile);
        fileReader = new FileReader(baseFile);
        context.evaluateReader(scope, fileReader, baseFile.getPath(), 0, null);
        fileReader.close();

        File scriptFile = result.getJsOutputFile();
        Assert.assertNotNull(scriptFile);
        fileReader = new FileReader(scriptFile);
        context.evaluateReader(scope, fileReader, scriptFile.getPath(), 0,
                null);
        fileReader.close();
        context.evaluateString(scope, "window.onload()", "inline", 0, null);
        context.evaluateString(scope, "Envjs.wait()", "inline", 0, null);
        String hiResult = (String) context.evaluateString(scope,
                "company.greeting.hello('Peter')", "inline", 0, null);
        Assert.assertEquals("Hello Peter", hiResult);

        String byeRresult = (String) context.evaluateString(scope,
                "company.greeting.goodbye('Peter')", "inline", 0, null);
        Assert.assertEquals("Good Bye Peter", byeRresult);

        String welcomeResult = (String) context.evaluateString(scope,
                "company.greeting.goodbye('Peter')", "inline", 0, null);
        Assert.assertEquals("Welcome Peter", welcomeResult);
        Context.exit();
    }


//    @Test
//    public void testBundledTranslations() throws Exception {
//        List<String> entryPoints = new ArrayList<>();
//        entryPoints.add("company.greeting");
//        builderOptions.setJavascriptEntryPoints(entryPoints);
//        builderOptions.setJavascriptSourceDirectories
//                (getJavascriptSourceDirectories());
//        builderOptions.setShouldCompile(true);
//        builderOptions.setShouldDebug(false);
//
//        InputStream messageStream = getClass().getResourceAsStream("/app/src/messages/company.xlf");
//        MessageBundle messageBundle = new XtbMessageBundle(messageStream, "unk");
//        builderOptions.setMessageBundle(messageBundle);
//
//        ClosureResult result = builder.buildJsOnly(builderOptions);
//
//        final Context context = new ContextFactory().enterContext();
//        context.setOptimizationLevel(-1);
//        context.setLanguageVersion(Context.VERSION_1_3);
//
//        //scope = context.initStandardObjects();
//        final Global scope = new Global(context);
//        ScriptableObject.putProperty(scope, "console", Context.javaToJS(console, scope));
//        final InputStream inputStream = getClass().getResourceAsStream("/env" +
//                ".rhino.js");
//        final InputStreamReader inputStreamReader = new InputStreamReader
//                (inputStream);
//        context.evaluateReader(scope, inputStreamReader, "/env/rhino.js", 0,
//                null);
//
//        FileReader fileReader;
//        File baseFile = result.getJsBaseFile();
//        Assert.assertNotNull(baseFile);
//        fileReader = new FileReader(baseFile);
//        context.evaluateReader(scope, fileReader, baseFile.getPath(), 0, null);
//        fileReader.close();
//
//        File scriptFile = result.getJsOutputFile();
//        Assert.assertNotNull(scriptFile);
//        fileReader = new FileReader(scriptFile);
//        context.evaluateReader(scope, fileReader, scriptFile.getPath(), 0,
//                null);
//        fileReader.close();
//        context.evaluateString(scope, "window.onload()", "inline", 0, null);
//        context.evaluateString(scope, "Envjs.wait()", "inline", 0, null);
//        String hiResult = (String) context.evaluateString(scope,
//                "company.greeting.hello('Peter')", "inline", 0, null);
//        Assert.assertEquals("Goeie dag Peter", hiResult);
//
//        String byeRresult = (String) context.evaluateString(scope,
//                "company.greeting.goodbye('Peter')", "inline", 0, null);
//        Assert.assertEquals("Totsiens Peter", byeRresult);
//
//        String welcomeResult = (String) context.evaluateString(scope,
//                "company.greeting.goodbye('Peter')", "inline", 0, null);
//        Assert.assertEquals("Welkom Peter", welcomeResult);
//        Context.exit();
//    }


}
