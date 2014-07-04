package slieb.closureutils.dependencies;


public class DependencyException extends IllegalStateException {
    public DependencyException() {}

    public DependencyException(String s) {
        super(s);
    }

    public DependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependencyException(Throwable cause) {
        super(cause);
    }
}
