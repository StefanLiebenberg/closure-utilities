package slieb.closure.javascript;


import com.google.javascript.rhino.head.BaseFunction;
import com.google.javascript.rhino.head.Callable;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.Scriptable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class ClosureImporter extends BaseFunction implements Callable {

    private static final String IMPORT_FAILURE = "Failed to import %s. %s";

    private static final String IMPORT_MESSAGE = "Importing %s";

    private Logger LOGGER = Logger.getLogger(getClass().getName());

    private boolean importFile(final String pathName,
                               final Context context,
                               final Scriptable scope) {
        LOGGER.finer(String.format(IMPORT_MESSAGE, pathName));

        final File targetFile = new File(pathName);
        if (targetFile.exists()) {
            try {
                FileReader reader = new FileReader(targetFile);
                context.evaluateReader(scope, reader, pathName, 0, null);
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
        return Context.javaToJS(importFile(args[0].toString(), cx, scope),
                scope);
    }

    public int getArity() {
        return 1;
    }
}
