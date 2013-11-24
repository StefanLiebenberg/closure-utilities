package liebenberg.closure_utilities.internal;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class AbstractBuildTest
        <A extends AbstractBuilder<B, C>, B, C>
        extends AbstractApplicationTest {

    public enum Flavour {
        /**
         * A very basic build.
         */
        BASIC,

        /**
         * A utility library.
         */
        LIBRARY,

        /**
         * A fully fledged application.
         */
        APPLICATION
    }

    protected final A builder;

    protected final Class<B> buildOptionsClass;

    protected B builderOptions;

    protected AbstractBuildTest(@Nonnull Class<A> builderClass,
                                @Nonnull Class<B> buildOptionsClass)
            throws InstantiationException, IllegalAccessException {
        builder = builderClass.newInstance();
        this.buildOptionsClass = buildOptionsClass;
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        builderOptions = buildOptionsClass.newInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        builderOptions = null;
        super.tearDown();
    }

    @Nonnull
    protected HashSet<File> getGssSourceDirectories() {
        return Sets.newHashSet(getApplicationDirectory("src/gss/"));
    }

    @Nonnull
    protected ArrayList<String> getGssEntryPoints(
            @Nonnull Flavour flavour) {
        return Lists.newArrayList(Lists.newArrayList("company-import"));
    }

    @Nonnull
    protected HashSet<File> getSoySourceDirectories() {
        return Sets.newHashSet(getApplicationDirectory("src/soy"));
    }

    @Nonnull
    protected ArrayList<String> getJavascriptEntryPoints(
            @Nonnull Flavour flavour) {
        switch (flavour) {
            case BASIC:
                return Lists.newArrayList("company.pack");
            case LIBRARY:
                return Lists.newArrayList("company.pack");
            case APPLICATION:
                return Lists.newArrayList("company.pack");
            default:
                return new ArrayList<>();
        }
    }

    @Nonnull
    protected HashSet<File> getJavascriptSourceDirectories() {
        return Sets.newHashSet(getApplicationDirectory("src/javascript"));
    }


}
