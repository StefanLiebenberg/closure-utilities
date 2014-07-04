package slieb.closureutils.html;


import slieb.closureutils.build.BuildOptions;
import slieb.closureutils.resources.Resource;

import java.util.List;

public class HtmlOptions implements BuildOptions {

    private List<Resource> scriptResources;

    private List<Resource> stylesheetResources;

    private Resource outputResource;

    // consider making resource.
    private String content;


    public List<Resource> getScriptResources() {
        return scriptResources;
    }

    public List<Resource> getStylesheetResources() {
        return stylesheetResources;
    }

    public Resource getOutputResource() {
        return outputResource;
    }

    public String getContent() {
        return content;
    }

    public void setScriptResources(List<Resource> scriptResources) {
        this.scriptResources = scriptResources;
    }

    public void setStylesheetResources(List<Resource> stylesheetResources) {
        this.stylesheetResources = stylesheetResources;
    }

    public void setOutputResource(Resource outputResource) {
        this.outputResource = outputResource;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
