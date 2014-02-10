package slieb.closure.internal;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.closure.build.internal.DependencyBuilder;
import slieb.closure.build.internal.SourceFileBase;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DependencyBuilderTest {

    private final DependencyBuilder<SourceFileBase> dependencyBuilder
            = new DependencyBuilder<>();

    private DependencyOptions<SourceFileBase> dependencyBuildOptions;

    @Before
    public void setUp() throws Exception {
        dependencyBuildOptions = new DependencyOptions<>();
    }

    @After
    public void tearDown() throws Exception {
        dependencyBuildOptions = null;
    }

    private SourceFileBase createSourceFile(final String path,
                                            final String[] provides,
                                            final String[] requires)
            throws Exception {
        SourceFileBase sourceFileBase = new SourceFileBase(path);
        for (String provide : provides) {
            sourceFileBase.addProvideNamespace(provide);
        }
        for (String require : requires) {
            sourceFileBase.addRequireNamespace(require);
        }
        return sourceFileBase;
    }

    @Test
    public void testBuild() throws Exception {
        Collection<SourceFileBase> sourceFileBases = new HashSet<>();
        SourceFileBase fileA = createSourceFile("/some/pathA.ext",
                new String[]{
                        "A"
                },
                new String[]{});
        sourceFileBases.add(fileA);
        SourceFileBase fileB = createSourceFile("/some/pathB.ext",
                new String[]{
                        "B"
                },
                new String[]{
                        "A"
                });
        sourceFileBases.add(fileB);
        dependencyBuildOptions.setSourceFiles(sourceFileBases);
        dependencyBuildOptions.setEntryPoints(Lists.newArrayList("B"));
        List<SourceFileBase> resolved =
                dependencyBuilder.build(dependencyBuildOptions);
        Assert.assertEquals(resolved.get(0), fileA);
        Assert.assertEquals(resolved.get(1), fileB);
    }
}
