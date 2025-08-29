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
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JTableItemLocator;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SimpleTable;

@ExtendWith(WindowtesterExtension.class)
class SimpleTableTest {

  @UIUnderTest(title = "Simple Table Demo")
  private SimpleTable panel = new SimpleTable();

  @Test
  void testMain(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("Simple Table Demo"), 1_0000);

    ui.click(new JTableItemLocator(new Point(0, 1)));
    ui.click(new JTableItemLocator(new Point(2, 3)));
  }
}
