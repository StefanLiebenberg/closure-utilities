package com.github.stefanliebenberg.javascript;

import com.github.stefanliebenberg.internal.IDependencyParser;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

public class ClosureDependencyParser implements
        IDependencyParser<ClosureSourceFile> {

    @Override
    public void parse(@Nonnull final ClosureSourceFile dependency,
                      @Nonnull final Reader content)
            throws IOException {
        final URI sourceLocation = dependency.getSourceLocation();
        final Parser parser = new Parser();
        final String srcString = sourceLocation.toString();
        final AstRoot astRoot = parser.parse(content, srcString, 1);
        visit(dependency, astRoot.getFirstChild());
    }

    @Override
    public void parse(@Nonnull final ClosureSourceFile dependency,
                      @Nonnull final String content)
            throws IOException {
        final URI sourceLocation = dependency.getSourceLocation();
        final String srcString = sourceLocation.toString();
        final Parser parser = new Parser();
        final AstRoot astRoot = parser.parse(content, srcString, 1);
        visit(dependency, astRoot.getFirstChild());
    }

    private boolean isName(final AstNode node) {
        return node instanceof Name;
    }

    private boolean isName(final AstNode node,
                           final String name) {
        return isName(node) && ((Name) node).getIdentifier().equals(name);
    }

    private boolean isPropertyGet(final AstNode node) {
        return node instanceof PropertyGet;
    }

    private boolean hasNamedProperty(final PropertyGet node,
                                     final String left,
                                     final String right) {
        return isName(node.getLeft(), left) && isName(node.getRight(), right);
    }


    private void visit_assignment(final ClosureSourceFile closureSourceFile,
                                  final Assignment assignment) {
        final AstNode left = assignment.getLeft();
        if (isPropertyGet(left) &&
                hasNamedProperty((PropertyGet) left, "goog", "base")) {
            closureSourceFile.setIsBaseFile(true);
        }
    }

    private <T extends AstNode> T getArgument(final FunctionCall functionCall,
                                              final Integer index,
                                              final Class<T> tClass) {
        final AstNode node = functionCall.getArguments().get(index);
        if (tClass.isInstance(node)) {
            return (T) node;
        } else {
            return null;
        }
    }

    private String getStringArgumentValue(final FunctionCall functionCall,
                                          final Integer index) {
        return getArgument(functionCall, index, StringLiteral.class).getValue();
    }


    private void visit_PropertyGet_googScan(final ClosureSourceFile
                                                    closureSourceFile,
                                            final FunctionCall funcCall,
                                            final PropertyGet propertyGet) {
        if (isName(propertyGet.getLeft(), "goog")) {
            final AstNode right = propertyGet.getRight();
            if (isName(right, "provide")) {
                closureSourceFile.addProvideNamespace(
                        getStringArgumentValue(funcCall, 0));
            } else if (isName(right, "require")) {
                closureSourceFile.addRequireNamespace(
                        getStringArgumentValue(funcCall, 0));
            }
        }
    }

    private void visit_FunctionCall(final ClosureSourceFile closureSourceFile,
                                    final FunctionCall functionCall) {
        final AstNode target = functionCall.getTarget();
        if (isPropertyGet(target)) {
            visit_PropertyGet_googScan(closureSourceFile, functionCall,
                    (PropertyGet) target);
        }
    }

    private void visit(final ClosureSourceFile dependency,
                       final Node node) {
        if (node == null) {
            return;
        }

        if (node instanceof FunctionCall) {
            visit_FunctionCall(dependency, (FunctionCall) node);
            visit(dependency, node.getNext());
        }


        if (!dependency.getIsBaseFile()) {
            // why are we going in here?
            if (node instanceof ExpressionStatement) {
                visit(dependency, ((ExpressionStatement) node).getExpression());
                visit(dependency, node.getNext());
                return;
            }

            if (node instanceof Assignment) {
                visit_assignment(dependency, (Assignment) node);
                visit(dependency, node.getNext());
                return;
            }
        }
        visit(dependency, node.getNext());
    }
}
