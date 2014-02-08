package liebenberg.closure_utilities.internal;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

public interface DependencyParserInterface<T extends SourceFileBase> {

    /**
     * @param dependency The dependency object
     * @param content    The actual source content.
     * @throws IOException
     */
    public void parse(@Nonnull final T dependency,
                      @Nonnull final String content)
            throws IOException;

    /**
     * @param dependency The dependency object
     * @param reader     A reader that will supply the source content.
     * @throws IOException
     */
    public void parse(@Nonnull final T dependency,
                      @Nonnull final Reader reader)
            throws IOException;
}
