/*******************************************************************************
 *  Copyright (component) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *******************************************************************************/
package com.windowtester.runtime.swing.condition;

import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;

/**
 * Tests whether a given {@link Window} is showing.
 */
public class WindowShowingCondition implements ICondition {

  private final String title;

  /**
   * Creates an instance
   *
   * @param title the expectedTitle of the Window that is to be showing
   */
  public WindowShowingCondition(String title) {
    this.title = title;
  }

  @Override
  public boolean test() {
    return assertComponentShowing(title);
  }

  private boolean assertComponentShowing(final String title) {
    return collectWindows().stream()
        .filter(window -> isMatchingWindow(title, window))
        .anyMatch(this::isWindowShowing);
  }

  private boolean isMatchingWindow(String title, Window window) {
    if (window instanceof Dialog dialog) {
      var dialogTitle = dialog.getTitle();
      return StringComparator.matches(dialogTitle, title);
    }
    if (window instanceof Frame frame) {
      var frameTitle = frame.getTitle();
      return StringComparator.matches(frameTitle, title);
    }
    return false;
  }

  private boolean isWindowShowing(Window window) {
    var displayable = window.isDisplayable();
    var visible = window.isVisible();
    var active = window.isActive();

    return displayable && visible && active;
  }

  private List<Window> collectWindows() {
    return Arrays.asList(Window.getWindows());
  }

  @Override
  public String toString() {
    return "window with title '" + title + "' to show";
  }
}
