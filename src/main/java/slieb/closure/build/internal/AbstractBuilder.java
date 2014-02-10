package slieb.closure.build.internal;


import com.google.common.collect.ImmutableSet;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

@Immutable
public abstract class AbstractBuilder<A, B>
        implements BuilderInterface<A, B> {

    protected final Logger LOGGER =
            Logger.getLogger(this.getClass().getName());

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

}

