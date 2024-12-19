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
 * Provides matching of components by class.
 */
public class ClassMatcher extends AbstractMatcher {
  private final Class cls;

  public ClassMatcher(Class cls) {
    this(cls, false);
  }

  public ClassMatcher(Class cls, boolean mustBeShowing) {
    this.cls = cls;
  }

  @Override
  public boolean matches(Component component) {
    if (component == null) {
      return false;
    }
    return cls.isAssignableFrom(component.getClass());
  }

  public String toString() {
    return "Class matcher (" + cls.getName() + ")";
  }
}
