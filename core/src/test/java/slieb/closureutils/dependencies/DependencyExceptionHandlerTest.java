package slieb.closureutils.dependencies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.resources.Resource;

import java.net.URI;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static slieb.closureutils.dependencies.DependencyExceptionHandler.circularDependency;
import static slieb.closureutils.dependencies.DependencyExceptionHandler.nothingProvides;

@RunWith(MockitoJUnitRunner.class)
public class DependencyExceptionHandlerTest {

    @Mock
    private DependencyNode mockNodeA, mockNodeB, mockNodeC;

    @Mock
    private Resource mockResourceA, mockResourceB, mockResourceC;

    @Before
    public void setUp() throws Exception {
        when(mockNodeA.getResource()).thenReturn(mockResourceA);
        when(mockNodeB.getResource()).thenReturn(mockResourceB);
        when(mockNodeC.getResource()).thenReturn(mockResourceC);
        when(mockResourceA.getUri()).thenReturn(new URI("mock://resourceA"));
        when(mockResourceB.getUri()).thenReturn(new URI("mock://resourceB"));
        when(mockResourceC.getUri()).thenReturn(new URI("mock://resourceC"));
    }

    @Test
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void testNothingProvides() throws Exception {
        DependencyException exception = nothingProvides("namespace", newArrayList(mockNodeA, mockNodeB, mockNodeC));
        assertEquals("Nothing provides 'namespace'.\nRequired in mock://resourceA\n", exception.getMessage());
    }


    @Test
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void testCircularDependency() throws Exception {
        when(mockNodeA.getProvides()).thenReturn(newHashSet("mock.A"));
        when(mockNodeA.getRequires()).thenReturn(newHashSet("mock.B"));

        when(mockNodeB.getProvides()).thenReturn(newHashSet("mock.B"));
        when(mockNodeB.getRequires()).thenReturn(newHashSet("mock.C"));

        when(mockNodeC.getProvides()).thenReturn(newHashSet("mock.C"));
        when(mockNodeC.getRequires()).thenReturn(newHashSet("mock.A"));

        DependencyException exception = circularDependency("mock.C", mockNodeC, newArrayList(mockNodeA, mockNodeB));
        assertTrue(exception.getMessage().startsWith("Circular dependency found in when trying to locate 'mock.C'"));
    }
}