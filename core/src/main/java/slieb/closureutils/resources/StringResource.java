package slieb.closureutils.resources;


import java.io.Reader;
import java.io.StringReader;

public class StringResource implements Resource {
    private final String string;

    public StringResource(String string) {
        this.string = string;
    }

    @Override
    public Reader getReader() {
        return new StringReader(string);
    }
}
