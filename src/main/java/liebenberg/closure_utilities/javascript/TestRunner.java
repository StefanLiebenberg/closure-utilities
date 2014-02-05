package liebenberg.closure_utilities.javascript;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.ContextFactory;
import com.google.javascript.rhino.head.ScriptableObject;
import com.google.javascript.rhino.head.tools.shell.Global;
import liebenberg.closure_utilities.internal.DependencyCalculator;
import liebenberg.closure_utilities.rhino.Console;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Collection;
import java.util.List;


public class TestRunner {

    public class TestException extends Exception {
        public TestException() {}
    }

    private static final Console console = new Console();

    private final ClosureDependencyParserInterface parser;

    private final Collection<File> sources;

    private final ImmutableList<ClosureSourceFileBase> dependencies;

    private final DependencyCalculator<ClosureSourceFileBase> calculator;

    private final File testFile;

    private File baseFile;

    public TestRunner(@Nonnull final File t,
                      @Nonnull final Collection<File> sourceDirectories)
            throws IOException {
        parser = new ClosureDependencyParserInterface();
        sources = FS.find(sourceDirectories, "js");

        ImmutableList.Builder<ClosureSourceFileBase> builder =
                new ImmutableList.Builder<>();
        ClosureSourceFileBase dep;
        FileReader reader;
        for (File sourceFile : sources) {
            dep = new ClosureSourceFileBase(sourceFile);
            reader = new FileReader(sourceFile);
            parser.parse(dep, reader);
            reader.close();
            if (dep.getIsBaseFile()) {
                baseFile = sourceFile;
            }
            builder.add(dep);
        }
        dependencies = builder.build();
        calculator = new DependencyCalculator<>(dependencies);
        testFile = t;
    }


    public void run() throws Exception {
        final Context context = new ContextFactory().enterContext();
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_3);

        //scope = context.initStandardObjects();
        final Global scope = new Global(context);
        ScriptableObject.putProperty(scope, "console",
                Context.javaToJS(console, scope));
        final InputStream inputStream =
                getClass().getResourceAsStream("/env.rhino.js");
        final InputStreamReader inputStreamReader =
                new InputStreamReader(inputStream);
        context.evaluateReader(scope, inputStreamReader, "/env/rhino.js", 0, null);


        Reader fileReader;
        fileReader = new FileReader(testFile);
        ClosureSourceFileBase testDep = new ClosureSourceFileBase(testFile);
        parser.parse(testDep, fileReader);
        fileReader.close();

        File depFile;
        fileReader = new FileReader(baseFile);
        context.evaluateReader(scope, fileReader, baseFile.getPath(), 0, null);
        fileReader.close();

        List<String> list = Lists.newArrayList(testDep.getRequiredNamespaces());
        for (ClosureSourceFileBase dep : calculator.getDependencyList(list)) {
            depFile = new File(dep.getSourceLocation());
            fileReader = new FileReader(depFile);
            context.evaluateReader(scope, fileReader, depFile.getPath(),
                    0, null);
            fileReader.close();
        }

        fileReader = new FileReader(testFile);
        context.evaluateReader(scope, fileReader, testFile.getPath(), 0,
                null);
        fileReader.close();
        context.evaluateString(scope, "window.onload()", "inline", 0, null);
        context.evaluateString(scope, "Envjs.wait()", "inline", 0, null);
        Boolean isSuccess = (Boolean) context.evaluateString(scope,
                "G_testRunner.isSuccess()", "inline", 0, null);
        Context.exit();

        if (!isSuccess) {
            throw new TestException();
        }

    }
}
