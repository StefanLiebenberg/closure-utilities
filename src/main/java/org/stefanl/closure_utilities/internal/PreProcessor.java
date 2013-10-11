package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PreProcessor {
    @Nonnull
    public String processString(
            @Nonnull final String content);


    public void processStream(
            @Nonnull final InputStream inputStream,
            @Nonnull final OutputStream outputStream)
            throws IOException;
}
