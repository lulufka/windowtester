package junit.extensions.abbot;

import abbot.Log;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.ComponentSearchException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;
import abbot.finder.matchers.WindowMatcher;
import abbot.tester.ComponentTester;
import abbot.tester.Robot;
import abbot.tester.WindowTracker;
import abbot.util.AWTFixtureHelper;
import abbot.util.Bugs;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import junit.framework.TestCase;

/**
 * Fixture for testing AWT and/or JFC/Swing components under JUnit.  Ensures proper setup and cleanup for a GUI
 * environment.  Provides methods for automatically placing a GUI component within a frame and properly handling Window
 * showing/hiding (including modal dialogs).  Catches exceptions thrown on the event dispatch thread and rethrows them
 * as test failures.<p> Use {@link #showFrame(Component)} when testing individual components, or {@link
 * #showWindow(Window)} when testing a {@link Frame}, {@link Dialog}, or {@link Window}.<p> Any member fields you define
 * which are classes derived from any of the classes in {@link #DISPOSE_CLASSES} will be automatically set to null after
 * the test is run.<p>
 * <b>WARNING:</b> Any tests which use significant or scarce resources
 * and reference them in member fields should explicitly null those fields in the tearDown method if those classes are
 * not included or derived from those in {@link #DISPOSE_CLASSES}.  Otherwise the resources will not be subject to GC
 * until the {@link TestCase} itself and any containing {@link junit.framework.TestSuite} is disposed (which, in the
 * case of the standard JUnit test runners, is
 * <i>never</i>).
 */
public class ComponentTestFixture extends ResolverFixture {

  public class EventDispatchException extends InvocationTargetException {
    private EventDispatchException(final Throwable t) {
      super(t, "An exception was thrown on the event dispatch thread: " + t.toString());
    }

    @Override
    public void printStackTrace() {
      getTargetException().printStackTrace();
    }

    @Override
    public void printStackTrace(final PrintStream p) {
      getTargetException().printStackTrace(p);
    }

    @Override
    public void printStackTrace(final PrintWriter p) {
      getTargetException().printStackTrace(p);
    }
  }

  /**
   * Typical delay to wait for a robot event to be translated into a Java event.
   */
  public static final int EVENT_GENERATION_DELAY = 5000;

  public static final int WINDOW_DELAY = 20000; // for slow systems
  public static final int POPUP_DELAY = 10000;

  /**
   * Any member data derived from these classes will be automatically set to <code>null</code> after the test has run.
   * This enables GC of said classes without GC of the test itself (the default JUnit runners never release their
   * references to the tests) or requiring explicit
   * <code>null</code>-setting in the {@link TestCase#tearDown()} method.
   */
  protected static final Class[] DISPOSE_CLASSES = {Component.class, ComponentTester.class};

  private static Robot robot;
  private static WindowTracker tracker;

  private AWTFixtureHelper savedState;
  private Throwable edtException;
  private long edtExceptionTime;

  protected Robot getRobot() {
    return robot;
  }

  protected WindowTracker getWindowTracker() {
    return tracker;
  }

  protected Frame showFrame(final Component comp) {
    return showFrame(comp, null);
  }

  /**
   * This method should be invoked to display the component under test, when a specific size of frame is desired.  The
   * method will return when the enclosing {@link Frame} is showing and ready for input.
   *
   * @param comp component
   * @param size Desired size of the enclosing frame, or <code>null</code> to make no explicit adjustments to its
   *             size.
   * @return frame
   */
  protected Frame showFrame(final Component comp, final Dimension size) {
    final JFrame frame = new JFrame(getName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final JPanel pane = (JPanel) frame.getContentPane();
    pane.setBorder(new EmptyBorder(10, 10, 10, 10));
    pane.add(comp);
    showWindow(frame, size, true);
    return frame;
  }

  protected void showWindow(final Window w) {
    showWindow(w, null, true);
  }

  protected void showWindow(final Window w, final Dimension size) {
    showWindow(w, size, true);
  }

  protected void showWindow(final Window w, final Dimension size, final boolean pack) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            if (pack) {
              w.pack();
              // Make sure the window is positioned away from
              // any toolbars around the display borders
              w.setLocation(100, 100);
            }
            if (size != null) {
              w.setSize(size.width, size.height);
            }
            w.show();
          }
        });
    // Ensure the window is visible before returning
    waitForWindow(w, true);
  }

  /**
   * Return when the window is ready for input or times out waiting.
   *
   * @param w
   */
  private void waitForWindow(final Window w, final boolean visible) {
    final Timer timer = new Timer();
    while (tracker.isWindowReady(w) != visible) {
      if (timer.elapsed() > WINDOW_DELAY) {
        throw new RuntimeException(
            "Timed out waiting for Window to "
                + (visible ? "open" : "close")
                + " ("
                + timer.elapsed()
                + "ms)");
      }
      robot.sleep();
    }
  }

  protected void hideWindow(final Window w) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            w.hide();
          }
        });
    waitForWindow(w, false);
    // Not strictly required, but if a test is depending on a window
    // event listener's actions on window hide/close, better to wait.
    robot.waitForIdle();
  }

  protected void disposeWindow(final Window w) {
    w.dispose();
    waitForWindow(w, false);
    robot.waitForIdle();
  }

  protected void installPopup(final Component invoker, final JPopupMenu popup) {
    invoker.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(final MouseEvent e) {
            mouseReleased(e);
          }

          @Override
          public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger()) {
              popup.show(e.getComponent(), e.getX(), e.getY());
            }
          }
        });
  }

  /**
   * Safely install and display a popup in the center of the given component, returning when it is visible.  Does not
   * install any mouse handlers not generate any mouse events.
   */
  protected void showPopup(final JPopupMenu popup, final Component invoker) {
    showPopup(popup, invoker, invoker.getWidth() / 2, invoker.getHeight() / 2);
  }

  /**
   * Safely install and display a popup, returning when it is visible. Does not install any mouse handlers not
   * generate any mouse events.
   */
  protected void showPopup(
      final JPopupMenu popup, final Component invoker, final int x, final int y) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            popup.show(invoker, x, y);
          }
        });
    final Timer timer = new Timer();
    while (!popup.isShowing()) {
      if (timer.elapsed() > POPUP_DELAY) {
        throw new RuntimeException("Timed out waiting for popup to show");
      }
      robot.sleep();
    }
    waitForWindow(SwingUtilities.getWindowAncestor(popup), true);
  }

  /**
   * Display a modal dialog and wait for it to show.  Useful for things like {@link
   * JFileChooser#showOpenDialog(Component)} or {@link JOptionPane#showInputDialog(Component, Object)}, or any other
   * instance where the dialog contents are not predefined and displaying the dialog involves anything more than
   * {@link Window#show()} (if {@link Window#show()} is all that is required, use the {@link #showWindow(Window)}
   * method instead).<p> The given {@link Runnable} should contain the code which will show the modal {@link Dialog}
   * (and thus block); it will be run on the event dispatch thread.<p> This method will return when a {@link Dialog}
   * becomes visible which contains the given component (which may be any component which will appear on the {@link
   * Dialog}), or the standard timeout (10s) is reached, at which point a {@link RuntimeException} will be thrown.<p>
   * For example,<br>
   * <pre><code>
   * Frame parent = ...;
   * showModalDialog(new Runnable) {
   * public void run() {
   * JOptionPane.showInputDialog(parent, "Hit me");
   * }
   * });
   * </code></pre>
   *
   * @see #showWindow(java.awt.Window)
   * @see #showWindow(java.awt.Window, java.awt.Dimension)
   * @see #showWindow(java.awt.Window, java.awt.Dimension, boolean)
   */
  protected Dialog showModalDialog(final Runnable showAction) throws Exception {
    EventQueue.invokeLater(showAction);
    // Wait for a modal dialog to appear
    final Matcher matcher =
        new ClassMatcher(Dialog.class, true) {
          @Override
          public boolean matches(final Component c) {
            return super.matches(c) && ((Dialog) c).isModal();
          }
        };
    final Timer timer = new Timer();
    while (true) {
      try {
        return (Dialog) getFinder().find(matcher);
      } catch (final ComponentSearchException e) {
        if (timer.elapsed() > 10000) {
          throw new RuntimeException("Timed out waiting for dialog to be ready");
        }
        robot.sleep();
      }
    }
  }

  /**
   * Similar to {@link #showModalDialog(Runnable)}, but provides for the case where some of the {@link Dialog}'s
   * contents are known beforehand.<p>
   *
   * @deprecated Use {@link #showModalDialog(Runnable)} instead.
   */
  protected Dialog showModalDialog(final Runnable showAction, final Component contents)
      throws Exception {
    return showModalDialog(showAction);
  }

  /**
   * Returns whether a Component is showing.  The ID may be the component name or, in the case of a Frame or Dialog,
   * the title.  Regular expressions may be used, but must be delimited by slashes, e.g. /expr/. Returns if one or
   * more matches is found.
   */
  protected boolean isShowing(final String id) {
    try {
      getFinder().find(new WindowMatcher(id, true));
    } catch (final ComponentNotFoundException e) {
      return false;
    } catch (final MultipleComponentsFoundException m) {
      // Might not be the one you want, but that's what the docs say
    }
    return true;
  }

  /**
   * Construct a test case with the given name.
   */
  public ComponentTestFixture(final String name) {
    super(name);
  }

  /**
   * Default Constructor.  The name will be automatically set from the selected test method.
   */
  public ComponentTestFixture() {}

  /**
   * Ensure proper test harness setup and teardown that won't be inadvertently overridden by a derived class.
   */
  @Override
  protected void fixtureSetUp() throws Throwable {
    super.fixtureSetUp();

    savedState = new AWTFixtureHelper();

    robot = new Robot();
    tracker = WindowTracker.getTracker();

    robot.reset();
    if (Bugs.hasMultiClickFrameBug()) {
      robot.delay(500);
    }
  }

  /**
   * Handles restoration of system state.  Automatically disposes of any Components used in the test.
   */
  @Override
  protected void fixtureTearDown() throws Throwable {
    super.fixtureTearDown();
    tracker = null;
    if (robot != null) {
      final int buttons = Robot.getState().getButtons();
      if (buttons != 0) {
        robot.mouseRelease(buttons);
      }
      // TODO: release any extant pressed keys
      robot = null;
    }
    edtExceptionTime = savedState.getEventDispatchErrorTime();
    edtException = savedState.getEventDispatchError();
    savedState.restore();
    savedState = null;
    clearTestFields();
  }

  /**
   * Clears all non-static {@link TestCase} fields which are instances of any class found in {@link
   * #DISPOSE_CLASSES}.
   */
  private void clearTestFields() {
    try {
      final Field[] fields = getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        if ((fields[i].getModifiers() & Modifier.STATIC) == 0) {
          fields[i].setAccessible(true);
          for (int c = 0; c < DISPOSE_CLASSES.length; c++) {
            final Class cls = DISPOSE_CLASSES[c];
            if (cls.isAssignableFrom(fields[i].getType())) {
              fields[i].set(this, null);
            }
          }
        }
      }
    } catch (final Exception e) {
      Log.warn(e);
    }
  }

  /**
   * If any exceptions are thrown on the event dispatch thread, they count as errors.  They will not, however
   * supersede any failures/errors thrown by the test itself.
   */
  @Override
  public void runBare() throws Throwable {
    Throwable exception = null;
    long exceptionTime = -1;
    try {
      super.runBare();
    } catch (final Throwable e) {
      exceptionTime = System.currentTimeMillis();
      exception = e;
    } finally {
      // Cf. StepRunner.runStep()
      // Any EDT exception which occurred *prior* to when the
      // exception on the main thread was thrown should be used
      // instead.
      if (edtException != null && (exception == null || edtExceptionTime < exceptionTime)) {
        exception = new EventDispatchException(edtException);
      }
    }
    if (exception != null) {
      throw exception;
    }
  }
}
