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
package com.windowtester.internal.swing;

import abbot.tester.ActionFailedException;
import abbot.util.AWT;
import java.awt.Component;
import java.awt.Point;
import java.lang.reflect.Method;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public class DelayUIDriverSwing extends UIDriverSwing {

  private static boolean printMessage = false;

  /**
   * Create an instance (using the default settings).
   */
  public DelayUIDriverSwing() {
    // TODO: Load settings from file
  }

  public Component click(Component owner, String labelOrPath) throws ActionFailedException {

    Component c = super.click(owner, labelOrPath);
    return c;
  }

  public Component click(int clickCount, Component w, int x, int y, int mask) {
    // first move the mouse to the point of interest:
    mouseMove(w);
    Component c = super.click(clickCount, w, x, y, mask);
    return c;
  }

  public Component clickComboBox(JComboBox owner, String labelOrPath, int clickCount)
      throws ActionFailedException {
    // first move the mouse to the point of interest:
    mouseMove(owner);
    Component c = super.clickComboBox(owner, labelOrPath, clickCount);
    return c;
  }

  public Component clickListItem(int clickCount, JList owner, String labelOrPath, int mask)
      throws ActionFailedException {
    // first move the mouse to the point of interest:
    mouseMove(owner);
    Component c = super.clickListItem(clickCount, owner, labelOrPath, mask);
    return c;
  }

  public Component clickMenuItem(JMenuItem owner) {
    Component c = super.clickMenuItem(owner);
    return c;
  }

  public Component clickTable(int clickCount, JTable owner, int row, int col, int mask) {
    mouseMove(owner);
    Component c = super.clickTable(clickCount, owner, row, col, mask);
    return c;
  }

  public Component clickTreeItem(int clickCount, Component owner, String path, int mask) {
    Component c = super.clickTreeItem(clickCount, owner, path, mask);
    return c;
  }

  public void keyClick(char key) {
    super.keyClick(key);
  }

  public void keyClick(int ctrl, char c) {
    super.keyClick(ctrl, c);
  }

  public void keyClick(int key) {
    super.keyClick(key);
  }

  private Point pointT;
  private Point cursorT;

  public void mouseMove(Component w, int x, int y) {
    // get location of the widget
    pointT = AWT.getLocationOnScreen(w);
    // get current location of the cursor/mouse for jre 1.5 and above
    //	PointerInfo info = MouseInfo.getPointerInfo();
    //	cursorT = info.getLocation();
    // since build system is 1.4, use reflection to make calls

    try {
      Class c = Class.forName("java.awt.MouseInfo");
      Method m = c.getMethod("getPointerInfo", null);
      Object o = m.invoke(null, null);
      Class cPointer = Class.forName("java.awt.PointerInfo");
      Method method = cPointer.getMethod("getLocation", null);
      cursorT = (Point) method.invoke(o, null);

    } catch (Throwable e) {
      if (!printMessage) {
        System.out.println(
            "Mouse Delay not supported, turn off Mouse Delay in"
                + " Window->Preferences->WindowTester->Playback");
        printMessage = true;
      }
      return;
    }

    Point cursorLocation = cursorT;
    Point target = new Point(pointT.x + x, pointT.y + y);

    Point[] path = getPath(cursorLocation, target);

    for (int i = 0; i < path.length; i++) {
      super.mouseMove(path[i].x, path[i].y);
    }
  }

  public void enterText(String txt) throws ActionFailedException {
    // enter text one char at a time, pausing as we go
    for (int i = 0; i <= txt.length() - 1; ++i) {
      keyClick(txt.charAt(i));
    }
  }

  //////////////////////////////////////////////////////////////////////
  //
  //
  // Utility methods
  //
  //
  /////////////////////////////////////////////////////////////////////

  public static Point[] getPath(Point p1, Point p2) {
    int numSteps = getNumSteps(p1, p2);
    return getPath(p1, p2, numSteps + 3);
  }

  private static int getNumSteps(Point p1, Point p2) {
    int dx = Math.abs(p1.x - p2.x);
    int dy = Math.abs(p1.y - p2.y);
    int numSteps = Math.max(dx, dy);
    return numSteps;
  }

  public static Point[] getPath(Point p1, Point p2, int numSteps) {
    int[] xsteps = getSteps(p1.x, p2.x, numSteps);
    int[] ysteps = getSteps(p1.y, p2.y, numSteps);

    Point[] points = new Point[xsteps.length];

    for (int i = 0; i < xsteps.length; ++i) points[i] = new Point(xsteps[i], ysteps[i]);

    return points;
  }

  static int[] getSteps(int start, int stop, int numOfSteps) {
    float[] fsteps = new float[numOfSteps];
    int[] steps = new int[numOfSteps];
    float delta = stop - start;
    float increment = delta / numOfSteps;

    fsteps[0] = start;
    for (int i = 1; i < numOfSteps; ++i) {
      fsteps[i] = fsteps[i - 1] + increment;
    }
    for (int i = 0; i < numOfSteps; ++i) {
      steps[i] = Math.round(fsteps[i]);
    }
    // sanity check last coord:
    steps[numOfSteps - 1] = stop;

    return steps;
  }
}
