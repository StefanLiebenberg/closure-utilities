package slieb.closureutils.rendering;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.SimpleResource;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class DefaultHtmlRendererTest {

    private DefaultHtmlRenderer renderer;

    private StringBuffer sb = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        renderer = new DefaultHtmlRenderer();
    }

    @After
    public void tearDown() throws Exception {
        sb.setLength(0);
    }

    @Test
    public void testRenderAttr() throws Exception {
        final String expected =" name=\"value\"";
        renderer.renderAttribute(sb, "name", "value");
        assertEquals(expected, sb.toString());
    }


    @Test
    public void testRenderStylesheet() throws Exception {
        String expected = "<link rel=\"stylesheet\" href=\"css/myfile.css\"/>";
        Resource cssResource = new SimpleResource(new URI("css/myfile.css"),
                null, null);
        renderer.renderStylesheet(sb, cssResource);
        assertEquals(expected, sb.toString());
    }


    @Test
    public void testRenderScriptLink() throws Exception {
        String expected = "<script type=\"text/javascript\" src=\"scripts/myscript.js\"></script>";
        Resource jsResource = new SimpleResource(new URI("scripts/myscript" +
                ".js"), null, null);
        renderer.renderScript(sb, jsResource);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testRenderStylesheets() throws Exception {
//        Resource resourceA = new SimpleResource(new URI("scripts/scriptA.js")
//                , null, null);
//        Resource resourceB = new SimpleResource(new URI("scripts/scriptB.js")
//                , null, null);
//        List<Resource> resources = Lists.newArrayList(resourceA, resourceB);
//        HtmlRenderOptions options = Mockito.mock(HtmlRenderOptions.class);
//        Mockito.when(options.getScriptResources()).thenReturn(resources);
//        String result = renderer.renderScripts(options);
//        String expected = renderer.renderScript(resourceA) + renderer
//                .renderScript(resourceB);
//        assertEquals(expected, result);
    }


    @Test
    public void testRenderTitle() throws Exception {
//        HtmlRenderOptions options = Mockito.mock(HtmlRenderOptions.class);
//        Mockito.when(options.getTitle()).thenReturn(null);
//        assertTrue(renderer.renderTitle(options).isEmpty());
//
//        Mockito.when(options.getTitle()).thenReturn("TheTitle");
//        assertEquals("<title>TheTitle</title>", renderer.renderTitle(options));
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