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

public class ContactManagerSwingTest extends UITestCaseSwing {

    private IUIContext ui;

    public ContactManagerSwingTest() {
//		super(ContactManagerSwing.class);
    }

    protected void setUp() throws Exception {
        ui = getUI();
    }

    public void testNewContactWizard() throws WidgetSearchException {
        //	ui.wait(new WindowShowingCondition("Contact Manager"));
        //	ui.pause(1000);
//		ui.click(new JMenuItemLocator("File/New Contact"));
//		ui.wait(new WindowShowingCondition("New Contact"));
//	//	ui.pause(2000);
//		ui.click(new LabeledTextLocator("First Name: "));
//		ui.enterText("John");
//		//ui.pause(500);
//		ui.click(new LabeledTextLocator("Last Name: "));
//		ui.enterText("Smith");
//		ui.click(new LabeledTextLocator("Street: "));
//		ui.enterText("1400 Washington street");
//		ui.click(new JButtonLocator("Finish"));
//		ui.wait(new WindowDisposedCondition("New Contact"));
//
//
//		//ui.pause(1000);
//
//		//ui.pause(1000);
//
//		ui.click(2,new JListLocator("James,Bond"));
//		ui.click(2,new JListLocator("John,Smith"));
//
//		ui.click(new JTabbedPaneLocator("James,Bond"));
//
//		ui.click(new LabeledTextLocator("Street: "));
//		ui.enterText("560 SW Walker Rd");

        //ui.pause(2000);
    }

}
