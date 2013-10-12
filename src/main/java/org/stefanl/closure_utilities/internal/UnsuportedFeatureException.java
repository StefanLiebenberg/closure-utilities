package org.stefanl.closure_utilities.internal;


public class UnsuportedFeatureException extends BuildException {
    public UnsuportedFeatureException() {
    }

    public UnsuportedFeatureException(String message) {
        super(message);
    }

    public UnsuportedFeatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsuportedFeatureException(Throwable cause) {
        super(cause);
    }

    public UnsuportedFeatureException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
