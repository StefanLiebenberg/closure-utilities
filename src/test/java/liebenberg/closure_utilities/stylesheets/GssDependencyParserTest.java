package liebenberg.closure_utilities.stylesheets;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


public class GssDependencyParserTest {

    GssDependencyParser parser;

    @Before
    public void setup() {
        parser = new GssDependencyParser();
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testProvideParse() throws Exception {
        GssSourceFile closureSourceFile = new GssSourceFile("/path.js");
        String content = "@provide a.b.c;";
        parser.parse(closureSourceFile, content);
        Set<String> provides = closureSourceFile.getProvidedNamespaces();
        Set<String> requires = closureSourceFile.getRequiredNamespaces();
        Assert.assertTrue(provides.contains("a.b.c"));
        Assert.assertEquals(1, provides.size());
        Assert.assertEquals(0, requires.size());
    }


    @Test
    public void testRequireParse() throws Exception {
        GssSourceFile closureSourceFile = new GssSourceFile("/path.js");
        String content = "@provide someProvide;\n@require someFacet;";
        parser.parse(closureSourceFile, content);

        Set<String> provides = closureSourceFile.getProvidedNamespaces();
        Set<String> requires = closureSourceFile.getRequiredNamespaces();

        Assert.assertEquals(1, provides.size());
        Assert.assertEquals(1, requires.size());
        Assert.assertTrue(requires.contains("someFacet"));

    }
}
