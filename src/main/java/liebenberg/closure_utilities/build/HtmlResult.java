package liebenberg.closure_utilities.build;


import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;

@Immutable
public class HtmlResult {

    private final File generatedHtmlFile;

    public HtmlResult(@Nullable final File generatedHtmlFile) {
        this.generatedHtmlFile = generatedHtmlFile;
    }

    public File getGeneratedHtmlFile() {
        return generatedHtmlFile;
    }
}
