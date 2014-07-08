package slieb.closureutils.commandline.runners;


import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.kohsuke.args4j.CmdLineException;
import slieb.closureutils.build.BuildException;

import java.io.IOException;
import java.io.PrintStream;
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
            CMD_CLI = "cli",
            CMD_HELP = "help";


    @Inject
    public CLIRunner(BuildRunner buildRunner, ServeRunner serveRunner, TestRunner testRunner) {
        this.buildRunner = buildRunner;
        this.serveRunner = serveRunner;
        this.testRunner = testRunner;
    }

    public void runCommand(String command, String... commandArgs) throws CmdLineException, BuildException {

        if (CMD_HELP.equalsIgnoreCase(command)) {
            printHelp(System.err);
            return;
        }

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
        } else {
            runCommand(command, commandArgs);
        }
    }

    public void runSafeCommand(String command, String[] commandArgs, PrintStream printStream) {
        try {
            runCommand(command, commandArgs);
        } catch (BuildException | CmdLineException e) {
            printStream.print(e.getMessage());
        }

    }

    public void runJLine(String... args) {
        try {
            ConsoleReader console = new ConsoleReader();
            console.setPrompt("closure-utils > ");
            String line = null;
            while ((line = console.readLine()) != null) {
                String[] lineParts = line.split(" ");
                if (lineParts.length > 0) {
                    String lineCommand = lineParts[0];
                    String[] lineArgs = Arrays.copyOfRange(lineParts, 1, lineParts.length);
                    String[] cmdArgs = ObjectArrays.concat(args, lineArgs, String.class);
                    runSafeCommand(lineCommand, cmdArgs, System.err);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void printHelp(PrintStream ps) {
        ps.println("Available commands are:");
        ps.println("   help");
        ps.println("   build");
        ps.println("   serve");
        ps.println("   test");
        ps.println("   cli");
    }
}
