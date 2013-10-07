package com.github.stefanliebenberg.internal;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PreProcessor {
    public String processString(String content);

    public void processStream(InputStream inputStream,
                                OutputStream outputStream)
            throws IOException;
}
