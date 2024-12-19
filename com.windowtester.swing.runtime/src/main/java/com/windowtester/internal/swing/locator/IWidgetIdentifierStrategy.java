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
package com.windowtester.internal.swing.locator;

import com.windowtester.runtime.swing.SwingWidgetLocator;
import java.awt.Component;

public interface IWidgetIdentifierStrategy {

  /**
   * Generates a <code>WidgetLocator</code> that uniquely identifies this widget relative to the
   * current widget hierarchy. If no uniquely identifying locator is found <code>null</code> is
   * returned.
   *
   * @param component the component to identify
   * @return a uniquely identifying <code>WidgetLocator</code> or <code>null</code> if none can be
   * inferred
   */
  SwingWidgetLocator identify(Component component);
}
