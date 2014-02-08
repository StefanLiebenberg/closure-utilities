package liebenberg.closure_utilities.javascript;

import junit.framework.Assert;
import liebenberg.closure_utilities.build.ClosureSourceFile;
import org.junit.Before;
import org.junit.Test;


public class ClosureSourceFileTest {

    public ClosureSourceFile closureSourceFile;

    @Before
    public void setup () throws Exception {
        closureSourceFile = new ClosureSourceFile("/path");
    }

    @Test
    public void testGetIsBaseFile() throws Exception {
        Assert.assertFalse(closureSourceFile.getIsBaseFile());
        closureSourceFile.setIsBaseFile(true);
        Assert.assertTrue(closureSourceFile.getIsBaseFile());
    }

}
