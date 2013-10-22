package org.stefanl.closure_utilities.templates;

import org.stefanl.closure_utilities.internal.AbstractBuilder;
import org.stefanl.closure_utilities.internal.BuildException;
import org.stefanl.closure_utilities.internal.BuilderInterface;

import java.io.File;

public class MultiLocaleSoyBuilder
        extends AbstractBuilder<MultiLocaleSoyBuildOptions>
        implements BuilderInterface {

    private final SoyBuilder soyBuilder = new SoyBuilder();

    public MultiLocaleSoyBuilder() {}

    public MultiLocaleSoyBuilder(MultiLocaleSoyBuildOptions buildOptions) {
        super(buildOptions);
    }

    @Override
    public void buildInternal() throws BuildException {
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
