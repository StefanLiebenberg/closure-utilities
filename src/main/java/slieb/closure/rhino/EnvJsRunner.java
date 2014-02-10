package slieb.closure.rhino;


import java.io.IOException;

public class EnvJsRunner extends AbstractRunner {

    private static final String ENV_RHINO_PATH = "/env.rhino.js";

    public void initialize() {
        super.initialize();
        try {
            evaluateResource(ENV_RHINO_PATH);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public void doLoad() {
        evaluateString("if (document.createEvent) { " +
                "var event = document.createEvent('Event'); " +
                "event.initEvent('DOMContentLoaded', false, false);" +
                "document.dispatchEvent(event, false);" +
                "event = document.createEvent('HTMLEvents'); " +
                "event.initEvent('load', false, false);" +
                "document.dispatchEvent(event, false); " +
                "}");
        evaluateString("try {" +
                "if (document === window.document) {" +
                "event = document.createEvent('HTMLEvents');" +
                "event.initEvent('load', false, false);" +
                "window.dispatchEvent(event, false);" +
                "}" +
                "} catch (e) {" +
                "console.log('window load event failed %s', e);" +
                "}");
        doWait();
    }

    public void doWait() {
        evaluateString("Envjs.wait()");
    }

    @Override
    public void close() {
        doWait();
        super.close();
    }


}
