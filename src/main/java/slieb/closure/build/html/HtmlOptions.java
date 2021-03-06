package slieb.closure.build.html;


import slieb.closure.build.internal.OptionsBase;
import slieb.closure.render.HtmlRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Map;

public class HtmlOptions extends OptionsBase {

    public HtmlOptions() {}

    public HtmlOptions(@Nonnull OptionsBase parentOptions) {
        super(parentOptions);
    }

    /**
     * Do not use yet. This might be supported in future, but not now.
     */
    @Deprecated
    private Boolean shouldBuildInline = false;

    private File outputFile;

    private List<File> javascriptFiles;

    private List<File> stylesheetFiles;

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

    /**
     * @return The html output file location.
     */
    @Nullable
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * @param outputFile The output file location where the content html will
     *                   be written to.
     */
    public void setOutputFile(@Nullable final File outputFile) {
        this.outputFile = outputFile;
    }
}
