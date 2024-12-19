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
package com.windowtester.runtime.swing.internal.junit;

import com.windowtester.internal.runtime.junit.core.IExecutionContext;
import com.windowtester.internal.runtime.junit.core.IExecutionMonitor;
import com.windowtester.runtime.IUIContext;

/**
 * An execution context for Swing tests.
 */
public class SwingExecutionContext implements IExecutionContext {

  private IExecutionMonitor monitor;

  @Override
  public IExecutionMonitor getExecutionMonitor() {
    if (monitor == null) {
      monitor = new SwingExecutionMonitor();
    }
    return monitor;
  }

  @Override
  public IUIContext getUI() {
    return ((SwingExecutionMonitor) getExecutionMonitor()).getUI();
  }
}
