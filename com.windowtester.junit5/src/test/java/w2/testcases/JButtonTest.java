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
package w2.testcases;

import static com.windowtester.internal.runtime.junit.core.ExecutionMonitor.getUI;

import com.windowtester.runtime.IUIContext;

public class JButtonTest
//    extends UITestCaseSwing
{

  private IUIContext ui;

  public JButtonTest() {
  //  super(SwingButton.class);
  }

  protected void setUp() throws Exception {
    ui = getUI();
  }

  /**
   * Main test method.
   */
  public void testButtons() throws Exception {
    //		IWidgetLocator locator;
    //		AbstractButton button;
    //
    //	//	IWidgetLocator[] locators = ui.findAll(new JButtonLocator("Close"));
    //
    //		ui.wait(new WindowShowingCondition("Swing Buttons"));
    //		JButtonLocator buttonLocator = new JButtonLocator("Test Button");
    //		ui.assertThat(buttonLocator.isEnabled());
    //	//	ui.ensureThat(buttonLocator.hasFocus());
    //		locator = ui.click(buttonLocator);
    //		ui.assertThat(buttonLocator.hasText("Test Button"));
    //
    //
    //		ui.assertThat(new JCheckBoxLocator("CheckBox").isEnabled());
    //		locator = ui.click(new JCheckBoxLocator("CheckBox"));
    //		ui.assertThat(new JCheckBoxLocator("CheckBox").isSelected());
    //		button = (AbstractButton)((IWidgetReference)locator).getWidget();
    //		System.out.println("Disable checkbox");
    //		button.setEnabled(false);
    //		System.out.println("Check if checkbox is disabled using isEnable condition");
    //		ui.assertThat(new JCheckBoxLocator("CheckBox").isEnabled(false));
    //
    //		JRadioButtonLocator radioButtonLocator = new JRadioButtonLocator("RadioButton");
    //		ui.assertThat(radioButtonLocator.isEnabled());
    //		ui.click(radioButtonLocator);
    //		ui.assertThat(radioButtonLocator.isSelected());
    //
    //		JToggleButtonLocator toggleButtonLocator = new JToggleButtonLocator("ToggleButton");
    //		ui.assertThat(toggleButtonLocator.isEnabled());
    //		ui.click(toggleButtonLocator);
    //		ui.assertThat(toggleButtonLocator.isSelected());
    //		ui.assertThat(toggleButtonLocator.hasText("ToggleButton"));

  }
}
