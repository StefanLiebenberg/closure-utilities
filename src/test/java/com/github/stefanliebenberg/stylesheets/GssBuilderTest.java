package com.github.stefanliebenberg.stylesheets;

import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.stylesheets.GssBuildOptions;
import com.github.stefanliebenberg.stylesheets.GssBuilder;
import com.github.stefanliebenberg.utilities.FsTool;
import junit.framework.Assert;
import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GssBuilderTest {

    private GssBuilder gssBuilder = new GssBuilder();

    private GssBuildOptions gssBuildOptions;

    private File outputFile;

    private final List<File> sourceFiles = new ArrayList<File>();

    private File setupSourceFile(String path, File directory)
            throws IOException {
        File outputSourceFile = new File(directory, path);
        FsTool.ensureDirectoryFor(outputSourceFile);
        try (InputStream inputStream = getClass().getResourceAsStream(path);
             OutputStream outputStream = new FileOutputStream
                     (outputSourceFile)) {
            IOUtil.copy(inputStream, outputStream);
        }
        return outputSourceFile;
    }


    public void setupDirectory() throws IOException {
        File tempDirectory = FsTool.getTempDirectory();

        File targetDirectory = new File(tempDirectory, "target");
        FsTool.ensureDirectory(targetDirectory);
        outputFile = new File(targetDirectory, "style.css");

        File sourceDirectory = new File(tempDirectory, "sources");
        FsTool.ensureDirectory(sourceDirectory);
        sourceFiles.add(setupSourceFile("/gss/static.gss", sourceDirectory));
    }

    @Before
    public void setUp() throws Exception {
        setupDirectory();
        gssBuildOptions = new GssBuildOptions();
        gssBuilder.setBuildOptions(gssBuildOptions);
    }

    @After
    public void tearDown() throws Exception {
        gssBuildOptions = null;
        gssBuilder.reset();
        sourceFiles.clear();
    }

    @Test
    public void testCompile() throws IOException, BuildException {
        gssBuildOptions.setShouldCalculateDependencies(false);
        gssBuildOptions.setSourceFiles(sourceFiles);
        gssBuildOptions.setAssetsDirectory(new File(outputFile.getParentFile
                (), "assets"));
        gssBuildOptions.setOutputFile(outputFile);
        gssBuilder.build();
        Assert.assertTrue(outputFile.exists());
    }

    @Test
    public void testSecondCompile() throws IOException, BuildException {
        gssBuildOptions.setShouldCalculateDependencies(false);
        gssBuildOptions.setSourceFiles(sourceFiles);
        gssBuildOptions.setAssetsDirectory(new File(outputFile.getParentFile
                (), "assets"));
        gssBuildOptions.setOutputFile(outputFile);
        gssBuilder.build();
        Assert.assertTrue(outputFile.exists());
    }
}
