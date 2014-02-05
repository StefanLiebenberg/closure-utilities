package liebenberg.closure_utilities;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides the version
 */
public class Version {

    private static String value;

    private static final String PROPERTY = "closure.utilities.version";

    private static final String DEFAULT_VALUE = "Unknown";

    public static String getVersion() throws IOException {
        if (value == null) {
            final Properties prop = new Properties();
            final InputStream inputStream = Version.class.
                    getResourceAsStream("/closure-utilities.properties");
            prop.load(inputStream);
            inputStream.close();
            value = prop.getProperty(PROPERTY, DEFAULT_VALUE);
        }
        return value;
    }
}
