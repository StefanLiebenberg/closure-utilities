package slieb.closure.render;


import javax.annotation.Nonnull;
import java.io.IOException;

public interface RendererInterface {

    public void render(@Nonnull final Appendable sb)
            throws RenderException, IOException;

    @Nonnull
    public String render() throws RenderException, IOException;
}
