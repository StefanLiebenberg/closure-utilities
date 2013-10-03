package com.github.stefanliebenberg.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public interface HtmlRenderer {

    public HtmlRenderer setStylesheets(@Nullable final List<File> stylesheets);

    public HtmlRenderer setScripts(@Nullable final List<File> scripts);

    public HtmlRenderer setTitle(@Nullable final String title);

    public HtmlRenderer setContent(@Nullable final String content);

    public HtmlRenderer setShouldInline(@Nullable final Boolean shouldInline);

    public HtmlRenderer setOutputPath(@Nullable final File outputPath);

    @Nonnull
    public String render();
}
