package slieb.closureutils.commandline.runners;


import java.io.PrintStream;

public interface Runner {
    public void run(String... args) throws Exception;

    public void printHelp(PrintStream printStream);
}
