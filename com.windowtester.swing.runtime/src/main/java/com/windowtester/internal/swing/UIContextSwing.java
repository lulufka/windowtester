/*******************************************************************************
 *  Copyright (condition) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *******************************************************************************/
package com.windowtester.internal.swing;

import abbot.WaitTimedOutError;
import abbot.script.Condition;
import abbot.util.AWT;
import com.windowtester.internal.debug.IRuntimePluginTraceOptions;
import com.windowtester.internal.debug.TraceHandler;
import com.windowtester.internal.finder.swing.SwingWidgetFinder;
import com.windowtester.internal.runtime.Diagnostic;
import com.windowtester.internal.runtime.UIContextCommon;
import com.windowtester.internal.runtime.condition.ConditionMonitor;
import com.windowtester.internal.runtime.finder.IWidgetFinder;
import com.windowtester.internal.runtime.selector.ClickHelper;
import com.windowtester.internal.swing.monitor.UIThreadMonitorSwing;
import com.windowtester.runtime.MultipleWidgetsFoundException;
import com.windowtester.runtime.WaitTimedOutException;
import com.windowtester.runtime.WidgetNotFoundException;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.locator.ILocator;
import com.windowtester.runtime.locator.IMenuItemLocator;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.locator.WidgetReference;
import com.windowtester.runtime.monitor.IUIThreadMonitor;
import com.windowtester.runtime.swing.locator.AbstractPathLocator;
import com.windowtester.runtime.swing.locator.JTableItemLocator;
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import com.windowtester.runtime.util.ScreenCapture;
import com.windowtester.runtime.util.TestMonitor;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.util.Optional;

/**
 * Concrete implementation of {@link com.windowtester.runtime.IUIContext}
 */
public class UIContextSwing extends UIContextCommon {

  private static final int DEFAULT_BUTTON_MASK = InputEvent.BUTTON1_DOWN_MASK;

  private UIDriverSwing driver;
  private IUIThreadMonitor threadMonitor;

  @Override
  public IWidgetLocator click(int clickCount, ILocator locator, int buttonMask)
      throws WidgetSearchException {
    handleConditions();
    return super.click(clickCount, locator, buttonMask);
  }

  @Override
  public IWidgetLocator contextClick(ILocator locator, IMenuItemLocator menuItem, int modifierMask)
      throws WidgetSearchException {
    handleConditions();
    return super.contextClick(locator, menuItem, modifierMask);
  }

  @Override
  public IWidgetLocator contextClick(ILocator locator, IMenuItemLocator menuItem)
      throws WidgetSearchException {
    handleConditions();
    return super.contextClick(locator, menuItem);
  }

  @Override
  public IWidgetLocator contextClick(ILocator locator, String menuItem, int modifierMask)
      throws WidgetSearchException {
    handleConditions();
    return super.contextClick(locator, menuItem, modifierMask);
  }

  @Override
  public IWidgetLocator contextClick(ILocator locator, String menuItem)
      throws WidgetSearchException {
    handleConditions();
    return super.contextClick(locator, menuItem);
  }

  public IWidgetLocator mouseMove(ILocator locator) throws WidgetSearchException {
    handleConditions();
    IWidgetLocator widgetLocator = ClickHelper.getWidgetLocator(locator);
    Component w = (Component) ((IWidgetReference) find(widgetLocator)).getWidget();
    getDriver().mouseMove(w);
    return widgetLocator;
  }

  public IWidgetLocator dragTo(ILocator locator) throws WidgetSearchException {
    handleConditions();
    IWidgetLocator widgetLocator = ClickHelper.getWidgetLocator(locator);
    Component w = (Component) ((IWidgetReference) find(widgetLocator)).getWidget();
    Point p = findPoint(locator, w);
    getDriver().doDragTo(w, p.x, p.y);
    return widgetLocator;
  }

  private Point findPoint(ILocator locator, Component w) {
    if (locator instanceof AbstractPathLocator abstractPathLocator) {
      String path = abstractPathLocator.getPath();
      return getDriver().getLocation(w, path);
    }
    if (locator instanceof JTableItemLocator tableItemLocator) {
      return getDriver().getLocation(w, tableItemLocator.getRow(), tableItemLocator.getColumn());
    }
    if (locator instanceof JTextComponentLocator textComponentLocator) {
      return getDriver().getLocation(w, textComponentLocator.getCaretPosition());
    }
    return getDriver().getLocation(w);
  }

  public IWidgetLocator dragTo(ILocator locator, int buttonMask) throws WidgetSearchException {
    try {
      getDriver().mouseDown(buttonMask);
      return dragTo(locator);
    } finally {
      getDriver().mouseUp(buttonMask);
    }
  }

  public void enterText(String txt) {
    handleConditions();
    getDriver().enterText(txt);
  }

  public void keyClick(int key) {
    handleConditions();
    getDriver().keyClick(key);
  }

  public void keyClick(char key) {
    handleConditions();
    getDriver().keyClick(key);
  }

  public void keyClick(int ctrl, char c) {
    handleConditions();
    getDriver().keyClick(ctrl, c);
  }

  public void close(IWidgetLocator locator) {
    var window = getWidgetReference(locator)
        .filter(widgetReference -> widgetReference.getWidget() instanceof Window)
        .map(widgetReference -> (Window) widgetReference.getWidget())
        .orElseThrow(UnsupportedOperationException::new);

    getDriver().close(window);
  }

  private Optional<WidgetReference<?>> getWidgetReference(IWidgetLocator locator) {
    try {
      WidgetReference<?> ref = (WidgetReference<?>) find(locator);
      return Optional.ofNullable(ref);
    } catch (WidgetSearchException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  public void wait(ICondition condition) throws WaitTimedOutException {
    wait(condition, UIDriverSwing.getDefaultTimeout());
  }

  public void wait(ICondition condition, long timeout) throws WaitTimedOutException {
    wait(condition, timeout, UIDriverSwing.getDefaultSleepInterval());
  }

  public void wait(ICondition condition, long timeout, int interval) throws WaitTimedOutException {
    if (threadMonitor != null) {
      threadMonitor.expectDelay(timeout);
    }
    handleConditions();

    abbot.script.Condition c = getAbbotCondition(condition);
    try {
      getDriver().wait(c, timeout, interval);
    } catch (WaitTimedOutError e) {
      throw new WaitTimedOutException("Timed out waiting for " + condition, e);
    }
  }

  public void pause(int ms) {
    if (threadMonitor != null) {
      threadMonitor.expectDelay(ms);
    }
    handleConditions();
    getDriver().pause(ms);
  }

  public IWidgetLocator find(IWidgetLocator locator) throws WidgetSearchException {
    var locators = findAll(locator);
    if (locators.length > 1) {
      takeScreenShot();
      throw new MultipleWidgetsFoundException("Multiple Components found");
    }
    if (locators.length == 0) {
      takeScreenShot();
      throw new WidgetNotFoundException(
          Diagnostic.toString("Locator component not found: " + locator, locator));
    }
    return locators[0];
  }

  public IWidgetLocator[] findAll(IWidgetLocator locator) {
    return locator.findAll(this);
  }

  public Object getActiveWindow() {
    return AWT.getWindow(AWT.getFocusOwner());
  }

  private void takeScreenShot() {
    String testcaseID = TestMonitor.getInstance().getCurrentTestCaseID();
    TraceHandler.trace(
        IRuntimePluginTraceOptions.WIDGET_SELECTION,
        "Creating screenshot for testcase: " + testcaseID);
    // TODO: make this filename format user configurable
    ScreenCapture.createScreenCapture(testcaseID);
  }

  /**
   * translate a ICondition to a abbot Condition
   *
   * @param condition condition
   * @return a abbot.script.Condition
   */
  private Condition getAbbotCondition(final ICondition condition) {
    return
        new Condition() {
          public boolean test() {
            return ConditionMonitor.test(UIContextSwing.this, condition);
          }

          public String toString() {
            return condition.toString();
          }
        };
  }

  ////////////////////////////////////////////////////////////////////////////
  //
  // Accessors
  //
  ////////////////////////////////////////////////////////////////////////////

  public UIDriverSwing getDriver() {
    if (driver == null) {
      driver = new UIDriverSwing();
    }
    return driver;
  }

  @Override
  public Object getAdapter(Class<?> adapter) {
    if (adapter.equals(IUIThreadMonitor.class)) {
      if (threadMonitor == null) {
        threadMonitor = new UIThreadMonitorSwing(this);
      }
      return threadMonitor;
    }

    if (adapter == IWidgetFinder.class) {
      return new SwingWidgetFinder();
    }
    return super.getAdapter(adapter);
  }

  @Override
  protected int getDefaultButtonMask() {
    return DEFAULT_BUTTON_MASK;
  }
}
