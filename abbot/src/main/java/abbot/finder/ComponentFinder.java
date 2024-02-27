package abbot.finder;

import java.awt.*;

/**
 * Interface to support looking up existing components based on a number of different criteria.
 *
 * @see Matcher
 */
public interface ComponentFinder {
  /**
   * Find a Component, using the given Matcher to determine whether a given component in the hierarchy used by this
   * ComponentFinder is the desired one.
   *
   * Note that {@link MultipleComponentsFoundException} can only be thrown if the {@link Matcher} argument is an
   * instance of {@link MultiMatcher}.
   *
   * @param m matcher
   * @return matching component
   * @throws ComponentNotFoundException component not found
   * @throws MultipleComponentsFoundException multiple components found
   */
  Component find(Matcher m) throws ComponentNotFoundException, MultipleComponentsFoundException;

  /**
   * Find a Component, using the given Matcher to determine whether a given component in the hierarchy under the given
   * root is the desired one.
   *
   * Note that {@link MultipleComponentsFoundException} can only be thrown if the {@link Matcher} argument is an
   * instance of {@link MultiMatcher}.
   *
   * @param m matcher
   * @param root root container
   * @return matching component
   * @throws ComponentNotFoundException component not found
   * @throws MultipleComponentsFoundException multiple components found
   */
  Component find(Container root, Matcher m)
      throws ComponentNotFoundException, MultipleComponentsFoundException;
}
