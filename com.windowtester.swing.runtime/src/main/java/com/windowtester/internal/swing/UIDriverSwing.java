/*******************************************************************************
 *  Copyright (component) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *******************************************************************************/
package com.windowtester.internal.swing;

import abbot.finder.AWTHierarchy;
import abbot.script.Condition;
import abbot.tester.ActionFailedException;
import abbot.tester.ComponentLocation;
import abbot.tester.ComponentMissingException;
import abbot.tester.ComponentTester;
import abbot.tester.JComboBoxTester;
import abbot.tester.JListLocation;
import abbot.tester.JTabbedPaneLocation;
import abbot.tester.JTabbedPaneTester;
import abbot.tester.JTableLocation;
import abbot.tester.JTextComponentTester;
import abbot.tester.JTreeLocation;
import abbot.tester.LocationUnavailableException;
import abbot.util.Properties;
import com.windowtester.internal.swing.util.KeyStrokeDecoder;
import com.windowtester.internal.tester.swing.JListTester;
import com.windowtester.internal.tester.swing.JTableTester;
import com.windowtester.internal.tester.swing.JTreeTester;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;

/**
 * A service that drives UI events.
 * <br>
 * The prefered way to access these functions is through a <code>UIContextSwing</code> instance.
 *
 * @see com.windowtester.internal.swing.UIContextSwing
 */
public class UIDriverSwing {

  /**
   * Base delay setting.
   */
  private static final int DEFAULT_TIMEOUT =
      Properties.getProperty("abbot.robot.default_delay", 30000, 0, 60000);

  private static final int SLEEP_INTERVAL = 10;

  public static int getDefaultTimeout() {
    return DEFAULT_TIMEOUT;
  }

  public static int getDefaultSleepInterval() {
    return SLEEP_INTERVAL;
  }

  private Component dragSource;
  private int dragSrcX;
  private int dragSrcY;

  ////////////////////////////////////////////////////////////////////////////
  //
  // Constructor
  //
  // //////////////////////////////////////////////////////////////////////////

  public UIDriverSwing() {
    ComponentTester.setTester(JList.class, new JListTester());
    ComponentTester.setTester(JTree.class, new JTreeTester());
    ComponentTester.setTester(JTable.class, new JTableTester());
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Click actions
  //
  ///////////////////////////////////////////////////////////////////////////

  public Component click(int clickCount, Component component, int x, int y, int mask) {
    ComponentTester tester = ComponentTester.getTester(component);
    dragSource = component;
    dragSrcX = x;
    dragSrcY = y;
    tester.actionClick(component, x, y, mask, clickCount);

    return component;
  }

  public Component click(Component component, String labelOrPath) throws ActionFailedException {
    // not checking for awt.MenuItem, not a subclass of Component
    ComponentTester tester = ComponentTester.getTester(component);
    dragSource = component;

    if (component instanceof JTable) {
      JTableLocation location = new JTableLocation(labelOrPath);
      Point cellLocation = location.getPoint(component);
      dragSrcX = cellLocation.x;
      dragSrcY = cellLocation.y;
      ((JTableTester) tester).actionSelectCell(component, new JTableLocation(labelOrPath));
    }
    if (component instanceof JTabbedPane) {
      ((JTabbedPaneTester) tester).actionSelectTab(component, new JTabbedPaneLocation(labelOrPath));
    }
    return component;
  }

  public Component clickTreeItem(int clickCount, Component component, String path, int mask) {
    //		 convert string to TreePath
    String[] nodeNames = path.split("/");
    TreePath treePath = new TreePath(nodeNames);
    dragSource = component;
    JTreeLocation location = new JTreeLocation(treePath);

    ComponentTester tester = ComponentTester.getTester(component);
    ((JTreeTester) tester).actionMakeVisible(component, treePath);
    try {
      Point point = location.getPoint(component);
      dragSrcX = point.x;
      dragSrcY = point.y;
    } catch (LocationUnavailableException e) {
      // do nothing
      System.out.println("Caught location unavailable exception");
    }
    ((JTreeTester) tester).actionSelectPath(clickCount, component, treePath, mask);
    return component;
  }

  /**
   * click on table cell selection based on row and col
   *
   * @param table table to click on
   * @param row   row index
   * @param col   column index
   * @return TODO: check with given string, if any whether we have the right cell
   */
  public Component clickTable(int clickCount, JTable table, int row, int col, int mask) {
    ComponentTester tester = ComponentTester.getTester(table);
    JTableLocation location = new JTableLocation(row, col);
    Point cellLocation = location.getPoint(table);
    dragSrcX = cellLocation.x;
    dragSrcY = cellLocation.y;
    dragSource = table;
    ((JTableTester) tester).actionSelectCell(clickCount, table, new JTableLocation(row, col), mask);
    return table;
  }

  public Component clickMenuItem(JMenuItem owner) {
    ComponentTester tester = ComponentTester.getTester(owner);
    tester.actionSelectMenuItem(owner);
    return owner;
  }

  public Component clickListItem(int clickCount, JList<?> list, String labelOrPath, int mask)
      throws ActionFailedException {
    ComponentTester tester = ComponentTester.getTester(list);
    dragSource = list;
    JListLocation location = new JListLocation(labelOrPath);
    Point point = location.getPoint(list);
    dragSrcX = point.x;
    dragSrcY = point.y;
    ((JListTester) tester).actionMultipleClick(list, clickCount, labelOrPath, mask);
    return list;
  }

  public Component clickComboBox(JComboBox<?> combobox, String labelOrPath, int clickCount)
      throws ActionFailedException {
    ComponentTester tester = ComponentTester.getTester(combobox);
    if (labelOrPath != null) {
      ((JComboBoxTester) tester).actionSelectItem(combobox, labelOrPath);
    } else {
      tester.actionClick(combobox, new ComponentLocation(), InputEvent.BUTTON1_DOWN_MASK,
          clickCount);
    }
    return combobox;
  }

  /**
   * Click at a particular position in a text component
   *
   * @param textComponent text component
   * @param caret         caret position
   * @return the text component
   */
  public Component clickTextComponent(JTextComponent textComponent, int caret) {
    ComponentTester tester = ComponentTester.getTester(textComponent);
    try {
      Rectangle2D rect = textComponent.modelToView2D(caret);
      dragSource = textComponent;
      dragSrcX = (int) (rect.getX() + rect.getWidth() / 2);
      dragSrcY = (int) (rect.getY() + rect.getHeight() / 2);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
    ((JTextComponentTester) tester).actionClick(textComponent, caret);
    return textComponent;
  }

  public Component contextClick(Component widget, String path)
      throws WidgetSearchException, ActionFailedException {
    ComponentTester tester = ComponentTester.getTester(widget);
    try {
      tester.actionSelectPopupMenuItem(widget, path);
    } catch (ComponentMissingException e) {
      throw new WidgetSearchException("Cant find menu item" + path);
    }
    return widget;
  }

  public Component contextClick(Component widget, int x, int y, String path)
      throws WidgetSearchException, ActionFailedException {
    ComponentTester tester = ComponentTester.getTester(widget);
    try {
      tester.actionSelectPopupMenuItem(widget, x, y, path);
    } catch (ComponentMissingException e) {
      throw new WidgetSearchException("Cant find menu item" + path);
    }
    return widget;
  }

  /**
   * Context click on tree node
   *
   * @param tree     tree component
   * @param itemPath tree item path
   * @param menuPath menu path
   * @return tree component
   * @throws ComponentMissingException
   * @throws ActionFailedException
   */
  public Component contextClickTree(JTree tree, String itemPath, String menuPath)
      throws ComponentMissingException, ActionFailedException {
    String[] nodeNames = itemPath.split("/");
    TreePath path = new TreePath(nodeNames);
    ComponentTester tester = ComponentTester.getTester(tree);
    ((JTreeTester) tester).actionMakeVisible(tree, path);
    tester.actionSelectPopupMenuItem(tree, new JTreeLocation(path), menuPath);
    return tree;
  }

  /**
   * Context click on a table
   *
   * @param table    table component
   * @param row      row index
   * @param col      column index
   * @param menuPath menu path
   * @return table component
   * @throws ComponentMissingException
   * @throws ActionFailedException
   */
  public Component contextClickTable(JTable table, int row, int col, String menuPath)
      throws ComponentMissingException, ActionFailedException {
    ComponentTester tester = ComponentTester.getTester(table);
    JTableLocation location = new JTableLocation(row, col);
    tester.actionShowPopupMenu(table, location);
    tester.actionSelectPopupMenuItem(table, location, menuPath);
    return table;
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Primitive mouse action commands
  //
  ///////////////////////////////////////////////////////////////////////////

  public void mouseMove(Component component) {
    mouseMove(component, component.getWidth() / 2, component.getHeight() / 2);
  }

  public void mouseMove(Component component, int x, int y) {
    ComponentTester tester = ComponentTester.getTester(component);
    tester.mouseMove(component, x, y);
  }

  public void mouseMove(int x, int y) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.mouseMove(x, y);
  }

  /**
   * Press the mouse.
   *
   * @param accel - the mouse accelerator.
   * @since 3.8.1
   */
  public void mouseDown(int accel) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.mousePress(accel);
  }

  /**
   * Release the mouse
   *
   * @param accel - the mouse accelerator.
   * @since 3.8.1
   */
  public void mouseUp(int accel) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.mouseRelease(accel);
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Drag and drop actions
  //
  ///////////////////////////////////////////////////////////////////////////

  /**
   * @since 3.8.1
   */
  public void doDragTo(Component target, int x, int y) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.drag(dragSource, dragSrcX, dragSrcY, InputEvent.BUTTON1_DOWN_MASK);
    tester.mouseMove(target, x, y);
    tester.drop(target, x, y);
    waitForIdle();
  }

  /**
   * @since 3.8.1
   */
  public Point getLocation(Component component, String path) {
    if (component instanceof JList) {
      JListLocation location = new JListLocation(path);
      return location.getPoint(component);
    }
    if (component instanceof JTree) {
      String[] nodeNames = path.split("/");
      JTreeLocation location = new JTreeLocation(new TreePath(nodeNames));
      return location.getPoint(component);
    }
    ComponentLocation location = new ComponentLocation();
    return location.getPoint(component);
  }

  /**
   * @since 3.8.1
   */
  public Point getLocation(Component component, int row, int col) {
    JTableLocation location = new JTableLocation(row, col);
    return location.getPoint(component);
  }

  /**
   * @since 3.8.1
   */
  public Point getLocation(Component component, int index) {
    JTextComponentTester tester =
        (JTextComponentTester) ComponentTester.getTester(JTextComponent.class);
    return tester.scrollToVisible(component, index);
  }

  /**
   * @since 3.8.1
   */
  public Point getLocation(Component component) {
    ComponentLocation location = new ComponentLocation();
    return location.getPoint(component);
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Text entry actions
  //
  ///////////////////////////////////////////////////////////////////////////

  public void enterText(String txt) throws ActionFailedException {
    // get the component and the tester
    ComponentTester tester = ComponentTester.getTester(Component.class);
    Component widget = tester.findFocusOwner();
    tester.actionKeyString(widget, txt);
  }

  // TODO: this needs to be fixed
  public void keyClick(int key) {
    ComponentTester tester = ComponentTester.getTester(Component.class);

    int[] extractedKeys = KeyStrokeDecoder.extractKeys(key);
    int modifiers = KeyStrokeDecoder.extractModifiers(key);
    for (int extractedKey : extractedKeys) {
      tester.actionKeyStroke(extractedKey, modifiers);
    }
  }

  public void keyClick(char key) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.keyStroke(key);
  }

  public void keyClick(int modifiers, char c) {
    ComponentTester tester = ComponentTester.getTester(Component.class);

    boolean shift = (modifiers & InputEvent.SHIFT_DOWN_MASK) != 0;
    if (shift) {
      tester.actionKeyPress(KeyEvent.VK_SHIFT);
    }

    boolean alt = (modifiers & InputEvent.ALT_DOWN_MASK) != 0;
    if (alt) {
      tester.actionKeyPress(KeyEvent.VK_ALT);
    }

    boolean ctrl = (modifiers & InputEvent.CTRL_DOWN_MASK) != 0;
    if (ctrl) {
      tester.actionKeyPress(KeyEvent.VK_CONTROL);
    }

    tester.keyStroke(c);
    waitForIdle();

    if (ctrl) {
      tester.actionKeyRelease(KeyEvent.VK_CONTROL);
    }
    if (alt) {
      tester.actionKeyRelease(KeyEvent.VK_ALT);
    }
    if (shift) {
      tester.actionKeyRelease(KeyEvent.VK_SHIFT);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // "Meta" actions
  //
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Set the focus on to the given component.
   */
  public void setFocus(Component component) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.actionFocus(component);
  }

  public void close(Window window) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.close(window);
  }

  ///////////////////////////////////////////////////////////////////////////
  //
  // Timing
  //
  ///////////////////////////////////////////////////////////////////////////

  public void waitForIdle() {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.actionWaitForIdle();
  }

  public void waitForWindowShowing(final String windowName, final int timeout) {

    wait(
        new Condition() {
          public boolean test() {
            return assertComponentShowing(windowName);
          }

          public String toString() {
            return windowName + " to show";
          }
        },
        timeout);
  }

  boolean boolT;

  private boolean assertComponentShowing(final String title) {
    var components = collectComponents();
    for (Component component : components) {
      if (component instanceof Frame
          && isFrameShowing(title, (Frame) component)) {
        return true;
      }
      if (component instanceof Dialog
          && isDialogShowing(title, (Dialog) component)) {
        return true;
      }
    }
    return false;
  }

  private boolean isDialogShowing(String expectedTitle, Dialog dialog) {
    var actualTitle = dialog.getTitle();
    return isComponentShowing(expectedTitle, actualTitle, dialog);
  }

  private boolean isFrameShowing(String expectedTitle, Frame window) {
    var actualTitle = window.getTitle();
    return isComponentShowing(expectedTitle, actualTitle, window);
  }

  private boolean isComponentShowing(
      String expectedTitle,
      String actualTitle,
      Window window) {
    return StringComparator.matches(actualTitle, expectedTitle)
        && window.isDisplayable()
        && window.isVisible()
        && window.isActive();
  }

  private List<Component> collectComponents() {
    List<Component> componentList = new ArrayList<>();
    AWTHierarchy hierarchy = new AWTHierarchy();
    for (Component component : hierarchy.getRoots()) {
      componentList.addAll(hierarchy.getComponents(component));
    }
    componentList.addAll(hierarchy.getRoots());
    return componentList;
  }

  public void pause(int ms) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.actionDelay(ms);
  }

  public void wait(Condition condition) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.wait(condition);
  }

  public void wait(Condition condition, long timeout) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.wait(condition, timeout);
  }

  public void wait(Condition condition, long timeout, int interval) {
    ComponentTester tester = ComponentTester.getTester(Component.class);
    tester.wait(condition, timeout, interval);
  }
}
