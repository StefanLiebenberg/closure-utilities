package com.github.stefanliebenberg.internal;

import java.io.IOException;
import java.io.Reader;

public interface IDependencyParser<T extends BaseSourceFile> {
    public void parse(final T dependency, final Reader content) throws IOException;

    public void parse(final T dependency, final String content) throws IOException;
}
