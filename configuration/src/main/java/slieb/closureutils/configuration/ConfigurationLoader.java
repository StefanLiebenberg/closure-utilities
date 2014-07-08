package slieb.closureutils.configuration;

import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

import java.io.IOException;

public class ConfigurationLoader {
    private final ResourceProvider resourceProvider;

    private final ConfigurationReader configurationReader;

    public ConfigurationLoader(ResourceProvider resourceProvider, ConfigurationReader configurationReader) {
        this.resourceProvider = resourceProvider;
        this.configurationReader = configurationReader;
    }

    public void loadResources() throws IOException {
        for (Resource resource : resourceProvider.getResources()) {
            configurationReader.read(resource);
        }
    }
}
