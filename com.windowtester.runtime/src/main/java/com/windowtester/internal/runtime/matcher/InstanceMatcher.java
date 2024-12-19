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
package com.windowtester.internal.runtime.matcher;

import com.windowtester.runtime.locator.IWidgetMatcher;

/**
 * A matcher that matches a given object instance.
 */
public class InstanceMatcher implements IWidgetMatcher<Object> {

  private final Object instance;

  public InstanceMatcher(Object instance) {
    this.instance = instance;
  }

  @Override
  public boolean matches(Object widget) {
    return widget == instance;
  }
}
