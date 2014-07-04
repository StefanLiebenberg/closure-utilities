package slieb.closureutils.html;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.closure.build.internal.AbstractApplicationTest;
import slieb.closure.build.internal.BuildException;
import slieb.closure.render.HtmlRenderer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

public class HtmlBuilderTest extends AbstractApplicationTest {

    /**
     * Adding this as static final member to prove its re-usability.
     */
    private static final HtmlBuilder builder = new HtmlBuilder();

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test(expected = BuildException.class)
    public void testEmptyBuild() throws Exception {
        builder.build(new HtmlOptions());
    }

    @Test
    public void testContentBuild() throws Exception {

        File contentOutputFile = new File(outputDirectory, "index.html");

        HtmlOptions htmlOptions = new HtmlOptions();
        String content = "content";
        htmlOptions.setContent(content);

        htmlOptions.setOutputFile(contentOutputFile);
        HtmlResult htmlResult = builder.build(htmlOptions);
        Assert.assertNotNull(htmlResult);

        Document document = Jsoup.parse(contentOutputFile,
                Charset.defaultCharset().name());
        Assert.assertEquals(content, document.body().text());
    }

    @Test
    public void testTemplateRender() throws Exception {
        File contentOutputFile = new File(outputDirectory, "index.html");

        Collection<File> templateDirectories = new ArrayList<>();
        templateDirectories.add(getApplicationDirectory("src/soy"));
        String templateName = "company.shell.Template";
        HtmlRenderer htmlRenderer =
                HtmlBuilder.getHtmlTemplateRenderer(
                        templateDirectories,
                        templateName);

        HtmlOptions htmlOptions = new HtmlOptions();
        String content = "content";
        htmlOptions.setContent(content);
        htmlOptions.setOutputFile(contentOutputFile);
        htmlOptions.setHtmlRenderer(htmlRenderer);

        builder.build(htmlOptions);

        Document document = Jsoup.parse(contentOutputFile,
                Charset.defaultCharset().name());
        Assert.assertEquals(content, document.body().text());
    }
}
