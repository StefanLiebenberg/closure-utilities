package liebenberg.closure_utilities.render;


import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class AbstractRenderer implements IRenderer {

    public abstract void render(@Nonnull final Appendable sb)
            throws RenderException, IOException;

    public abstract void reset();

    @Nonnull
    @Override
    public String render() throws RenderException, IOException {
        final StringBuffer sb = new StringBuffer();
        render(sb);
        return sb.toString();
    }
}
