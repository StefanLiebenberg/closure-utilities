package com.github.stefanliebenberg.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public abstract class AbstractHtmlRenderer implements HtmlRenderer {

    protected Boolean shouldInline = false;

    protected List<File> stylesheets;

    protected List<File> scripts;

    protected String title;

    protected String content;

    protected File outputPath;

    public AbstractHtmlRenderer() {}

    @Override
    public HtmlRenderer setStylesheets(@Nullable final List<File> stylesheets) {
        this.stylesheets = stylesheets;
        return this;
    }

    @Override
    public HtmlRenderer setScripts(@Nullable final List<File> scripts) {
        this.scripts = scripts;
        return this;
    }

    @Override
    public HtmlRenderer setTitle(@Nullable final String title) {
        this.title = title;
        return this;
    }

    @Override
    public HtmlRenderer setContent(@Nullable final String content) {
        this.content = content;
        return this;
    }

    @Override
    public HtmlRenderer setShouldInline(@Nullable final Boolean shouldInline) {
        this.shouldInline = shouldInline;
        return this;
    }

    @Override
    public HtmlRenderer setOutputPath(@Nullable final File outputPath) {
        this.outputPath = outputPath;
        return this;
    }


    @Override
    @Nonnull
    public abstract String render();
}
