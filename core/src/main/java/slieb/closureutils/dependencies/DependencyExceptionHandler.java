package slieb.closureutils.dependencies;


import java.util.List;

public class DependencyExceptionHandler {

    /**
     * @param namespace The namespace that is not being provided.
     * @param parents   A list of parents for this namespace.
     * @return The nothing provides exception.
     */
    public static DependencyException nothingProvides(String namespace, List<DependencyNode> parents) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nothing provides '").append(namespace).append("'.\n");

        DependencyNode first = parents.get(0);
        if (first != null) {
            stringBuilder.append("Required in ").append(first.getResource().getUri().toString()).append("\n");
        }

        return new DependencyException(stringBuilder.toString());
    }


    /**
     * @param namespace The namespace where this circular exception is found.
     * @param node      The node where this circular exception is found.
     * @param parents   The parents above that require this node, a segment of which is part of the circular exception.
     * @return A instance of a dependency exception to indciate the circular exception error.
     */
    public static DependencyException circularDependency(String namespace, DependencyNode node, List<DependencyNode> parents) {
        StringBuilder sb = new StringBuilder();
        sb.append("Circular dependency found in when trying to locate '").append(namespace).append("'").append("\n");
        sb.append("  Parents:\n");
        for (DependencyNode parentNode : parents) {
            sb.append("  -  ").append(parentNode.getResource().getUri()).append("\n");
        }
        return new DependencyException(sb.toString());
    }

}
