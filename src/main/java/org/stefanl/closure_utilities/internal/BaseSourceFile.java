package org.stefanl.closure_utilities.internal;

import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;


public class BaseSourceFile {

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
