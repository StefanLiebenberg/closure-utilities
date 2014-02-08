package liebenberg.closure_utilities.build;

import com.google.javascript.jscomp.MessageBundle;
import com.google.javascript.jscomp.XtbMessageBundle;
import liebenberg.closure_utilities.rhino.EnvJsRunner;
import liebenberg.closure_utilities.translation.XliffMessageBundle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
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

        File scriptFile = result.getJsOutputFile();
        Assert.assertNotNull(scriptFile);

        EnvJsRunner runner = new EnvJsRunner();
        runner.initialize();
        runner.evaluateFile(scriptFile);
        runner.doLoad();
        runner.doWait();

        String greetingResult, greetingExpected;

        greetingResult = runner.getString("company.greeting.hello('Peter')");
        greetingExpected = "Hello Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.goodbye('Peter')");
        greetingExpected = "Good Bye Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.welcome('Peter')");
        greetingExpected = "Welcome Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        runner.close();
    }

    @Test
    public void testBundledTranslations() throws Exception {

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("company.greeting");
        builderOptions.setJavascriptEntryPoints(entryPoints);
        builderOptions.setJavascriptSourceDirectories
                (getJavascriptSourceDirectories());
        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(false);

        final InputStream messageStream = getClass().
                getResourceAsStream("/app/src/messages/company-af.xtb");
        final MessageBundle messageBundle =
                new XtbMessageBundle(messageStream, "company");
        builderOptions.setMessageBundle(messageBundle);

        final ClosureResult result = builder.buildJsOnly(builderOptions);

        final File scriptFile = result.getJsOutputFile();
        Assert.assertNotNull(scriptFile);

        final EnvJsRunner runner = new EnvJsRunner();
        runner.initialize();
        runner.evaluateFile(scriptFile);
        runner.doLoad();
        runner.doWait();

        String greetingResult, greetingExpected;

        greetingResult = runner.getString("company.greeting.hello('Peter')");
        greetingExpected = "Goeie Dag Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.goodbye('Peter')");
        greetingExpected = "Totsiens Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.welcome('Peter')");
        greetingExpected = "Welkom Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        runner.close();
    }

    @Test
    public void testBundledXLFTranslations() throws Exception {

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("company.greeting");
        builderOptions.setJavascriptEntryPoints(entryPoints);
        builderOptions.setJavascriptSourceDirectories
                (getJavascriptSourceDirectories());
        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(false);

        final InputStream messageStream = getClass().
                getResourceAsStream("/app/src/messages/company-af.xlif");
        final MessageBundle messageBundle =
                new XliffMessageBundle(messageStream, "company");
        builderOptions.setMessageBundle(messageBundle);

        final ClosureResult result = builder.buildJsOnly(builderOptions);

        final File scriptFile = result.getJsOutputFile();
        Assert.assertNotNull(scriptFile);

        final EnvJsRunner runner = new EnvJsRunner();
        runner.initialize();

        runner.evaluateFile(scriptFile);
        runner.doLoad();
        runner.doWait();

        String greetingResult, greetingExpected;

        greetingResult = runner.getString("company.greeting.hello('Peter')");
        greetingExpected = "Goeie Dag Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.goodbye('Peter')");
        greetingExpected = "Totsiens Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        greetingResult = runner.getString("company.greeting.welcome('Peter')");
        greetingExpected = "Welkom Peter";
        Assert.assertEquals(greetingExpected, greetingResult);

        runner.close();
    }


}
