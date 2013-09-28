package com.github.stefanliebenberg.compiler.soy;


import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Nel
 * @author Stefan Liebenberg
 */
public class SoyDelegateOptimizer implements CompilerPass {

    private Compiler compiler;

    private final DelegateFinder delegateFinder = new DelegateFinder();

    private final DelegateOptimiser delegateOptimizer = new DelegateOptimiser();

    public SoyDelegateOptimizer(final Compiler compiler) {
        this.compiler = compiler;
    }

    private HashMap<String, Double> dataMap;

    protected void reset() {
        dataMap = new HashMap<String, Double>();
    }

    @Override
    public void process(Node externs, Node root) {
        reset();
        NodeTraversal.traverse(compiler, root, delegateFinder);
        NodeTraversal.traverse(compiler, root, delegateOptimizer);
    }

    private class DelegateFinder extends NodeTraversal
            .AbstractPostOrderCallback {

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            final int type = n.getType();
            switch (type) {
                case Token.CALL:
                    String qualifiedName = n.getFirstChild()
                            .getQualifiedName();
                    if ("soy.$$registerDelegateFn".equals(qualifiedName)) {
                        String name = n.getFirstChild().getNext()
                                .getLastChild().getString();
                        String variant = n.getChildAtIndex(1).getNext()
                                .getString();
                        Double priority = n.getChildAtIndex(3).getDouble();
                        String key = name + ":" + variant;
                        Double currentPriorityInMap = dataMap.get(key);
                        if (currentPriorityInMap == null ||
                                currentPriorityInMap < priority) {
                            dataMap.put(name + ":" + variant, priority);
                        }
                    }
            }
        }
    }

    public class DelegateOptimiser extends NodeTraversal
            .AbstractPostOrderCallback {

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            final int type = n.getType();
            if (type == Token.CALL) {
                String qualifiedName = n.getFirstChild().getQualifiedName();
                if ("soy.$$registerDelegateFn".equals(qualifiedName)) {
                    String name = n.getFirstChild().getNext().getLastChild()
                            .getString();
                    String variant = n.getChildAtIndex(1).getNext()
                            .getString();
                    Double priority = n.getChildAtIndex(3).getDouble();
                    String key = name + ":" + variant;
                    Double highestPriorityInMap = dataMap.get(key);
                    if (priority < highestPriorityInMap) {
                        System.out.println("remove node");
                        parent.detachFromParent();
                        compiler.reportCodeChange();
                    }
                }
            }
        }
    }


    public static void addToCompile(Compiler compiler,
                                    CompilerOptions compilerOptions) {
        Multimap<CustomPassExecutionTime, CompilerPass> customPasses =
                compilerOptions.customPasses;
        if (customPasses == null) {
            customPasses = LinkedListMultimap.create();
        }
        Map<String, Double> delegatesMap = new HashMap<String, Double>();
        customPasses.put(CustomPassExecutionTime.BEFORE_CHECKS,
                new SoyDelegateOptimizer(compiler));
        compilerOptions.setCustomPasses(customPasses);
    }
}
