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
package com.windowtester.swing.event.recorder;

import com.windowtester.recorder.event.ISemanticEventListener;
import com.windowtester.recorder.event.IUISemanticEvent;
import com.windowtester.recorder.event.meta.RecorderErrorEvent;
import com.windowtester.recorder.event.meta.RecorderTraceEvent;

public class ConsoleReportingListener implements ISemanticEventListener {

  /**
   * A describer used for stateful event descrptions
   */
  // private EventDescriber _describer = new EventDescriber();

  @Override
  public void notify(IUISemanticEvent event) {
    //        String description = _describer.describe(event);
    //        if (description != null)
    //            System.out.println(description);
    System.out.println("got event" + event);
  }

  @Override
  public void notifyAssertionHookAdded(String hookName) {
    System.out.println("hook added: " + hookName);
  }

  @Override
  public void notifyStart() {
    System.out.println("recording started");
  }

  @Override
  public void notifyStop() {
    System.out.println("recording stopped");
  }

  @Override
  public void notifyPause() {
    System.out.println("recording paused");
  }

  @Override
  public void notifyWrite() {
    System.out.println("recording written");
  }

  @Override
  public void notifyDispose() {
    System.out.println("display disposed");
  }

  @Override
  public void notifyRestart() {
    System.out.println("recording restarted");
  }

  @Override
  public void notifyError(RecorderErrorEvent event) {
    System.out.println("an internal error occured: " + event);
  }

  @Override
  public void notifyTrace(RecorderTraceEvent event) {
    System.out.println("a trace event was sent: " + event);
  }

  @Override
  public void notifyControllerStart(int port) {
    System.out.println("controller started on: " + port);
  }

  @Override
  public void notifyDisplayNotFound() {
    System.out.println("display not found");
  }

  @Override
  public void notifySpyModeToggle() {
    System.out.println("spy mode toggled");
  }
}
