package org.stefanl.closure_utilities.internal;


public class InvalidBuildOptionsException extends BuildException {
    public InvalidBuildOptionsException() {
    }

    public InvalidBuildOptionsException(String message) {
        super(message);
    }

    public InvalidBuildOptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBuildOptionsException(Throwable cause) {
        super(cause);
    }

    public InvalidBuildOptionsException(String message, Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
