package com.github.stefanliebenberg.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public interface HtmlRenderer {

    @Nonnull
    public HtmlRenderer setStylesheets(@Nullable final List<File> stylesheets);

    @Nonnull
    public HtmlRenderer setScripts(@Nullable final List<File> scripts);

    @Nonnull
    public HtmlRenderer setTitle(@Nullable final String title);

    @Nonnull
    public HtmlRenderer setContent(@Nullable final String content);

    @Nonnull
    public HtmlRenderer setShouldInline(@Nullable final Boolean shouldInline);

    @Nonnull
    public HtmlRenderer setOutputPath(@Nullable final File outputPath);

    @Nonnull
    public String render();
}
