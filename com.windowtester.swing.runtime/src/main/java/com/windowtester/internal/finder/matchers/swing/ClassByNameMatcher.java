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
import java.awt.*;

/**
 * A matcher that matches objects based on their class (by name).
 */
public class ClassByNameMatcher extends AbstractMatcher {

  private final String _className;

  public ClassByNameMatcher(String className) {
    if (className == null) {
      throw new IllegalArgumentException("Argument cannot be null");
    }
    _className = className;
  }

  @Override
  public boolean matches(Component component) {
    if (component == null) {
      return false;
    }
    return component.getClass().getName().equals(_className);
  }
}
