package slieb.closureutils.rendering;


import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * The Default Html Renderer takes a instance of HtmlRenderOptions and renders a html file.
 * <p/>
 * <p/>
 * TODO: Improve docs here.
 */
public class DefaultHtmlRenderer extends AbstractRenderer<HtmlRenderOptions>
        implements HtmlRenderer {

    private static final String
            DOCTYPE_CONTENT = "<!DOCTYPE html>",
            HTML = "html",
            BODY = "body",
            HEAD = "head",
            TITLE = "title",
            SCRIPT = "script",
            LINK = "link",
            STYLESHEET = "style",
            SPACE = " ",
            EQUALS = "=",
            QUOTE = "\"",
            START_XML = "<",
            END_XML = ">";


    @Override
    public void render(@Nonnull Appendable sb,
                       @Nonnull HtmlRenderOptions options)
            throws RenderException {
        try {
            renderDoctype(sb);
            renderHtmlTag(options, sb);
        } catch (IOException ioException) {
            throw new RenderException(ioException);
        }
    }


    protected void renderHtmlTag(HtmlRenderOptions options, Appendable sb) throws IOException {
        renderContentTag(sb, HTML, false);
        renderHeadTag(sb, options);
        renderBodyTag(sb, options);
        renderContentTag(sb, HTML, true);
    }


    protected void renderDoctype(Appendable sb) throws IOException {
        sb.append(DOCTYPE_CONTENT);
    }


    private String getFilePath(Resource resource) {
        return resource.getUri().getPath();
    }


    protected void renderAttribute(Appendable sb, @Nonnull final String name, @Nonnull final String value) throws IOException {
        sb
                .append(SPACE)
                .append(name)
                .append(EQUALS)
                .append(QUOTE)
                .append(value)
                .append(QUOTE);
    }

    protected void renderStylesheetTag(Appendable sb, final String content) throws IOException {
        renderContentTag(sb, STYLESHEET, false);
        sb.append(content);
        renderContentTag(sb, STYLESHEET, true);
    }


    protected void renderStylesheetLink(Appendable sb, Resource sourceFile)
            throws IOException {
        sb.append("<link");
        renderAttribute(sb, "rel", "stylesheet");
        renderAttribute(sb, "href", getFilePath(sourceFile));
        sb.append("/>");
    }

    protected void renderStylesheet(Appendable sb, Resource resource)
            throws IOException {
        renderStylesheetLink(sb, resource);
    }


    protected void renderScript(Appendable sb, final Resource scriptFile)
            throws IOException {
        renderScriptLink(sb, scriptFile);
    }


    protected void renderScriptLink(Appendable sb, final Resource scriptFile)
            throws IOException {
        sb.append("<script");
        renderAttribute(sb, "type", "text/javascript");
        renderAttribute(sb, "src", getFilePath(scriptFile));
        sb.append(">").append("</script>");
    }


    protected void renderScriptContent(Appendable sb, final String content)
            throws IOException {
        sb.append("<script");
        renderAttribute(sb, "type", "text/javascript");
        sb.append(">").append(content).append("</script>");
    }


    protected void renderStylesheets(Appendable sb, HtmlRenderOptions options)
            throws IOException {
        List<Resource> stylesheets = options.getStylesheetsResources();
        if (stylesheets != null && !stylesheets.isEmpty()) {
            for (Resource stylesheet : stylesheets) {
                renderStylesheet(sb, stylesheet);
            }
        }
    }


    protected void renderScripts(Appendable sb, HtmlRenderOptions options)
            throws IOException {

        List<Resource> scriptResources = options.getScriptResources();
        if (scriptResources != null && !scriptResources.isEmpty()) {
            for (Resource script : scriptResources) {
                renderScript(sb, script);
            }

        }

    }


    protected void renderContent(Appendable sb, HtmlRenderOptions options)
            throws IOException {
        String content = options.getContent();
        if (content != null) {
            sb.append(content);
        }
    }


    protected void renderTitle(Appendable sb, HtmlRenderOptions options)
            throws IOException {
        String title = options.getTitle();
        if (title != null) {
            renderContentTag(sb, TITLE, false);
            sb.append(title);
            renderContentTag(sb, TITLE, true);
        }
    }


    protected void renderHeadTag(Appendable sb, HtmlRenderOptions options)
            throws IOException {
        renderContentTag(sb, HEAD, false);
        renderTitle(sb, options);
        renderScripts(sb, options);
        renderStylesheets(sb, options);
        renderContentTag(sb, HEAD, false);
    }


    protected void renderBodyTag(Appendable sb, HtmlRenderOptions options)
            throws IOException {
        renderContentTag(sb, BODY, false);
        renderContent(sb, options);
        renderContentTag(sb, BODY, true);
    }

    private void renderContentTag(Appendable sb, String tagName,
                                  boolean isClosingTag)
            throws IOException {
        sb.append(START_XML);
        if (isClosingTag) {
            sb.append("/");
        }
        sb.append(tagName);
        sb.append(END_XML);
    }
}
