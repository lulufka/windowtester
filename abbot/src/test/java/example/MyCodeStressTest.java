package example;

import junit.extensions.abbot.ScriptFixture;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Simple example of a stress test on an app.
 */
public class MyCodeStressTest extends TestCase {

  public static Test suite() {
    final int ITERATIONS = 10;
    TestSuite suite = new TestSuite();
    for (int i = 0; i < ITERATIONS; i++) {
      suite.addTest(new ScriptFixture("target/test-classes/example/StressMyCode.xml"));
    }
    return suite;
  }
}
