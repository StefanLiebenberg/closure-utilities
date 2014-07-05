package slieb.closureutils.commandline.options;


import org.kohsuke.args4j.Argument;

public class BaseOptions {

    private String[] configurationFiles;

    public String[] getConfigurationFiles() {
        return configurationFiles;
    }

    @Argument(multiValued = true)
    public void setConfigurationFiles(String[] configurationFiles) {
        this.configurationFiles = configurationFiles;
    }
}
