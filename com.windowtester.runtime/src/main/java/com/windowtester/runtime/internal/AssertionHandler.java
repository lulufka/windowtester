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
package com.windowtester.runtime.internal;

import com.windowtester.internal.runtime.condition.ConditionMonitor;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WaitTimedOutException;
import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.condition.IConditionHandler;

/**
 * A handler for invariants.
 */
public class AssertionHandler implements IAssertionHandler {

  protected static final int ASSERTION_WAIT_TIMEOUT = 3000;

  private final IUIContext ui;

  public AssertionHandler(IUIContext ui) {
    this.ui = ui;
  }

  protected IUIContext getUI() {
    return ui;
  }

  @Override
  public void assertThat(ICondition condition) {
    waitFor(condition);
  }

  @Override
  public void assertThat(String message, ICondition condition) throws WaitTimedOutException {
    try {
      waitFor(condition);
    } catch (WaitTimedOutException e) {
      throw new WaitTimedOutException(message);
    }
  }

  @Override
  public void ensureThat(IConditionHandler conditionHandler) throws Exception {
    if (isTrue(conditionHandler)) {
      return;
    }
    conditionHandler.handle(getUI());
    assertThat(conditionHandler);
  }

  private boolean isTrue(ICondition condition) {
    return ConditionMonitor.test(getUI(), condition);
  }

  private void waitFor(ICondition condition) throws WaitTimedOutException {
    getUI().wait(condition, ASSERTION_WAIT_TIMEOUT);
  }
}
