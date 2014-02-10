package slieb.closure.internal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface GssProcessor {

    /**
     * @param inputString The input string
     * @param base        The base path
     * @return The result.
     */
    @Nonnull
    public String processString(@Nonnull final String inputString,
                                @Nullable final String base);

    /**
     * @param inputStream  The source stream.
     * @param outputStream The output stream
     * @param base         The base path
     * @throws IOException
     */
    public void processStream(@Nonnull final InputStream inputStream,
                              @Nonnull final OutputStream outputStream,
                              @Nullable final String base)
            throws IOException;
}
