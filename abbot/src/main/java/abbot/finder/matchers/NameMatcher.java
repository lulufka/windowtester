package abbot.finder.matchers;

import abbot.util.AWT;
import java.awt.Component;

/**
 * Provides matching of Components by component name.
 */
public class NameMatcher extends AbstractMatcher {

  private final String name;

  /**
   * Construct a matcher that will match any component that has explicitly been assigned the given
   * <code>name</code>. Auto-generated names (e.g. <code>win0</code>, <code>frame3</code>, etc. for
   * AWT (native) based components will not match.
   *
   * @param name name
   */
  public NameMatcher(String name) {
    this.name = name;
  }

  @Override
  public boolean matches(Component component) {
    var componentName = component.getName();
    if (name == null) {
      return componentName == null || AWT.hasDefaultName(component);
    }
    return stringsMatch(name, componentName);
  }

  @Override
  public String toString() {
    return "Name matcher (" + name + ")";
  }
}
