package com.github.stefanliebenberg.internal;


public class DependencyException extends Exception {
    public DependencyException() {
    }

    public DependencyException(String message) {
        super(message);
    }

    public DependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependencyException(Throwable cause) {
        super(cause);
    }

    public DependencyException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void throwNothingProvides(final String namespace)
            throws DependencyException {
        throw new DependencyException(
                "Nothing provides the namespace, " + namespace);
    }

    public static void throwCircularError(final String namespace,
                                          final String provider,
                                          final String... parents)
            throws DependencyException {
        StringBuilder message = new StringBuilder();
        message.append("Circular Error detected while trying to import '")
                .append(namespace).append("'.");
        for (String parent : parents) {
            message.append("\n   ").append(parent);
        }
        message.append("\n   ").append(provider);
        throw new DependencyException(message.toString());
    }
}
