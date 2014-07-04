package slieb.closureutils.rendering;

import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;


public interface HtmlRenderOptions {

    @Nullable
    public String getTitle();

    @Nullable
    public List<Resource> getStylesheetsResources();

    @Nullable
    public List<Resource> getScriptResources();

    @Nullable
    public String getContent();

    @Nullable
    public URI getOutputUri();
}
