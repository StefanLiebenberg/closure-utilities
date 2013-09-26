package com.github.stefanliebenberg.javascript;

import com.github.stefanliebenberg.internal.IDependencyParser;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

public class ClosureDependencyParser implements
        IDependencyParser<ClosureSourceFile> {

    private boolean isName(final AstNode node) {
        return node instanceof Name;
    }

    private boolean isName(final AstNode node, final String name) {
        return isName(node) && ((Name) node).getIdentifier().equals
                (name);
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

    public <T extends AstNode> T getArgument(final FunctionCall functionCall,
                                             final Integer index,
                                             final Class<T> tClass) {
        AstNode node = functionCall.getArguments().get(index);
        if (tClass.isInstance(node)) {
            return (T) node;
        } else {
            return null;
        }
    }

    public String getStringArgumentValue(final FunctionCall functionCall,
                                         final Integer index) {
        return getArgument(functionCall, index, StringLiteral.class).getValue();
    }


    public void visit_PropertyGet_googScan(final ClosureSourceFile
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

    public void visit_FunctionCall(final ClosureSourceFile closureSourceFile,
                                   final FunctionCall functionCall) {
        final AstNode target = functionCall.getTarget();
        if (isPropertyGet(target)) {
            visit_PropertyGet_googScan(closureSourceFile, functionCall,
                    (PropertyGet) target);
        }
    }

    private void visit(ClosureSourceFile dependency, Node node) {
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

    @Override
    public void parse(ClosureSourceFile dependency, Reader content) throws
            IOException {
        URI sourceLocation = dependency.getSourceLocation();
        Parser parser = new Parser();
        AstRoot astRoot = parser.parse(content, sourceLocation.toString(), 1);
        visit(dependency, astRoot.getFirstChild());
    }

    @Override
    public void parse(ClosureSourceFile dependency, String content) throws
            IOException {
        URI sourceLocation = dependency.getSourceLocation();
        Parser parser = new Parser();
        AstRoot astRoot = parser.parse(content, sourceLocation.toString(), 1);
        visit(dependency, astRoot.getFirstChild());
    }
}
