package slieb.closureutils.commandline.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CLIRunnerTest {

    private CLIRunner cliRunner;

    @Mock
    private BuildRunner mockBuildRunner;

    @Mock
    private ServeRunner mockServeRunner;

    @Mock
    private TestRunner mockTestRunner;

    @Before
    public void setUp() throws Exception {
        cliRunner = new CLIRunner(mockBuildRunner, mockServeRunner, mockTestRunner);
    }

    @Test(expected = Exception.class)
    public void testRunWithNoArguments() throws Exception {
        cliRunner.run();
    }

    @Test(expected = Exception.class)
    public void testRunWithBadCommand() throws Exception {
        cliRunner.run("tents"); // you cannot have tents.
    }

    @Test
    public void testRunWithBuild() throws Exception {
        cliRunner.run("build");
        verify(mockBuildRunner, times(1)).run();
        verify(mockServeRunner, times(0)).run();
        verify(mockTestRunner, times(0)).run();
    }

    @Test
    public void testRunWithServe() throws Exception {
        cliRunner.run("serve");
        verify(mockBuildRunner, times(0)).run();
        verify(mockServeRunner, times(1)).run();
        verify(mockTestRunner, times(0)).run();
    }

    @Test
    public void testRunWithTest() throws Exception {
        cliRunner.run("test");
        verify(mockBuildRunner, times(0)).run();
        verify(mockServeRunner, times(0)).run();
        verify(mockTestRunner, times(1)).run();
    }
}