package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.PreProcessor;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUrlProcessor implements PreProcessor {

    private static final Pattern IMAGE_URL_PATTERN =
            Pattern.compile("image-url\\(([^\\)]+)\\)");

    private static final String SINGLE_QUOTE = "'";

    private static final String DOUBLE_QUOTE = "\"";

    private static String stripQoutes(String quotedString) {
        return StringUtils.strip(StringUtils.strip(quotedString,
                DOUBLE_QUOTE), SINGLE_QUOTE);
    }

    private String imageRoot;

    public void setImageRoot(@Nullable final String imageRoot) {
        this.imageRoot = imageRoot;
    }

    @Nonnull
    private String getStringPath(@Nonnull String inputPath) {
        if (imageRoot != null) {
            try {

                if (!new URI(inputPath).isAbsolute() && !Paths.get(inputPath)
                        .isAbsolute()) {
                    return Paths.get(imageRoot, inputPath).toString();
                }
            } catch (Exception e) {
                System.err.println("WARNING: getStringPath exception");
                e.printStackTrace();

            }
        }
        return inputPath;
    }


    @Nonnull
    @Override
    public String processString(
            @Nonnull final String inputString) {
        final Matcher matcher = IMAGE_URL_PATTERN.matcher(inputString);
        final StringBuffer sb = new StringBuffer();
        String urlSegment, stripped, path;
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                urlSegment = matcher.group(1);
                stripped = stripQoutes(urlSegment);
                path = getStringPath(stripped);
                matcher.appendReplacement(sb, "url(" + path + ")");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public void processStream(InputStream inputStream,
                              OutputStream outputStream)
            throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        String result = processString(writer.toString());
        StringReader reader = new StringReader(result);
        IOUtils.copy(reader, outputStream);
    }
}
