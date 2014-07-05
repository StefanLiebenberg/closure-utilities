package slieb.closureutils.commandline;


import slieb.closureutils.commandline.runners.CLIRunner;
import slieb.closureutils.commandline.runners.Runner;

public class ClosureCLIRunner {

    public static void main(String... args) {
        Runner cliRunner = new CLIRunner();
        if (args.length > 0) {
            cliRunner.run(args);
        } else {
            cliRunner.printHelp(System.err);
            System.exit(1);
        }
    }
}
