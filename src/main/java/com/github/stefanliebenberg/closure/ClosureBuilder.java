package com.github.stefanliebenberg.closure;


import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.stylesheets.GssBuildOptions;
import com.github.stefanliebenberg.stylesheets.GssBuilder;
import com.github.stefanliebenberg.templates.SoyBuildOptions;
import com.github.stefanliebenberg.templates.SoyBuilder;

public class ClosureBuilder
        extends AbstractBuilder<ClosureBuilder>
        implements IBuilder {

    public ClosureBuilder() {}

    public ClosureBuilder(ClosureBuilder buildOptions) {
        super(buildOptions);
    }

    private final GssBuilder gssBuilder = new GssBuilder();

    private final SoyBuilder soyBuilder = new SoyBuilder();


    @Override
    public void reset() {
        super.reset();
        gssBuilder.reset();
        soyBuilder.reset();
    }

    public GssBuildOptions getGssBuildOptions() {
        GssBuildOptions gssBuildOptions = new GssBuildOptions();
        return gssBuildOptions;
    }

    public void buildGss()
            throws BuildException {
        GssBuildOptions gssBuildOptions = getGssBuildOptions();
        gssBuilder.setBuildOptions(gssBuildOptions);
        gssBuilder.build();
    }

    public SoyBuildOptions getSoyBuildOptions() {
        SoyBuildOptions soyBuildOptions = new SoyBuildOptions();
        return soyBuildOptions;
    }

    public void buildSoy()
            throws BuildException {
        SoyBuildOptions soyBuildOptions = getSoyBuildOptions();
        soyBuilder.setBuildOptions(soyBuildOptions);
        soyBuilder.build();
    }

    public void buildJs() {

    }


    @Override
    public void build() throws BuildException {
        checkBuildOptions();
        buildGss();
        buildSoy();
        buildJs();
    }
}
