package slieb.closure.html.models;

import slieb.soy.annotations.Soy;

@Soy(useOriginalToString = true)
public class Script {

    private String source;

    public Script(String source) {
        this.source = source;
    }

    @Soy.Method("Source")
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return String.format("<script type=\"text/javascript\" src=\"%s\"></script>", source);
    }
}
