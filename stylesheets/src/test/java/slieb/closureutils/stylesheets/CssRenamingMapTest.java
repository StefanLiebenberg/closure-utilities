package slieb.closureutils.stylesheets;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CssRenamingMapTest {


    @Test
    public void testGetCssName() throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("foo", "a");
        map.put("bar", "b");
        CssRenamingMap renamingMap = new CssRenamingMap(map);

        assertEquals("a", renamingMap.getCssName("foo"));
        assertEquals("b", renamingMap.getCssName("bar"));
        assertEquals("bird", renamingMap.getCssName("bird"));
        assertEquals("a-b", renamingMap.getCssName("foo-bar"));
        assertEquals("a-b-bird", renamingMap.getCssName("foo-bar-bird"));
    }


    @Test
    public void testParseString() throws Exception {
        CssRenamingMap renamingMap = CssRenamingMap.parseString("var X = {\"foo\": \"f\", \"bar\" : \"b\" };");
        assertEquals("f", renamingMap.getCssName("foo"));
        assertEquals("b", renamingMap.getCssName("bar"));
    }
}