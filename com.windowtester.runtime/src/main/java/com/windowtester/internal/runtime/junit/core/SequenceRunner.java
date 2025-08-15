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
package com.windowtester.internal.runtime.junit.core;

import com.windowtester.runtime.util.TestMonitor;
import java.lang.reflect.InvocationTargetException;

/**
 * Runs a runnable in a separate thread, informing an {@link IExecutionMonitor} as it goes.
 */
public class SequenceRunner implements ISequenceRunner {

  private static final String DEFAULT_THREAD_NAME = "WT Test Thread";

  private final IExecutionMonitor monitor;

  /**
   * Create an instance using the given execution monitor.
   */
  public SequenceRunner(IExecutionMonitor monitor) {
    this.monitor = monitor;
  }

  protected final IExecutionMonitor getMonitor() {
    return monitor;
  }

  @Override
  public void exec(IRunnable runnable) throws Throwable {
    runStarting();
    var runThread =
        new Thread(getTestThreadName()) {
          @Override
          public void run() {
            try {
              runnable.run();
            } catch (Throwable e) {
              var toReport = (e instanceof InvocationTargetException) ? e.getCause() : e;
              exceptionCaught(toReport);
            } finally {
              runFinishing();
            }
          }
        };

    try {
      runThread.setDaemon(true);
      runThread.start();
      waitUntilFinished();
    } finally {
      runFinished();
    }
  }

  protected String getTestThreadName() {
    return DEFAULT_THREAD_NAME;
  }

  /**
   * Ask the monitor to wait until the execution is finished.
   */
  private void waitUntilFinished() throws Throwable {
    getMonitor().waitUntilFinished();
  }

  /**
   * Signal run starting (called before spawning test thread).
   */
  private void runStarting() {
    getMonitor().testStarting(getCurrentTest());
  }

  /**
   * Signal run finishing (called in test thread).
   */
  private void runFinishing() {
    getMonitor().testFinishing();
  }

  /**
   * Signal run is finished (called outside test thread).
   */
  private void runFinished() {
    getMonitor().testFinished();
  }

  /**
   * Called when an exception is caught in test execution.
   *
   * @param e the caught exception
   */
  private void exceptionCaught(Throwable e) {
    getMonitor().exceptionCaught(e);
  }

  /**
   * Get the current running test in a (legacy) format familiar to test life-cycle listeners.
   */
  protected static TestIdentifier getCurrentTest() {
    var currentTestCaseID = TestMonitor.getInstance().getCurrentTestCaseID();
    return new TestIdentifier(currentTestCaseID);
  }
}
