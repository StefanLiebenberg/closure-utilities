package com.github.stefanliebenberg.stylesheets;

import junit.framework.Assert;
import org.codehaus.plexus.util.StringInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;


public class ImageUrlProcessorTest {

    private String getExpected(String path) {
        return ".some-class { background-image: url(" + path +
                "); }";
    }

    private String getInput(String path) {
        return ".some-class { background-image: image-url(" + path +
                "); }";
    }

    private void abstractTest(final String inputPath,
                              final String imageRoot,
                              final String expectedPath) {
        final String expected = getExpected(expectedPath);
        final String input = getInput(inputPath);
        final ImageUrlProcessor imageUrlProcessor = new ImageUrlProcessor();
        imageUrlProcessor.setImageRoot(imageRoot);
        final String result = imageUrlProcessor.processString(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAbsoluteImageRoot() throws Exception {
        abstractTest("pasta.png", "/images", "/images/pasta.png");
    }

    @Test
    public void testNoImageRoot() throws Exception {
        abstractTest("pasta.png", null, "pasta.png");
    }

    @Test
    public void testExternalInput() throws Exception {
        abstractTest("http://abr.com/pasta.png", "/my-local-images/",
                "http://abr.com/pasta.png");
    }

    @Test
    public void testAbsoluteInput() throws Exception {
        abstractTest("/my-other-images/image.jpg", "/my-local-images/",
                "/my-other-images/image.jpg");
    }
}
