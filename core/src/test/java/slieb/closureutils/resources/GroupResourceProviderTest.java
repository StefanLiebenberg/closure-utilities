package slieb.closureutils.resources;

import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Iterators.contains;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static slieb.closureutils.resources.Resources.getResourceByUri;

@RunWith(MockitoJUnitRunner.class)
public class GroupResourceProviderTest {

    @Mock
    Resource mockResourceA, mockResourceB, mockResourceC, mockResourceD;

    @Mock
    ResourceProvider mockResourceProviderA, mockResourceProviderB;

    GroupResourceProvider groupResourceProvider;

    URI baseURI;

    public void setupResource(Resource mockResource, URI resourceUri) {
        when(mockResource.getUri()).thenReturn(resourceUri);
    }


    @Before
    public void setUp() throws Exception {
        when(mockResourceProviderA.getResources())
                .thenReturn(newArrayList(mockResourceA, mockResourceB));

        when(mockResourceProviderA.getUri())
                .thenReturn(new URI("resourcesA/"));


        when(mockResourceProviderB.getResources())
                .thenReturn(newArrayList(mockResourceC, mockResourceD));

        when(mockResourceProviderB.getUri())
                .thenReturn(new URI("resourcesB/"));

        setupResource(mockResourceA, new URI("resourcesA/resourceA.js"));
        setupResource(mockResourceB, new URI("resourcesA/resourceB.js"));
        setupResource(mockResourceC, new URI("resourcesB/resourceC.js"));
        setupResource(mockResourceD, new URI("resourcesB/resourceD.js"));

        baseURI = new URI("http://localhost/scripts/");
        groupResourceProvider = new GroupResourceProvider(baseURI,
                newArrayList(mockResourceProviderA, mockResourceProviderB));
    }

    @Test
    public void testGetResources() throws Exception {
        assertNotNull(getResourceByUri(groupResourceProvider,
                new URI("http://localhost/scripts/resourcesA/resourceA.js")));
        assertNotNull(getResourceByUri(groupResourceProvider,
                new URI("http://localhost/scripts/resourcesA/resourceB.js")));
        assertNotNull(getResourceByUri(groupResourceProvider,
                new URI("http://localhost/scripts/resourcesB/resourceC.js")));
        assertNotNull(getResourceByUri(groupResourceProvider,
                new URI("http://localhost/scripts/resourcesB/resourceD.js")));
    }
}