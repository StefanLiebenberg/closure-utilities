package liebenberg.closure_utilities.build;


import javax.annotation.Nonnull;

public class BuildException extends Exception {
    public BuildException() {}

    public BuildException(@Nonnull final String message) {
        super(message);
    }

    public BuildException(@Nonnull final String message,
                          @Nonnull final Throwable cause) {
        super(message, cause);
    }

    public BuildException(@Nonnull final Throwable cause) {
        super(cause);
    }

    public BuildException(@Nonnull final String message,
                          @Nonnull final Throwable cause,
                          final boolean enableSuppression,
                          final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
