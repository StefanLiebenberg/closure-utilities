package org.stefanl.closure_utilities.stylesheets;


import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractGssProcessor implements GssProcessor {

    public static Boolean IGNORE_EXCEPTIONS = false;

    private static final String SINGLE_QUOTE = "'";

    private static final String DOUBLE_QUOTE = "\"";

    protected static String stripQuotes(String quotedString) {
        return quotedString
                .replaceAll(SINGLE_QUOTE, "")
                .replaceAll(DOUBLE_QUOTE, "");
    }

    @Override
    public void processStream(@Nonnull final InputStream inputStream,
                              @Nonnull final OutputStream outputStream,
                              @Nullable final String imageRoot)
            throws IOException {
        final StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        final String result = processString(writer.toString(), imageRoot);
        final StringReader reader = new StringReader(result);
        IOUtils.copy(reader, outputStream);
    }

    @Nonnull
    protected String getStringPath(@Nonnull final String inputPathname,
                                   @Nullable final String imageRoot) {
        if (imageRoot != null) {
            try {
                URI inputUri = new URI(inputPathname);
                Path inputPath = Paths.get(inputPathname);
                if (!inputUri.isAbsolute() && !inputPath.isAbsolute()) {
                    return Paths.get(imageRoot, inputPathname)
                            .normalize()
                            .toString();
                }
            } catch (Exception e) {
                if (!IGNORE_EXCEPTIONS) {
                    throw new RuntimeException(e);
                }
            }
        }
        return inputPathname;
    }


}
