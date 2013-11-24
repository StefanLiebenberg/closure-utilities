package liebenberg.closure_utilities.render;

import liebenberg.closure_utilities.javascript.ClosureSourceFile;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Paths;
import java.util.Collection;

public class DependencyFileRenderer
        extends AbstractRenderer
        implements IRenderer {

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

    protected void renderDependencyPath(@Nonnull final ClosureSourceFile dep,
                                        @Nonnull final StringBuffer sb) {
        final String path = dep.getSourceLocation().getPath();
        if (basePath != null) {
            final String relative = FS.getRelativePath(
                    Paths.get(path),
                    Paths.get(basePath)).toString();
            sb.append(relative);
        } else {
            // this must be changed.
            sb.append(path);
        }
    }

    protected void renderNamespaceArray(
            @Nonnull final Collection<String> namespaces,
            @Nonnull final StringBuffer sb) {
        sb.append("[");
        String delim = "";
        for (String provideNs : namespaces) {
            sb.append(delim).append("'").append(provideNs).append("'");
            delim = ", ";
        }
        sb.append("]");
    }

    protected void renderDependency(
            @Nonnull final ClosureSourceFile dep,
            @Nonnull final StringBuffer sb) {
        sb.append("goog.addDependency('");
        renderDependencyPath(dep, sb);
        sb.append("', ");
        renderNamespaceArray(dep.getProvidedNamespaces(), sb);
        sb.append(", ");
        renderNamespaceArray(dep.getRequiredNamespaces(), sb);
        sb.append(");");
    }


    @Override
    public void render(@Nonnull StringBuffer sb) throws RenderException {
        String delim = "";
        for (ClosureSourceFile dep : dependencies) {
            sb.append(delim);
            renderDependency(dep, sb);
            delim = "\n";
        }
    }


}
