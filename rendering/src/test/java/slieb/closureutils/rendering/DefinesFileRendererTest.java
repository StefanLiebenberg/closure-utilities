package slieb.closureutils.rendering;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slieb.closureutils.javascript.runtimes.BareRunner;
import slieb.closureutils.javascript.runtimes.EnvJsRunner;
import slieb.closureutils.javascript.runtimes.RunnerInterface;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DefinesFileRendererTest {

    private final StringBuffer sb = new StringBuffer();

    private DefinesFileRenderer definesFileRenderer;

    @Before
    public void setUp() throws Exception {
        definesFileRenderer = new DefinesFileRenderer();
    }

    @After
    public void tearDown() throws Exception {
        sb.setLength(0);
    }

    @Test
    public void testRenderPropertyValueForBoolean() throws Exception {
        definesFileRenderer.renderPropertyValue(sb, true);
        assertEquals("true", sb.toString());

        sb.setLength(0);

        definesFileRenderer.renderPropertyValue(sb, false);
        assertEquals("false", sb.toString());
    }

    @Test
    public void testRenderPropertyValueForString() throws Exception {
        definesFileRenderer.renderPropertyValue(sb, "name");
        assertEquals("'name'", sb.toString());
    }

    @Test
    public void testRenderPropertyValueForNumber() throws Exception {
        definesFileRenderer.renderPropertyValue(sb, 1234);
        assertEquals("1234", sb.toString());
    }

    @Test
    public void testRenderPropertyValueForFloat() throws Exception {
        definesFileRenderer.renderPropertyValue(sb, 1234.3f);
        assertEquals("1234.3", sb.toString());
    }

    @Test
    public void testRender() throws Exception {
        Map<String, Object> valueMap =
                new ImmutableMap.Builder<String, Object>()
                        .put("numberValue", 1)
                        .put("booleanValue", false)
                        .put("dot.seperated.string", "burn")
                        .build();
        definesFileRenderer.render(sb, valueMap);

        try (RunnerInterface runner = new BareRunner()) {
            runner.initialize();
            runner.evaluateString(sb.toString());
            assertTrue(runner.getBoolean("CLOSURE_DEFINES != null"));
            assertTrue(runner.getBoolean("CLOSURE_DEFINES['numberValue'] == 1;"));
            assertTrue(runner.getBoolean("CLOSURE_DEFINES['booleanValue'] === false;"));
            assertTrue(runner.getBoolean("CLOSURE_DEFINES['dot.seperated.string'] == 'burn';"));
            assertFalse(runner.getBoolean("CLOSURE_DEFINES['foo'] == 'bar';"));
            assertTrue(runner.getBoolean("CLOSURE_DEFINES['foo'] == undefined;"));
        }
    }
}