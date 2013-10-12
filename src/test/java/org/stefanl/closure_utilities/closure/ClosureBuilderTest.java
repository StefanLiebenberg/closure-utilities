package org.stefanl.closure_utilities.closure;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.stefanl.closure_utilities.internal.AbstractBuildTest;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.utilities.FsTool;

import javax.annotation.Nonnull;


public class ClosureBuilderTest extends AbstractBuildTest<ClosureBuilder,
        ClosureBuildOptions> {

    public ClosureBuilderTest() throws IllegalAccessException,
            InstantiationException {
        super(ClosureBuilder.class, ClosureBuildOptions.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        builderOptions.setOutputDirectory(outputDirectory);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.setUp();
    }

    @Nonnull
    public Boolean testBuildForFailure() {
        try {
            builder.build();
            return false;
        } catch (BuildException buildException) {
            return true;
        }
    }

    @Test
    public void testBuildFailureWhenNoOptionsAreGiven() throws Exception {
        Assert.assertTrue("Expect build failure when no options are specified",
                testBuildForFailure());
    }

    @Test
    public void testBuildSuccessWhenNoOptionsAreGivenButIgnoreIsFlagged()
            throws Exception {
        builderOptions.setIgnoreBuilds();
        Assert.assertFalse("Expect build success when no options are specified",
                testBuildForFailure());
    }


    @Test
    public void testBuildGssBuild() throws Exception {
        builderOptions.setIgnoreBuilds();
        builderOptions.setIgnoreGssBuild(false);
        builderOptions.setGssSourceDirectories(getGssSourceDirectories());
        builderOptions.setGssEntryPoints(getGssEntryPoints());
        builderOptions.setCssClassRenameMap(null);
        builder.build();

        Assert.assertNotNull(builder.getGssOutputFile());
        final String content = FsTool.read(builder.getGssOutputFile());
        Assert.assertTrue(content.contains(".foo-color{background:red}"));
    }

    @Test
    public void testBuildSoyBuild() throws Exception {
        builderOptions.setIgnoreBuilds();
        builderOptions.setIgnoreSoyBuild(false);
        builderOptions.setSoySourceDirectories(getSoySourceDirectories());

        builder.build();
    }

}
