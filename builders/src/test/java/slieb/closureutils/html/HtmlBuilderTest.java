package slieb.closureutils.html;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.rendering.HtmlRendererFactory;
import slieb.closureutils.resources.MemoryResource;
import slieb.closureutils.resources.Resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HtmlBuilderTest {

    @Mock
    private HtmlRendererFactory rendererFactory;

    @Mock
    private HtmlOptions mockOptions;


    private HtmlBuilder builder;

    @Before
    public void setUp() throws Exception {

        builder = new HtmlBuilder(rendererFactory);
    }

    @Test
    public void testBuild() throws Exception {
        Resource outputResource = new MemoryResource();
        when(mockOptions.getOutputResource()).thenReturn(outputResource);
        HtmlResult result = builder.build(mockOptions);
    }
}