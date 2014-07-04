package slieb.closureutils.dependencies;


import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.resources.Resource;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static slieb.closureutils.dependencies.DependencyCalculator.resolveDependencies;

@RunWith(MockitoJUnitRunner.class)
public class DependencyCalculatorTest {


    @Mock
    private DependencyTree mockDependencyTree;

    @Mock
    private DependencyNode nodeA, nodeB, nodeC, nodeD;

    @Mock
    private Resource resourceA, resourceB, resourceC, resourceD;

    private void setupNodeResource(DependencyNode mockNode,
                                   Resource mockResource, String namespace) {
        when(mockNode.getResource()).thenReturn(mockResource);
        when(mockNode.getProvides()).thenReturn(newHashSet(namespace));
        when(mockNode.getRequires()).thenReturn(Sets.<String>newHashSet());
        when(mockDependencyTree.getProviderOf(namespace)).thenReturn(mockNode);
    }


    @Before
    public void setUp() throws Exception {
        setupNodeResource(nodeA, resourceA, "A");
        setupNodeResource(nodeB, resourceB, "B");
        setupNodeResource(nodeC, resourceC, "C");
        setupNodeResource(nodeD, resourceD, "D");

    }


    @Test
    public void testResolveFindsSingleItem() throws Exception {
        List<DependencyNode> dependencies = newArrayList();
        List<DependencyNode> parentNodes = newArrayList();
        resolveDependencies(mockDependencyTree, "A", dependencies, parentNodes);

        assertTrue(parentNodes.isEmpty());
        assertEquals(1, dependencies.size());
        assertEquals(nodeA, dependencies.get(0));
    }


    @Test
    public void testResolveFindsRequiredItems() throws Exception {

        when(nodeA.getRequires()).thenReturn(newHashSet("B", "C"));

        List<DependencyNode> dependencies = newArrayList();
        List<DependencyNode> parentNodes = newArrayList();
        resolveDependencies(mockDependencyTree, "A", dependencies, parentNodes);

        assertTrue(parentNodes.isEmpty());
        assertEquals(3, dependencies.size());
        assertTrue(dependencies.contains(nodeB));
        assertTrue(dependencies.contains(nodeC));
        assertTrue(dependencies.contains(nodeA));
        assertTrue(dependencies.indexOf(nodeB) < dependencies.indexOf(nodeA));
        assertTrue(dependencies.indexOf(nodeC) < dependencies.indexOf(nodeA));

    }

    @Test
    public void testResolveFindsNestedRequiredItems() throws Exception {

        when(nodeA.getRequires()).thenReturn(newHashSet("B"));
        when(nodeB.getRequires()).thenReturn(newHashSet("C", "D"));

        List<DependencyNode> dependencies = newArrayList();
        List<DependencyNode> parentNodes = newArrayList();
        resolveDependencies(mockDependencyTree, "A", dependencies,
                parentNodes);

        assertTrue(parentNodes.isEmpty());
        assertEquals(4, dependencies.size());
        assertTrue(dependencies.contains(nodeA));
        assertTrue(dependencies.contains(nodeB));
        assertTrue(dependencies.contains(nodeC));
        assertTrue(dependencies.contains(nodeD));

        assertTrue(dependencies.indexOf(nodeB) < dependencies.indexOf(nodeA));
        assertTrue(dependencies.indexOf(nodeC) < dependencies.indexOf(nodeB));
        assertTrue(dependencies.indexOf(nodeD) < dependencies.indexOf(nodeB));
    }

    @Test(expected = IllegalStateException.class)
    public void testDetectsCircularException() throws Exception {

        when(nodeA.getRequires()).thenReturn(newHashSet("B"));
        when(nodeB.getRequires()).thenReturn(newHashSet("C", "D"));
        when(nodeD.getRequires()).thenReturn(newHashSet("C", "A"));

        List<DependencyNode> dependencies = newArrayList();
        List<DependencyNode> parentNodes = newArrayList();
        resolveDependencies(mockDependencyTree, "A", dependencies, parentNodes);
    }

    @Test(expected = IllegalStateException.class)
    public void testNothingProvidesException() throws Exception {

        when(nodeA.getRequires()).thenReturn(newHashSet("B"));
        when(nodeB.getRequires()).thenReturn(newHashSet("C", "D"));
        when(nodeD.getRequires()).thenReturn(newHashSet("C", "E"));

        List<DependencyNode> dependencies = newArrayList();
        List<DependencyNode> parentNodes = newArrayList();
        resolveDependencies(mockDependencyTree, "A", dependencies, parentNodes);
    }
}
