package com.windowtester.junit5.initializer;

import java.awt.Frame;
import java.lang.reflect.Parameter;
import javax.swing.JFrame;
import org.junit.jupiter.api.extension.ParameterResolutionException;

public class UIInitializerFactory {

  private UIInitializerFactory() {
    // do nothing
  }

  public static UIIinitializer<?> getInstance(Parameter parameter)
      throws ParameterResolutionException {
    var type = parameter.getType();
    if (isAWTFrame(type)) {
      return new AWTUIInitializer();
    }
    if (isSwingJFrame(type)) {
      return new SwingUIInitializer();
    }

    var message = String.format(
        "@UIUnderTest field must be of the type javax.swing.JFrame or java.awt.Frame.\n"
            + "Field: '%s' is of type '%s'!"
        , parameter.getName(), type.getCanonicalName()
    );
    throw new ParameterResolutionException(message);
  }

  private static boolean isAWTFrame(Class<?> givenType) {
    return Frame.class.equals(givenType);
  }

  private static boolean isSwingJFrame(Class<?> givenType) {
    return JFrame.class.equals(givenType);
  }

}
