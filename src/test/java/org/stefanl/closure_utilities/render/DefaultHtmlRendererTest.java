package org.stefanl.closure_utilities.render;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultHtmlRendererTest {


    DefaultHtmlRenderer htmlRenderer;

    @Before
    public void setUp() throws Exception {
        htmlRenderer = new DefaultHtmlRenderer();
    }

    @After
    public void tearDown() throws Exception {
        htmlRenderer = null;
    }

    @Test
    public void testGetFilePath() throws Exception {}

    @Test
    public void testRender_attr() throws Exception {
        String result = htmlRenderer.render_attr("key", "value");
        Assert.assertEquals("key=\"value\"", result);
    }

    @Test
    public void testRender_tag_base() throws Exception {
        String[] attributes = new String[]{
                "attributeA=\"valueA\"",
                "attributeB=\"valueB\""
        };
        String result = htmlRenderer.render_tag_base("tagName", attributes,
                false, "content");
        Assert.assertEquals("<tagName attributeA=\"valueA\" " +
                "attributeB=\"valueB\">content</tagName>", result);
    }

    @Test
    public void testRender_tag() throws Exception {
        String result = htmlRenderer.render_tag("TAG");
        Assert.assertEquals("<TAG></TAG>", result);

        String contentResult = htmlRenderer.render_tag("T", "content");
        Assert.assertEquals("<T>content</T>", contentResult);
    }

    @Test
    public void testRenderStylesheet() throws Exception {
        String inlineResult = htmlRenderer.renderStylesheet("" +
                ".a{background:red}");
        Assert.assertEquals("<style type=\"text/stylesheet\">" +
                ".a{background:red}</style>", inlineResult);

        String linkResult = htmlRenderer.renderStylesheet(new File
                ("/some/path.css"));
        Assert.assertEquals("<link rel=\"stylesheet\" href=\"/some/path" +
                ".css\"/>", linkResult);

        htmlRenderer.setOutputPath(new File("/some/other/path/index.html"));
        String relativelinkResult = htmlRenderer.renderStylesheet(new File
                ("/some/stylish/path.css"));
        Assert.assertEquals("<link rel=\"stylesheet\" href=\"../." +
                "./stylish/path.css\"/>", relativelinkResult);
    }

//    @Test
//    public void testRenderStylesheet() throws Exception {
//
//    }

    @Test
    public void testRenderScriptInline() throws Exception {
        final String content = "alert('foo')";
        final String result = htmlRenderer.renderScript(content);
        final String expected = "<script type=\"text/javascript\">" + content
                + "</script>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRenderScriptFile() throws Exception {
        final String path = "/some/path.js";
        final File script = new File(path);
        final String result = htmlRenderer.renderScript(script);
        final String expected = "<script src=\"" + path + "\" type=\"text" +
                "/javascript\"></script>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRenderScriptRelativeFile() throws Exception {
        final File script = new File("/some/javascript/path.js");
        final File output = new File("/some/html/index.html");
        htmlRenderer.setOutputPath(output);
        final String result = htmlRenderer.renderScript(script);
        final String expectedPath = "../javascript/path.js";
        final String expected = "<script src=\"" + expectedPath +
                "\" type=\"text/javascript\"></script>";
        Assert.assertEquals(expected, result);
    }
//
//    @Test
//    public void testRenderScript() throws Exception {
//
//    }

    @Test
    public void testRenderEmptyStylesheets() throws Exception {
        final String actual = htmlRenderer.renderStylesheets();
        final String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderStylesheets() throws Exception {
        final List<File> stylesheets = new ArrayList<File>();
        stylesheets.add(new File("/a/stylesheet.css"));
        htmlRenderer.setStylesheets(stylesheets);
        final String actual = htmlRenderer.renderStylesheets();
        final String expected = "<link rel=\"stylesheet\" " +
                "href=\"/a/stylesheet.css\"/>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderEmptyScripts() throws Exception {
        final String actual = htmlRenderer.renderScripts();
        final String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderScripts() throws Exception {
        final List<File> scripts = new ArrayList<File>();
        scripts.add(new File("/a/script.js"));
        htmlRenderer.setScripts(scripts);
        final String actual = htmlRenderer.renderScripts();
        final String expected = "<script src=\"/a/script.js\" " +
                "type=\"text/javascript\"></script>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderEmptyContent() throws Exception {
        final String actual = htmlRenderer.renderContent();
        final String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderContent() throws Exception {
        final String content = "CONTENT";
        htmlRenderer.setContent(content);
        final String actual = htmlRenderer.renderContent();
        final String expected = content;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderEmptyTitle() throws Exception {
        final String actual = htmlRenderer.renderTitle();
        final String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderTitle() throws Exception {
        final String title = "page title";
        htmlRenderer.setTitle(title);
        final String actual = htmlRenderer.renderTitle();
        final String expected = "<title>" + title + "</title>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderHeadTag() throws Exception {
        final String actual = htmlRenderer.renderHeadTag();
        final String expected = "<head></head>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderBodyTag() throws Exception {
        final String actual = htmlRenderer.renderBodyTag();
        final String expected = "<body></body>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderHtmlTag() throws Exception {
        final String actual = htmlRenderer.renderHtmlTag();
        final String expected = "<html><head></head><body></body></html>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderDoctype() throws Exception {
        final String actual = htmlRenderer.renderDoctype();
        final String expected = "<!DOCTYPE html>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRender() throws Exception {
        final String actual = htmlRenderer.render();
        final String expected = "<!DOCTYPE html><html><head></head><body></body></html>";
        Assert.assertEquals(expected, actual);
    }
}
