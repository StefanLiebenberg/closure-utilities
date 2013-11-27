package liebenberg.closure_utilities.closure;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import liebenberg.closure_utilities.internal.AbstractBuildTest;
import liebenberg.closure_utilities.internal.BuildException;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ClosureBuilderTest extends
        AbstractBuildTest<ClosureBuilder, ClosureOptions, ClosureResult> {

    public ClosureBuilderTest() throws
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

    @Nonnull
    public Boolean testBuildForFailure() {
        try {
            builder.build(builderOptions);
            return false;
        } catch (BuildException buildException) {
            return true;
        }
    }

    @Test
    public void testBuildFailureWhenNoOptionsAreGiven() throws Exception {
        Assert.assertTrue("Expect build failure when no options are specified",
                testBuildForFailure());
    }

    private void setupGssBuildOptions(@Nonnull Flavour flavour) {
        builderOptions.setGssSourceDirectories(getGssSourceDirectories());
        builderOptions.setGssEntryPoints(getGssEntryPoints(flavour));
        builderOptions.setCssClassRenameMap(null);
    }

    private void setupSoyBuildOptions(@Nonnull Flavour flavour) {
        builderOptions.setSoySourceDirectories(getSoySourceDirectories());
    }

    private void setupJavascriptBuildOptions(@Nonnull Flavour flavour) {
        builderOptions.setJavascriptEntryPoints(getJavascriptEntryPoints
                (flavour));
        builderOptions.setJavascriptSourceDirectories
                (getJavascriptSourceDirectories());
    }

    @Test
    public void testBuildGssBuild() throws Exception {
        setupGssBuildOptions(Flavour.BASIC);
        final ClosureResult closureResult = builder.buildGssOnly(builderOptions);
        final File gssOutputFile = closureResult.getGeneratedStylesheet();
        Assert.assertNotNull(gssOutputFile);
        Assert.assertTrue(gssOutputFile.exists());
        Assert.assertTrue(gssOutputFile.isFile());
        final String content = FS.read(gssOutputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
    }

    @Test
    public void testBuildSoyBuild() throws Exception {
        setupSoyBuildOptions(Flavour.BASIC);
        ClosureResult closureResult = builder.buildSoyOnly(builderOptions);
        final File soyOutputDir = closureResult.getSoyOutputDirectory();
        Assert.assertNotNull(soyOutputDir);
        Assert.assertTrue(soyOutputDir.exists());
        Assert.assertTrue(soyOutputDir.isDirectory());
        final Collection<File> compiledSources =
                FS.find(soyOutputDir, "soy.js");
        final Collection<File> sourceDirs =
                builderOptions.getSoySourceDirectories();
        if (sourceDirs != null) {
            final Collection<File> sources = FS.find(sourceDirs, "soy");
            Assert.assertEquals(sources.size(), compiledSources.size());
        }
    }

    @Test
    public void testBuildJavascript() throws Exception {
        setupJavascriptBuildOptions(Flavour.BASIC);
        final ClosureResult closureResult = builder.buildJsOnly(builderOptions);
    }


    @Test
    public void testEmptyBuildHtml() throws Exception {
        final ClosureResult closureResult =
                builder.buildHtmlOnly(builderOptions);

        final File htmlOutputFile = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutputFile);
        Assert.assertTrue("Output file exists", htmlOutputFile.exists());
        Assert.assertTrue("Output file is a file", htmlOutputFile.isFile());

        final String actual = FS.read(htmlOutputFile);
        final String expected =
                "<!DOCTYPE html><html><head></head><body></body></html>";
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testBuildHtml() throws Exception {
        List<File> scripts = new ArrayList<File>();
        scripts.add(new File(outputDirectory, "one.js"));
        scripts.add(new File(outputDirectory, "two.js"));
        scripts.add(new File(outputDirectory, "three.js"));
        builderOptions.setExternalScripts(scripts);

        List<File> stylesheets = new ArrayList<File>();
        stylesheets.add(new File(outputDirectory, "one.css"));
        stylesheets.add(new File(outputDirectory, "two.css"));
        builderOptions.setExternalStylesheets(stylesheets);
        builderOptions.setHtmlContent("CONTENT!!");

        ClosureResult closureResult = builder.buildHtmlOnly(builderOptions);
        final File htmlOutputFile = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutputFile);
        Assert.assertTrue(htmlOutputFile.exists());
        Assert.assertTrue(htmlOutputFile.isFile());
        final String actual = FS.read(htmlOutputFile);

        final String expectedScripts =
                "<script src=\"one.js\" type=\"text/javascript\"></script>" +
                        "<script src=\"two.js\" " +
                        "type=\"text/javascript\"></script>" +
                        "<script src=\"three.js\" " +
                        "type=\"text/javascript\"></script>";
        final String expectedStylesheets =
                "<link rel=\"stylesheet\" href=\"one.css\"/>" +
                        "<link rel=\"stylesheet\" href=\"two.css\"/>";
        final String expectedHead =
                "<head>" + expectedScripts + expectedStylesheets + "</head>";
        final String expectedBody = "<body>CONTENT!!</body>";
        final String expected =
                "<!DOCTYPE html><html>" + expectedHead + expectedBody +
                        "</html>";
        Assert.assertEquals(expected, actual);
    }


    public String getAbsolutePath(String path) {
        return new File(path).getAbsolutePath();
    }

    @Test
    public void testDevelopmentBuild() throws Exception {
        setupGssBuildOptions(Flavour.BASIC);
        setupSoyBuildOptions(Flavour.BASIC);
        setupJavascriptBuildOptions(Flavour.BASIC);

        ClosureResult closureResult = builder.build(builderOptions);

        final File htmlOutput = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutput);
        Assert.assertTrue(htmlOutput.exists());
        Assert.assertTrue(htmlOutput.isFile());
        final String htmlContent = FS.read(htmlOutput);
        Document htmlDocument = Jsoup.parse(htmlContent);

        Element headElement = htmlDocument.select("head").first();
        Assert.assertNotNull(headElement);
        Element bodyElement = htmlDocument.select("body").first();
        Assert.assertNotNull(bodyElement);

        Elements scripts = headElement.select("script");
        Assert.assertEquals(3, scripts.size()); // 3 because extra stuff

        final File jsDirectory = getApplicationDirectory("src/javascript");

        Path expectedPath, actualPath;
        expectedPath =
                Paths.get(jsDirectory.getAbsolutePath(),
                        "company/constants.js");

        actualPath =
                Paths.get(htmlOutput.getParentFile().getAbsolutePath(),
                        scripts.get(1).attr("src")).normalize();
        Assert.assertEquals(expectedPath, actualPath);


        expectedPath =
                Paths.get(jsDirectory.getAbsolutePath(), "company/package.js");
        actualPath =
                Paths.get(htmlOutput.getParentFile().getAbsolutePath(),
                        scripts.get(2).attr("src")).normalize();
        Assert.assertEquals(expectedPath, actualPath);

        Elements stylesheets = headElement.select("link");
        Assert.assertEquals(1, stylesheets.size());

        Assert.assertEquals("style.css",
                stylesheets.get(0).attr("href"));

        Assert.assertEquals("", bodyElement.html());
    }

    @Test
    public void testProductionBuild() throws Exception {


        setupGssBuildOptions(Flavour.BASIC);
        setupSoyBuildOptions(Flavour.BASIC);
        setupJavascriptBuildOptions(Flavour.BASIC);

        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(false);

        ClosureResult closureResult = builder.build(builderOptions);

        final File htmlOutput = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutput);
        Assert.assertTrue(htmlOutput.exists());
        Assert.assertTrue(htmlOutput.isFile());
        final String htmlContent = FS.read(htmlOutput);
        Document htmlDocument = Jsoup.parse(htmlContent);

        Element headElement = htmlDocument.select("head").first();
        Element bodyElement = htmlDocument.select("body").first();

        Elements scripts = headElement.select("script");
        Assert.assertEquals(1, scripts.size());

        Elements stylesheets = headElement.select("link");
        Assert.assertEquals(1, stylesheets.size());

        Assert.assertEquals("", bodyElement.html());
    }

    @Test
    public void testDebugBuild() throws Exception {

        setupGssBuildOptions(Flavour.BASIC);
        setupSoyBuildOptions(Flavour.BASIC);
        setupJavascriptBuildOptions(Flavour.BASIC);
        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(true);

        final ClosureResult closureResult = builder.build(builderOptions);
        final File htmlOutput = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutput);
        Assert.assertTrue(htmlOutput.exists());
        Assert.assertTrue(htmlOutput.isFile());
        final String htmlContent = FS.read(htmlOutput);
        final Document htmlDocument = Jsoup.parse(htmlContent);
        final Element headElement = htmlDocument.select("head").first();
        final Element bodyElement = htmlDocument.select("body").first();
        final Elements scripts = headElement.select("script");
        Assert.assertEquals(1, scripts.size());
        final Elements stylesheets = headElement.select("link");
        Assert.assertEquals(1, stylesheets.size());
        Assert.assertEquals("", bodyElement.html());
    }


    @Test
    public void testCustomTemplateBuild() throws Exception {
        setupGssBuildOptions(Flavour.BASIC);
        setupSoyBuildOptions(Flavour.BASIC);
        setupJavascriptBuildOptions(Flavour.BASIC);
        builderOptions.setShouldCompile(true);
        builderOptions.setShouldDebug(true);
        builderOptions.setHtmlTemplate("company.shell.Template");
        final ClosureResult closureResult = builder.build(builderOptions);
        final File htmlOutput = closureResult.getHtmlOutputFile();
        Assert.assertNotNull(htmlOutput);
        Assert.assertTrue(htmlOutput.exists());
        Assert.assertTrue(htmlOutput.isFile());
        final String htmlContent = FS.read(htmlOutput);
        final Document htmlDocument = Jsoup.parse(htmlContent);
        final Element headElement = htmlDocument.select("head").first();
        final Element bodyElement = htmlDocument.select("body").first();
        final Elements scripts = headElement.select("script");
        Assert.assertEquals(1, scripts.size());
        final Elements stylesheets = headElement.select("link");
        Assert.assertEquals(1, stylesheets.size());
        Assert.assertEquals("", bodyElement.html());
    }

    @Test
    public void testConfigurationToGlobals () throws Exception {

        Map<String, Object> globals = new HashMap<>();
        List<Configuration> configurations = new ArrayList<>();

        PropertiesConfiguration propertiesConfiguration =
                new PropertiesConfiguration();
        InputStream propertiesStream =
                getClass().getResourceAsStream("/test.properties");
        propertiesConfiguration.load(propertiesStream);
        propertiesStream.close();

        configurations.add(propertiesConfiguration);

        PropertiesConfiguration propertiesConfiguration2 =
                new PropertiesConfiguration();
        InputStream propertiesStream2 =
                getClass().getResourceAsStream("/test2.properties");
        propertiesConfiguration2.load(propertiesStream2);
        propertiesStream2.close();

        configurations.add(propertiesConfiguration2);

        ClosureBuilder.getJsGlobalsFromConfigurations(globals, configurations);

        Assert.assertEquals(true, globals.get("booleanProperty"));
        Assert.assertEquals(false, globals.get("falseBooleanProperty"));
        Assert.assertEquals("yolo!", globals.get("stringProperty"));
        Assert.assertEquals("override value true", globals.get("overrideProperty"));

    }

}
