package org.stefanl.closure_utilities.rhino;

import java.io.PrintStream;

/**
 * Provides a console object in rhino.
 */
public class Console {

    private static void print(PrintStream stream, String prefix, String... messages) {
        StringBuilder output = new StringBuilder();
        String delim = "";
        for (String message : messages) {
            output.append(delim).append(message);
            delim = " ";
        }

        for (String line : output.toString().split("\n")) {
            stream.print(prefix);
            stream.print(" ");
            stream.println(line);
        }

        stream.flush();
    }

    public static void log(String... messages) {
        print(System.out, "[LOG]", messages);
    }

    public static void error(String... messages) {
        print(System.err, "[ERROR]", messages);
    }

    public static void debug(String... messages) {
        print(System.out, "[DEBUG]", messages);
    }

    public static void warn(String... messages) {
        print(System.err, "[WARN]", messages);
    }
}
