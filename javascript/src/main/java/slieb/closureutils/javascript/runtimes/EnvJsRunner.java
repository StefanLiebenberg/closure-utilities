package slieb.closureutils.javascript.runtimes;


import java.io.IOException;

public class EnvJsRunner extends AbstractRunner {

    private static final String ENV_RHINO = "/scripts/rhino/env.rhino.js";

    private static final String LOAD_RHINO = "/scripts/rhino/load.rhino.js";

    private static final String WAIT_COMMAND = "Envjs.wait()";

    public void initialize() {
        super.initialize();
        try {
            evaluateClassResource(ENV_RHINO);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public void doLoad() throws IOException {
        evaluateClassResource(LOAD_RHINO);
        doWait();
    }

    public void doWait() {
        evaluateString(WAIT_COMMAND);
    }

    @Override
    public void close() {
        doWait();
        super.close();
    }


}
