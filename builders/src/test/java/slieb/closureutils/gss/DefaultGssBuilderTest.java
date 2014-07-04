package slieb.closureutils.gss;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.resources.*;
import slieb.closureutils.stylesheets.CssRenamingMap;

import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGssBuilderTest {

    @Mock
    private DependencyParser mockDependencyParser;


    @Mock
    private ResourceProvider mockResourceProvider;

    private DefaultGssBuilder defaultGssBuilder;

    private Set<Resource> resources;

    public void setResource(Set<String> provides, Set<String> requires,
                            final String content) throws Exception {
        Resource newResource = new StringResource(content, null);
        DependencyNode mockNode = mock(DependencyNode.class);

        when(mockDependencyParser.parse(newResource)).thenReturn(mockNode);
        when(mockNode.getProvides()).thenReturn(provides);
        when(mockNode.getRequires()).thenReturn(requires);
        when(mockNode.getResource()).thenReturn(newResource);

        resources.add(newResource);
    }


    @Before
    public void setUp() throws Exception {

        resources = newHashSet();
        when(mockResourceProvider.getResources()).thenReturn(resources);


        defaultGssBuilder = new DefaultGssBuilder(mockDependencyParser);
    }


    @Test
    public void testCompileGeneratedRenameMap() throws Exception {

        setResource(newHashSet("A"), Sets.<String>newHashSet(),
                ".foo { background: red; }");


        Resource resource = new MemoryResource();
        Resource renameResource = new MemoryResource();

        GssOptions options = new GssOptions.Builder()
                .setResourceProvider(mockResourceProvider)
                .setEntryPoints(newArrayList("A"))
                .setOutputResource(resource)
                .setRenameMapResource(renameResource)
                .build();
        defaultGssBuilder.build(options);

        String renameContent = Resources.readResource(renameResource);
        CssRenamingMap renamingMap = CssRenamingMap.parseString(renameContent);
        String output = Resources.readResource(resource);
        assertTrue(output.contains(renamingMap.getCssName("foo")));
        assertFalse(output.contains(renamingMap.getCssName("bar")));
    }


    @Test
    public void testCompileGeneratedRenameMapProductionMode() throws Exception {

        setResource(newHashSet("A"), Sets.<String>newHashSet(),
                ".foo { background: red; }");

        Resource resource = new MemoryResource();
        Resource renameResource = new MemoryResource();

        defaultGssBuilder.build(
                new GssOptions.Builder()
                        .setResourceProvider(mockResourceProvider)
                        .setEntryPoints(newArrayList("A"))
                        .setOutputResource(resource)
                        .setShouldGenerateForProduction(true)
                        .setShouldGenerateForDebug(false)
                        .setRenameMapResource(renameResource)
                        .build());

        String renameContent = Resources.readResource(renameResource);
        CssRenamingMap renamingMap = CssRenamingMap.parseString(renameContent);
        String output = Resources.readResource(resource);
        assertTrue(output.contains(renamingMap.getCssName("foo")));
        assertFalse(output.contains(renamingMap.getCssName("bar")));
    }
}