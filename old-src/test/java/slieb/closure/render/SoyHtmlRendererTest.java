package slieb.closure.render;

import org.junit.Before;
import org.junit.Test;
import slieb.closure.testing.TestingHelper;

import java.io.File;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;


public class SoyHtmlRendererTest {

    private TestingHelper testingHelper = new TestingHelper();

    private static final String PATH_TO_SOY = "app/src/soy";

    private static final String TEMPLATE_NAME = "company.shell.Template";

    private SoyHtmlRenderer soyHtmlRenderer;

    @Before
    public void setUp() {
        soyHtmlRenderer = new SoyHtmlRenderer(
                newHashSet(testingHelper.getTestResourcesFile(PATH_TO_SOY)),
                TEMPLATE_NAME);
    }

    private static final String BASIC_HTML_FORMAT =
            "<html><head><title>%s</title>%s</head><body>%s</body></html>";

    private static final String SCRIPT_FORMAT =
            "<script src=\"%s\" type=\"text/javascript\"></script>";

    private static final String STYLESHEET_FORMAT =
            "<link rel=\"stylesheet\" href=\"%s\"/>";

    @Test
    public void testRenderWithNoData() throws Exception {
        String expected = format(BASIC_HTML_FORMAT, "", "", "");
        assertEquals(expected, soyHtmlRenderer.render());
    }

    @Test
    public void testRenderWithTitle() throws Exception {
        String expected = format(BASIC_HTML_FORMAT, "Some Title", "",
                "");
        String actual = soyHtmlRenderer
                .setTitle("Some Title")
                .render();
        assertEquals(expected, actual);
    }

    @Test
    public void testRenderWithContent() throws Exception {
        String expected = format(BASIC_HTML_FORMAT, "", "", "Content");
        String actual = soyHtmlRenderer
                .setContent("Content")
                .render();
        assertEquals(expected, actual);
    }

    @Test
    public void testRenderWithScripts() throws Exception {
        String scriptContent = new StringBuilder()
                .append(format(SCRIPT_FORMAT, "app/src/javascript/fileA.js"))
                .append(format(SCRIPT_FORMAT, "app/src/javascript/fileB.js"))
                .toString();
        String expected = format(BASIC_HTML_FORMAT, "", scriptContent, "");
        String actual = soyHtmlRenderer
                .setScripts(newArrayList(
                        new File("app/src/javascript/fileA.js"),
                        new File("app/src/javascript/fileB.js")))
                .render();
        assertEquals(expected, actual);
    }

    @Test
    public void testRenderWithStylesheets() throws Exception {
        String scriptContent = new StringBuilder()
                .append(format(STYLESHEET_FORMAT, "app/src/gss/fileA.gss"))
                .append(format(STYLESHEET_FORMAT, "app/src/gss/fileB.gss"))
                .toString();
        String expected = format(BASIC_HTML_FORMAT, "", scriptContent, "");
        String actual = soyHtmlRenderer
                .setStylesheets(newArrayList(
                        new File("app/src/gss/fileA.gss"),
                        new File("app/src/gss/fileB.gss")))
                .render();
        assertEquals(expected, actual);
    }
}
