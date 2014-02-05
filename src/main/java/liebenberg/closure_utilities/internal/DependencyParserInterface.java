package liebenberg.closure_utilities.internal;

import java.io.IOException;
import java.io.Reader;

public interface DependencyParserInterface<T extends SourceFileBase> {

    /**
     * @param dependency The dependency object
     * @param content The actual source file content.
     * @throws IOException
     */
    public void parse(final T dependency, final String content)
            throws IOException;

    public void parse(final T dependency, final Reader content) throws
            IOException;
}
