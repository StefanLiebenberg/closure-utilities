package slieb.closureutils.javascript;

import com.google.javascript.jscomp.MessageBundle;
import slieb.closureutils.build.BuildOptions;
import slieb.closureutils.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JsOptions implements BuildOptions {

    private ResourceProvider resourceProvider;

    private List<String> entryPoints;

    private Map<String, Object> globals;

    private MessageBundle messageBundle = null;

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public Map<String, Object> getGlobals() {
        return globals;
    }

    public MessageBundle getMessageBundle() {
        return messageBundle;
    }
}
