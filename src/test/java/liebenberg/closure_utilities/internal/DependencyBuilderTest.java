package liebenberg.closure_utilities.internal;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DependencyBuilderTest {

    private final DependencyBuilder<BaseSourceFile> dependencyBuilder
            = new DependencyBuilder<BaseSourceFile>();

    private DependencyOptions<BaseSourceFile> dependencyBuildOptions;

    @Before
    public void setUp() throws Exception {
        dependencyBuildOptions = new DependencyOptions<>();
    }

    @After
    public void tearDown() throws Exception {
        dependencyBuildOptions = null;
    }

    private BaseSourceFile createSourceFile(final String path,
                                            final String[] provides,
                                            final String[] requires)
            throws Exception {
        BaseSourceFile baseSourceFile = new BaseSourceFile(path);
        for (String provide : provides) {
            baseSourceFile.addProvideNamespace(provide);
        }
        for (String require : requires) {
            baseSourceFile.addRequireNamespace(require);
        }
        return baseSourceFile;
    }

    @Test
    public void testBuild() throws Exception {
        Collection<BaseSourceFile> baseSourceFiles = new
                HashSet<BaseSourceFile>();
        BaseSourceFile fileA = createSourceFile("/some/pathA.ext",
                new String[]{
                        "A"
                },
                new String[]{});
        baseSourceFiles.add(fileA);
        BaseSourceFile fileB = createSourceFile("/some/pathB.ext",
                new String[]{
                        "B"
                },
                new String[]{
                        "A"
                });
        baseSourceFiles.add(fileB);
        dependencyBuildOptions.setSourceFiles(baseSourceFiles);
        dependencyBuildOptions.setEntryPoints(Lists.newArrayList("B"));
        List<BaseSourceFile> resolved =
                dependencyBuilder.build(dependencyBuildOptions);
        Assert.assertEquals(resolved.get(0), fileA);
        Assert.assertEquals(resolved.get(1), fileB);
    }
}
