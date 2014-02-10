package slieb.closure.build.internal;


import javax.annotation.Nonnull;

public class BuildOptionsException extends BuildException {
    public BuildOptionsException() { }

    public BuildOptionsException(@Nonnull String message) {
        super(message);
    }

    public BuildOptionsException(@Nonnull String message,
                                 @Nonnull Throwable cause) {
        super(message, cause);
    }

    public BuildOptionsException(@Nonnull Throwable cause) {
        super(cause);
    }

    public BuildOptionsException(@Nonnull String message,
                                 @Nonnull Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
