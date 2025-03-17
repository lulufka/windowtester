package com.windowtester.junit5;

import java.awt.Component;
import java.awt.Window;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
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

    Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    // Ensure disposal is complete
    while (anyWindowStillVisible()) {
      try {
        pauseForASecond();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void pauseForASecond() throws InterruptedException {
    TimeUnit.SECONDS.sleep(1);
  }

  // Check if any window is still active
  private static boolean anyWindowStillVisible() {
    return Arrays.stream(Window.getWindows()).anyMatch(Window::isVisible);
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
