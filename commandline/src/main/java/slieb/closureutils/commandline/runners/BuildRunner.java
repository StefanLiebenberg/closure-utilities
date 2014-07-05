package slieb.closureutils.commandline.runners;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import slieb.closureutils.commandline.options.BuildOptions;
import slieb.closureutils.gss.DefaultGssBuilder;
import slieb.closureutils.html.HtmlBuilder;
import slieb.closureutils.javascript.JsBuilder;
import slieb.closureutils.templates.SoyBuilder;

import java.io.OutputStream;

public class BuildRunner implements Runner {

    private final SoyBuilder soyBuilder = new SoyBuilder();
    private final DefaultGssBuilder gssBuilder = new DefaultGssBuilder(null);
    private final HtmlBuilder htmlBuilder = new HtmlBuilder(null);
    private final JsBuilder jsBuilder = new JsBuilder(null);

    public BuildOptions parse(String[] args) throws CmdLineException {
        final BuildOptions configurable = new BuildOptions();
        final CmdLineParser cmdLineParser = new CmdLineParser(configurable);
        cmdLineParser.parseArgument(args);
        return configurable;
    }

    @Override
    public void run(String... args) throws Exception {
        BuildOptions options = parse(args);


    }

    @Override
    public void printHelp(OutputStream inputStream) {

    }
}
