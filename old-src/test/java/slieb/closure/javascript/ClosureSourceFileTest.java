package slieb.closure.javascript;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.closure.build.ClosureSourceFile;


public class ClosureSourceFileTest {

    public ClosureSourceFile closureSourceFile;

    @Before
    public void setup() throws Exception {
        closureSourceFile = new ClosureSourceFile("/path");
    }

    @Test
    public void testGetIsBaseFile() throws Exception {
        Assert.assertFalse(closureSourceFile.getIsBaseFile());
        closureSourceFile.setIsBaseFile(true);
        Assert.assertTrue(closureSourceFile.getIsBaseFile());
    }

}
