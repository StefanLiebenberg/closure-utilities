package slieb.closureutils.commandline.runners;


import java.io.OutputStream;

public interface Runner {
    public void run(String... args) throws Exception;

    public void printHelp(OutputStream inputStream);
}
