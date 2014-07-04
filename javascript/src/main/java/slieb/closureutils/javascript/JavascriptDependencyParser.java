package slieb.closureutils.javascript;


import com.google.javascript.rhino.head.Node;
import com.google.javascript.rhino.head.Parser;
import com.google.javascript.rhino.head.ast.*;
import slieb.closureutils.dependencies.DependencyNode;
import slieb.closureutils.dependencies.DependencyParser;
import slieb.closureutils.resources.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

public class JavascriptDependencyParser implements DependencyParser {

    public static final String IS_BASE = "isBase";

    private boolean isName(
            @Nonnull final AstNode node) {
        return node instanceof Name;
    }

    private boolean isName(
            @Nonnull final AstNode node,
            @Nonnull final String name) {
        return isName(node) && ((Name) node).getIdentifier().equals(name);
    }

    private boolean isPropertyGet(
            @Nonnull final AstNode node) {
        return node instanceof PropertyGet;
    }

    private boolean hasNamedProperty(
            @Nonnull final PropertyGet node,
            @Nonnull final String left,
            @Nonnull final String right) {
        return isName(node.getLeft(), left) && isName(node.getRight(), right);
    }


    private void visit_assignment(
            @Nonnull final DependencyNode.Builder builder,
            @Nonnull final Assignment assignment) {
        final AstNode left = assignment.getLeft();
        if (isPropertyGet(left) &&
                hasNamedProperty((PropertyGet) left, "goog", "base")) {
            builder.setFlag(IS_BASE, true);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T extends AstNode> T getArgument(
            @Nonnull final FunctionCall functionCall,
            @Nonnull final Integer index,
            @Nonnull final Class<T> tClass) {
        final AstNode node = functionCall.getArguments().get(index);
        if (tClass.isInstance(node)) {

            return (T) node;
        } else {
            return null;
        }
    }

    @Nullable
    private String getStringArgumentValue(
            @Nonnull final FunctionCall functionCall,
            @Nonnull final Integer index) {
        final StringLiteral stringNode =
                getArgument(functionCall, index, StringLiteral.class);
        if (stringNode != null) {
            return stringNode.getValue();
        } else {
            return null;
        }

    }


    private void visit_PropertyGet_googScan(
            @Nonnull final DependencyNode.Builder builder,
            @Nonnull final FunctionCall funcCall,
            @Nonnull final PropertyGet propertyGet) {
        if (isName(propertyGet.getLeft(), "goog")) {
            final AstNode right = propertyGet.getRight();
            if (isName(right, "provide")) {
                builder.provides(getStringArgumentValue(funcCall, 0));
            } else if (isName(right, "require")) {
                builder.requires(getStringArgumentValue(funcCall, 0));
            }
        }
    }

    private void visit_FunctionCall(
            @Nonnull final DependencyNode.Builder builder,
            @Nonnull final FunctionCall functionCall) {
        final AstNode target = functionCall.getTarget();
        if (isPropertyGet(target)) {
            visit_PropertyGet_googScan(builder, functionCall,
                    (PropertyGet) target);
        }
    }


    private void visit(
            @Nonnull final DependencyNode.Builder builder,
            @Nullable final Node node) {
        if (node == null) {
            return;
        }
        if (node instanceof Scope) {
            for (AstNode statement : ((Scope) node).getStatements()) {
                visit(builder, statement);
            }
            visit(builder, node.getNext());
            return;
        }

        if (node instanceof FunctionCall) {
            visit_FunctionCall(builder, (FunctionCall) node);
            visit(builder, node.getNext());
            return;
        }

        if (node instanceof IfStatement) {
            visit(builder, ((IfStatement) node).getThenPart());
            visit(builder, ((IfStatement) node).getElsePart());
            visit(builder, node.getNext());
            return;
        }


//        if (!isBase) {
        // why are we going in here?
        if (node instanceof ExpressionStatement) {

            visit(builder, ((ExpressionStatement) node).getExpression());

            visit(builder, node.getNext());
            return;
        }

        if (node instanceof Assignment) {

            visit_assignment(builder, (Assignment) node);

            visit(builder, node.getNext());
            return;
        }
//        }
        visit(builder, node.getNext());

    }


    public void parse(@Nonnull final DependencyNode.Builder builder,
                      @Nonnull final Reader content)
            throws IOException {
        final URI sourceLocation = builder.resource.getUri();
        final Parser parser = new Parser();
        final String srcString = sourceLocation.toString();
        final AstRoot astRoot = parser.parse(content, srcString, 1);
        visit(builder, astRoot.getFirstChild());
    }


    public void parse(@Nonnull final DependencyNode.Builder builder,
                      @Nonnull final String content)
            throws IOException {
        final URI sourceLocation = builder.resource.getUri();
        final String srcString = sourceLocation.toString();
        final Parser parser = new Parser();
        final AstRoot astRoot = parser.parse(content, srcString, 1);
        visit(builder, astRoot.getFirstChild());
    }

    @Override
    public DependencyNode parse(Resource resource) {
        DependencyNode.Builder builder = new DependencyNode.Builder(resource);
        try (Reader reader = resource.getReader()) {
            parse(builder, reader);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        return builder.build();
    }
}