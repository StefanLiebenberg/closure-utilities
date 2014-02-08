package liebenberg.closure_utilities.internal;

import junit.framework.Assert;
import liebenberg.closure_utilities.build.SourceFileBase;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class DependencyCalculatorTest {

    @Test
    public void testBaseDependencyCalculator() throws Exception {

        Collection<SourceFileBase> dependencies = new HashSet<SourceFileBase>();

        SourceFileBase dependencyA = new SourceFileBase("/a.ext");
        dependencyA.addProvideNamespace("test.A");

        SourceFileBase dependencyB = new SourceFileBase("/b.ext");
        dependencyB.addProvideNamespace("test.B");
        dependencyB.addRequireNamespace("test.A");

        SourceFileBase dependencyC = new SourceFileBase("/c.ext");
        dependencyC.addProvideNamespace("test.C");
        dependencyC.addRequireNamespace("test.B");

        SourceFileBase dependencyD = new SourceFileBase("/d.ext");
        dependencyD.addProvideNamespace("test.D");
        dependencyD.addRequireNamespace("test.A");
        dependencyD.addRequireNamespace("test.B");

        SourceFileBase dependencyE = new SourceFileBase("/e.ext");
        dependencyE.addProvideNamespace("test.E");
        dependencyE.addRequireNamespace("test.D");
        dependencyE.addRequireNamespace("test.C");

        dependencies.add(dependencyA);
        dependencies.add(dependencyB);
        dependencies.add(dependencyC);
        dependencies.add(dependencyD);
        dependencies.add(dependencyE);

        DependencyCalculator<SourceFileBase> calculator =
                new DependencyCalculator<SourceFileBase>(dependencies);

        List<SourceFileBase> results = calculator.getDependencyList("test.E");

        Assert.assertEquals(5, results.size());
        Assert.assertTrue(results.contains(dependencyA));
        Assert.assertTrue(results.contains(dependencyB));
        Assert.assertTrue(results.contains(dependencyC));
        Assert.assertTrue(results.contains(dependencyD));
        Assert.assertTrue(results.contains(dependencyE));

        Assert.assertEquals(0, results.indexOf(dependencyA));
        Assert.assertTrue(results.indexOf(dependencyB) < results.indexOf
                (dependencyC));
        Assert.assertTrue(results.indexOf(dependencyA) < results.indexOf(dependencyD));
        Assert.assertTrue(results.indexOf(dependencyB) < results.indexOf(dependencyD));
        Assert.assertTrue(results.indexOf(dependencyC) < results.indexOf(dependencyE));
        Assert.assertTrue(results.indexOf(dependencyD) < results.indexOf(dependencyE));
        Assert.assertEquals(4, results.indexOf(dependencyE));

    }

}
