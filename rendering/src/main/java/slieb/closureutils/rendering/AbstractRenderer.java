package slieb.closureutils.rendering;


import javax.annotation.Nonnull;

/**
 * @param <T> The object type that this render will rendering into a string.
 */
public abstract class AbstractRenderer<T> implements RendererInterface<T> {

    /**
     * @param sb      The appendable instance into which to render.
     * @param options The rendering options object, determined by the renderer generic.
     * @throws RenderException
     */
    @Override
    public abstract void render(@Nonnull final Appendable sb, @Nonnull T options)
            throws RenderException;

    /**
     * @param options The rendering options object.
     * @return A rendered string.
     * @throws RenderException
     */
    @Nonnull
    @Override
    public final String render(@Nonnull T options) throws RenderException {
        final StringBuffer sb = new StringBuffer();
        render(sb, options);
        return sb.toString();
    }


}
