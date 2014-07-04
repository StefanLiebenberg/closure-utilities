package slieb.closureutils.templates;


import com.google.common.collect.ImmutableMap;
import slieb.closureutils.build.BuildOptions;
import slieb.closureutils.resources.Resource;
import slieb.closureutils.resources.ResourceProvider;

public class SoyOptions implements BuildOptions {

    private ResourceProvider resourceProvider;

    private ImmutableMap<String, String> globalStringMap;

    private Resource messageFile;

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public ImmutableMap<String, String> getGlobalStringMap() {
        return globalStringMap;
    }

    public Resource getMessageFile() {
        return messageFile;
    }
}
