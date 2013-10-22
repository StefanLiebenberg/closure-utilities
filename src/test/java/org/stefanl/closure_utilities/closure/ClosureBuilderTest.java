package org.stefanl.closure_utilities.closure;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.stefanl.closure_utilities.internal.AbstractBuildTest;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.utilities.FsTool;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClosureBuilderTest extends
        AbstractBuildTest<ClosureBuilder, ClosureBuildOptions> {

    public ClosureBuilderTest() throws
            IllegalAccessException,
            InstantiationException {
        super(ClosureBuilder.class, ClosureBuildOptions.class);
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
            builder.build();
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

        builder.buildGss();

        Assert.assertNotNull(builder.getGssOutputFile());
        final String content = FsTool.read(builder.getGssOutputFile());
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
    }

    @Test
    public void testBuildSoyBuild() throws Exception {
        setupSoyBuildOptions(Flavour.BASIC);
        builder.buildSoy();

        final File soyOutputDir = builder.getSoyOutputDirectory();
        final Collection<File> compiledSources = FsTool.find(soyOutputDir,
                "soy.js");
        final Collection<File> sourceDirs =
                builderOptions.getSoySourceDirectories();
        if (sourceDirs != null) {
            final Collection<File> sources = FsTool.find(sourceDirs, "soy");
            Assert.assertEquals(sources.size(), compiledSources.size());
        }
    }

    @Test
    public void testBuildJavascript() throws Exception {
        setupJavascriptBuildOptions(Flavour.BASIC);
        builder.buildJs();
    }


    @Test
    public void testEmptyBuildHtml() throws Exception {
        builder.buildHtml();

        final File htmlOutputFile = builder.getHtmlOutputFile();
        Assert.assertTrue("Output file exists", htmlOutputFile.exists());

        final String actual = FsTool.read(htmlOutputFile);
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

        builder.buildHtml();
        final File htmlOutputFile = builder.getHtmlOutputFile();
        final String actual = FsTool.read(htmlOutputFile);

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

        builder.build();

        final File htmlOutput = builder.getHtmlOutputFile();
        final String htmlContent = FsTool.read(htmlOutput);
        Document htmlDocument = Jsoup.parse(htmlContent);

        Element headElement = htmlDocument.select("head").first();
        Element bodyElement = htmlDocument.select("body").first();

        Elements scripts = headElement.select("script");
        Assert.assertEquals(2, scripts.size());

        final File jsDirectory = getApplicationDirectory("src/javascript");

        Path expectedPath, actualPath;
        expectedPath =
            Paths.get(jsDirectory.getAbsolutePath(), "company/constants.js");

        actualPath =
                Paths.get(htmlOutput.getParentFile().getAbsolutePath(),
                        scripts.get(0).attr("src")).normalize();
        System.out.println("EXP: " + expectedPath.toString());
        System.out.println("ACT: " + actualPath.toString());
        Assert.assertEquals(expectedPath, actualPath);


        expectedPath =
                Paths.get(jsDirectory.getAbsolutePath(), "company/package.js");
        actualPath =
                Paths.get(htmlOutput.getParentFile().getAbsolutePath(),
                        scripts.get(1).attr("src")).normalize();
        System.out.println("EXP: " + expectedPath.toString());
        System.out.println("ACT: " + actualPath.toString());
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

        builder.build();

        final File htmlOutput = builder.getHtmlOutputFile();
        final String htmlContent = FsTool.read(htmlOutput);
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


        builder.build();

        final File htmlOutput = builder.getHtmlOutputFile();
        final String htmlContent = FsTool.read(htmlOutput);
        Document htmlDocument = Jsoup.parse(htmlContent);

        Element headElement = htmlDocument.select("head").first();
        Element bodyElement = htmlDocument.select("body").first();

        Elements scripts = headElement.select("script");
        Assert.assertEquals(1, scripts.size());

        Elements stylesheets = headElement.select("link");
        Assert.assertEquals(1, stylesheets.size());

        Assert.assertEquals("", bodyElement.html());
    }


}
