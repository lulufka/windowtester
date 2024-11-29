package com.windowtester.junit5;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import com.windowtester.junit5.resolver.AnnotationResolver;
import com.windowtester.junit5.resolver.FieldInfo;
import com.windowtester.junit5.resolver.SwingUIContextParameterResolver;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window;
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
    var baseFrame = new AnnotationResolver(context).tryToFindAnnotatedField(BaseFrame.class)
        .map(fieldInfo -> (JFrame) fieldInfo.result())
        .orElseGet(() -> {
          JFrame frame = new JFrame("");
          frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
          return frame;
        });

    new AnnotationResolver(context).tryToFindAnnotatedField(UIUnderTest.class)
        .filter(fieldInfo -> fieldInfo.result() instanceof Component)
        .ifPresent(fieldInfo -> {
          baseFrame.setTitle(getUIUnderTestTitle(fieldInfo, context.getTestClass()));
          createAndShowUI(baseFrame, (Component) fieldInfo.result(), context);
        });
  }

  private String getUIUnderTestTitle(FieldInfo fieldInfo, Optional<Class<?>> testClass) {
    var title = fieldInfo.title();
    if (title.isEmpty()) {
      return testClass.map(Class::getSimpleName).orElse("");
    }
    return title;
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    getStorage(context).wipe();
  }

  private void createAndShowUI(
      JFrame frame,
      Component component,
      ExtensionContext context) {
    var window = createWindow(frame, component);
    getStorage(context).saveUIComponent(window);
    showWindow(window);
  }

  private Window createWindow(JFrame jFrame, Component component) {
    if (component instanceof Frame frame) {
      return createAndShowFrameUI(frame);
    }
    return createAndShowPanelUI(jFrame, component);
  }

  private Window createAndShowFrameUI(Frame frame) {
    if (frame instanceof JFrame jFrame) {
      jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    return frame;
  }

  private Window createAndShowPanelUI(JFrame jFrame, Component component) {
    var pane = (JPanel) jFrame.getContentPane();
    pane.setBorder(new EmptyBorder(10, 10, 10, 10));

    if (component instanceof JComponent component1) {
      component1.setOpaque(true);
    }

    if (!(component instanceof Window)) {
      pane.add(component);
    }
    return jFrame;
  }

  private void showWindow(Window window) {
    EventQueue.invokeLater(
        () -> {
          // Make sure the window is positioned away from
          // any toolbars around the display borders
          // Display the window.
          window.setLocation(100, 100);
          window.pack();
          window.pack();
          window.setSize(400, 300);

          window.setVisible(true);
        });
  }

  private boolean isSwingUIContextParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) {
    return swingUIContextResolver.supportsParameter(parameterContext, extensionContext);
  }

  private WindowtesterExecutionStorage getStorage(ExtensionContext extensionContext) {
    return new WindowtesterExecutionStorage(extensionContext);
  }
}
