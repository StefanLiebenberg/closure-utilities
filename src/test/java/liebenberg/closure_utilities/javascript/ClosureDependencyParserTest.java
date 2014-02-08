package liebenberg.closure_utilities.javascript;

import com.google.common.collect.ImmutableSet;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


public class ClosureDependencyParserTest {

    ClosureDependencyParser parser;

    @Before
    public void setup() {
        parser = new ClosureDependencyParser();
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testProvideParse() throws Exception {
        ClosureSourceFile closureSourceFile = new ClosureSourceFile("/path.js");
        String content = "goog.provide('a.b.c')";
        parser.parse(closureSourceFile, content);
        ImmutableSet<String> provides =
                closureSourceFile.getProvidedNamespaces();
        ImmutableSet<String> requires =
                closureSourceFile.getRequiredNamespaces();
        Assert.assertTrue(provides.contains("a.b.c"));
        Assert.assertEquals(1, provides.size());
        Assert.assertFalse(closureSourceFile.getIsBaseFile());
        Assert.assertEquals(0, requires.size());
    }

    @Test
    public void testIsBaseFile() throws Exception {
        ClosureSourceFile closureSourceFile = new ClosureSourceFile("/path.js");
        String content = "goog.provide('someProvide');\ngoog.base = function " +
                "() {};";
        parser.parse(closureSourceFile, content);
        Assert.assertTrue(closureSourceFile.getIsBaseFile());
    }

    @Test
    public void testRequireParse() throws Exception {
        ClosureSourceFile closureSourceFile = new ClosureSourceFile("/path.js");
        String content = "goog.provide('someProvide');goog.require" +
                "('someFacet')";
        parser.parse(closureSourceFile, content);

        Set<String> provides = closureSourceFile.getProvidedNamespaces();
        Set<String> requires = closureSourceFile.getRequiredNamespaces();

        Assert.assertEquals(1, provides.size());
        Assert.assertEquals(1, requires.size());
        Assert.assertTrue(requires.contains("someFacet"));

    }
}
