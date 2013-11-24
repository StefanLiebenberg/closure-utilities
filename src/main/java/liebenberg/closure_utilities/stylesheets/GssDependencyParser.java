package liebenberg.closure_utilities.stylesheets;


import liebenberg.closure_utilities.internal.IDependencyParser;
import com.google.common.io.LineReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class GssDependencyParser implements IDependencyParser<GssSourceFile> {

    @Override
    public void parse(final GssSourceFile dependency, final Reader reader)
            throws IOException {
        scan(dependency, new LineReader(reader));
    }

    @Override
    public void parse(final GssSourceFile dependency, final String content)
            throws IOException {
        parse(dependency, new StringReader(content));
    }

    private String extractStatement(final String statement,
                                    final String line) {
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

    private String extractProvideStatement(final String line) {
        return extractStatement("@provide", line);
    }

    private String extractRequireStatement(final String line) {
        return extractStatement("@require", line);
    }

    private void scan(final GssSourceFile dependency,
                      final LineReader lineReader)
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
