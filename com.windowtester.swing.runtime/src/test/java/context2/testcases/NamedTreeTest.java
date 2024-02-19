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

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.swing.locator.JTreeItemLocator;
import com.windowtester.runtime.swing.locator.NamedWidgetLocator;
import swing.samples.SwingTree;

public class NamedTreeTest extends UITestCaseSwing {

    public NamedTreeTest() {
        super(SwingTree.class);
    }

    public void testNamedTrees() throws WidgetSearchException {
        IUIContext ui = getUI();

        ui.click(new JTreeItemLocator("Root/Parent1/Child10/grandChild102",
                new NamedWidgetLocator("tree1")));
        ui.click(new JTreeItemLocator("Root/Item 1/Node 10/",
                new NamedWidgetLocator("tree2")));

        ui.click(new JTreeItemLocator("Root/Parent3/Child31",
                new NamedWidgetLocator("tree1")));
        ui.click(new JTreeItemLocator("Root/Item 3",
                new NamedWidgetLocator("tree2")));

    }
}
