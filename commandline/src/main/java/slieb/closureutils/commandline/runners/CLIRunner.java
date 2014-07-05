package slieb.closureutils.commandline.runners;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.OutputStream;
import java.util.Arrays;

@Singleton
public class CLIRunner implements Runner {

    private final BuildRunner buildRunner;

    private final ServeRunner serveRunner;

    private final TestRunner testRunner;

    private static final String
            CMD_BUILD = "build",
            CMD_SERVE = "serve",
            CMD_TEST = "test",
            CMD_CLI = "cli";


    @Inject
    public CLIRunner(BuildRunner buildRunner, ServeRunner serveRunner, TestRunner testRunner) {
        this.buildRunner = buildRunner;
        this.serveRunner = serveRunner;
        this.testRunner = testRunner;
    }

    public void runCommand(String command, String... commandArgs) throws Exception {

        if (CMD_BUILD.equalsIgnoreCase(command)) {
            buildRunner.run(commandArgs);
            return;
        }

        if (CMD_SERVE.equalsIgnoreCase(command)) {
            serveRunner.run(commandArgs);
            return;
        }

        if (CMD_TEST.equalsIgnoreCase(command)) {
            testRunner.run(commandArgs);
            return;
        }

        this.printHelp(System.err);
        throw new RuntimeException(command + " not a command");
    }


    public void run(String... args) throws Exception {
        String command = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        if (CMD_CLI.equalsIgnoreCase(command)) {
            runJLine(args);
        }

        runCommand(command, commandArgs);
    }

    public void runJLine(String... args) {

    }

    @Override
    public void printHelp(OutputStream outputStream) {

    }
}
