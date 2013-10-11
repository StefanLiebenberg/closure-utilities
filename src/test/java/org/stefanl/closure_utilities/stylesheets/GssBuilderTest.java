package org.stefanl.closure_utilities.stylesheets;

import org.stefanl.closure_utilities.internal.AbstractBuildTest;
import org.stefanl.closure_utilities.utilities.FsTool;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;

public class GssBuilderTest extends AbstractBuildTest<GssBuilder,
        GssBuildOptions> {

    protected File outputFile;

    public GssBuilderTest()
            throws InstantiationException, IllegalAccessException {
        super(GssBuilder.class, GssBuildOptions.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        outputFile = new File(outputDirectory, "output.css");
        builderOptions.setOutputFile(outputFile);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        outputFile = null;
        super.tearDown();
    }

    @Test
    public void testNormalCompile() throws Exception {
        File srcGssDir = getApplicationDirectory("src/gss");
        Assert.assertTrue(srcGssDir.exists());

        builderOptions.setEntryPoints(Lists.newArrayList("company-import"));
        builderOptions.setSourceDirectories(Lists.newArrayList(srcGssDir));
        builderOptions.setShouldCalculateDependencies(true);
        builder.build();

        String content = FsTool.read(outputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
        Assert.assertTrue(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertTrue(content.contains(".foo-image{background:url" +
                "(images/path-to-chicken.jpg)}"));
    }

    @Test
    public void testCompileWithImagePath() throws Exception {

        File srcGssDir = getApplicationDirectory("src/gss");
        Assert.assertTrue(srcGssDir.exists());

        File outputFile = new File(outputDirectory, "output.css");
        builderOptions.setEntryPoints(Lists.newArrayList("company-import"));
        builderOptions.setSourceDirectories(Lists.newArrayList(srcGssDir));
        builderOptions.setShouldCalculateDependencies(true);

        URI assetsDirectory = new URI("/assets");
        builderOptions.setAssetsUri(assetsDirectory);
        builder.build();

        String content = FsTool.read(outputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
        Assert.assertTrue(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertTrue(content.contains(".foo-image{background:url" +
                "(/assets/images/path-to-chicken.jpg)}"));
    }

}
