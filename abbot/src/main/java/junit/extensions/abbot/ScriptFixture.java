package junit.extensions.abbot;

import abbot.Log;
import abbot.finder.AWTHierarchy;
import abbot.finder.Hierarchy;
import abbot.finder.TestHierarchy;
import abbot.script.Script;
import abbot.script.StepRunner;
import abbot.util.AWTFixtureHelper;
import junit.framework.TestCase;

/**
 * Simple wrapper for a test script to run under JUnit.  If the script does not contain a launch step, the hierarchy
 * used will include existing components.  No automatic cleanup of components is performed, since it is assumed that a
 * Terminate step within the script will trigger that operation if it is required.<p>
 */
public class ScriptFixture extends TestCase {

    private static AWTFixtureHelper oldContext = null;
    private static final Hierarchy DUMMY_HIERARCHY = new AWTHierarchy();
    private StepRunner runner;

    /**
     * Construct a test case with the given name, which <i>must</i> be the filename of the script to run.
     */
    public ScriptFixture(final String filename) {
        // It is essential that the name be passed to super() unmodified, or
        // the JUnit GUI will consider it a different test.
        super(filename);
    }

    /**
     * Saves the current UI state for restoration when the fixture (if any) is terminated.  Also sets up a {@link
     * TestHierarchy} for the duration of the test.
     */
    @Override
    protected void setUp() throws Exception {
        if (oldContext == null) {
            oldContext = new AWTFixtureHelper();
        }
        runner = new StepRunner(oldContext);
        // Support for deprecated ComponentTester.assertFrameShowing usage
        // only.  Eventually this will go away.
        AWTHierarchy.setDefault(runner.getHierarchy());
    }

    @Override
    protected void tearDown() throws Exception {
        AWTHierarchy.setDefault(null);
        runner = null;
    }

    /**
     * Override the default TestCase runTest method to invoke the script. The {@link Script} is created and a default
     * {@link StepRunner} is used to run it.
     *
     * @see junit.framework.TestCase#runTest
     */
    @Override
    protected void runTest() throws Throwable {
        final Script script = new Script(getName(), DUMMY_HIERARCHY);
        Log.log("Running " + script + " with " + getClass());

        try {
            runner.run(script);
        } finally {
            Log.log(script + " finished");
        }
    }
}
