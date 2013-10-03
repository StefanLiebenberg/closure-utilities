package com.github.stefanliebenberg.closure;


import com.github.stefanliebenberg.internal.AbstractBuilder;
import com.github.stefanliebenberg.internal.BuildException;
import com.github.stefanliebenberg.internal.IBuilder;
import com.github.stefanliebenberg.javascript.JsBuildOptions;
import com.github.stefanliebenberg.javascript.JsBuilder;
import com.github.stefanliebenberg.stylesheets.GssBuildOptions;
import com.github.stefanliebenberg.stylesheets.GssBuilder;
import com.github.stefanliebenberg.templates.SoyBuildOptions;
import com.github.stefanliebenberg.templates.SoyBuilder;

import javax.annotation.Nonnull;

public class ClosureBuilder
        extends AbstractBuilder<ClosureBuildOptions>
        implements IBuilder {

    public ClosureBuilder() {}

    public ClosureBuilder(@Nonnull final ClosureBuildOptions buildOptions) {
        super(buildOptions);
    }

    private final GssBuilder gssBuilder = new GssBuilder();

    private final SoyBuilder soyBuilder = new SoyBuilder();

    private final JsBuilder jsBuilder = new JsBuilder();


    @Override
    public void reset() {
        super.reset();
        gssBuilder.reset();
        soyBuilder.reset();
        jsBuilder.reset();
    }

    @Nonnull
    public GssBuildOptions getGssBuildOptions() {
        GssBuildOptions gssBuildOptions = new GssBuildOptions();
        return gssBuildOptions;
    }

    public void buildGss()
            throws BuildException {
        gssBuilder.setBuildOptions(getGssBuildOptions());
        gssBuilder.build();
    }

    @Nonnull
    public SoyBuildOptions getSoyBuildOptions() {
        SoyBuildOptions soyBuildOptions = new SoyBuildOptions();
        return soyBuildOptions;
    }

    public void buildSoy()
            throws BuildException {
        soyBuilder.setBuildOptions(getSoyBuildOptions());
        soyBuilder.build();
    }

    @Nonnull
    public JsBuildOptions getJsBuildOptions() {
        return new JsBuildOptions();
    }

    public void buildJs()
            throws BuildException {
        jsBuilder.setBuildOptions(getJsBuildOptions());
        jsBuilder.build();
    }


    @Override
    public void build() throws BuildException {
        checkBuildOptions();
        buildGss();
        buildSoy();
        buildJs();
    }
}
