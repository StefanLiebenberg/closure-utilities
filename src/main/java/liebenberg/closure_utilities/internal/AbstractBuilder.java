package liebenberg.closure_utilities.internal;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;

@Immutable
public abstract class AbstractBuilder<A, B>
        implements BuilderInterface<A, B> {

    public static final String BUILD_EXCEPTION_MESSAGE =
            "An exception occurred during the build";

    private static final String BUILD_OPTIONS_ERROR =
            "Build options have not been set.";

    public AbstractBuilder() {}

    @Nonnull
    protected static ImmutableSet<File> findSourceFiles(
            @Nonnull final ImmutableCollection<File> directories,
            @Nonnull final String extension) throws IOException {
        final ImmutableSet.Builder<File> builder = new ImmutableSet.Builder<>();
        for (File sourceFile : FS.find(directories, extension)) {
            builder.add(sourceFile.getAbsoluteFile());
        }
        return builder.build();
    }

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


    @Override
    public abstract void checkOptions(@Nonnull final A options)
            throws BuildOptionsException;
}

