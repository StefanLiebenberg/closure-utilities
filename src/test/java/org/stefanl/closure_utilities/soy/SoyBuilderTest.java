package org.stefanl.closure_utilities.soy;


import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stefanl.closure_utilities.internal.AbstractBuildTest;
import org.stefanl.closure_utilities.templates.SoyBuildOptions;
import org.stefanl.closure_utilities.templates.SoyBuilder;
import org.stefanl.closure_utilities.templates.SoyBuilder;
import org.stefanl.closure_utilities.utilities.FsTool;

import java.io.File;
import java.util.Collection;

public class SoyBuilderTest extends
        AbstractBuildTest<SoyBuilder, SoyBuildOptions> {

    protected File outputDirectory;

    public SoyBuilderTest() throws InstantiationException,
            IllegalAccessException {
        super(SoyBuilder.class, SoyBuildOptions.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        outputDirectory = FsTool.getTempDirectory();
        builderOptions.setOutputDirectory(outputDirectory);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        outputDirectory = null;
        super.tearDown();
    }

    @Test
    public void testBuild() throws Exception {
        Collection<File> sourceDirectories =
                Lists.newArrayList(getApplicationDirectory("src/soy"));
        builderOptions.setSourceDirectories(sourceDirectories);
        builder.build();
    }
}
