package abbot.finder;

import java.awt.*;
import java.util.Collection;

/**
 * Provides access to all components in a hierarchy.
 */
public interface Hierarchy {
  /**
   * Provides all root components in the hierarchy.  Similar to Frame.getFrames().
   * @return all root components
   */
  Collection<Component> getRoots();

  /**
   * Returns all sub-components of the given component.  What constitutes a sub-component may vary depending on the
   * Hierarchy implementation.
   * @param c component
   * @return sub-components
   */
  Collection<Component> getComponents(Component c);

  /**
   * Return the parent component for the given Component.
   * @param c component
   * @return the parent component for the given Component.
   */
  Container getParent(Component c);

  /**
   * Returns whether the hierarchy contains the given Component.
   * @param c component
   * @return whether the hierarchy contains the given component
   */
  boolean contains(Component c);

  /**
   * Provide proper disposal of the given Window, appropriate to this Hierarchy.  After disposal, the Window and its
   * descendents will no longer be reachable from this Hierarchy.
   * @param w window
   */
  void dispose(Window w);
}
