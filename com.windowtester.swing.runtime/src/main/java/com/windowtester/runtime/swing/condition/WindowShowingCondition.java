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

import abbot.finder.AWTHierarchy;
import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.util.StringComparator;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
    var components = collectComponents();
    for (Component component : components) {
      if (component instanceof Frame frame
          && isFrameShowing(title, frame)) {
        return true;
      }
      if (component instanceof Dialog dialog
          && isDialogShowing(title, dialog)) {
        return true;
      }
    }
    return false;
  }

  private boolean isDialogShowing(String expectedTitle, Dialog dialog) {
    var actualTitle = dialog.getTitle();
    return isComponentShowing(expectedTitle, actualTitle, dialog);
  }

  private boolean isFrameShowing(String expectedTitle, Frame window) {
    var actualTitle = window.getTitle();
    return isComponentShowing(expectedTitle, actualTitle, window);
  }

  private boolean isComponentShowing(
      String expectedTitle,
      String actualTitle,
      Window window) {
    final boolean titleMatches = StringComparator.matches(actualTitle, expectedTitle);
    final boolean displayable = window.isDisplayable();
    final boolean visible = window.isVisible();
    final boolean active = window.isActive();
    if (titleMatches) {
      System.out.println("WindowShowingCondition.isComponentShowing:\n"
          + "\ttitle(" + expectedTitle + ":" + actualTitle + "): " + titleMatches + "\n"
          + "\tvisible: " + visible + "\n"
          + "\tactive: " + active + "\n"
          + "\tdisplayable: " + displayable + "\n");
    }

    return titleMatches
        && displayable
        && visible
        && active;
  }

  private List<Component> collectComponents() {
    var hierarchy = new AWTHierarchy();
    List<Component> comps = hierarchy.getRoots().stream()
        .map(hierarchy::getComponents)
        .flatMap(Collection::stream)
        .toList();

    return Stream
        .concat(
            comps.stream(),
            hierarchy.getRoots().stream())
        .toList();
  }

  @Override
  public String toString() {
    return title + " to show";
  }
}
