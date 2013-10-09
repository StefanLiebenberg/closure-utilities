package com.github.stefanliebenberg.render;


import javax.annotation.Nonnull;

public interface IRenderer {

    public void render(@Nonnull final StringBuffer sb)
            throws RenderException;

    @Nonnull
    public String render() throws RenderException;
}
