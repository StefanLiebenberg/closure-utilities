package liebenberg.closure_utilities.rhino;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

public interface RunnerInterface {

    public void initialize();

    public void close();

    @Nullable
    public Object evaluateReader(@Nonnull final Reader reader,
                                 @Nonnull final String path)
            throws IOException;

    @Nullable
    public Object evaluateString(@Nonnull String command);

    @Nullable
    public Object evaluateFile(@Nonnull File file) throws IOException;

    @Nullable
    public Boolean getBoolean(@Nonnull String command);

    @Nullable
    public String getString(@Nonnull String command);

    @Nullable
    public Number getNumber(@Nonnull String command);

}
