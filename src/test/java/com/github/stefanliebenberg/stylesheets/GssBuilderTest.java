package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.AbstractBuildTest;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.stylesheets.GssBuildOptions;
import com.github.stefanliebenberg.stylesheets.GssBuilder;
import com.github.stefanliebenberg.utilities.FsTool;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GssBuilderTest extends AbstractBuildTest<GssBuilder,
        GssBuildOptions> {

    public GssBuilderTest()
            throws InstantiationException, IllegalAccessException {
        super(GssBuilder.class, GssBuildOptions.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCompile() throws Exception {
        File srcGssDir = getApplicationDirectory("src/gss");
        Assert.assertTrue(srcGssDir.exists());

        File outputFile = new File(outputDirectory, "output.css");

        builderOptions.setEntryPoints(Lists.newArrayList("company-import"));
        builderOptions.setSourceDirectories(Lists.newArrayList(srcGssDir));
        builderOptions.setShouldCalculateDependencies(true);
        builderOptions.setOutputFile(outputFile);
        builder.build();

        Assert.assertTrue(outputFile.exists());

        String content = FsTool.read(outputFile);
        System.out.println(content);
        Assert.assertTrue(content.contains("font-size:11px"));
    }

//    private File outputFile;
//
//    private final List<File> sourceFiles = new ArrayList<File>();
//
//    private File setupSourceFile(String path, File directory)
//            throws IOException {
//        File outputSourceFile = new File(directory, path);
//        FsTool.ensureDirectoryFor(outputSourceFile);
//        try (InputStream inputStream = getClass().getResourceAsStream(path);
//             OutputStream outputStream = new FileOutputStream
//                     (outputSourceFile)) {
//            IOUtil.copy(inputStream, outputStream);
//        }
//        return outputSourceFile;
//    }
//
//
//    public void setupDirectory() throws IOException {
//        File tempDirectory = FsTool.getTempDirectory();
//
//        File targetDirectory = new File(tempDirectory, "target");
//        FsTool.ensureDirectory(targetDirectory);
//        outputFile = new File(targetDirectory, "style.css");
//
//        File sourceDirectory = new File(tempDirectory, "sources");
//        FsTool.ensureDirectory(sourceDirectory);
//        sourceFiles.add(setupSourceFile("/gss/static.gss", sourceDirectory));
//    }
//
//
//    @Test
//    public void testCompile() throws IOException, BuildException {
//        builderOptions.setShouldCalculateDependencies(false);
//        builderOptions.setSourceFiles(sourceFiles);
//        builderOptions.setAssetsDirectory(new File(outputFile.getParentFile
//                (), "assets"));
//        builderOptions.setOutputFile(outputFile);
//        builder.build();
//        Assert.assertTrue(outputFile.exists());
//    }
//
//    @Test
//    public void testSecondCompile() throws IOException, BuildException {
//        builderOptions.setShouldCalculateDependencies(false);
//        builderOptions.setSourceFiles(sourceFiles);
//        builderOptions.setAssetsDirectory(new File(outputFile.getParentFile
//                (), "assets"));
//        builderOptions.setOutputFile(outputFile);
//        builder.build();
//        Assert.assertTrue(outputFile.exists());
//    }
}
