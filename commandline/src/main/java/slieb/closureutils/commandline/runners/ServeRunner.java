package slieb.closureutils.commandline.runners;


import com.google.inject.Singleton;

import java.io.PrintStream;

@Singleton
public class ServeRunner implements Runner {

    @Override
    public void run(String... args) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public void printHelp(PrintStream ps) {
        throw new RuntimeException("Not implemented yet.");
    }
}
