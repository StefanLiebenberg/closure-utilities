package slieb.closureutils.javascript.runtimes;


import com.google.javascript.rhino.head.BaseFunction;
import com.google.javascript.rhino.head.Callable;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.Scriptable;
import slieb.closureutils.resources.Resource;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Logger;

import static com.google.javascript.rhino.head.Context.javaToJS;

public class ClosureImporter extends BaseFunction implements Callable {

    private static final String IMPORT_FAILURE = "Failed to import %s. %s";

    private static final String IMPORT_MESSAGE = "Importing %s";

    private Logger LOGGER = Logger.getLogger(getClass().getName());

    private final Map<String, Resource> resourceMap;

    public ClosureImporter(Map<String, Resource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    private boolean importPath(final String pathName, final Context context,
                               final Scriptable scope) {
        LOGGER.finer(String.format(IMPORT_MESSAGE, pathName));
        Resource resource = resourceMap.get(pathName);
        if (resource != null) {
            try (Reader reader = resource.getReader()) {
                context.evaluateReader(scope, reader,
                        resource.getUri().getPath(), 0, null);
                return true;
            } catch (IOException exception) {
                String failMsg = String.format(IMPORT_FAILURE, pathName,
                        exception.getMessage());
                throw new RuntimeException(failMsg, exception);
            }
        } else {
            LOGGER.finer(String.format(IMPORT_FAILURE, pathName,
                    "File does not exist."));
        }
        return false;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj,
                       Object[] args) {
        return javaToJS(importPath(args[0].toString(), cx, scope), scope);
    }

    public int getArity() {
        return 1;
    }
}
