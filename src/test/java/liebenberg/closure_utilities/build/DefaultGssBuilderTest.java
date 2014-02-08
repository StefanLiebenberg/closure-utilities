package liebenberg.closure_utilities.build;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import liebenberg.closure_utilities.utilities.FS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;

public class DefaultGssBuilderTest
        extends AbstractBuildTest<DefaultGssBuilder, GssOptions, GssResult> {

    protected File outputFile;

    public DefaultGssBuilderTest()
            throws InstantiationException, IllegalAccessException {
        super(DefaultGssBuilder.class, GssOptions.class);
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

        builderOptions.setEntryPoints(getGssEntryPoints(Flavour.BASIC));
        builderOptions.setSourceDirectories(getGssSourceDirectories());
        builderOptions.setShouldCalculateDependencies(true);
        builder.build(builderOptions);

        String content = FS.read(outputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
        Assert.assertTrue(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertTrue(content.contains(".foo-image{background:url" +
                "(images/path-to-chicken.jpg)}"));


        builderOptions.setEntryPoints(getGssEntryPoints(Flavour.BASIC));
        builderOptions.setSourceDirectories(getGssSourceDirectories());
        builderOptions.setShouldCalculateDependencies(true);
        builder.build(builderOptions);

        content = FS.read(outputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
        Assert.assertTrue(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertTrue(content.contains(".foo-image{background:url" +
                "(images/path-to-chicken.jpg)}"));

        builderOptions.setEntryPoints(Lists.newArrayList("shared-reset"));
        builderOptions.setSourceDirectories(getGssSourceDirectories());
        builderOptions.setShouldCalculateDependencies(true);
        builder.build(builderOptions);

        content = FS.read(outputFile);
        Assert.assertFalse(content.contains(".foo-color{background:red}"));
        Assert.assertFalse(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertFalse(content.contains(".foo-image{background:url" +
                "(images/path-to-chicken.jpg)}"));
    }

    @Test
    public void testCompileWithImagePath() throws Exception {

        final File srcGssDir = getApplicationDirectory("src/gss");
        Assert.assertTrue(srcGssDir.exists());

        final File outputFile = new File(outputDirectory, "output.css");
        builderOptions.setEntryPoints(Lists.newArrayList("company-import"));
        builderOptions.setSourceDirectories(Lists.newArrayList(srcGssDir));
        builderOptions.setShouldCalculateDependencies(true);

        final URI assetsDirectory = new URI("/assets");
        builderOptions.setAssetsUri(assetsDirectory);
        builder.build(builderOptions);

        final String content = FS.read(outputFile);
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
        Assert.assertTrue(content.contains(".foo-font{font:Arial,12px}"));
        Assert.assertTrue(content.contains(".foo-image{background:url" +
                "(/assets/images/path-to-chicken.jpg)}"));
    }

}
