package org.stefanl.closure_utilities.internal;


import javax.annotation.Nonnull;
import java.io.PrintStream;

public abstract class AbstractBuilder<A>
        implements BuilderInterface {

    public enum LogLevel {
        INFO(1), WARN(2), DEBUG(3);

        protected int levelValue;

        LogLevel(int value) {
            levelValue = value;
        }
    }

    public static final LogLevel defaultLevel = LogLevel.DEBUG;

    protected A buildOptions;

    public AbstractBuilder() {}

    public AbstractBuilder(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    public void setBuildOptions(@Nonnull final A buildOptions) {
        this.buildOptions = buildOptions;
    }

    protected abstract void buildInternal() throws Exception;

    public static final String BUILD_EXCEPTION_MESSAGE =
            "An exception occurred during the build";

    @Override
    public void build() throws BuildException {
        checkOptions();
        try {
            buildInternal();
        } catch (BuildException buildException) {
            throw buildException;
        } catch (Exception exception) {
            throw new BuildException(BUILD_EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public void reset() {
        buildOptions = null;
    }


    private static final String BUILD_OPTIONS_ERROR =
            "Build options have not been set.";

    @Override
    public void checkOptions()
            throws BuildOptionsException {
        if (buildOptions == null) {
            throw new BuildOptionsException(
                    BUILD_OPTIONS_ERROR,
                    new NullPointerException());
        }
    }

    private static final Boolean PRINT_LEVEL_PREFIX = true;

    private static final int PRINT_LEVEL_PREFIX_INDENT = 10;

    private static final String EMPTY_PREFIX = "              ";

    private static final PrintStream logPrintStream = System.out;

    protected void log(
            @Nonnull final String message,
            @Nonnull final LogLevel level) {
        if (level.levelValue <= defaultLevel.levelValue) {
            if (PRINT_LEVEL_PREFIX) {
                String value = level.toString();
                int size = value.length();
                logPrintStream.print(level.toString());
                logPrintStream.print(" ");
                for (int i = size; i < PRINT_LEVEL_PREFIX_INDENT; i++) {
                    logPrintStream.print(".");
                }
                logPrintStream.print(" : ");

                String prefix = "";
                for (String part : message.split("\n")) {
                    logPrintStream.print(prefix);
                    logPrintStream.println(part);
                    prefix = EMPTY_PREFIX;
                }
            } else {
                logPrintStream.println(message);
            }
        }
    }

    protected void log(@Nonnull final String message) {
        log(message, LogLevel.INFO);
    }

}

