package slieb.closureutils.javascript;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.resources.ClassResource;
import slieb.closureutils.resources.Resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class JavascriptDependencyParserTest {

    private JavascriptDependencyParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new JavascriptDependencyParser();
    }

    @Test
    public void testParseRequireStatements() throws Exception {
        Resource resource = new ClassResource(getClass(),
                "/slieb/closureutils/javascript/examples/require_example.js");
        DependencyNode node = parser.parse(resource);
        assertTrue(node.getRequires().contains("example.requireA"));
        assertTrue(node.getRequires().contains("example.requireB"));
        assertFalse(node.getRequires().contains("example.requireC"));
        assertFalse(node.getRequires().contains("example.requireD"));
        assertTrue(node.getRequires().contains("example.requireE"));
        assertFalse(node.getRequires().contains("example.requireF"));
    }

    @Test
    public void testParseProvideStatements() throws Exception {
        Resource resource = new ClassResource(getClass(),
                "/slieb/closureutils/javascript/examples/require_example.js");
        DependencyNode node = parser.parse(resource);
        assertTrue(node.getProvides().contains("example.requireExample"));
    }

    @Test
    public void testParseBaseFile() throws Exception {
        Resource resource = new ClassResource(getClass(),
                "/slieb/closureutils/javascript/examples/base_example.js");
        DependencyNode node = parser.parse(resource);
        assertTrue(node.getFlag(JavascriptDependencyParser.IS_BASE));
    }

    @Test
    public void testDoesNotParseWrongBaseFile() throws Exception {
        Resource resource = new ClassResource(getClass(),
                "/slieb/closureutils/javascript/examples/require_example.js");
        DependencyNode node = parser.parse(resource);
        assertFalse(node.getFlag(JavascriptDependencyParser.IS_BASE));
    }


}