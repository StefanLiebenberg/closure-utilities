package slieb.closureutils.rendering;


import javax.annotation.Nonnull;

public abstract class AbstractRenderer<T> implements RendererInterface<T> {

    @Nonnull
    @Override
    public final String render(@Nonnull T options) throws RenderException {
        final StringBuffer sb = new StringBuffer();
        render(sb, options);
        return sb.toString();
    }

    @Override
    public abstract void render(@Nonnull final Appendable sb,
                                @Nonnull T options)
            throws RenderException;
}
