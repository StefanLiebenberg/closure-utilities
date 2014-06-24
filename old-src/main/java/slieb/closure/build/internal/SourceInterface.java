package slieb.closure.build.internal;


import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public interface SourceInterface {

    @Nonnull
    public Reader getReader() throws IOException;

    @Nonnull
    public File getFileLocation();

    @Nonnull
    public Collection<String> getRequires();

    @Nonnull
    public Collection<String> getProvides();

}
