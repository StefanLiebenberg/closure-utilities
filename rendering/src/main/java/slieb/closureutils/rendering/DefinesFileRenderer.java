package slieb.closureutils.rendering;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static java.util.Map.Entry;

public class DefinesFileRenderer extends AbstractRenderer<Map<String, Object>> {

    public DefinesFileRenderer() {}


    protected void renderPropertyValue(Appendable sb, Object object)
            throws IOException {
        if (object == null) {
            sb.append("null");
            return;
        }

        if (object instanceof String) {
            sb.append("'").append(object.toString()).append("'");
            return;
        }

        sb.append(object.toString());
    }

    @Override
    public void render(@Nonnull Appendable sb,
                       @Nonnull Map<String, Object> map)
            throws RenderException {
        try {
            sb.append("var CLOSURE_DEFINES = {\n");
            Iterator<Entry<String, Object>> iterator = map.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry entry = iterator.next();
                sb.append("  ");
                renderPropertyValue(sb, entry.getKey());
                sb.append(": ");
                renderPropertyValue(sb, entry.getValue());
                if (iterator.hasNext()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("};\n");
        } catch (IOException ioException) {
            throw new RenderException(ioException);
        }
    }
}
