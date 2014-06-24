package slieb.closure.render;


import javax.annotation.Nonnull;

public abstract class AbstractRenderer implements RendererInterface {

    @Nonnull
    @Override
    public String render() throws RenderException {
        final StringBuffer sb = new StringBuffer();
        render(sb);
        return sb.toString();
    }

    public abstract void reset();

    public abstract void render(@Nonnull final Appendable sb)
            throws RenderException;


}
