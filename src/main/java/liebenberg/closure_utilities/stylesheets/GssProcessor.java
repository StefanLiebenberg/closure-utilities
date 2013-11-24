package liebenberg.closure_utilities.stylesheets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface GssProcessor {

    @Nonnull
    public String processString(@Nonnull final String inputString,
                                @Nullable final String base);

    public void processStream(@Nonnull final InputStream inputStream,
                              @Nonnull final OutputStream outputStream,
                              @Nullable final String base)
            throws IOException;
}
