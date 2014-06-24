package slieb.closure.html.models;

import slieb.soy.annotations.Soy;

import static java.lang.String.format;

@Soy(useOriginalToString = true)
public class Stylesheet {
    private String source;

    public Stylesheet(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return format("<link rel=\"stylesheet\" href=\"%s\" />", source);
    }
}
