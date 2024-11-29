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

import com.windowtester.junit5.SwingUIContext;
import com.windowtester.junit5.UIUnderTest;
import com.windowtester.junit5.WindowtesterExtension;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JMenuItemLocator;
import com.windowtester.runtime.swing.locator.JTableItemLocator;
import java.awt.Point;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SimpleTable;

@ExtendWith(WindowtesterExtension.class)
class JTableTest {

  @UIUnderTest(title = "TableDemo")
  private SimpleTable panel = new SimpleTable();

  @Test
  void testTableItemClicks(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("TableDemo"), 1_000);

    IWidgetLocator locator = ui.click(new JTableItemLocator(new Point(0, 0)));

    JTable table = (JTable) ((IWidgetReference) locator).getWidget();
    ui.assertThat(new JTableItemLocator(new Point(0, 0)).isSelected());
    assertEquals(1, table.getSelectedRowCount());

    locator = ui.click(1, new JTableItemLocator(new Point(2, 0)), InputEvent.BUTTON1_DOWN_MASK |
        InputEvent.SHIFT_DOWN_MASK);
    table = (JTable) ((IWidgetReference) locator).getWidget();
    assertEquals(3, table.getSelectedRowCount());
    ui.assertThat(new JTableItemLocator(new Point(2, 0)).isSelected());
    ui.assertThat(new JTableItemLocator(new Point(1, 0)).isSelected());
    ui.assertThat(new JTableItemLocator(new Point(0, 0)).isSelected());

    locator = ui.click(new JTableItemLocator(new Point(0, 0)));
    table = (JTable) ((IWidgetReference) locator).getWidget();

    assertEquals(1, table.getSelectedRowCount());

    ui.click(1, new JTableItemLocator(new Point(2, 0)), InputEvent.BUTTON1_DOWN_MASK |
        InputEvent.CTRL_DOWN_MASK);
    assertEquals(table.getSelectedRowCount(), 2);
    ui.assertThat(new JTableItemLocator(new Point(1, 0)).isSelected(false));
  }

  @Test
  void testTableRowClicks(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    IWidgetLocator locator = ui.click(new JTableItemLocator(new Point(0, 1)));

    JTable table = (JTable) ((IWidgetReference) locator).getWidget();
    TableModel model = table.getModel();
    assertEquals(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()), "two");

    ui.click(new JTableItemLocator(new Point(1, 3)));
    assertEquals(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()), "eight");

    ui.click(new JTableItemLocator(new Point(5, 1)));
    assertEquals(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()), "twenty-two");

    ui.click(new JTableItemLocator(new Point(0, 2)));
    assertEquals(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()), "three");

    ui.click(new JMenuItemLocator("File/Exit"));
  }

  @Test
  void testCtrlClicks(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    IWidgetLocator locator = ui.click(new JTableItemLocator(new Point(0, 0)));

    JTable table = (JTable) ((IWidgetReference) locator).getWidget();

    ui.assertThat(new JTableItemLocator(new Point(0, 0)).isSelected());
    assertEquals(1, table.getSelectedRowCount());

    ui.click(1, new JTableItemLocator(new Point(2, 0)), InputEvent.BUTTON1_DOWN_MASK |
        InputEvent.CTRL_DOWN_MASK);

    ui.assertThat(new JTableItemLocator(new Point(0, 0)).isSelected());
    ui.assertThat(new JTableItemLocator(new Point(2, 0)).isSelected());

    assertEquals(table.getSelectedRowCount(), 2);
  }
}
