package liebenberg.closure_utilities.javascript;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import liebenberg.closure_utilities.internal.DependencyCalculator;
import liebenberg.closure_utilities.rhino.EnvJsRunner;
import liebenberg.closure_utilities.utilities.FS;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;


public class TestRunner {

    public class TestException extends Exception {
        public TestException() {}
    }


    private final ClosureDependencyParserInterface parser;

    private final Collection<File> sources;

    private final ImmutableList<ClosureSourceFile> dependencies;

    private final DependencyCalculator<ClosureSourceFile> calculator;

    private final File testFile;

    private File baseFile;

    private final EnvJsRunner envJsRunner;

    public TestRunner(@Nonnull final File t,
                      @Nonnull final Collection<File> sourceDirectories)
            throws IOException {

        envJsRunner = new EnvJsRunner();
        envJsRunner.initialize();

        parser = new ClosureDependencyParserInterface();
        sources = FS.find(sourceDirectories, "js");

        ImmutableList.Builder<ClosureSourceFile> builder =
                new ImmutableList.Builder<>();
        ClosureSourceFile dep;
        FileReader reader;
        for (File sourceFile : sources) {
            dep = new ClosureSourceFile(sourceFile);
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


        Reader fileReader;
        fileReader = new FileReader(testFile);
        ClosureSourceFile testDep = new ClosureSourceFile(testFile);
        parser.parse(testDep, fileReader);
        fileReader.close();

        envJsRunner.evaluateFile(baseFile);

        List<String> list = Lists.newArrayList(testDep.getRequiredNamespaces());
        for (ClosureSourceFile dep : calculator.getDependencyList(list)) {
            envJsRunner.evaluateFile(new File(dep.getSourceLocation()));
        }

        envJsRunner.evaluateFile(testFile);
        envJsRunner.doLoad();
        Boolean isSuccess =
                envJsRunner.getBoolean("G_testRunner.isSuccess()");
        envJsRunner.doClose();

        if (!isSuccess) {
            throw new TestException();
        }

    }
}
