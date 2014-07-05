package slieb.closureutils.rendering;


import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.javascript.JavascriptDependencyTree;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

import static slieb.closureutils.tools.URIUtils.getRelativeURISafe;

/**
 * Produces the content of a deps.js file when given a JavascriptDependencyTree to render.
 */
public class DependencyFileRenderer extends AbstractRenderer<JavascriptDependencyTree> {

    private String getPath(@Nonnull final DependencyNode dep,
                           DependencyNode baseNode) {
        URI baseUri = baseNode.getResource().getUri();
        URI nodeURI = dep.getResource().getUri();
        return getRelativeURISafe(baseUri, nodeURI).toString();
    }

    protected void renderDependencyPath(Appendable sb,
                                        DependencyNode dep,
                                        DependencyNode baseNode)
            throws IOException {
        sb.append(getPath(dep, baseNode));
    }

    protected void renderNamespaceArray(
            @Nonnull final Appendable sb,
            @Nonnull final Collection<String> namespaces)
            throws IOException {
        sb.append("[");
        Iterator<String> iterator = namespaces.iterator();
        while (iterator.hasNext()) {
            sb.append("'").append(iterator.next()).append("'");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
    }

    private static final String STRING_FORMAT = "'%s'";

    private String getStringValue(String value) {
        return String.format(STRING_FORMAT, value);
    }

    protected void renderDependency(
            Appendable sb, DependencyNode dep, DependencyNode baseNode)
            throws IOException {
        sb.append("goog.addDependency(");
        sb.append(getStringValue(getPath(dep, baseNode)));
        sb.append(", ");
        renderNamespaceArray(sb, dep.getProvides());
        sb.append(", ");
        renderNamespaceArray(sb, dep.getRequires());
        sb.append(");");
    }

    @Override
    public void render(@Nonnull Appendable sb,
                       @Nonnull JavascriptDependencyTree tree)
            throws RenderException {
        try {
            DependencyNode baseNode = tree.getBaseNode();
            for (DependencyNode dep : tree.getDependencyNodes()) {
                renderDependency(sb, dep, baseNode);
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }
}
