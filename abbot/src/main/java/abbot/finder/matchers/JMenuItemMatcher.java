package abbot.finder.matchers;

import abbot.finder.Matcher;
import com.windowtester.runtime.util.StringComparator;
import java.awt.*;
import javax.swing.*;

/**
 * Matches a {@link JMenuItem} given a simple label or a menu path of the format "menu|submenu|menuitem", for example
 * "File|Open|Can of worms".
 *
 * @author twall
 * @version $Id: JMenuItemMatcher.java,v 1.2 2008-01-11 20:10:25 drubel Exp $
 */
public class JMenuItemMatcher implements Matcher {
  private final String label;

  public JMenuItemMatcher(String label) {
    this.label = label;
  }

  private String getPath(JMenuItem item) {
    var parent = (Component) item.getParent();
    if (parent instanceof JPopupMenu popupMenu) {
      parent = popupMenu.getInvoker();
    }
    if (parent instanceof JMenuItem menuItem) {
      return getPath(menuItem) + "|" + item.getText();
    }
    return item.getText();
  }

  @Override
  public boolean matches(Component component) {
    if (component instanceof JMenuItem menuItem) {
      var text = menuItem.getText();
      return StringComparator.matches(text, label)
          || StringComparator.matches(getPath(menuItem), label);
    }
    return false;
  }
}
