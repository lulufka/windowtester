package com.windowtester.junit5;

import java.awt.Component;
import java.awt.Window;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

public class WindowtesterExecutionStorage {

  private static final String UI_COMPONENT_KEY = "UI_COMPONENT";
  private static final String UI_CONTEXT_KEY = "UI_CONTEXT";

  private final Store store;

  public WindowtesterExecutionStorage(ExtensionContext extensionContext) {
    this.store = extensionContext.getStore(Namespace.create("Windowtester", "UI"));
  }

  public Component saveUIComponent(Component uiComponent) {
    store.put(UI_COMPONENT_KEY, uiComponent);
    return uiComponent;
  }

  public Object saveUIContext(Object uiContext) {
    store.put(UI_CONTEXT_KEY, uiContext);
    return uiContext;
  }

  void wipe() {
    wipeUIComponent();

    Arrays.stream(Window.getWindows())
        .forEach(Window::dispose);
  }

  private void wipeUIComponent() {
    Object o = store.get(UI_COMPONENT_KEY);
    if (o instanceof Window window) {
      window.setVisible(false);
      window.dispose();
    } else if (o instanceof Component component) {
      component.setVisible(false);
    }
    store.remove(UI_CONTEXT_KEY);
  }

}
