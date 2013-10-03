package com.github.stefanliebenberg.render;


import com.github.stefanliebenberg.utilities.FsTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class DefaultHtmlRenderer
        extends AbstractHtmlRenderer {

    protected String getFilePath(File file) {
        if (outputPath != null) {
            return FsTool.getRelative(file, outputPath.getParentFile());
        } else {
            return file.getPath();
        }
    }


    @Nonnull
    protected String render_attr(@Nonnull final String name,
                                 @Nonnull final String value) {
        return name + "=\"" + value + "\"";
    }

    @Nonnull
    protected String render_tag_base(
            @Nonnull final String tagName,
            @Nullable final String[] attributes,
            @Nonnull final Boolean terminatesSelf,
            @Nullable final String... content) {
        StringBuilder html = new StringBuilder();
        html.append("<").append(tagName);
        if (attributes != null && attributes.length > 0) {
            for (String attr : attributes) {
                html.append(" ").append(attr);
            }
        }
        if (terminatesSelf) {
            html.append("/>");
        } else {
            html.append(">");
            if (content != null && content.length > 0) {
                for (String str : content) {
                    html.append(str);
                }
            }
            html.append("</").append(tagName).append(">");
        }
        return html.toString();
    }

    protected String render_tag(@Nonnull final String tagName,
                                String... content) {
        return render_tag_base(tagName, null, false, content);
    }

    @Nonnull
    protected String renderStylesheet(@Nonnull final String... content) {
        return render_tag_base("style", new String[]{
                render_attr("type", "text/stylesheet")
        }, false, content);
    }

    @Nonnull
    protected String renderStylesheet(File sourceFile) {
        return render_tag_base("link", new String[]{
                render_attr("rel", "stylesheet"),
                render_attr("href", getFilePath(sourceFile))
        }, true);
    }

    @Nonnull
    protected String renderScript(@Nonnull final File scriptFile) {
        return render_tag_base("script", new String[]{
                render_attr("src", getFilePath(scriptFile)),
                render_attr("type", "text/javascript")
        }, false);
    }

    @Nonnull
    protected String renderScript(@Nonnull final String content) {
        return render_tag_base("script", new String[]{
                render_attr("type", "text/javascript")
        }, false, content);
    }

    @Nonnull
    protected String renderStylesheets() {
        StringBuilder html = new StringBuilder();
        if (stylesheets != null && !stylesheets.isEmpty()) {
            if (shouldInline) {
                StringBuilder content = new StringBuilder();
                for (File stylesheet : stylesheets) {
                    content.append(FsTool.safeRead(stylesheet));
                }
                html.append(renderStylesheet(content.toString()));
            } else {
                for (File stylesheet : stylesheets) {
                    html.append(renderStylesheet(stylesheet));
                }
            }
        }
        return html.toString();
    }

    @Nonnull
    protected String renderScripts() {
        StringBuilder html = new StringBuilder();
        if (scripts != null && !scripts.isEmpty()) {
            if (shouldInline) {
                StringBuilder content = new StringBuilder();
                for (File script : scripts) {
                    content.append(FsTool.safeRead(script));
                }
                html.append(renderScript(content.toString()));
            } else {
                for (File script : scripts) {
                    html.append(renderScript(script));
                }
            }
        }
        return html.toString();
    }

    @Nonnull
    protected String renderContent() {
        if (content != null) {
            return content;
        } else {
            return "";
        }
    }

    @Nonnull
    protected String renderTitle() {
        if (title != null) {
            return render_tag("title", title);
        } else {
            return "";
        }
    }

    @Nonnull
    protected String renderHeadTag() {
        return render_tag("html",
                renderTitle(),
                renderScripts(),
                renderStylesheets());
    }

    @Nonnull
    protected String renderBodyTag() {
        return render_tag("body", renderContent());
    }

    @Nonnull
    protected String renderHtmlTag() {
        return render_tag("html",
                renderHeadTag(),
                renderBodyTag());
    }


    @Nonnull
    protected String renderDoctype() {
        return "<!DOCTYPE html>";
    }

    @Override
    @Nonnull
    public String render() {
        return renderDoctype() + renderHtmlTag();
    }
}
