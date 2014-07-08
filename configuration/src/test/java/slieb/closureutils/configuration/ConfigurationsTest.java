package slieb.closureutils.configuration;

import org.junit.Before;
import org.junit.Test;
import slieb.closureutils.resources.ClassResource;
import slieb.closureutils.resources.Resource;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static slieb.closureutils.configuration.Configurations.*;

public class ConfigurationsTest {

    ConfigurationReader reader;

    Resource resource;
    RootConfiguration rootConfiguration;


    @Before
    public void setUp() throws Exception {
        resource = new ClassResource(getClass(), "/closureutils/config_example.yml");
        reader = new ConfigurationReader();
        rootConfiguration = reader.read(resource);
    }


    @Test
    public void testGetInheritanceTree() throws Exception {
        Configuration sampleApp = rootConfiguration.getComponent("sample_app");
        assertEquals(newArrayList(rootConfiguration, sampleApp), getInheritanceTree(rootConfiguration, sampleApp));
    }

    @Test
    public void testGetSources() throws Exception {
        Configuration sampleApp = rootConfiguration.getComponent("sample_app");
        assertEquals(newArrayList("src/main/javascript"), getSources(rootConfiguration, sampleApp));
    }

    @Test
    public void testGetTestSources() throws Exception {
        Configuration sampleApp = rootConfiguration.getComponent("sample_app");
        assertEquals(newArrayList("src/test/javascript"), getTestSources(rootConfiguration, sampleApp));
    }

    @Test
    public void testGetPropertiesFiles() throws Exception {
        Configuration sampleApp = rootConfiguration.getComponent("sample_app");
        assertEquals(newArrayList("src/main/resources/globals.properties"), getPropertiesFiles(rootConfiguration, sampleApp));
    }

    @Test
    public void testGetProperties() throws Exception {

        Configuration sampleApp = rootConfiguration.getComponent("sample_app");
        Map<String, Object> properties = getProperties(rootConfiguration, sampleApp);
        assertEquals(false, properties.get("goog.DEBUG"));

        Configuration sampleAppDebug = rootConfiguration.getComponent("sample_app_debug");
        Map<String, Object> debugProperties = getProperties(rootConfiguration, sampleAppDebug);
        assertEquals(true, debugProperties.get("goog.DEBUG"));
    }
}