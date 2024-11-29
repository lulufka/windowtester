/*******************************************************************************
 *  Copyright (c) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *******************************************************************************/
package context2.testcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.windowtester.junit5.SwingUIContext;
import com.windowtester.junit5.UIUnderTest;
import com.windowtester.junit5.WindowtesterExtension;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swing.SwingWidgetLocator;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JMenuItemLocator;
import com.windowtester.runtime.swing.locator.JTreeItemLocator;
import java.awt.event.InputEvent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.TreePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SwingTree;

@ExtendWith(WindowtesterExtension.class)
class JTreeTest {

  @UIUnderTest
  private SwingTree panel = new SwingTree("Swing Tree Example");

  @Test
  void testTreeSelections(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

//    IWidgetLocator locator = ui.click(new JTreeItemLocator("Root/Parent1/Child10/grandChild102",
//        new SwingWidgetLocator(JViewport.class,
//            new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))));
//    JTree tree = (JTree) ((IWidgetReference) locator).getWidget();
    JTree tree = doTreeClick(
        1,
        "Root/Parent1/Child10/grandChild102",
        "scrollPane1",
        InputEvent.BUTTON1_DOWN_MASK,
        ui
    );

    TreePath path = tree.getSelectionPath();
    int[] items = tree.getSelectionRows();
    assertEquals(1, items.length);
    assertEquals("grandChild102", path.getLastPathComponent().toString());

    ui.click(new JTreeItemLocator("Root/Parent3/Child30",
        new SwingWidgetLocator(JViewport.class,
            new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))));
    path = tree.getSelectionPath();
    items = tree.getSelectionRows();
    assertEquals(1, items.length);
    assertEquals("Child30", path.getLastPathComponent().toString());

  }

  @Test
  void testTreeShiftSelections(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    // tree selections
    JTree tree =
        doTreeClick(1, "Root/Parent1/Child10/grandChild102", "scrollPane1",
            InputEvent.BUTTON1_DOWN_MASK, ui);
    TreePath path = tree.getSelectionPath();
    int[] items = tree.getSelectionRows();
    assertEquals(1, items.length);
    assertEquals("grandChild102", path.getLastPathComponent().toString());

    tree = doTreeClick(1, "Root/Parent3/Child30", "scrollPane1", InputEvent.BUTTON1_DOWN_MASK, ui);
    path = tree.getSelectionPath();
    items = tree.getSelectionRows();
    assertEquals(1, items.length);
    assertEquals("Child30", path.getLastPathComponent().toString());

    // test shift clicks
    tree = doTreeClick(1, "Root/Item 0/Node 01", "scrollPane2", InputEvent.BUTTON1_DOWN_MASK, ui);
    doTreeClick(1, "Root/Item 1/Node 10", "scrollPane2", InputEvent.BUTTON1_DOWN_MASK |
        InputEvent.SHIFT_DOWN_MASK, ui);
    TreePath[] paths = tree.getSelectionPaths();
    assertEquals(3, paths.length);

    IWidgetLocator locator = ui.click(new JTreeItemLocator("Root/Item 0/Node 01",
        new SwingWidgetLocator(JViewport.class,
            new SwingWidgetLocator(JScrollPane.class, "scrollPane2"))));
    ui.click(1, new JTreeItemLocator("Root/Item 1/Node 10",
            new SwingWidgetLocator(JViewport.class,
                new SwingWidgetLocator(JScrollPane.class, "scrollPane2"))),
        InputEvent.BUTTON1_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

    tree = (JTree) ((IWidgetReference) locator).getWidget();
    paths = tree.getSelectionPaths();
    assertEquals(3, paths.length);

  }

  @Test
  void testTreeCtrlSelections(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    // test ctrl clicks
    IWidgetLocator locator = ui.click(new JTreeItemLocator("Root/Parent1/Child10/grandChild100",
        new SwingWidgetLocator(JViewport.class,
            new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))));
//    doTreeClick(1, "Root/Parent1/Child10/grandChild100", "scrollPane1",
//        InputEvent.BUTTON1_DOWN_MASK, ui);

    ui.click(1, new JTreeItemLocator("Root/Parent1/Child10/grandChild102",
            new SwingWidgetLocator(JViewport.class,
                new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))),
        InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK);

//    JTree tree = doTreeClick(1, "Root/Parent1/Child10/grandChild102", "scrollPane1",
//        InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK, ui);

    JTree tree = (JTree) ((IWidgetReference) locator).getWidget();
    TreePath[] paths = tree.getSelectionPaths();
    assertEquals(1, paths.length);
  }

  @Test
  void testContextMenuSelection(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    assertFalse(panel.isChoice1());
    ui.contextClick(new JTreeItemLocator("Root/Item 1/Node 11",
            new SwingWidgetLocator(JViewport.class,
                new SwingWidgetLocator(JScrollPane.class, "scrollPane2"))),
        new JMenuItemLocator("choice1"));

    assertTrue(panel.isChoice1());

    assertFalse(panel.isChoice2());
    ui.contextClick(new JTreeItemLocator("Root/Item 2/Node 21",
            new SwingWidgetLocator(JViewport.class,
                new SwingWidgetLocator(JScrollPane.class, "scrollPane2"))),
        new JMenuItemLocator("choice2"));
    assertTrue(panel.isChoice2());
  }

  @Test
  void testDoubleClicks(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    IWidgetLocator locator = ui.click(2, new JTreeItemLocator("Root/Parent2",
        new SwingWidgetLocator(JViewport.class,
            new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))));

    JTree tree = (JTree) ((IWidgetReference) locator).getWidget();
    TreePath path = tree.getSelectionPath();
    assertTrue(tree.isExpanded(path));

  }

  private JTree doTreeClick(int clickCount, String node, String scrollPane, int mask, IUIContext ui)
      throws WidgetSearchException {
    IWidgetLocator locator = ui.click(
        clickCount,
        new JTreeItemLocator(
            node,
            new SwingWidgetLocator(
                JViewport.class, new SwingWidgetLocator(JScrollPane.class, scrollPane))),
        mask);
    return (JTree) ((IWidgetReference) locator).getWidget();
  }
}
