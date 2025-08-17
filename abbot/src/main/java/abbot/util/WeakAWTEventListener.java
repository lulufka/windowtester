package abbot.util;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.lang.ref.WeakReference;

/**
 * Provides add-and-forget listening to the AWT event queue. Provides an AWTEventListener which will automatically
 * disconnect the target listener when the target gets garbage-collected.   Once the target is GC'd, this listener will
 * remove itself from the AWT event listener list.
 */
public class WeakAWTEventListener implements AWTEventListener {

  private final WeakReference listener;

  public WeakAWTEventListener(AWTEventListener l, long mask) {
    listener = new WeakReference(l);
    Toolkit.getDefaultToolkit().addAWTEventListener(this, mask);
  }

  public void dispose() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(this);
  }

  public void eventDispatched(AWTEvent e) {
    AWTEventListener l = (AWTEventListener) listener.get();
    if (l != null) {
      try {
        l.eventDispatched(e);
      } catch (RuntimeException ex) {
        throw ex;
      }
    } else {
      dispose();
    }
  }
}
