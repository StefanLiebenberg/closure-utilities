package slieb.closureutils.configuration;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import slieb.closureutils.resources.Resource;

import java.io.IOException;
import java.io.Reader;

public class ConfigurationReader {

    public static Yaml getYaml() {
        Constructor constructor = new Constructor(RootConfiguration.class);
        return new Yaml(constructor);
    }


    public RootConfiguration read(Resource resource) throws IOException {
        Yaml yaml = getYaml();
        try (Reader reader = resource.getReader()) {
            return yaml.loadAs(reader, RootConfiguration.class);
        }
    }
}
