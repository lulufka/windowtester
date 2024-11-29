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
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JButtonLocator;
import com.windowtester.runtime.swing.locator.JCheckBoxLocator;
import com.windowtester.runtime.swing.locator.JRadioButtonLocator;
import com.windowtester.runtime.swing.locator.JToggleButtonLocator;
import javax.swing.AbstractButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.SwingButton;

@ExtendWith(WindowtesterExtension.class)
class JButtonTest {

  @UIUnderTest(title = "Swing Buttons")
  private SwingButton button = new SwingButton();

  @Test
  void testButtons(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("Swing Buttons"), 1_000);

    var buttonLocator = new JButtonLocator("Test Button");
    ui.assertThat(buttonLocator.isEnabled());
    ui.click(buttonLocator);
    ui.assertThat(buttonLocator.hasText("Test Button"));

    var checkboxLocator = new JCheckBoxLocator("CheckBox");
    ui.assertThat(checkboxLocator.isEnabled());
    IWidgetLocator locator = ui.click(checkboxLocator);
    ui.assertThat(checkboxLocator.isSelected());

    AbstractButton button = (AbstractButton) ((IWidgetReference) locator).getWidget();

    // Disable checkbox
    button.setEnabled(false);

    // Check if checkbox is disabled using isEnable condition
    ui.assertThat(checkboxLocator.isEnabled(false));

    JRadioButtonLocator radioButtonLocator = new JRadioButtonLocator("RadioButton");
    ui.assertThat(radioButtonLocator.isEnabled());
    ui.click(radioButtonLocator);
    ui.assertThat(radioButtonLocator.isSelected());

    JToggleButtonLocator toggleButtonLocator = new JToggleButtonLocator("ToggleButton");
    ui.assertThat(toggleButtonLocator.isEnabled());
    ui.click(toggleButtonLocator);
    ui.assertThat(toggleButtonLocator.isSelected());
    ui.assertThat(toggleButtonLocator.hasText("ToggleButton"));
  }
}
