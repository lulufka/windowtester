package abbot.editor;

import java.awt.*;

import abbot.script.ComponentReference;

public interface ComponentBrowserListener {
    void selectionChanged(
            ComponentBrowser src,
            Component comp,
            ComponentReference ref);

    void propertyAction(
            ComponentBrowser src,
            java.lang.reflect.Method m,
            Object value,
            boolean sample);
}
