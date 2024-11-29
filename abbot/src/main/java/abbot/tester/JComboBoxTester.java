package abbot.tester;

import abbot.Log;
import abbot.finder.BasicFinder;
import abbot.finder.ComponentFinder;
import abbot.finder.ComponentSearchException;
import abbot.finder.matchers.ClassMatcher;
import abbot.i18n.Strings;
import abbot.util.AWT;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JComboBoxTester extends JComponentTester {

  private final JListTester listTester = new JListTester();

  /**
   * Return an array of strings that represent the combo box list. Note that the current selection
   * might not be included, since it's possible to have a custom (edited) entry there that is not
   * included in the default contents.
   */
  public String[] getContents(JComboBox<?> cb) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < cb.getItemCount(); i++) {
      list.add(cb.getItemAt(i).toString());
    }
    return list.toArray(new String[0]);
  }

  public void actionSelectIndex(Component comp, final int index) {
    final JComboBox<?> cb = (JComboBox<?>) comp;

    // activate it, if not already showing
    if (!cb.getUI().isPopupVisible(cb)) {
      // NOTE: if the index is out of range, the selected item will be
      // one end or the other of the list.
      if (cb.isEditable()) {
        // Location of popup button activator is LAF-dependent
        invokeAndWait(() -> cb.getUI().setPopupVisible(cb, true));
      } else {
        actionClick(cb);
      }
    }
    JList<?> list = findComboList(cb);
    listTester.actionSelectIndex(list, index);
  }

  /**
   * Find the JList in the popup raised by this combo box.
   */
  public JList<?> findComboList(JComboBox<?> combobox) {
    Component popup = AWT.findActivePopupMenu();
    if (popup == null) {
      long now = System.currentTimeMillis();
      while ((popup = AWT.findActivePopupMenu()) == null) {
        if (System.currentTimeMillis() - now > popupDelay) {
          throw new ActionFailedException(Strings.get("tester.JComboBox.popup_not_found"));
        }
        sleep();
      }
    }

    Component comp = findJList((Container) popup);
    if (comp == null) {
      throw new ActionFailedException(Strings.get("tester.JComboBox.popup_not_found"));
    }
    return (JList<?>) comp;
  }

  private JList<?> findJList(Container parent) {
    try {
      ComponentFinder finder = BasicFinder.getDefault();
      return (JList<?>) finder.find(parent, new ClassMatcher(JList.class));
    } catch (ComponentSearchException e) {
      return null;
    }
  }

  /**
   * If the value looks meaningful, return it, otherwise return null.
   */
  public String getValueAsString(JComboBox<?> combo, JList<?> list, Object item, int index) {
    String value = item.toString();
    // If the value is the default Object.toString method (which
    // returns <class>@<pointer value>), try to find something better.
    if (value.startsWith(item.getClass().getName() + "@")) {
      ListCellRenderer<? super Object> renderer = (ListCellRenderer<? super Object>) combo.getRenderer();
      Component c = renderer.getListCellRendererComponent(list, item, index, true, true);
      if (c instanceof javax.swing.JLabel) {
        return ((javax.swing.JLabel) c).getText();
      }
      return null;
    }
    return value;
  }

  public void actionSelectItem(Component comp, String item) {
    JComboBox<?> cb = (JComboBox<?>) comp;
    Object obj = cb.getSelectedItem();
    if ((obj == null && item == null)
        || (obj != null && StringComparator.matches(obj.toString(), item))) {
      return;
    }

    for (int i = 0; i < cb.getItemCount(); i++) {
      obj = cb.getItemAt(i);
      Log.debug("Comparing against '" + obj + "'");
      if ((obj == null && item == null)
          || (obj != null && StringComparator.matches(obj.toString(), item))) {
        actionSelectIndex(comp, i);
        return;
      }
    }
    // While actions are supposed to represent real user actions, it's
    // possible that the current environment does not match sufficiently,
    // so we need to throw an appropriate exception that can be used to
    // diagnose the problem.
    String mid = "[";
    StringBuilder contents = new StringBuilder();
    for (int i = 0; i < cb.getItemCount(); i++) {
      contents.append(mid);
      contents.append(cb.getItemAt(i).toString());
      mid = ", ";
    }
    contents.append("]");
    throw new ActionFailedException(
        Strings.get("tester.JComboBox.item_not_found", new Object[]{item, contents.toString()}));
  }
}
