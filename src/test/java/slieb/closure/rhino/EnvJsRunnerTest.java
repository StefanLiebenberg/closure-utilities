package slieb.closure.rhino;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EnvJsRunnerTest {

    EnvJsRunner runner;

    @Before
    public void setUp() throws Exception {
        runner = new EnvJsRunner();
        runner.initialize();
    }

    @After
    public void tearDown() throws Exception {
        runner.close();
        runner = null;
    }

    @Test
    public void testInitialize() throws Exception {
        Assert.assertTrue(runner.getBoolean("window != null"));
        Assert.assertTrue(runner.getBoolean("document != null"));
        Assert.assertTrue(runner.getBoolean("console != null"));
    }


    @Test
    public void testDoLoad() throws Exception {
        runner.evaluateString("window.onload = function(){window" +
                ".__HAS_BEEN_LOADED__ = true;};");
        runner.doLoad();
        Assert.assertTrue(runner.getBoolean("window.__HAS_BEEN_LOADED__"));
    }

    @Test
    public void testDoWait() throws Exception {
        runner.doLoad();
        runner.evaluateString("window.setTimeout(function(){window" +
                ".__TIMEOUT_FIRED__ = true;}, 1000);");
        runner.doWait();
        Assert.assertTrue(runner.getBoolean("window.__TIMEOUT_FIRED__"));
    }
}
