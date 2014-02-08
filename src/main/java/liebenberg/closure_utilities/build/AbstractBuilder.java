package liebenberg.closure_utilities.build;


import com.google.common.collect.ImmutableSet;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

@Immutable
public abstract class AbstractBuilder<A, B>
        implements BuilderInterface<A, B> {

    private final String builderName;

    protected AbstractBuilder(@Nonnull String name) {
        this.builderName = name;
    }

    public static final String BUILD_EXCEPTION_MESSAGE =
            "An exception occurred during the build";

    private static final String BUILD_OPTIONS_ERROR =
            "Build options have not been set.";

    @Override
    public abstract void checkOptions(@Nonnull final A options)
            throws BuildOptionsException;

    @Nonnull
    protected abstract B buildInternal(@Nonnull final A options)
            throws Exception;

    @Override
    @Nonnull
    public B build(@Nonnull final A options) throws BuildException {
        checkOptions(options);
        try {
            return buildInternal(options);
        } catch (BuildException buildException) {
            throw buildException;
        } catch (Exception exception) {
            throw new BuildException(BUILD_EXCEPTION_MESSAGE, exception);
        }
    }

    @Nonnull
    protected static ImmutableSet<File> findSourceFiles(
            @Nonnull final Collection<File> directories,
            @Nonnull final String extension) throws IOException {
        final ImmutableSet.Builder<File> builder = new ImmutableSet.Builder<>();
        for (File sourceFile : FS.find(directories, extension)) {
            builder.add(sourceFile.getAbsoluteFile());
        }
        return builder.build();
    }


    private void log(PrintStream printStream, Object... msgParts) {
        printStream.print(builderName);
        for (Object msgPart : msgParts) {
            printStream.print(" ");
            printStream.print(msgPart.toString());
        }
        printStream.println("");
    }

    private final PrintStream outStream = System.out;

    protected void log(Object... msgParts) {
        log(outStream, msgParts);
    }

    private final PrintStream errStream = System.err;

    protected void logErr(Object... msgParts) {
        log(errStream, msgParts);
    }


}

