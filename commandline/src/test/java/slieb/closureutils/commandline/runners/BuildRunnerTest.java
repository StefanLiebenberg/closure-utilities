package slieb.closureutils.commandline.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.gss.DefaultGssBuilder;
import slieb.closureutils.gss.GssOptions;
import slieb.closureutils.html.HtmlBuilder;
import slieb.closureutils.html.HtmlOptions;
import slieb.closureutils.javascript.JsBuilder;
import slieb.closureutils.javascript.JsOptions;
import slieb.closureutils.templates.SoyBuilder;
import slieb.closureutils.templates.SoyOptions;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuildRunnerTest {

    private BuildRunner buildRunner;

    @Mock
    private SoyBuilder mockSoyBuilder;
    @Mock
    private DefaultGssBuilder mockGssBuilder;
    @Mock
    private HtmlBuilder mockHtmlBuilder;
    @Mock
    private JsBuilder mockJsBuilder;

    @Before
    public void setUp() throws Exception {
        buildRunner = new BuildRunner(mockSoyBuilder, mockGssBuilder, mockHtmlBuilder, mockJsBuilder);
    }

    @Test
    public void testRun() throws Exception {
        buildRunner.run();
        verify(mockGssBuilder, times(1)).build(any(GssOptions.class));
        verify(mockSoyBuilder, times(1)).build(any(SoyOptions.class));
        verify(mockJsBuilder, times(1)).build(any(JsOptions.class));
        verify(mockHtmlBuilder, times(1)).build(any(HtmlOptions.class));
    }

    @Test
    public void testRunGss() throws Exception {
        buildRunner.run("-M", "css");
        verify(mockGssBuilder, times(1)).build(any(GssOptions.class));
        verify(mockSoyBuilder, times(0)).build(any(SoyOptions.class));
        verify(mockJsBuilder, times(0)).build(any(JsOptions.class));
        verify(mockHtmlBuilder, times(0)).build(any(HtmlOptions.class));
    }

    @Test
    public void testRunSoy() throws Exception {
        buildRunner.run("-M", "soy");
        verify(mockGssBuilder, times(0)).build(any(GssOptions.class));
        verify(mockSoyBuilder, times(1)).build(any(SoyOptions.class));
        verify(mockJsBuilder, times(0)).build(any(JsOptions.class));
        verify(mockHtmlBuilder, times(0)).build(any(HtmlOptions.class));
    }

    @Test
    public void testRunJs() throws Exception {
        buildRunner.run("-M", "js");
        verify(mockGssBuilder, times(0)).build(any(GssOptions.class));
        verify(mockSoyBuilder, times(0)).build(any(SoyOptions.class));
        verify(mockJsBuilder, times(1)).build(any(JsOptions.class));
        verify(mockHtmlBuilder, times(0)).build(any(HtmlOptions.class));
    }

    @Test
    public void testRunHtml() throws Exception {
        buildRunner.run("-M", "html");
        verify(mockGssBuilder, times(0)).build(any(GssOptions.class));
        verify(mockSoyBuilder, times(0)).build(any(SoyOptions.class));
        verify(mockJsBuilder, times(0)).build(any(JsOptions.class));
        verify(mockHtmlBuilder, times(1)).build(any(HtmlOptions.class));
    }
}