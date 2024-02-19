package abbot.editor.recorder;

import java.awt.*;

import abbot.script.Action;
import abbot.script.ComponentReference;
import abbot.script.Resolver;
import abbot.script.Step;

/**
 * Record basic semantic events you might find on an Window. <p>
 */
public class DialogRecorder extends WindowRecorder {

    public DialogRecorder(Resolver resolver) {
        super(resolver);
    }

    protected Step createResize(
            Window window,
            Dimension size) {
        Step step = null;
        if (((Dialog) window).isResizable()) {
            ComponentReference ref = getResolver().addComponent(window);
            step = new Action(getResolver(),
                    null, "actionResize",
                    new String[]{ref.getID(),
                            String.valueOf(size.width),
                            String.valueOf(size.height),
                    }, Dialog.class);
        }
        return step;
    }
}

