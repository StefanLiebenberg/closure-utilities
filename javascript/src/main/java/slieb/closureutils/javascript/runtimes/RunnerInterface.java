package slieb.closureutils.javascript.runtimes;


import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public interface RunnerInterface extends Closeable {

    public void initialize();

    public void close();

    @Nullable
    public Object evaluateReader(@Nonnull final Reader reader,
                                 @Nonnull final String path)
            throws IOException;

    @Nullable
    public Object evaluateString(@Nonnull String command);

    @Nullable
    public Object evaluateResource(@Nonnull Resource resource) throws
            IOException;

    @Nullable
    public Boolean getBoolean(@Nonnull String command);

    @Nullable
    public String getString(@Nonnull String command);

    @Nullable
    public Number getNumber(@Nonnull String command);

}
