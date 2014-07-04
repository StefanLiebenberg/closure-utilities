package slieb.closureutils.dependencies;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DependencyScannerTest {

    @Mock
    private ResourceProvider mockResourceProvider;

    @Mock
    private DependencyParser mockDependencyParser;


    @Mock
    private DependencyNode nodeA, nodeB, nodeC;

    @Mock
    private Resource resourceA, resourceB, resourceC;

    private DependencyScanner scanner;

    public void setupNode(DependencyNode mockNode, Resource mockResource,
                          String namespace, Set<String> requires) {
        when(mockDependencyParser.parse(mockResource)).thenReturn(mockNode);
        when(mockNode.getResource()).thenReturn(mockResource);
        when(mockNode.getProvides()).thenReturn(newHashSet(namespace));
        when(mockNode.getRequires()).thenReturn(requires);
    }

    @Before
    public void setup() throws Exception {
        when(mockResourceProvider.getResources()).thenReturn(Sets.newHashSet
                (resourceA, resourceB, resourceC));
        setupNode(nodeA, resourceA, "A", Sets.<String>newHashSet("B"));
        setupNode(nodeB, resourceB, "B", Sets.<String>newHashSet());
        setupNode(nodeC, resourceC, "C", Sets.<String>newHashSet());
        scanner = new DependencyScanner(mockResourceProvider,
                mockDependencyParser);
    }

    @Test
    public void getDependencyTree() throws Throwable {
        DependencyTree tree = scanner.getDependencyTree();
        assertEquals(nodeA, tree.getProviderOf("A"));
        assertEquals(nodeB, tree.getProviderOf("B"));
        assertEquals(nodeC, tree.getProviderOf("C"));
    }

    @Test
    public void getDependencies() {
        List<DependencyNode> dependencies = scanner.getDependencies("A");
        assertTrue(dependencies.contains(nodeA));
        assertTrue(dependencies.contains(nodeB));
        assertFalse(dependencies.contains(nodeC));

    }
}
