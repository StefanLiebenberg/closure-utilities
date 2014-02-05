package liebenberg.closure_utilities.internal;


import java.util.List;

public interface IDependencyCalculator<T extends SourceFileBase> {

    public List<T> getDependencyList(final String entryPoint)
            throws DependencyException;

    public List<T> getDependencyList(final List<String> entryPoints)
            throws DependencyException;

}
