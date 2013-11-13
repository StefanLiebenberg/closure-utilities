package org.stefanl.closure_utilities.html;


import org.stefanl.closure_utilities.render.HtmlRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Map;

public class HtmlOptions {

    private File outputFile;

    private List<File> javascriptFiles;

    private List<File> stylesheetFiles;

    private Boolean shouldBuildInline = false;

    private Map<File, File> locationMap;

    private String content;

    private HtmlRenderer htmlRenderer;

    @Nullable
    public List<File> getJavascriptFiles() {
        return javascriptFiles;
    }

    public void setJavascriptFiles(@Nullable final List<File> javascriptFiles) {
        this.javascriptFiles = javascriptFiles;
    }

    @Nullable
    public List<File> getStylesheetFiles() {
        return stylesheetFiles;
    }

    public void setStylesheetFiles(@Nullable final List<File> stylesheetFiles) {
        this.stylesheetFiles = stylesheetFiles;
    }

    @Nonnull
    public Boolean getShouldBuildInline() {
        return shouldBuildInline;
    }

    public void setShouldBuildInline(@Nonnull final Boolean shouldBuildInline) {
        this.shouldBuildInline = shouldBuildInline;
    }

    @Nullable
    public Map<File, File> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(@Nullable final Map<File, File> locationMap) {
        this.locationMap = locationMap;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable final String content) {
        this.content = content;
    }

    @Nullable
    public HtmlRenderer getHtmlRenderer() {
        return htmlRenderer;
    }

    public void setHtmlRenderer(@Nullable final HtmlRenderer htmlRenderer) {
        this.htmlRenderer = htmlRenderer;
    }

    @Nullable
    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(
            @Nullable final File outputFile) {
        this.outputFile = outputFile;
    }
}
