package abbot.editor.recorder;

import abbot.script.Action;
import abbot.script.ComponentReference;
import abbot.script.Resolver;
import abbot.script.Step;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;

/**
 * Record basic semantic events you might find on a Window.
 */
public class DialogRecorder extends WindowRecorder {

  public DialogRecorder(Resolver resolver) {
    super(resolver);
  }

  protected Step createResize(Window window, Dimension size) {
    Step step = null;
    if (((Dialog) window).isResizable()) {
      ComponentReference ref = getResolver().addComponent(window);
      step =
          new Action(
              getResolver(),
              null,
              "actionResize",
              new String[] {
                ref.getID(), String.valueOf(size.width), String.valueOf(size.height),
              },
              Dialog.class);
    }
    return step;
  }
}
