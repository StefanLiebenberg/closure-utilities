package liebenberg.closure_utilities.rhino;


import com.google.javascript.rhino.head.*;
import com.google.javascript.rhino.head.tools.shell.Global;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.URL;

public abstract class AbstractRunner implements RunnerInterface {

    protected final Context context;

    protected final Global scope;

    public AbstractRunner() {
        context = new ContextFactory().enterContext();
        scope = new Global(context);
    }

    @Override
    public void initialize() {
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_3);
    }


    public Context getContext() {
        return context;
    }

    public Global getScope() {
        return scope;
    }

    @Override
    public void close() {
        Context.exit();
    }

    @Nullable
    @Override
    public Object evaluateReader(@Nonnull final Reader reader,
                                 @Nonnull final String path)
            throws IOException {
        return context.evaluateReader(scope, reader, path, 0, null);
    }

    @Nullable
    public Object evaluateURI(@Nonnull final URI uri) throws IOException {
        return evaluateURL(uri.toURL());
    }

    @Nullable
    public Object evaluateURL(@Nonnull final URL url)
            throws IOException {
        InputStream stream = url.openStream();
        InputStreamReader reader = new InputStreamReader(stream);
        Object result = evaluateReader(reader, url.getPath());
        reader.close();
        stream.close();
        return result;
    }

    @Nullable
    public Object evaluateString(@Nonnull final String content) {
        return context.evaluateString(scope, content, "inline", 0, null);
    }

    @Nullable
    public Object evaluateStream(@Nonnull final InputStream stream,
                                 @Nonnull final String path)
            throws IOException {
        final InputStreamReader reader = new InputStreamReader(stream);
        final Object result = evaluateReader(reader, path);
        reader.close();
        return result;
    }

    @Nullable
    public Object evaluateResource(@Nonnull final String path)
            throws IOException {
        final InputStream stream = getClass().getResourceAsStream(path);
        final Object result = evaluateStream(stream, path);
        stream.close();
        return result;
    }

    @Override
    @Nullable
    public Object evaluateFile(@Nonnull final File file) throws IOException {
        final FileReader fileReader = new FileReader(file);
        final Object result = evaluateReader(fileReader, file.getPath());
        fileReader.close();
        return result;
    }

    public void putObject(@Nonnull String name,
                          @Nonnull Object object) {
        ScriptableObject.putProperty(scope, name, object);
    }

    public void putJavaObject(@Nonnull String name,
                              @Nonnull Object object) {
        putObject(name, Context.javaToJS(object, scope));
    }

    public Function getFunction(@Nonnull String name) {
        return (Function) scope.get(name, scope);
    }

    public Object callFunction(@Nonnull String name, Scriptable thisObject,
                               Object... args) {
        Function func = getFunction(name);
        return func.call(context, scope, thisObject, args);
    }

    public Object javaToJS(Object object) {
        return Context.javaToJS(object, scope);
    }

    public Object jsToJava(Object object, Class<?> classType) {
        return Context.jsToJava(object, classType);
    }

    @Override
    @Nullable
    public String getString(@Nonnull String command) {
        return (String) evaluateString(command);
    }

    @Override
    @Nullable
    public Boolean getBoolean(@Nonnull String command) {
        return (Boolean) evaluateString(command);
    }

    @Override
    @Nullable
    public Number getNumber(@Nonnull String command) {
        return (Number) evaluateString(command);
    }
}
