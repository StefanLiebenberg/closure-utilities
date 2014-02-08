package liebenberg.closure_utilities.build;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

public class SourceFileBase {

    /**
     * <b>Example:</b><br />
     * <code>
     * File inputFile = new File("/path/to/style.gss");
     * Function<File, GssSourceFile> transformFunc =
     * SourceFileBase.getTransformFunction(GssSourceFile.class);
     * GssSourceFile sourceFile = transformFunc.apply(inputFile);
     * </code>
     *
     * @param aClass The class type.
     * @return A function that will transform any File into the specified
     *         source file.
     */
    @Nonnull
    public static <A extends SourceFileBase> Function<File, A>
    getTransformFunction(@Nonnull final Class<A> aClass) {
        try {
            final Constructor<A> aConstructor =
                    aClass.getConstructor(File.class);
            return new Function<File, A>() {
                @Nullable
                @Override
                public A apply(@Nullable File input) {
                    try {
                        return aConstructor.newInstance(input);
                    } catch (ReflectiveOperationException reflectException) {
                        throw new RuntimeException(reflectException);
                    }
                }
            };
        } catch (ReflectiveOperationException reflectException) {
            throw new RuntimeException(reflectException);
        }
    }

    /**
     * A constant Function to turn any sourceFile into a normal file.
     * <p/>
     * <b>Example:</b>
     * <code>
     * GssSourceFile sourceFile = ...;
     * File inputFile = SourceFileBase.TO_FILE.apply(sourceFile);
     * </code>
     */
    public final static Function<SourceFileBase, File> TO_FILE =
            new Function<SourceFileBase, File>() {
                @Nullable
                @Override
                public File apply(@Nullable SourceFileBase input) {
                    if (input == null) {
                        throw new NullPointerException("SourceFile is null " +
                                "and cannot be converted to File.");
                    }
                    return new File(input.getSourceLocation());
                }
            };

    /**
     * A set of namespaces provided.
     */
    private final HashSet<String> providedNamespaces = new HashSet<String>();

    /**
     * A set of namespaces required.
     */
    private final HashSet<String> requiredNamespaces = new HashSet<String>();

    /**
     * The actual source url.
     */
    private final URI sourceLocation;

    /**
     * @param sourceLocation The uri location where source file resides.
     */
    public SourceFileBase(final URI sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    /**
     * @param sourceFile The file object where source file resides.
     */
    public SourceFileBase(final File sourceFile) {
        this.sourceLocation = sourceFile.toURI();
    }

    /**
     * @param sourcePath The uri path, specified by string.
     * @throws URISyntaxException
     */
    public SourceFileBase(final String sourcePath)
            throws URISyntaxException {
        this.sourceLocation = new URI(sourcePath);
    }

    /**
     * @param namespace A namespace that this source file requires.
     */
    public void addRequireNamespace(final String namespace) {
        this.requiredNamespaces.add(namespace);
    }

    /**
     * @param namespace A namespace that this source file provides.
     */
    public void addProvideNamespace(final String namespace) {
        this.providedNamespaces.add(namespace);
    }

    /**
     * @return The source location where this source file can be found.
     */
    public URI getSourceLocation() {
        return this.sourceLocation;
    }

    /**
     * @return A Immutable set of provided namespaces.
     */
    public ImmutableSet<String> getProvidedNamespaces() {
        return ImmutableSet.copyOf(providedNamespaces);
    }

    /**
     * @return A Immutable set of required namespaces.
     */
    public ImmutableSet<String> getRequiredNamespaces() {
        return ImmutableSet.copyOf(requiredNamespaces);
    }

    /**
     * Convert a File object into a source file.
     * <p/>
     * Example:
     * <code>
     * File inputFile = ...;
     * GssSourceFile = SourceFileBase.fromFile(inputFile, GssSourceFile.class);
     * </code>
     *
     * @param inputFile
     * @param aClass
     * @return
     */
    @Nonnull
    public static <A extends SourceFileBase> A fromFile(
            @Nonnull final File inputFile,
            @Nonnull final Class<A> aClass) {
        final Function<File, A> transformFunction = getTransformFunction
                (aClass);
        final A sourceFile = transformFunction.apply(inputFile);
        if (sourceFile != null) {
            return sourceFile;
        }
        throw new NullPointerException("Source file is not supposed to be " +
                "null");
    }
}
