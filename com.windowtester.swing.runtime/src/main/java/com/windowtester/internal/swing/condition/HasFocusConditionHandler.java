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
package com.windowtester.internal.swing.condition;

import abbot.tester.ComponentTester;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.condition.IUIConditionHandler;
import com.windowtester.runtime.condition.UICondition;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import java.awt.Component;

/**
 * Has Focus condition handler.
 */
public class HasFocusConditionHandler extends UICondition implements IUIConditionHandler {

  private final IWidgetLocator locator;

  public HasFocusConditionHandler(IWidgetLocator locator) {
    this.locator = locator;
  }

  @Override
  public void handle(IUIContext ui) throws Exception {
    setFocus(ui);
  }

  @Override
  public boolean testUI(IUIContext ui) {
    try {
      return hasFocus(ui);
    } catch (WidgetSearchException e) {
      return false;
    }
  }

  /**
   * @param ui ui context
   * @return true if the component has the focus, false otherwise
   * @throws WidgetSearchException in case of error
   */
  public boolean hasFocus(IUIContext ui) throws WidgetSearchException {
    var found = ui.find(locator);
    if (!(found instanceof IWidgetReference)) {
      return false;
    }

    var widget = ((IWidgetReference) found).getWidget();
    return ((Component) widget).isFocusOwner();
  }

  private void setFocus(IUIContext ui) throws WidgetSearchException {
    var widget = findWidget(ui, locator);
    setFocus(widget);
  }

  private void setFocus(Component widget) {
    var tester = ComponentTester.getTester(Component.class);
    tester.actionFocus(widget);
  }

  private Component findWidget(IUIContext ui, IWidgetLocator locator) throws WidgetSearchException {
    var ref = (IWidgetReference) ui.find(locator);
    var target = ref.getWidget();
    if (target == null) {
      throw new IllegalArgumentException("widget reference must not be null");
    }

    if (target instanceof Component component) {
      return component;
    }
    // NULL is now a sentinel
    return null;
  }
}
