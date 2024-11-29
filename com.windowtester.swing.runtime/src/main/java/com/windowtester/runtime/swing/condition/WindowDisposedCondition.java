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
package com.windowtester.runtime.swing.condition;

import abbot.finder.AWTHierarchy;
import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JDialog;

/**
 * Tests whether a given {@link Window} has been disposed.
 */
public class WindowDisposedCondition implements ICondition {

  private final String windowName;

  /**
   * Creates an instance
   *
   * @param title the title of the Window that is to be checked
   */
  public WindowDisposedCondition(String title) {
    windowName = title;
  }

  @Override
  public boolean test() {
    return assertComponentNotShowing(windowName);
  }

  private synchronized boolean assertComponentNotShowing(String title) {
    var componentList = collectComponents();
    for (Component c : componentList) {
      if (isMatchingFrame(title, c)) {
        return noFrameShowing(c);
      }
      if (isMatchingDialog(title, c)) {
        return noDialogShowing(c);
      }
    }
    // component not found
    return true;
  }

  private boolean noDialogShowing(Component c) {
    return noComponentShowing((JDialog) c);
  }

  private boolean noFrameShowing(Component c) {
    return noComponentShowing((Frame) c);
  }

  private boolean noComponentShowing(Window window) {
    var isNotDisplayable = !window.isDisplayable();
    var isNotVisible = !window.isVisible();
    var isNotActive = !window.isActive();

    return isNotDisplayable && isNotVisible && isNotActive;
  }

  private boolean isMatchingDialog(String title, Component c) {
    if (c instanceof JDialog dialog) {
      return StringComparator.matches(dialog.getTitle(), title);
    }
    return false;
  }

  private boolean isMatchingFrame(String title, Component c) {
    if (c instanceof Frame frame) {
      return StringComparator.matches(frame.getTitle(), title);
    }
    return false;
  }

  private List<Component> collectComponents() {
    var hierarchy = new AWTHierarchy();
    var components = hierarchy.getRoots().stream()
        .map(hierarchy::getComponents)
        .flatMap(Collection::stream)
        .toList();

    return Stream
        .concat(
            components.stream(),
            hierarchy.getRoots().stream())
        .toList();
  }
}
