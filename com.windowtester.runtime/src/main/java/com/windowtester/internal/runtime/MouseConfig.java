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
package com.windowtester.internal.runtime;

import com.windowtester.runtime.WT;
import java.awt.event.MouseEvent;

public class MouseConfig {

  public static final int BUTTON_MASK = WT.BUTTON_MASK;

  /**
   * Constant that specifies whether primary and secondary mouse buttons have been swapped.
   */
  public static final boolean BUTTONS_REMAPPED = false;

  /**
   * Constant that identifies the user specified primary mouse button.
   */
  public static final int PRIMARY_BUTTON = BUTTONS_REMAPPED ? 3 : 1;

  /**
   * Constant that identifies the user specified secondary mouse button.
   */
  public static final int SECONDARY_BUTTON = BUTTONS_REMAPPED ? 1 : 3;

  public static final int UNSPECIFIED = 0;

  /**
   * Given a mouse accelerator, extract the button value.  For use in synthesizing raw events. NOTE:
   * since WT and SWT constants are identical this can be used in both SWT and WT use constant
   * cases.
   */
  public static int getButton(int accelerator) {
    accelerator &= BUTTON_MASK;
    if ((accelerator & MouseEvent.BUTTON1) == MouseEvent.BUTTON1) {
      // WT.BUTTON1 is the same
      return MouseConfig.PRIMARY_BUTTON;
    }
    if ((accelerator & MouseEvent.BUTTON2) == MouseEvent.BUTTON2) {
      // WT.BUTTON2 is the same
      return 2;
    }
    if ((accelerator & MouseEvent.BUTTON3) == MouseEvent.BUTTON3) {
      // WT.BUTTON3 is the same
      return MouseConfig.SECONDARY_BUTTON;
    }
    // is this an error?
    return UNSPECIFIED;
  }

  private MouseConfig() {
    // hide public constructor
  }
}
