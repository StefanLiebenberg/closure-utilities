package slieb.closureutils.commandline.runners;


import com.google.inject.Singleton;

import java.io.OutputStream;

@Singleton
public class ServeRunner implements Runner {

    @Override
    public void run(String... args) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public void printHelp(OutputStream outputStream) {
        throw new RuntimeException("Not implemented yet.");
    }
}
