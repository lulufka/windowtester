package abbot.util;

import abbot.Log;
import java.util.Date;
import java.util.Timer;

/**
 * Prevents misbehaving TimerTasks from canceling the timer thread by throwing exceptions and/or errors.  Also extends
 * the basic Timer to use a name for its thread.  Naming the timer thread facilitates discerning different threads in a
 * full stack dump.
 */
public class NamedTimer extends Timer {

  /**
   * Creates a non-daemon named timer.
   */
  public NamedTimer(final String name) {
    this(name, false);
  }

  /**
   * Creates a named timer, optionally running as a daemon thread.
   */
  public NamedTimer(final String name, boolean isDaemon) {
    super(isDaemon);
    schedule(
        new AbbotTimerTask() {
          public void run() {
            Thread.currentThread().setName(name);
          }
        },
        0);
  }

  /**
   * Handle an exception thrown by a AbbotTimerTask.  The default does nothing.
   */
  protected void handleException(Throwable thrown) {
    Log.warn(thrown);
  }

  // TODO: prevent scheduled tasks from throwing uncaught exceptions and
  // thus canceling the Timer.
  // We can easily wrap scheduled tasks with a catcher, but we can't readily
  // cancel the wrapper when

  private class ProtectingTimerTask extends AbbotTimerTask {
    private final AbbotTimerTask task;

    public ProtectingTimerTask(AbbotTimerTask orig) {
      this.task = orig;
    }

    public void run() {
      if (isCanceled()) {
        cancel();
      } else {
        try {
          task.run();
        } catch (Throwable thrown) {
          handleException(thrown);
        }
      }
    }
  }

  public void schedule(AbbotTimerTask task, Date time) {
    super.schedule(new ProtectingTimerTask(task), time);
  }

  public void schedule(AbbotTimerTask task, Date firstTime, long period) {
    super.schedule(new ProtectingTimerTask(task), firstTime, period);
  }

  public void schedule(AbbotTimerTask task, long delay) {
    super.schedule(new ProtectingTimerTask(task), delay);
  }

  public void schedule(AbbotTimerTask task, long delay, long period) {
    super.schedule(new ProtectingTimerTask(task), delay, period);
  }

  public void scheduleAtFixedRate(AbbotTimerTask task, Date firstTime, long period) {
    super.scheduleAtFixedRate(new ProtectingTimerTask(task), firstTime, period);
  }

  public void scheduleAtFixedRate(AbbotTimerTask task, long delay, long period) {
    super.scheduleAtFixedRate(new ProtectingTimerTask(task), delay, period);
  }
}
