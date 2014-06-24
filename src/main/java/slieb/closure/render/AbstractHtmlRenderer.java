package slieb.closure.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public abstract class AbstractHtmlRenderer
        extends AbstractRenderer implements HtmlRenderer {

    protected Boolean shouldInline = false;

    protected List<File> stylesheets;

    protected List<File> scripts;

    protected String title;

    protected String content;

    protected File outputPath;

    public AbstractHtmlRenderer() {}

    @Override
    public void reset() {
        shouldInline = false;
        stylesheets = null;
        scripts = null;
        title = null;
        content = null;
        outputPath = null;
    }

    @Override
    @Nonnull
    public HtmlRenderer setStylesheets(@Nullable final List<File> stylesheets) {
        this.stylesheets = stylesheets;
        return this;
    }

    @Override
    @Nonnull
    public HtmlRenderer setScripts(@Nullable final List<File> scripts) {
        this.scripts = scripts;
        return this;
    }

    @Override
    @Nonnull
    public HtmlRenderer setTitle(@Nullable final String title) {
        this.title = title;
        return this;
    }

    @Override
    @Nonnull
    public HtmlRenderer setContent(@Nullable final String content) {
        this.content = content;
        return this;
    }

    @Override
    @Nonnull
    public HtmlRenderer setShouldInline(@Nonnull final Boolean shouldInline) {
        this.shouldInline = shouldInline;
        return this;
    }

    @Override
    @Nonnull
    public HtmlRenderer setOutputPath(@Nullable final File outputPath) {
        this.outputPath = outputPath;
        return this;
    }


}
