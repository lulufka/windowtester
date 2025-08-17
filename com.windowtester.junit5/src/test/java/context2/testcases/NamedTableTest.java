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

import com.windowtester.junit5.SwingUIContext;
import com.windowtester.junit5.UIUnderTest;
import com.windowtester.junit5.WindowtesterExtension;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JTableItemLocator;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SwingTables;

@ExtendWith(WindowtesterExtension.class)
class NamedTableTest {

  @UIUnderTest(title = "TableDemo2")
  private SwingTables panel = new SwingTables();

  @Test
  void testNamedTable(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("TableDemo2"), 1_000);

    ui.click(new JTableItemLocator(new Point(1, 1), "table1"));
    ui.click(new JTableItemLocator(new Point(3, 2), "table1"));
    ui.click(new JTableItemLocator(new Point(1, 0), "table2"));
    ui.click(new JTableItemLocator(new Point(3, 1), "table2"));
  }
}
