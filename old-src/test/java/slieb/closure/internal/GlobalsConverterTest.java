package slieb.closure.internal;


import org.junit.Assert;
import org.junit.Test;

public class GlobalsConverterTest {

    private final GlobalsConverter globalsConverter =
            new GlobalsConverter();

    @Test
    public void testConvertValue() throws Exception {
        Object expected, actual;
        String value;

        value = "true";
        expected = true;
        actual = globalsConverter.convertValue(value);
        Assert.assertEquals(expected, actual);

        value = "false";
        expected = false;
        actual = globalsConverter.convertValue(value);
        Assert.assertEquals(expected, actual);

        value = "123.1";
        expected = 123.10;
        actual = globalsConverter.convertValue(value);
        Assert.assertEquals(expected, actual);

        value = "\"The cat jumps over the moon.\"";
        expected = "The cat jumps over the moon.";
        actual = globalsConverter.convertValue(value);
        Assert.assertEquals(expected, actual);

        value = "NULL";
        expected = null;
        actual = globalsConverter.convertValue(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConvert() throws Exception {

    }
}
