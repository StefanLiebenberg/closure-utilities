package liebenberg.closure_utilities.render;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefinesFileRenderer extends AbstractRenderer {

    public static String VAR_DEFINES_BEGIN = "var CLOSURE_DEFINES = {\n";
    public static String VAR_DEFINES_DELIM = ",\n";
    public static String VAR_DEFINES_KEY_BEGIN = "  ";
    public static String VAR_DEFINES_KEY_END = ": ";
    public static String VAR_DEFINES_END = "\n};\n";

    public final Map<String, Object> map = new LinkedHashMap<>();

    public DefinesFileRenderer() {}

    public DefinesFileRenderer setMapValues(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    @Override
    public void reset() {
        map.clear();
    }

    protected String renderPropertyValue(@Nullable Object object) {
        if (object == null) {
            return "null";
        }

        if (object instanceof String) {
            return "'" + object.toString() + "'";
        }

        return object.toString();
    }

    @Override
    public void render(@Nonnull Appendable sb) throws RenderException,
            IOException {
        sb.append(VAR_DEFINES_BEGIN);
        String delim = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(delim)
                    .append(VAR_DEFINES_KEY_BEGIN)
                    .append(renderPropertyValue(entry.getKey()))
                    .append(VAR_DEFINES_KEY_END)
                    .append(renderPropertyValue(entry.getValue()));
            delim = VAR_DEFINES_DELIM;
        }
        sb.append(VAR_DEFINES_END);
    }
}
