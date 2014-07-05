package slieb.closureutils.commandline;


import slieb.closureutils.commandline.runners.CLIRunner;
import slieb.closureutils.commandline.runners.Runner;

import static com.google.inject.Guice.createInjector;
import static java.lang.System.err;
import static java.lang.System.exit;


public class ClosureCommandLine {

    /**
     * The entry point for command line usage. This needs better exception handling.
     *
     * @param args String array of arguments.
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        final Runner runner = createInjector().getInstance(CLIRunner.class);
        if (args.length > 0) {
            runner.run(args);
        } else {
            runner.printHelp(err);
            exit(1);
        }
    }
}
