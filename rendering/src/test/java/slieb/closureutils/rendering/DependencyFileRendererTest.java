package slieb.closureutils.rendering;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.javascript.JavascriptDependencyParser;
import slieb.closureutils.javascript.JavascriptDependencyTree;
import slieb.closureutils.resources.Resource;

import java.net.URI;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DependencyFileRendererTest {

    private final StringBuffer sb = new StringBuffer();

    @Mock
    private JavascriptDependencyTree mockDependencyTree;

    @Mock
    private DependencyNode mockDependencyNodeA, mockDependencyNodeB,mockDependencyNodeC, mockDependencyNodeBase;

    @Mock
    private Resource  mockResourceBase, mockResourceA, mockResourceB, mockResourceC;

    private DependencyFileRenderer dependencyFileRenderer;

    @Before
    public void setUp() throws Exception {
        when(mockDependencyNodeBase.getResource()).thenReturn(mockResourceBase);
        when(mockDependencyNodeA.getResource()).thenReturn(mockResourceA);
        when(mockDependencyNodeB.getResource()).thenReturn(mockResourceB);
        when(mockDependencyNodeC.getResource()).thenReturn(mockResourceC);

        when(mockResourceBase.getUri())
                .thenReturn(new URI("http://localhost:4343/lib/goog/base.js"));
        when(mockResourceA.getUri())
                .thenReturn(new URI("http://localhost:4343/lib/custom/fileA.js"));
        when(mockResourceB.getUri())
                .thenReturn(new URI("http://localhost:4343/lib/custom/fileB.js"));
        when(mockResourceC.getUri())
                .thenReturn(new URI("http://localhost:4343/lib/custom/fileC.js"));

        when(mockDependencyNodeBase.getFlag(JavascriptDependencyParser
                .IS_BASE)).thenReturn(Boolean.TRUE);
        dependencyFileRenderer = new DependencyFileRenderer();
    }

    @After
    public void tearDown() throws Exception {
        sb.setLength(0);
    }

    @Test
    public void testRenderDependencyPath() throws Exception {
        dependencyFileRenderer.renderDependencyPath(sb, mockDependencyNodeA, mockDependencyNodeBase);
        assertEquals("../custom/fileA.js", sb.toString());
    }

    @Test
    public void testRenderNamespaceArray() throws Exception {
        dependencyFileRenderer.renderNamespaceArray(sb, newArrayList("a", "b"));
        assertEquals("['a', 'b']", sb.toString());
    }

    @Test
    public void testRenderDependency() throws Exception {

        when(mockDependencyNodeA.getProvides()).thenReturn(newArrayList("example.A"));
        when(mockDependencyNodeA.getRequires()).thenReturn(newArrayList("example.B", "example.C"));

        dependencyFileRenderer.renderDependency(sb, mockDependencyNodeA, mockDependencyNodeBase);
        String expected = "goog.addDependency('../custom/fileA.js', ['example.A'], ['example.B', 'example.C']);";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testRender() throws Exception {

        when(mockDependencyNodeA.getProvides()).thenReturn(newArrayList("example.A"));
        when(mockDependencyNodeA.getRequires()).thenReturn(newArrayList("example.B", "example.C"));

        when(mockDependencyNodeB.getProvides()).thenReturn(newArrayList("example.B"));
        when(mockDependencyNodeB.getRequires()).thenReturn(newArrayList("example.C"));

        when(mockDependencyNodeC.getProvides()).thenReturn(newArrayList("example.C"));
//        when(mockDependencyNodeC.getRequires()).thenReturn(newArrayList());


        when(mockDependencyTree.getBaseNode()).thenReturn(mockDependencyNodeBase);
        when(mockDependencyTree.getDependencyNodes()).thenReturn(newArrayList(mockDependencyNodeA, mockDependencyNodeB, mockDependencyNodeC));

        dependencyFileRenderer.render(sb, mockDependencyTree);
        String expectedA = "goog.addDependency('../custom/fileA.js', ['example.A'], ['example.B', 'example.C']);";
        String expectedB = "goog.addDependency('../custom/fileB.js', ['example.B'], ['example.C']);";
        String expectedC = "goog.addDependency('../custom/fileC.js', ['example.C'], []);";
        String expected = String.format("%s\n%s\n%s\n", expectedA, expectedB, expectedC);
        assertEquals(expected, sb.toString());
    }
}