package org.stefanl.closure_utilities.internal;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;


public class BaseSourceFile {

    @Nonnull
    public static <A extends BaseSourceFile> A fromFile(
            @Nonnull final File intputFile,
            @Nonnull final Class<A> aClass) {
        try {
            return aClass.getConstructor(File.class).newInstance(intputFile);
        } catch (ReflectiveOperationException reflectException) {
            throw new RuntimeException(reflectException);
        }
    }

    @Nonnull
    public static <A extends BaseSourceFile> Function<File, A>
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

    public final static Function<BaseSourceFile, File> TO_FILE =
            new Function<BaseSourceFile, File>() {
                @Nullable
                @Override
                public File apply(@Nullable BaseSourceFile input) {
                    if (input == null) {
                        throw new NullPointerException("SourceFile is null " +
                                "and cannot be converted to File.");
                    }
                    return new File(input.getSourceLocation());
                }
            };

    private final HashSet<String> providedNamespaces = new HashSet<String>();

    private final HashSet<String> requiredNamespaces = new HashSet<String>();

    private final URI sourceLocation;

    public BaseSourceFile(final URI sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public BaseSourceFile(final File sourceFile) {
        this.sourceLocation = sourceFile.toURI();
    }

    public BaseSourceFile(final String sourcePath)
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

    public URI getSourceLocation() {
        return this.sourceLocation;
    }

    public ImmutableSet<String> getProvidedNamespaces() {
        return ImmutableSet.copyOf(providedNamespaces);
    }

    public ImmutableSet<String> getRequiredNamespaces() {
        return ImmutableSet.copyOf(requiredNamespaces);
    }
}
