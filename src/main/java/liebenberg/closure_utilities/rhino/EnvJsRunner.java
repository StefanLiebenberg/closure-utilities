package liebenberg.closure_utilities.rhino;


import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.ContextFactory;
import com.google.javascript.rhino.head.ScriptableObject;
import com.google.javascript.rhino.head.tools.shell.Global;

import javax.annotation.Nonnull;
import java.io.*;

public class EnvJsRunner {

    private static final Console console = new Console();
    private final Context context;
    private final Global scope;

    private static final String ENV_RHINO_PATH = "/env.rhino.js";

    public EnvJsRunner() {
        context = new ContextFactory().enterContext();
        scope = new Global(context);
    }

    public void initialize() throws IOException {
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_3);
        Object jsConsole = Context.javaToJS(console, scope);
        ScriptableObject.putProperty(scope, "console", jsConsole);
        evaluateResource(ENV_RHINO_PATH);
    }

    public Object evaluateReader(@Nonnull final Reader reader,
                                 @Nonnull final String path)
            throws IOException {
        return context.evaluateReader(scope, reader, path, 0, null);
    }

    public Object evaluateString(@Nonnull final String content) {
        return context.evaluateString(scope, content, "inline", 0, null);
    }

    public Object evaluateStream(@Nonnull final InputStream stream,
                                 @Nonnull final String path)
            throws IOException {
        final InputStreamReader reader = new InputStreamReader(stream);
        final Object result = evaluateReader(reader, path);
        reader.close();
        return result;
    }

    public Object evaluateResource(@Nonnull final String path)
            throws IOException {
        final InputStream stream = getClass().getResourceAsStream(path);
        final Object result = evaluateStream(stream, path);
        stream.close();
        return result;
    }

    public Object evaluateFile(@Nonnull final File file) throws IOException {
        final FileReader fileReader = new FileReader(file);
        final Object result = evaluateReader(fileReader, file.getPath());
        fileReader.close();
        return result;
    }

    public void doLoad() {
        evaluateString("window.onload()");
        doWait();
    }

    public void doWait() {
        evaluateString("Envjs.wait()");
    }

    public void doClose() {
        doWait();
        Context.exit();
    }

    public String getString(String command) {
        return (String) evaluateString(command);
    }

    public Boolean getBoolean(String command) {
        return (Boolean) evaluateString(command);
    }
}
