package slieb.closureutils.configuration;


import java.util.Map;

public class RootConfiguration extends Configuration {

    private Map<String, ComponentConfiguration> components;

    public Map<String, ComponentConfiguration> getComponents() {
        return components;
    }

    public ComponentConfiguration getComponent(String name) {
        return components.get(name);
    }

    public void setComponents(Map<String, ComponentConfiguration> components) {
        this.components = components;
    }
}
