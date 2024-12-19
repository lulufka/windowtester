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
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.TreePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SwingTree;

@ExtendWith(WindowtesterExtension.class)
class JTreeTest {

  @UIUnderTest
  private SwingTree panel;

  @BeforeEach
  void setUp() {
    panel = new SwingTree("Swing Tree Example");
  }

  @Test
  void testTreeSelections(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    var firstClickTree = doTreeClick(
        1,
        "Root/Parent1/Child10/grandChild102",
        "scrollPane1",
        InputEvent.BUTTON1_DOWN_MASK,
        ui
    );

    assertEquals(1, firstClickTree.getSelectionRows().length);
    assertEquals("grandChild102", firstClickTree.getSelectionPath().getLastPathComponent().toString());

    var secondClickTree = doTreeClick(1, "Root/Parent3/Child30", "scrollPane1", ui);

    assertEquals(1, secondClickTree.getSelectionRows().length);
    assertEquals("Child30", secondClickTree.getSelectionPath().getLastPathComponent().toString());
  }

  @Test
  void testTreeShiftSelections(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    var firstClickTree = doTreeClick(
        1,
        "Root/Item 0/Node 01",
        "scrollPane2",
        ui);

    doTreeClick(
        1,
        "Root/Item 1/Node 10",
        "scrollPane2",
        InputEvent.BUTTON1_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK,
        ui);

    assertEquals(3, firstClickTree.getSelectionPaths().length);

    var tree = doTreeClick(
      1,
        "Root/Item 0/Node 01",
        "scrollPane2",
        ui
    );

    doTreeClick(
      1,
        "Root/Item 1/Node 10",
        "scrollPane2",
        InputEvent.BUTTON1_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK,
        ui
    );

    assertEquals(3, tree.getSelectionPaths().length);
  }

  @Test
  void testTreeCtrlSelections(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Example"), 1_000);

    // test ctrl clicks
//    IWidgetLocator locator = ui.click(new JTreeItemLocator("Root/Parent1/Child10/grandChild100",
//        new SwingWidgetLocator(JViewport.class,
//            new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))));
    var tree = doTreeClick(1, "Root/Parent1/Child10/grandChild100", "scrollPane1",
        InputEvent.BUTTON1_DOWN_MASK, ui);

//    ui.click(1, new JTreeItemLocator("Root/Parent1/Child10/grandChild102",
//            new SwingWidgetLocator(JViewport.class,
//                new SwingWidgetLocator(JScrollPane.class, "scrollPane1"))),
//        InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK);
//    JTree tree = (JTree) ((IWidgetReference) locator).getWidget();

    tree = doTreeClick(1, "Root/Parent1/Child10/grandChild102", "scrollPane1",
        InputEvent.BUTTON1_DOWN_MASK | InputEvent.CTRL_DOWN_MASK, ui);

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

    var tree = doTreeClick(2, "Root/Parent2", "scrollPane1", ui);

    var path = tree.getSelectionPath();

    assertTrue(tree.isExpanded(path));
  }

  private JTree doTreeClick(int clickCount, String node, String scrollPane, IUIContext ui)
      throws WidgetSearchException {
    final var treeItemLocator = createTreeItemLocator(node, scrollPane);

    var locator = ui.click(clickCount, treeItemLocator);
    return (JTree) ((IWidgetReference) locator).getWidget();
  }

  private JTree doTreeClick(int clickCount, String node, String scrollPane, int mask, IUIContext ui)
      throws WidgetSearchException {
    var treeItemLocator = createTreeItemLocator(node, scrollPane);
    var locator = ui.click(clickCount, treeItemLocator, mask);
    return (JTree) ((IWidgetReference) locator).getWidget();
  }

  private JTreeItemLocator createTreeItemLocator(String node, String scrollPane) {
    return new JTreeItemLocator(
        node,
        new SwingWidgetLocator(
            JViewport.class, new SwingWidgetLocator(JScrollPane.class, scrollPane)
        )
    );
  }
}
