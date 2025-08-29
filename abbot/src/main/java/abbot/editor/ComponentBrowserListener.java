package abbot.editor;

import abbot.script.ComponentReference;
import java.awt.Component;

public interface ComponentBrowserListener {
  void selectionChanged(ComponentBrowser src, Component comp, ComponentReference ref);

  void propertyAction(
      ComponentBrowser src, java.lang.reflect.Method m, Object value, boolean sample);
}
