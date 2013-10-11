package org.stefanl.closure_utilities.render;


import javax.annotation.Nonnull;

public abstract class AbstractRenderer implements IRenderer {

    public abstract void render(
            @Nonnull final StringBuffer sb) throws RenderException;

    public abstract void reset();

    @Nonnull
    @Override
    public String render() throws RenderException {
        StringBuffer sb = new StringBuffer();
        render(sb);
        return sb.toString();
    }
}
