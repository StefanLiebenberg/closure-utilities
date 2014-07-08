package slieb.closureutils.configuration;


import java.util.List;
import java.util.Map;

public class Configuration {

    private List<String> sources;
    private List<String> testSources;
    private List<String> gssSources;
    private List<String> templateSources;
    private List<String> entryPoints;
    private Map<String, Object> properties;
    private Map<String, Object> options;
    private List<String> propertiesFiles;
    private String outputScript;
    private String parent;
    private String assetsLocation;
    private String assetsUri;
    private String messageFile;

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getTestSources() {
        return testSources;
    }

    public void setTestSources(List<String> testSources) {
        this.testSources = testSources;
    }

    public List<String> getGssSources() {
        return gssSources;
    }

    public void setGssSources(List<String> gssSources) {
        this.gssSources = gssSources;
    }

    public List<String> getTemplateSources() {
        return templateSources;
    }

    public void setTemplateSources(List<String> templateSources) {
        this.templateSources = templateSources;
    }

    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public List<String> getPropertiesFiles() {
        return propertiesFiles;
    }

    public void setPropertiesFiles(List<String> propertiesFiles) {
        this.propertiesFiles = propertiesFiles;
    }

    public String getOutputScript() {
        return outputScript;
    }

    public void setOutputScript(String outputScript) {
        this.outputScript = outputScript;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public void setAssetsLocation(String assetsLocation) {
        this.assetsLocation = assetsLocation;
    }

    public String getAssetsUri() {
        return assetsUri;
    }

    public void setAssetsUri(String assetsUri) {
        this.assetsUri = assetsUri;
    }

    public String getMessageFile() {
        return messageFile;
    }

    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }
}
