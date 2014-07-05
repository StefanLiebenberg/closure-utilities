package slieb.closureutils.commandline.runners;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import slieb.closureutils.commandline.options.BuildOptions;
import slieb.closureutils.gss.DefaultGssBuilder;
import slieb.closureutils.html.HtmlBuilder;
import slieb.closureutils.javascript.JsBuilder;
import slieb.closureutils.templates.SoyBuilder;

import java.io.OutputStream;
import java.util.Set;

@Singleton
public class BuildRunner implements Runner {

    private final SoyBuilder soyBuilder;
    private final DefaultGssBuilder gssBuilder;
    private final HtmlBuilder htmlBuilder;
    private final JsBuilder jsBuilder;

    @Inject
    public BuildRunner(SoyBuilder soyBuilder, DefaultGssBuilder gssBuilder, HtmlBuilder htmlBuilder, JsBuilder jsBuilder) {
        this.soyBuilder = soyBuilder;
        this.gssBuilder = gssBuilder;
        this.htmlBuilder = htmlBuilder;
        this.jsBuilder = jsBuilder;
    }

    public BuildOptions parse(String[] args) throws CmdLineException {
        final BuildOptions configurable = new BuildOptions();
        final CmdLineParser cmdLineParser = new CmdLineParser(configurable);
        cmdLineParser.parseArgument(args);
        return configurable;
    }


    public Set<String> getModules(BuildOptions options) {
        return options.getModules();
    }


    @Override
    public void run(String... args) throws Exception {
        BuildOptions options = parse(args);
        Set<String> modules = getModules(options);

        if (modules.contains("css")) {
            gssBuilder.build(null);
        }

        if (modules.contains("soy")) {
            soyBuilder.build(null);
        }

        if (modules.contains("js")) {
            jsBuilder.build(null);
        }

        if (modules.contains("html")) {
            htmlBuilder.build(null);
        }
    }

    @Override
    public void printHelp(OutputStream outputStream) {
        final BuildOptions configurable = new BuildOptions();
        final CmdLineParser cmdLineParser = new CmdLineParser(configurable);
        cmdLineParser.printUsage(outputStream);


    }
}
