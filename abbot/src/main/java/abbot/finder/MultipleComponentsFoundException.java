package abbot.finder;

import abbot.tester.Robot;
import java.awt.Component;

/**
 * Indicates more than one component was found (usually where only one was desired).
 */
public class MultipleComponentsFoundException extends ComponentSearchException {

  private final Component[] components;

  public MultipleComponentsFoundException(Component[] components) {
    this.components = components;
  }

  public MultipleComponentsFoundException(String msg, Component[] components) {
    super(msg);
    this.components = components;
  }

  public Component[] getComponents() {
    return components;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder(super.toString());
    builder.append(": ");

    for (int i = 0; i < components.length; i++) {
      builder
          .append("\n (")
          .append(i)
          .append(") ")
          .append(Robot.toHierarchyPath(components[i]))
          .append(": ")
          .append(components[i].toString());
    }
    return builder.toString();
  }
}
