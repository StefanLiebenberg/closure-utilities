package com.github.stefanliebenberg.templates;

import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;

import java.io.File;

public class MultiLocaleSoyBuilder
        extends AbstractBuilder<MultiLocaleSoyBuildOptions>
        implements IBuilder {

    private final SoyBuilder soyBuilder = new SoyBuilder();

    public MultiLocaleSoyBuilder() {}

    public MultiLocaleSoyBuilder(MultiLocaleSoyBuildOptions buildOptions) {
        super(buildOptions);
    }

    @Override
    public void build() throws BuildException {
        for (File soyMessageFile : buildOptions.getSoyMessageFiles()) {
            soyBuilder.reset();
            buildOptions.setMessageFile(soyMessageFile);
            soyBuilder.setBuildOptions(buildOptions);
            soyBuilder.build();
        }
    }

    @Override
    public void reset() {
        super.reset();
        soyBuilder.reset();
    }
}
