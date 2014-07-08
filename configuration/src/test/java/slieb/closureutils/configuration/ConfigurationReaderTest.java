package slieb.closureutils.configuration;


import org.junit.Before;
import org.junit.Test;
import slieb.closureutils.resources.ClassResource;
import slieb.closureutils.resources.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationReaderTest {

    ConfigurationReader reader;


    @Before
    public void setUp() throws Exception {

        reader = new ConfigurationReader();
    }

    @Test
    public void testRead() throws Exception {
        Resource resource = new ClassResource(getClass(), "/closureutils/config_example.yml");
        RootConfiguration configuration = reader.read(resource);
        assertEquals(2, configuration.getComponents().size());
        ComponentConfiguration sampleApp = configuration.getComponents().get("sample_app");
        assertNotNull(sampleApp);
        assertNull(sampleApp.getParent());
        assertEquals("target/output/generated.min.js", sampleApp.getOutputScript());
        List<String> sampleAppEntryPoints = sampleApp.getEntryPoints();
        assertNotNull(sampleAppEntryPoints);
        assertEquals(1, sampleAppEntryPoints.size());
        assertEquals("sample.app", sampleAppEntryPoints.get(0));
        Map<String, Object> sampleAppPropertiesMap = sampleApp.getProperties();
        assertNotNull(sampleAppPropertiesMap);
        assertEquals(Boolean.FALSE, sampleAppPropertiesMap.get("goog.DEBUG"));
        List<String> sampleAppPropertiesFiles = sampleApp.getPropertiesFiles();
        assertNotNull(sampleAppPropertiesFiles);
    }
}