package liebenberg.closure_utilities.render;


import javax.annotation.Nonnull;

public class RenderException extends Exception {
    public RenderException() {}

    public RenderException(@Nonnull final String message) {
        super(message);
    }

    public RenderException(@Nonnull final String message,
                           @Nonnull final Throwable cause) {
        super(message, cause);
    }

    public RenderException(@Nonnull final Throwable cause) {
        super(cause);
    }

    public RenderException(@Nonnull final String message,
                           @Nonnull final Throwable cause,
                           final boolean enableSuppression,
                           final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
