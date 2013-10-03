package com.github.stefanliebenberg.render;


import com.github.stefanliebenberg.internal.IBuildOptions;

import java.io.File;
import java.util.List;
import java.util.Map;


public class HtmlBuildOptions implements IBuildOptions {

    private List<File> javascriptFiles;

    private List<File> stylesheetFiles;

    private Boolean shouldBuildInline = false;

    private Map<File, File> locationMap;

    private String content;

    private HtmlRenderer htmlRenderer;


    public List<File> getJavascriptFiles() {
        return javascriptFiles;
    }

    public void setJavascriptFiles(List<File> javascriptFiles) {
        this.javascriptFiles = javascriptFiles;
    }

    public List<File> getStylesheetFiles() {
        return stylesheetFiles;
    }

    public void setStylesheetFiles(List<File> stylesheetFiles) {
        this.stylesheetFiles = stylesheetFiles;
    }

    public Boolean getShouldBuildInline() {
        return shouldBuildInline;
    }

    public void setShouldBuildInline(Boolean shouldBuildInline) {
        this.shouldBuildInline = shouldBuildInline;
    }

    public Map<File, File> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(Map<File, File> locationMap) {
        this.locationMap = locationMap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HtmlRenderer getHtmlRenderer() {
        return htmlRenderer;
    }

    public void setHtmlRenderer(HtmlRenderer htmlRenderer) {
        this.htmlRenderer = htmlRenderer;
    }
}
