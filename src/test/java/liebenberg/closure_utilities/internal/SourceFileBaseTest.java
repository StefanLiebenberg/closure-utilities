package liebenberg.closure_utilities.internal;

import junit.framework.Assert;
import liebenberg.closure_utilities.build.SourceFileBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Set;


public class SourceFileBaseTest {

    private SourceFileBase sourceFileBase;

    @Before
    public void setUp() throws Exception {
        sourceFileBase = new SourceFileBase("/path");
    }

    @After
    public void tearDown() throws Exception {
        sourceFileBase = null;
    }

    @Test
    public void testAddRequireNamespace() throws Exception {
        sourceFileBase.addProvideNamespace("a");
        sourceFileBase.addProvideNamespace("b");
        sourceFileBase.addProvideNamespace("a");

        Set<String> provides = sourceFileBase.getProvidedNamespaces();
        Set<String> requires = sourceFileBase.getRequiredNamespaces();
        Assert.assertEquals(0, requires.size());
        Assert.assertEquals(2, provides.size());
        Assert.assertTrue(provides.contains("a"));
        Assert.assertTrue(provides.contains("b"));
        Assert.assertFalse(provides.contains("c"));
    }

    @Test
    public void testAddProvideNamespace() throws Exception {
        sourceFileBase.addRequireNamespace("a");
        sourceFileBase.addRequireNamespace("b");
        sourceFileBase.addRequireNamespace("c");

        Set<String> provides = sourceFileBase.getProvidedNamespaces();
        Set<String> requires = sourceFileBase.getRequiredNamespaces();
        Assert.assertEquals(0, provides.size());
        Assert.assertEquals(3, requires.size());
        Assert.assertTrue(requires.contains("a"));
        Assert.assertTrue(requires.contains("b"));
        Assert.assertTrue(requires.contains("c"));
        Assert.assertFalse(requires.contains("d"));
    }



    @Test
    public void testGetSourceLocation() throws Exception {
        Assert.assertEquals(new URI("/path"),
                sourceFileBase.getSourceLocation());
    }
}
