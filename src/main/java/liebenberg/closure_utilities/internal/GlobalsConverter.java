package liebenberg.closure_utilities.internal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GlobalsConverter {

    @Nullable
    public Object convertValue(@Nullable final String stringValue) {
        if (stringValue == null || stringValue.equalsIgnoreCase("null")) {
            return null;
        }

        if (stringValue.equalsIgnoreCase("true")) {
            return true;
        }

        if (stringValue.equalsIgnoreCase("false")) {
            return false;
        }

        if (stringValue.startsWith("'") || stringValue.startsWith("\"")) {
            return stringValue.substring(1, stringValue.length() - 1);
        }

        return new Double(stringValue);
    }

    @Nonnull
    public Map<String, Object> convert(
            @Nonnull final Map<String, String> stringMap) {
        Map<String, Object> convertedMap = new HashMap<>();
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            convertedMap.put(entry.getKey(), convertValue(entry.getValue()));
        }
        return convertedMap;
    }
}

