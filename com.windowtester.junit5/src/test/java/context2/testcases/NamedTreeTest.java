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
import com.windowtester.runtime.swing.locator.JTreeItemLocator;
import com.windowtester.runtime.swing.locator.NamedWidgetLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SwingTree;

@ExtendWith(WindowtesterExtension.class)
class NamedTreeTest {

  @UIUnderTest(title = "Swing Tree Demo")
  private SwingTree panel = new SwingTree("Swing Tree Demo");

  @Test
  void testNamedTrees(@SwingUIContext IUIContext ui) throws WidgetSearchException {
    ui.wait(new WindowShowingCondition("Swing Tree Demo"), 1_0000);

    ui.click(
        new JTreeItemLocator(
            "Root/Parent1/Child10/grandChild102", new NamedWidgetLocator("tree1")));
    ui.click(new JTreeItemLocator("Root/Item 1/Node 10/", new NamedWidgetLocator("tree2")));

    ui.click(new JTreeItemLocator("Root/Parent3/Child31", new NamedWidgetLocator("tree1")));
    ui.click(new JTreeItemLocator("Root/Item 3", new NamedWidgetLocator("tree2")));
  }
}
