package slieb.closureutils.rendering;


import javax.annotation.Nonnull;


public interface RendererInterface<T> {

    public void render(@Nonnull final Appendable sb, @Nonnull T options)
            throws RenderException;

    @Nonnull
    public String render(@Nonnull T options) throws RenderException;
}
