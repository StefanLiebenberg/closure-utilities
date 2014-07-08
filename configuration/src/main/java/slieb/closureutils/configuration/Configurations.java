package slieb.closureutils.configuration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configurations {

    private Configurations() {}

    public static List<Configuration> getInheritanceTree(RootConfiguration rootConfiguration, Configuration configuration) {
        List<Configuration> list = new ArrayList<>();
        list.add(rootConfiguration);
        if (!rootConfiguration.equals(configuration)) {
            String parentName = configuration.getParent();
            if (parentName != null) {
                Configuration parent = rootConfiguration.getComponent(parentName);

                if (parent != null) {
                    for (Configuration ancestor : getInheritanceTree(rootConfiguration, parent)) {
                        if (!list.contains(ancestor)) {
                            list.add(ancestor);
                        }
                    }
                }
            }
            list.add(configuration);
        }
        return list;
    }


    public static List<String> getSources(RootConfiguration rootConfig, Configuration configuration) {
        List<String> sources = new ArrayList<>();
        for (Configuration config : getInheritanceTree(rootConfig, configuration)) {
            List<String> configSources = config.getSources();
            if (configSources != null) {
                for (String source : configSources) {
                    if (!sources.contains(source)) {
                        sources.add(source);
                    }
                }
            }
        }
        return sources;
    }

    public static List<String> getTestSources(RootConfiguration rootConfig, Configuration configuration) {
        List<String> testSources = new ArrayList<>();
        for (Configuration config : getInheritanceTree(rootConfig, configuration)) {
            List<String> configTestSources = config.getTestSources();
            if (configTestSources != null) {
                for (String source : configTestSources) {
                    if (!testSources.contains(source)) {
                        testSources.add(source);
                    }
                }
            }
        }
        return testSources;
    }

    public static List<String> getPropertiesFiles(RootConfiguration rootConfig, Configuration configuration) {
        List<String> sources = new ArrayList<>();
        for (Configuration config : getInheritanceTree(rootConfig, configuration)) {
            List<String> configPropertiesFiles = config.getPropertiesFiles();
            if (configPropertiesFiles != null) {
                for (String source : configPropertiesFiles) {
                    if (!sources.contains(source)) {
                        sources.add(source);
                    }
                }
            }
        }
        return sources;
    }


    public static Map<String, Object> getProperties(RootConfiguration rootConfig, Configuration configuration) {
        Map<String, Object> propertiesMap = new HashMap<>();
        for (Configuration config : getInheritanceTree(rootConfig, configuration)) {
            Map<String, Object> configProperties = config.getProperties();
            if (configProperties != null) {
                for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
                    propertiesMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return propertiesMap;
    }


}
