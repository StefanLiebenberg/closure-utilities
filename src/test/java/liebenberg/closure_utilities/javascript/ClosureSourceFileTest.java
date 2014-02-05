package liebenberg.closure_utilities.javascript;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class ClosureSourceFileTest {

    public ClosureSourceFileBase closureSourceFile;

    @Before
    public void setup () throws Exception {
        closureSourceFile = new ClosureSourceFileBase("/path");
    }

    @Test
    public void testGetIsBaseFile() throws Exception {
        Assert.assertFalse(closureSourceFile.getIsBaseFile());
        closureSourceFile.setIsBaseFile(true);
        Assert.assertTrue(closureSourceFile.getIsBaseFile());
    }

}
