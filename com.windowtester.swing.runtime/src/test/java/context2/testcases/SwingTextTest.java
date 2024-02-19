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
import com.windowtester.runtime.swing.UITestCaseSwing;
import swing.samples.SwingText;

public class SwingTextTest extends UITestCaseSwing {

  private IUIContext ui;

  public SwingTextTest() {
    super(SwingText.class);
  }

  public void testTextEntry() throws Exception {

    //		ui = getUI();
    //		ui.wait(new WindowShowingCondition("Swing Text"));
    //
    //	//	ui.click(new JTextComponentLocator(JTextField.class));
    //		ui.pause(200);
    //		ui.click(new SwingWidgetLocator(JTextField.class,"textField"));
    //		ui.enterText("foo\b\n");
    //
    //
    //		ui.click(new LabeledTextLocator("Name"));
    //		ui.enterText(" Smith");
    //		ui.assertThat(new LabeledTextLocator("Name").hasText("Jane Smith"));
    //
    //
    //		// context clicks
    //		ui.contextClick(new JTextComponentLocator(JTextField.class,0,null), new
    // JMenuItemLocator("choice2"));
    //
    //
    //		ui.contextClick(new JTextComponentLocator(JTextField.class,0,null), new
    // JMenuItemLocator("choice1"));
    //
    //
    //
    //
    //
    //		try {
    //			ui.contextClick(new JTextComponentLocator(JTextField.class,0,null),new JMenuItemLocator(
    // "bogus"));
    //			fail("should have thrown a CNF exception");
    //
    //
    //		} catch (ActionFailedException e) {
    //			fail("should not have thrown " + e);
    //		} catch (WidgetSearchException e) {
    //			// pass
    //			System.out.println("Got widget search exception");
    //		//	e.printStackTrace();
    //		}
  }
}
