package com.github.stefanliebenberg.internal;


import com.github.stefanliebenberg.utilities.FsTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public abstract class AbstractBuildTest<A extends AbstractBuilder<B>, B> {

    protected final A builder;

    protected final Class<B> buildOptionsClass;

    protected B builderOptions;

    protected File outputDirectory;

    protected AbstractBuildTest(@Nonnull Class<A> builderClass,
                                @Nonnull Class<B> buildOptionsClass)
            throws InstantiationException, IllegalAccessException {
        builder = builderClass.newInstance();
        this.buildOptionsClass = buildOptionsClass;
    }


    protected void setUp() throws Exception {
        builderOptions = buildOptionsClass.newInstance();
        builder.setBuildOptions(builderOptions);
        outputDirectory = FsTool.getTempDirectory();
    }

    protected void tearDown() throws Exception {
        outputDirectory = null;
        builderOptions = null;
        builder.reset();
    }

    @Nonnull
    protected File getApplicationDirectory() {
        return new File("src/test/resources/app");
    }

    @Nonnull
    protected File getApplicationDirectory(String path) {
        return new File(getApplicationDirectory(), path);
    }


}
