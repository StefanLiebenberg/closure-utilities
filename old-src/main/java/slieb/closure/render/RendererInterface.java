package slieb.closure.render;


import javax.annotation.Nonnull;


public interface RendererInterface {

    public void render(@Nonnull final Appendable sb) throws RenderException;

    @Nonnull
    public String render() throws RenderException;
}
