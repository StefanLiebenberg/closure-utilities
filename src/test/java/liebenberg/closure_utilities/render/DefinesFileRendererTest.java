package liebenberg.closure_utilities.render;


import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class DefinesFileRendererTest {

    private final DefinesFileRenderer renderer =
            new DefinesFileRenderer();

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        renderer.reset();
    }


    @Test
    public void testRenderPropertyValue() throws Exception {
        Assert.assertEquals("true",renderer.renderPropertyValue(true));
        Assert.assertEquals("false",renderer.renderPropertyValue(false));
        Assert.assertEquals("10.1",renderer.renderPropertyValue(10.10));
        Assert.assertEquals("0",renderer.renderPropertyValue(0));
        Assert.assertEquals("'Office Space'",
                renderer.renderPropertyValue("Office Space"));
        Assert.assertEquals("null", renderer.renderPropertyValue(null));
    }

    @Test
    public void testRender() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("truth", true);
        renderer.setMapValues(map);

        String expected = "var CLOSURE_DEFINES = {\n  'truth': true\n};\n";
        String actual = renderer.render();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRenderTwoProperties() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("truth", true);
        map.put("falsehood", false);
        renderer.setMapValues(map);

        String expected = "var CLOSURE_DEFINES = {\n  'truth': true,\n  'falsehood': false\n};\n";
        String actual = renderer.render();
        Assert.assertEquals(expected, actual);
    }
}
