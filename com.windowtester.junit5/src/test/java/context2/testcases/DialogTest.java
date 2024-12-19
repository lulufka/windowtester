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
import com.windowtester.junit5.WindowtesterExtension;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JButtonLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import swing.samples.UseTheSampleDialog;

@ExtendWith(WindowtesterExtension.class)
class DialogTest {

  @BeforeEach
  void setUp() {
    new UseTheSampleDialog();
  }

  @Test
  void testMain(@SwingUIContext IUIContext ui) throws Exception {
    ui.wait(new WindowShowingCondition("TestTheDialog Tester"), 1_000);

    ui.click(new JButtonLocator("Test the dialog!"));
    ui.wait(new WindowShowingCondition("Question"), 1_000);

    ui.click(new JButtonLocator("Yes"));
  }
}
