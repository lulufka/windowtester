package abbot.tester;

import abbot.i18n.Strings;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JList;

/**
 * Provides encapsulation of the location of a row on a JList (a coordinate, item index or value).
 */
public class JListLocation extends ComponentLocation {

  private String value = null;
  private int row = -1;

  public JListLocation() {
    // do nothing
  }

  public JListLocation(String value) {
    this.value = value;
  }

  public JListLocation(int row) {
    if (row < 0) {
      String msg = Strings.get("tester.JList.invalid_index", new Object[] {row});
      throw new LocationUnavailableException(msg);
    }
    this.row = row;
  }

  public JListLocation(Point where) {
    super(where);
  }

  @Override
  protected String badFormat(String encoded) {
    return Strings.get("location.list.bad_format", new Object[] {encoded});
  }

  /**
   * Convert the given index into a coordinate.
   */
  protected Point indexToPoint(JList<String> list, int index) {
    if (index < 0 || index >= list.getModel().getSize()) {
      String msg = Strings.get("tester.JList.invalid_index", new Object[] {index});
      throw new LocationUnavailableException(msg);
    }
    Rectangle rect = list.getCellBounds(index, index);
    return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
  }

  /**
   * Find the first String match in the list and return the index.
   */
  private int valueToIndex(JList<?> list, String value) {
    int size = list.getModel().getSize();
    for (int i = 0; i < size; i++) {
      String str = JListTester.valueToString(list, i);
      if (StringComparator.matches(str, value)) {
        return i;
      }
    }
    return -1;
  }

  public int getIndex(JList<?> list) {
    if (value != null) {
      return valueToIndex(list, value);
    }
    if (row != -1) {
      return row;
    }
    return list.locationToIndex(super.getPoint(list));
  }

  @Override
  public Point getPoint(Component component) {
    JList<String> list = (JList<String>) component;
    if (value != null || row != -1) {
      return indexToPoint(list, getIndex(list));
    }
    return super.getPoint(list);
  }

  @Override
  public Rectangle getBounds(Component component) {
    JList<String> list = (JList<String>) component;
    int index = getIndex(list);
    if (index == -1) {
      String msg = Strings.get("tester.JList.invalid_index", new Object[] {index});
      throw new LocationUnavailableException(msg);
    }
    return list.getCellBounds(index, index);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof JListLocation) {
      JListLocation loc = (JListLocation) o;
      if (value != null) {
        return value.equals(loc.value);
      }
      if (row != -1) {
        return row == loc.row;
      }
    }
    return super.equals(o);
  }

  @Override
  public String toString() {
    if (value != null) {
      return encodeValue(value);
    }
    if (row != -1) {
      return encodeIndex(row);
    }
    return super.toString();
  }

  @Override
  public ComponentLocation parse(String encoded) {
    encoded = encoded.trim();
    if (isValue(encoded)) {
      value = parseValue(encoded);
      return this;
    }
    if (isIndex(encoded)) {
      row = parseIndex(encoded);
      return this;
    }
    return super.parse(encoded);
  }
}
