package slieb.closure.render;

import slieb.closure.build.ClosureSourceFile;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import static java.nio.file.Paths.get;
import static slieb.closure.tools.FS.getRelativePath;

public class DependencyFileRenderer extends AbstractRenderer {

    private Collection<ClosureSourceFile> dependencies;

    private String basePath;

    @Override
    public void reset() {
        dependencies = null;
        basePath = null;
    }

    @Nonnull
    public DependencyFileRenderer setDependencies(
            @Nullable final Collection<ClosureSourceFile> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    @Nonnull
    public DependencyFileRenderer setBasePath(@Nullable String basePath) {
        this.basePath = basePath;
        return this;
    }

    private String getPath(@Nonnull final ClosureSourceFile dep) {
        final String path = dep.getSourceLocation().getPath();
        if (basePath != null) {
            return getRelativePath(
                    get(path),
                    get(basePath)).toString();

        } else {
            return path;
        }
    }

    protected void renderDependencyPath(@Nonnull final ClosureSourceFile dep,
                                        @Nonnull final Appendable sb)
            throws IOException {
        final String path = dep.getSourceLocation().getPath();
        if (basePath != null) {
            final String relative = getRelativePath(
                    get(path),
                    get(basePath)).toString();
            sb.append(relative);
        } else {
            // this must be changed.
            sb.append(path);
        }
    }

    protected void renderNamespaceArray(
            @Nonnull final Collection<String> namespaces,
            @Nonnull final Appendable sb)
            throws IOException {

        sb.append("[");
        String delim = "";
        for (String provideNs : namespaces) {
            sb.append(delim).append("'").append(provideNs).append("'");
            delim = ", ";
        }
        sb.append("]");
    }

    private static final String DEPENDENCY_FORMAT =
            "goog.addDependency(%s, %s, %s);";

    private static final String STRING_FORMAT = "'%s'";

    private String getStringValue(String value) {
        return String.format(STRING_FORMAT, value);
    }

    private String getCollectionStringValue(Collection<String> values) {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        sb.append("[");
        for (String value : values) {
            sb.append(delim).append(getStringValue(value));
            delim = ", ";
        }
        sb.append("]");
        return sb.toString();
    }

    private String getDependencyString(String path,
                                       Collection<String> provides,
                                       Collection<String> requires) {
        return String.format(DEPENDENCY_FORMAT,
                getStringValue(path),
                getCollectionStringValue(provides),
                getCollectionStringValue(requires));
    }

    protected void renderDependency(
            @Nonnull final ClosureSourceFile dep,
            @Nonnull final Appendable sb) throws IOException {
        sb.append(getDependencyString(
                getPath(dep),
                dep.getProvidedNamespaces(),
                dep.getRequiredNamespaces()));
        sb.append("\n");
    }

    @Override
    public void render(@Nonnull Appendable sb) throws RenderException {
        try {
            String delim = "";
            for (ClosureSourceFile dep : dependencies) {
                sb.append(delim);
                renderDependency(dep, sb);
                delim = "\n";
            }
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }
}
