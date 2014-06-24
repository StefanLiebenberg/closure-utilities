package slieb.closure.html.models;

import slieb.soy.annotations.Soy;

import java.util.List;

@Soy
public class Page {
    private final String title, content;

    private final List<Script> scripts;

    private final List<Stylesheet> stylesheets;

    public Page(String title, String content, List<Script> scripts, List<Stylesheet> stylesheets) {
        this.title = title;
        this.content = content;
        this.scripts = scripts;
        this.stylesheets = stylesheets;
    }

    @Soy.Method("Title")
    public String getTitle() {
        return title;
    }

    @Soy.Method("Content")
    public String getContent() {
        return content;
    }

    @Soy.Method("Scripts")
    public List<Script> getScripts() {
        return scripts;
    }

    @Soy.Method("Stylesheets")
    public List<Stylesheet> getStylesheets() {
        return stylesheets;
    }

}
