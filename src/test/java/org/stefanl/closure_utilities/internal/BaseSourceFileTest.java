package org.stefanl.closure_utilities.internal;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Set;


public class BaseSourceFileTest {

    private BaseSourceFile baseSourceFile;

    @Before
    public void setUp() throws Exception {
        baseSourceFile = new BaseSourceFile("/path");
    }

    @After
    public void tearDown() throws Exception {
        baseSourceFile = null;
    }

    @Test
    public void testAddRequireNamespace() throws Exception {
        baseSourceFile.addProvideNamespace("a");
        baseSourceFile.addProvideNamespace("b");
        baseSourceFile.addProvideNamespace("a");

        Set<String> provides = baseSourceFile.getProvidedNamespaces();
        Set<String> requires = baseSourceFile.getRequiredNamespaces();
        Assert.assertEquals(0, requires.size());
        Assert.assertEquals(2, provides.size());
        Assert.assertTrue(provides.contains("a"));
        Assert.assertTrue(provides.contains("b"));
        Assert.assertFalse(provides.contains("c"));
    }

    @Test
    public void testAddProvideNamespace() throws Exception {
        baseSourceFile.addRequireNamespace("a");
        baseSourceFile.addRequireNamespace("b");
        baseSourceFile.addRequireNamespace("c");

        Set<String> provides = baseSourceFile.getProvidedNamespaces();
        Set<String> requires = baseSourceFile.getRequiredNamespaces();
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
                baseSourceFile.getSourceLocation());
    }
}
