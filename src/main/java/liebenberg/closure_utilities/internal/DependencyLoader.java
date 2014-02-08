package liebenberg.closure_utilities.internal;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import liebenberg.closure_utilities.build.SourceFileBase;
import liebenberg.closure_utilities.utilities.FS;
import liebenberg.closure_utilities.utilities.Immuter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public abstract class DependencyLoader<A extends SourceFileBase> {

    private final DependencyParserInterface<A> dependencyParser;

    private final IDependencyCalculator<A> dependencyCalculator;

    private final ImmutableCollection<A> dependencies;

    public DependencyLoader(
            @Nonnull final DependencyParserInterface<A> depParser,
            @Nonnull final Collection<File> fileDependencies)
            throws IllegalAccessException, InstantiationException, IOException {
        dependencyParser = depParser;
        final ImmutableSet.Builder<A> builder = new ImmutableSet.Builder<>();
        for (File file : fileDependencies) {
            A dep = createDependency(file);
            depParser.parse(dep, FS.read(file));
            builder.add(dep);
        }
        dependencies = builder.build();
        dependencyCalculator = new DependencyCalculator<A>(dependencies);
    }

    protected abstract A createDependency(@Nonnull File input);

    @Nonnull
    public ImmutableList<File> getDependenciesFor(
            @Nonnull final List<String> entryPoints)
            throws DependencyException {
        return Immuter.list(dependencyCalculator.getDependencyList
                (entryPoints), SourceFileBase.TO_FILE);
    }

    @Nonnull
    public ImmutableList<File> getDependenciesFor(
            @Nonnull final String entryPoint)
            throws DependencyException {
        return Immuter.list(dependencyCalculator.getDependencyList
                (entryPoint), SourceFileBase.TO_FILE);
    }

}
