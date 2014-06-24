package slieb.closure.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefinesFileRenderer extends AbstractRenderer {

    private static final String DEFINES_FORMAT =
            "var CLOSURE_DEFINES = {\n%s\n};\n";
    private static final String ENTRY_FORMAT = "  %s: %s";
    private static final String ENTRY_DELIM = ",\n";

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
    public void render(@Nonnull Appendable sb) throws RenderException {
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String delim = "";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String keyString = renderPropertyValue(entry.getKey());
                String valueString = renderPropertyValue(entry.getValue());
                String jsonEntry = String.format(ENTRY_FORMAT, keyString,
                        valueString);
                jsonBuilder.append(delim).append(jsonEntry);
                delim = ENTRY_DELIM;
            }
            sb.append(String.format(DEFINES_FORMAT, jsonBuilder.toString()));
        } catch (IOException ioException) {
            throw new RenderException(ioException);
        }
    }
}
