package slieb.closure.internal;


import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CustomPassExecutionTime;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.Function;
import com.google.javascript.rhino.head.Scriptable;
import com.google.javascript.rhino.head.tools.shell.Global;
import slieb.closure.rhino.EnvJsRunner;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;

public class JavascriptPluginCompilerPass implements CompilerPass {

    public class FunctionTraverse implements NodeTraversal.Callback {

        private final Function traverseFunction;

        private final Context context;

        private final Global scope;

        private final Scriptable thisObject;

        public FunctionTraverse(Context context, Global scope,
                                Scriptable thisObject,
                                Function traverseFunction) {
            this.context = context;
            this.scope = scope;
            this.thisObject = thisObject;
            this.traverseFunction = traverseFunction;
        }


        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal,
                                      Node n,
                                      Node parent) {
            return true;
        }

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            Object[] args = new Object[]{
                    Context.javaToJS(t, scope),
                    Context.javaToJS(n, scope),
                    Context.javaToJS(parent, scope)
            };
            traverseFunction.call(context, scope, thisObject, args);
        }
    }


    public class Plugin {
        private final EnvJsRunner runner;

        private final Compiler compiler;

        public Plugin(Compiler compiler, EnvJsRunner runner) {
            this.compiler = compiler;
            this.runner = runner;
        }

        public void traverse(Node root, Function function) {
            FunctionTraverse traverseFunction =
                    new FunctionTraverse(runner.getContext(),
                            runner.getScope(), null, function);
            NodeTraversal.traverse(compiler, root, traverseFunction);
        }
    }

    private final String content;

    private final Compiler compiler;

    public JavascriptPluginCompilerPass(Reader inputReader,
                                        Compiler compiler) throws IOException {
        this.content = IOUtils.toString(inputReader);
        this.compiler = compiler;
    }

    public void configurePasses(Multimap<CustomPassExecutionTime,
            CompilerPass> customPasses) {
        customPasses.put(CustomPassExecutionTime.BEFORE_CHECKS, this);
    }


    @Override
    public void process(Node externs, Node root) {
        EnvJsRunner runner = new EnvJsRunner();
        runner.initialize();
        Plugin plugin = new Plugin(compiler, runner);
        runner.putJavaObject("plugin", plugin);
        runner.putJavaObject("compiler", compiler);
        runner.evaluateString(content);
        Global scope = runner.getScope();
        runner.callFunction("visit", null,
                runner.javaToJS(externs),
                Context.javaToJS(root, scope));
        runner.close();
    }

}
