package com.github.stefanliebenberg.javascript;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class JsBuildOptions {

    public Collection<File> getJavaScriptSourceDirectories() {
        return javaScriptSourceDirectories;
    }

    public void setJavaScriptSourceDirectories(
            final Collection<File> javaScriptSourceDirectories) {
        this.javaScriptSourceDirectories = javaScriptSourceDirectories;
    }

    private Collection<File> javaScriptSourceDirectories;

    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    private List<String> entryPoints;


}
