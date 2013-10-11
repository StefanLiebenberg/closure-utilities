package org.stefanl.closure_utilities.internal;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class DependencyCalculatorTest {

    @Test
    public void testBaseDependencyCalculator() throws Exception {

        Collection<BaseSourceFile> dependencies = new HashSet<BaseSourceFile>();

        BaseSourceFile dependencyA = new BaseSourceFile("/a.ext");
        dependencyA.addProvideNamespace("test.A");

        BaseSourceFile dependencyB = new BaseSourceFile("/b.ext");
        dependencyB.addProvideNamespace("test.B");
        dependencyB.addRequireNamespace("test.A");

        BaseSourceFile dependencyC = new BaseSourceFile("/c.ext");
        dependencyC.addProvideNamespace("test.C");
        dependencyC.addRequireNamespace("test.B");

        BaseSourceFile dependencyD = new BaseSourceFile("/d.ext");
        dependencyD.addProvideNamespace("test.D");
        dependencyD.addRequireNamespace("test.A");
        dependencyD.addRequireNamespace("test.B");

        BaseSourceFile dependencyE = new BaseSourceFile("/e.ext");
        dependencyE.addProvideNamespace("test.E");
        dependencyE.addRequireNamespace("test.D");
        dependencyE.addRequireNamespace("test.C");

        dependencies.add(dependencyA);
        dependencies.add(dependencyB);
        dependencies.add(dependencyC);
        dependencies.add(dependencyD);
        dependencies.add(dependencyE);

        DependencyCalculator<BaseSourceFile> calculator =
                new DependencyCalculator<BaseSourceFile>(dependencies);

        List<BaseSourceFile> results = calculator.getDependencyList("test.E");

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
