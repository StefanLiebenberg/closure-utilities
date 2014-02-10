package slieb.closure.javascript;


import com.google.javascript.rhino.head.BaseFunction;
import com.google.javascript.rhino.head.Callable;
import com.google.javascript.rhino.head.Context;
import com.google.javascript.rhino.head.Scriptable;

import java.io.File;
import java.io.FileReader;

public class ClosureImporter extends BaseFunction implements Callable {

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj,
                       Object[] args) {
        String filename = args[0].toString();
        File targetFile = new File(filename);
        try {
            if (targetFile.exists()) {
                FileReader reader = new FileReader(targetFile);
                cx.evaluateReader(scope, reader, targetFile.getPath(), 0, null);
                reader.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            cx.evaluateString(scope, "console.warn('Error while importing " +
                    targetFile.getPath() + "')", "inline", 1, null);
            e.printStackTrace();
            throw new Error(e.getMessage(), e);
        }
    }

    public int getArity() {
        return 1;
    }
}
