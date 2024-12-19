package com.windowtester.junit5;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import com.windowtester.junit5.resolver.AnnotationResolver;
import com.windowtester.junit5.resolver.FieldInfo;
import com.windowtester.junit5.resolver.SwingUIContextParameterResolver;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class WindowtesterExtension implements ParameterResolver, BeforeTestExecutionCallback,
    AfterTestExecutionCallback {

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 300;

  private final SwingUIContextParameterResolver swingUIContextResolver;

  public WindowtesterExtension() {
    swingUIContextResolver = new SwingUIContextParameterResolver();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return isSwingUIContextParameter(parameterContext, extensionContext);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return swingUIContextResolver.resolveParameter(parameterContext, extensionContext);
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    new AnnotationResolver(context).tryToFindAnnotatedField(UIUnderTest.class)
        .filter(fieldInfo -> fieldInfo.result() instanceof Component)
        .ifPresentOrElse(
            fieldInfo -> showUI(fieldInfo, context),
            () -> {
              throw new RuntimeException("Unable to find @UIUnderTest annotation in test class.");
            }
        );
  }

  private void showUI(
      FieldInfo fieldInfo,
      ExtensionContext context) {
    var uiUnderTest = fieldInfo.result();
    if (uiUnderTest instanceof Window window) {
      showWindow(window, context);
    } else {
      var window = createWindow((Component) uiUnderTest, fieldInfo, context);
      showWindow(window, context);
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    getStorage(context).wipe();
  }

  private Window createWindow(
      Component component,
      FieldInfo fieldInfo,
      ExtensionContext context) {
    var baseFrame = createBaseFrame(fieldInfo, context);
    attachComponentToFrame(baseFrame, component);
    return new Window(baseFrame);
  }

  private JFrame createBaseFrame(FieldInfo fieldInfo, ExtensionContext context) {
    var baseFrame = new JFrame("");
    baseFrame.setTitle(getUIUnderTestTitle(fieldInfo, context.getTestClass()));
    baseFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    return baseFrame;
  }

  private String getUIUnderTestTitle(FieldInfo fieldInfo, Optional<Class<?>> testClass) {
    var title = fieldInfo.title();
    if (title.isEmpty()) {
      return testClass.map(Class::getSimpleName).orElse("");
    }
    return title;
  }

  private void attachComponentToFrame(JFrame jFrame, Component component) {
    var pane = (JPanel) jFrame.getContentPane();
    pane.setBorder(new EmptyBorder(10, 10, 10, 10));

    if (component instanceof JComponent comp) {
      comp.setOpaque(true);
    }

    pane.add(component);
  }

  private void showWindow(Window window, ExtensionContext context) {
    getStorage(context).saveUIComponent(window);

    try {
      EventQueue.invokeAndWait(
          () -> {
            // Make sure the window is positioned away from
            // any toolbars around the display borders
            // Display the window.
            window.setLocation(100, 100);
            window.pack();
            window.pack();
            window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

            window.setAutoRequestFocus(true);
            window.setAlwaysOnTop(true);
            window.toFront();

            window.setVisible(true);
          });
    } catch (InvocationTargetException | InterruptedException e) {
      throw new RuntimeException("Fail to close window.", e);
    }
  }

  private boolean isSwingUIContextParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) {
    return swingUIContextResolver.supportsParameter(parameterContext, extensionContext);
  }

  private WindowtesterExecutionStorage getStorage(ExtensionContext extensionContext) {
    return new WindowtesterExecutionStorage(extensionContext);
  }
}
