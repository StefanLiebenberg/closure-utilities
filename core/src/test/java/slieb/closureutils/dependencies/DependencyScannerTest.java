package slieb.closureutils.dependencies;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DependencyScannerTest {

    @Mock
    private ResourceProvider mockResourceProvider;

    @Mock
    private DependencyParser mockDependencyParser;

    @Mock
    private DependencyCalculator mockDependencyCalculator;

    @Mock
    private DependencyNode nodeA, nodeB, nodeC;

    @Mock
    private Resource resourceA, resourceB, resourceC;

    private DependencyScanner scanner;

    public void setupNode(DependencyNode mockNode, Resource mockResource, String namespace) {
        when(mockDependencyParser.parse(mockResource)).thenReturn(mockNode);
        when(mockNode.getResource()).thenReturn(mockResource);
        when(mockNode.getProvides()).thenReturn(copyOf(newHashSet(namespace)));
//        when(mockNode.getRequires()).thenReturn(copyOf(Sets.<String>newHashSet()));
    }

    @Before
    public void setup() throws Exception {
        when(mockResourceProvider.getResources()).thenReturn(Sets.newHashSet(resourceA, resourceB, resourceC));
        setupNode(nodeA, resourceA, "A");
        setupNode(nodeB, resourceB, "B");
        setupNode(nodeC, resourceC, "C");
        scanner = new DependencyScanner(mockResourceProvider, mockDependencyParser, mockDependencyCalculator);
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
        when(mockDependencyCalculator.resolve(any(DependencyTree.class), anyString(), any(List.class), any(Collection.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Object list = args[2];
                if (list instanceof List) {
                    List<Object> castList = (List<Object>) list;
                    castList.add(nodeA);
                    castList.add(nodeB);
                }
                return null;
            }
        });

        List<DependencyNode> dependencies = scanner.getDependencies("A");
        assertTrue(dependencies.contains(nodeA));
        assertTrue(dependencies.contains(nodeB));
        assertFalse(dependencies.contains(nodeC));

    }
}
