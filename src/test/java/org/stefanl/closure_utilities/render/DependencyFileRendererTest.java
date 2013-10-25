package org.stefanl.closure_utilities.render;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stefanl.closure_utilities.javascript.ClosureSourceFile;

import java.io.File;

public class DependencyFileRendererTest {

    public DependencyFileRenderer renderer;

    @Before
    public void setup() {
        renderer = new DependencyFileRenderer();
    }

    @After
    public void tearDown() {
        renderer = null;
    }


    @Test
    public void testRenderDependencyPath() throws Exception {
        File file = new File("/path.js");
        ClosureSourceFile dependency = new ClosureSourceFile(file);
        StringBuffer sb = new StringBuffer();
        renderer.renderDependencyPath(dependency, sb);
        String expected = "/path.js";
        Assert.assertEquals(expected, sb.toString());
    }


    @Test
    public void testRenderDependencyPathWithBasePath() throws Exception {
        File file = new File("/a/b/c/path.js");
        ClosureSourceFile dependency = new ClosureSourceFile(file);
        StringBuffer sb = new StringBuffer();
        // The parent file for /a/b/e/base.js
        renderer.setBasePath("/a/b/e/");
        renderer.renderDependencyPath(dependency, sb);
        String expected = "../c/path.js";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testRenderNamespaceArray() throws Exception {

    }

    @Test
    public void testRenderDependency() throws Exception {
        File file = new File("/path.js");
        ClosureSourceFile dependency = new ClosureSourceFile(file);
        StringBuffer sb = new StringBuffer();
        renderer.renderDependency(dependency, sb);
        String expected = "goog.addDependency('/path.js', [], []);";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testRenderDependencyWithProvide() throws Exception {
        File file = new File("/path.js");
        ClosureSourceFile dependency = new ClosureSourceFile(file);
        dependency.addProvideNamespace("package.one");
        StringBuffer sb = new StringBuffer();
        renderer.renderDependency(dependency, sb);
        String expected = "goog.addDependency('/path.js', ['package.one'], " +
                "[]);";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testRenderDependencyWithRequiresAndProvide() throws Exception {
        File file = new File("/path.js");
        ClosureSourceFile dependency = new ClosureSourceFile(file);
        dependency.addProvideNamespace("package.one");
        dependency.addRequireNamespace("package.two");
        StringBuffer sb = new StringBuffer();
        renderer.renderDependency(dependency, sb);
        String expected = "goog.addDependency('/path.js', ['package.one'], " +
                "['package.two']);";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testRender() throws Exception {

    }
}
