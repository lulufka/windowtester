package abbot.editor.recorder;

import abbot.BugReport;
import abbot.Log;
import abbot.script.Resolver;
import abbot.script.Step;
import abbot.tester.Robot;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The <code>Recorder</code> provides a mechanism for recording an event stream and generating a sequence of script
 * steps from that stream.
 *
 * NOTE: when writing a recorder, be very careful not to test for platform-specific behavior, and avoid being
 * susceptible to platform-specific bugs.  Please make sure the recorder works on both pointer-focus and click-to-focus
 * window managers, as well as on at least two platforms.
 */
public abstract class Recorder {
  private ActionListener al;
  private final Resolver resolver;
  private long lastEventTime = 0;

  public Recorder(Resolver resolver) {
    this.resolver = resolver;
  }

  public void addActionListener(ActionListener al) {
    this.al = al;
  }

  protected ActionListener getListener() {
    return al;
  }

  public void start() {
    lastEventTime = System.currentTimeMillis();
  }

  public abstract void terminate() throws RecordingFailedException;

  public long getLastEventTime() {
    return lastEventTime;
  }

  /**
   * Create a step or sequence of steps based on the event stream so far.
   * @return step
   */
  protected abstract Step createStep();

  public Step getStep() {
    return createStep();
  }

  public void insertStep(Step step) {
    // Default does nothing
  }

  /**
   * Process the given event.
   * @param event event
   * @throws RecordingFailedException if an error was encountered and recording should discontinue.
   */
  public void record(java.awt.AWTEvent event) throws RecordingFailedException {

    if (Log.isClassDebugEnabled(getClass())) {
      Log.debug("REC: " + Robot.toString(event));
    }
    lastEventTime = System.currentTimeMillis();
    try {
      recordEvent(event);
    } catch (RecordingFailedException e) {
      System.err.println("!!!exception: ");
      e.printStackTrace();
      throw e;
    } catch (Throwable thrown) {
      thrown.printStackTrace();
      Log.log("REC: Unexpected failure: " + thrown);
      //  String msg = Strings.get("editor.recording.exception");
      //  throw new RecordingFailedException(new BugReport(msg, thrown));
      String msg = "recording.exception";
      throw new com.windowtester.swing.recorder.RecordingFailedException(
          new BugReport(msg, thrown));
    }
  }

  /**
   * Implement this to actually handle the event.
   * @param event event
   *
   * @throws RecordingFailedException if an error was encountered and recording should be discontinued.
   */
  protected abstract void recordEvent(AWTEvent event) throws RecordingFailedException;

  public long getEventMask() {
    return -1;
  }

  /**
   * @return the {@link Resolver} to be used by this <code>Recorder</code>.
   */
  protected Resolver getResolver() {
    return resolver;
  }

  protected void setStatus(String msg) {
    if (al != null) {
      ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, msg);
      al.actionPerformed(event);
    }
  }
}
