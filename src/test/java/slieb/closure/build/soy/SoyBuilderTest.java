package slieb.closure.build.soy;


import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slieb.closure.build.internal.AbstractBuildTest;
import slieb.closure.tools.FS;

import java.io.File;
import java.util.Collection;

public class SoyBuilderTest extends
        AbstractBuildTest<DefaultSoyBuilder, SoyOptions, SoyResult> {

    protected File outputDirectory;

    public SoyBuilderTest() throws InstantiationException,
            IllegalAccessException {
        super(DefaultSoyBuilder.class, SoyOptions.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        outputDirectory = FS.getTempDirectory();
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
        builder.build(builderOptions);
    }
}
