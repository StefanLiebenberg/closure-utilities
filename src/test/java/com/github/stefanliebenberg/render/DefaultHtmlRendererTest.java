package com.github.stefanliebenberg.render;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
    public void testRenderStylesheets() throws Exception {

    }

    @Test
    public void testRenderScripts() throws Exception {

    }

    @Test
    public void testRenderContent() throws Exception {

    }

    @Test
    public void testRenderTitle() throws Exception {

    }

    @Test
    public void testRenderHeadTag() throws Exception {

    }

    @Test
    public void testRenderBodyTag() throws Exception {

    }

    @Test
    public void testRenderHtmlTag() throws Exception {

    }

    @Test
    public void testRenderDoctype() throws Exception {

    }

    @Test
    public void testRender() throws Exception {

    }
}
