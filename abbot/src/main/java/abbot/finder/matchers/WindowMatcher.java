package abbot.finder.matchers;

import java.awt.*;

/**
 * Provides matching of a Window by title or component name.
 */
public class WindowMatcher extends ClassMatcher {
  private final String id;
  private final boolean mustBeShowing;

  public WindowMatcher(String id) {
    this(id, true);
  }

  public WindowMatcher(String id, boolean mustBeShowing) {
    super(Window.class);
    this.id = id;
    this.mustBeShowing = mustBeShowing;
  }

  public boolean matches(Component c) {
    return super.matches(c)
        && (c.isShowing() || !mustBeShowing)
        && (stringsMatch(id, c.getName())
            || (c instanceof Frame && stringsMatch(id, ((Frame) c).getTitle()))
            || (c instanceof Dialog && stringsMatch(id, ((Dialog) c).getTitle())));
  }

  public String toString() {
    return "Window matcher (id=" + id + ")";
  }
}
