package liebenberg.closure_utilities.internal;


import com.google.common.io.LineReader;
import liebenberg.closure_utilities.build.GssSourceFile;
import liebenberg.closure_utilities.internal.DependencyParserInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Class is used to parse gss dependency information.
 */
public class GssDependencyParser implements
        DependencyParserInterface<GssSourceFile> {

    @Override
    public void parse(@Nonnull final GssSourceFile dependency,
                      @Nonnull final Reader reader)
            throws IOException {
        scan(dependency, new LineReader(reader));
    }

    @Override
    public void parse(@Nonnull final GssSourceFile dependency,
                      @Nonnull final String content)
            throws IOException {
        try (StringReader reader = new StringReader(content)) {
            parse(dependency, reader);
        }
    }

    @Nullable
    private String extractStatement(@Nonnull final String statement,
                                    @Nonnull final String line) {
        if (line.contains(statement)) {
            final Integer startIdx = line.indexOf(statement) + statement
                    .length();
            final Integer endIdx = line.indexOf(";", startIdx);
            if (startIdx != -1 && endIdx != -1) {
                return line.substring(startIdx, endIdx).trim();
            }
        }
        return null;
    }

    private final static String PROVIDE_STATEMENT = "@provide";


    @Nullable
    private String extractProvideStatement(@Nonnull final String line) {
        return extractStatement(PROVIDE_STATEMENT, line);
    }

    private final static String REQUIRE_STATEMENT = "@require";

    @Nullable
    private String extractRequireStatement(@Nonnull final String line) {
        return extractStatement(REQUIRE_STATEMENT, line);
    }

    private void scan(@Nonnull final GssSourceFile dependency,
                      @Nonnull final LineReader lineReader)
            throws IOException {
        String line = lineReader.readLine(), statement;
        while (line != null) {
            statement = extractProvideStatement(line);
            if (statement != null) {
                dependency.addProvideNamespace(statement);
            } else {
                statement = extractRequireStatement(line);
                if (statement != null) {
                    dependency.addRequireNamespace(statement);
                }
            }
            line = lineReader.readLine();
        }
    }
}
