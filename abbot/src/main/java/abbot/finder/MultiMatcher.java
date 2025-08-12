package abbot.finder;

import java.awt.Component;

/**
 * Provides methods for determining the best match among a group of matching components. For use
 * with implementations of {@link ComponentFinder}. You can conveniently inline a custom matcher
 * like so:<br>
 * <pre>
 * ComponentFinder finder = BasicFinder.getDefault();
 * ...
 * // Find the widest label with known text
 * JLabel label = (JLabel)finder.find(new MultiMatcher() {
 * public boolean matches(Component c) {
 * return c instanceof JLabel
 * &amp;&amp; "OK".equals(((JLabel)c).getText());
 * }
 * public Component bestMatch(Component[] candidates)
 * throws MultipleComponentsFoundException {
 * Component biggest = candidates[0];
 * for (int i=1;i &lt; candidates.length;i++) {
 * if (biggest.getWidth() &lt; candidates[i].getWidth())
 * biggest = candidates[i];
 * }
 * return biggest;
 * }
 * });
 * </pre>
 *
 * @see ComponentFinder
 */
public interface MultiMatcher extends Matcher {

  /**
   * Returns the best match among all the given candidates, or throws an exception if there is no
   * best match.
   *
   * @param candidates candidates
   * @return best matching component
   * @throws MultipleComponentsFoundException multiple components found
   */
  Component bestMatch(Component[] candidates) throws MultipleComponentsFoundException;
}
