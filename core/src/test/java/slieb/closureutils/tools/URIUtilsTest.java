package slieb.closureutils.tools;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;
import static slieb.closureutils.tools.URIUtils.getRelativeURI;
import static slieb.closureutils.tools.URIUtils.hasSameLocation;

public class URIUtilsTest {

    @Test
    public void testGetRelative() throws Exception {
        URI base, relative, result, expected;

        base = new URI("http://localhost/lib/goog/base.js");

        relative = new URI("http://localhost/lib/custom/play.js");
        result = getRelativeURI(base, relative);
        expected = new URI("../custom/play.js");
        assertEquals(expected, result);

        base = new URI("http://localhost/lib/goog/base.js");
        relative = new URI("https://server.com/lib/custom/play.js");
        result = getRelativeURI(base, relative);
        expected = new URI("https://server.com/lib/custom/play.js");
        assertEquals(expected, result);
    }

    @Test
    public void testFromSameLocation() throws Exception {
        assertTrue(hasSameLocation(new URI("http://localhost:100/pathA"),
                new URI("http://localhost:100/pathB")));
        assertFalse(hasSameLocation(new URI("http://localhost:100/pathA"),
                new URI("http://localhost:101/pathB")));
        assertFalse(hasSameLocation(new URI("localhost.com/pathA"),
                new URI("http://localhost:101/pathB")));

    }
}