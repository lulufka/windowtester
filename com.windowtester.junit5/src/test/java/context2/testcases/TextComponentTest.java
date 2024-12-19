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
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JTextPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.TextComponentDemo;

@ExtendWith(WindowtesterExtension.class)
class TextComponentTest {

  @UIUnderTest(title = "Text Component Demo", width = 800, height = 600)
  private TextComponentDemo panel = new TextComponentDemo();

  @Test
  void testMain(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("Text Component Demo"), 1_000);

    JTextComponentLocator textComponentLocator = new JTextComponentLocator(253, JTextPane.class);
    ui.assertThat(textComponentLocator.isEnabled());

    ui.click(textComponentLocator);
    ui.keyClick(InputEvent.CTRL_DOWN_MASK, 'b');
    ui.enterText("the ");

    ui.click(new JTextComponentLocator(89, JTextPane.class));
    ui.enterText("from ");

    ui.keyClick(InputEvent.SHIFT_DOWN_MASK | KeyEvent.VK_LEFT);
    ui.keyClick(InputEvent.SHIFT_DOWN_MASK | KeyEvent.VK_LEFT);
    ui.keyClick(InputEvent.SHIFT_DOWN_MASK | KeyEvent.VK_LEFT);
    //		ui.keyClick(InputEvent.SHIFT_DOWN_MASK | KeyEvent.VK_LEFT);
    //		ui.keyClick(InputEvent.SHIFT_DOWN_MASK | KeyEvent.VK_LEFT);
    ui.pause(1000);
  }
}
