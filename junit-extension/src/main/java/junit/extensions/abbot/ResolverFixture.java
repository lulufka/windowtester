package junit.extensions.abbot;

import abbot.Log;
import abbot.finder.BasicFinder;
import abbot.finder.ComponentFinder;
import abbot.finder.Hierarchy;
import abbot.finder.Matcher;
import abbot.finder.TestHierarchy;
import abbot.script.Resolver;
import abbot.script.Script;
import java.awt.Component;
import java.awt.Window;
import java.util.Iterator;

/**
 * Simple wrapper for testing objects which require a Resolver.
 */
public class ResolverFixture {

  private final String name;

  /**
   * Simple matcher that may be used to verify that a specific component is found by a given ComponentFinder.
   */
  protected class ComponentMatcher implements Matcher {
    private final Component component;

    public ComponentMatcher(Component c) {
      component = c;
    }

    public boolean matches(Component c) {
      return c == component;
    }
  }

  private Hierarchy hierarchy;
  private ComponentFinder finder;
  private Resolver resolver;

  protected Hierarchy getHierarchy() {
    return hierarchy;
  }

  protected Hierarchy createHierarchy() {
    return new TestHierarchy();
  }

  protected ComponentFinder getFinder() {
    return finder;
  }

  protected Resolver getResolver() {
    return resolver;
  }

  protected void fixtureSetUp() throws Throwable {
    hierarchy = createHierarchy();

    finder = new BasicFinder(hierarchy);
    // FIXME kind of a hack, but Script is the only implementation of
    // Resolver we've got at the moment.
    resolver = new Script(hierarchy);
  }

  protected void fixtureTearDown() throws Throwable {
    Iterator iter = hierarchy.getRoots().iterator();
    while (iter.hasNext()) {
      hierarchy.dispose((Window) iter.next());
    }
    // Explicitly set these null, since the test fixture instance may
    // be kept around by the test runner
    hierarchy = null;
    resolver = null;
    finder = null;
  }

  public void runBare() throws Throwable {
    Log.log("setting up fixture: " + getName());
    Throwable exception = null;
    fixtureSetUp();
    try {
      super.runBare();
    } catch (Throwable e) {
      exception = e;
    } finally {
      Log.log("tearing down fixture: " + getName());
      try {
        fixtureTearDown();
      } catch (Throwable tearingDown) {
        if (exception == null) {
          exception = tearingDown;
        }
      }
    }
    if (exception != null) {
      throw exception;
    }
  }

  public ResolverFixture(String name) {
    this.name = name;
  }

  /**
   * Default Constructor.  The name will be automatically set from the selected test method.
   */
  public ResolverFixture() {
    this("");
  }

  public String getName() {
    return name;
  }
}
