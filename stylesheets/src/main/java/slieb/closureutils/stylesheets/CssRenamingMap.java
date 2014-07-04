package slieb.closureutils.stylesheets;


import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.MapUtils;
import org.codehaus.jackson.map.ObjectMapper;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.Resources;

import java.io.IOException;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.Builder;

public class CssRenamingMap {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ImmutableMap<String, String> classNameMap;

    public CssRenamingMap(Map<String, String> map) {
        Builder<String, String> builder = new Builder<>();
        if (map != null) {
            builder.putAll(map);
        }
        this.classNameMap = builder.build();
    }

    public String getCssName(final String cssName) {
        final StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String part : cssName.split("-")) {
            sb.append(delim).append(getCssNamePart(part));
            delim = "-";
        }
        return sb.toString();
    }

    protected String getCssNamePart(final String cssName) {
        return MapUtils.getString(classNameMap, cssName, cssName);
    }

    public static CssRenamingMap parseResource(Resource resource)
            throws IOException {
        return parseString(Resources.readResource(resource));
    }

    public static CssRenamingMap parseString(String content)
            throws IOException {
        return new CssRenamingMap(getMapFromString(content));
    }

    private static Map<String, String> getMapFromString(String content)
            throws IOException {
        final Integer firstIndex = content.indexOf("{");
        final Integer lastIndex = content.lastIndexOf("}");
        if (!firstIndex.equals(-1) && !lastIndex.equals(-1)) {
            final String jsonContent = content.substring(firstIndex,
                    lastIndex + 1);
            return (Map<String, String>)
                    objectMapper.readValue(jsonContent, Map.class);
        }
        return null;
    }
}
