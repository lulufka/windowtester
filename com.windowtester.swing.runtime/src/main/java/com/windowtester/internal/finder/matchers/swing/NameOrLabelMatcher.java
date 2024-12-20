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
package com.windowtester.internal.finder.matchers.swing;

import abbot.finder.matchers.AbstractMatcher;
import abbot.finder.matchers.NameMatcher;
import java.awt.Component;

/***
 * Provides matching with either name or label
 *
 * Names are set using the <code>setName(..)</code> (<code>component.setName("name");</code>
 * method; labels are found by inspecting the component's text attribute (this corresponds,
 * for example, to the human-readable text in a button).
 */

public class NameOrLabelMatcher extends AbstractMatcher {

  /**
   * The name matcher for matching on names
   */
  private final NameMatcher nameMatcher;

  /**
   * The label matcher for matching on labels
   */
  private final TxtMatcher labelMatcher;

  /**
   * Construct a matcher that will match any component that has explicitly been assigned the given
   * <code>name</code>. Auto-generated names (e.g. <code>win0</code>, <code>frame3</code>, etc. for
   * AWT (native) based components will not match.
   */
  public NameOrLabelMatcher(String nameOrLabel) {
    nameMatcher = new NameMatcher(nameOrLabel);
    labelMatcher =
        new TxtMatcher(nameOrLabel) {
          @Override
          protected boolean stringsMatch(String expected, String actual) {
            if (expected == null || actual == null) {
              // they should both be null for a match
              return expected == null && actual == null;
            }

            var trimmed = getTrimmedValue(actual);
            if (expected.equals(trimmed)) {
              return true;
            }
            return super.stringsMatch(expected, actual);
          }
        };
  }

  private String getTrimmedValue(String actual) {
    String trimmed = actual;
    int index = trimmed.indexOf('\t');
    if (index != -1) {
      trimmed = trimmed.substring(0, index);
    }
    index = trimmed.indexOf('&');
    if (index != -1) {
      trimmed = trimmed.substring(0, index) + trimmed.substring(index + 1);
    }
    return trimmed;
  }

  @Override
  public boolean matches(Component component) {
    try {
      return nameMatcher.matches(component) || labelMatcher.matches(component);
    } catch (Exception e) {
      // TODO: push this up into Finder?
      return false;
    }
  }

  @Override
  public String toString() {
    return "Name Or Label matcher (" + labelMatcher.getText() + ")";
  }
}
