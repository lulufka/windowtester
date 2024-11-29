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
package com.windowtester.recorder.event.meta;

import com.windowtester.recorder.event.IRecorderSemanticEvent;
import com.windowtester.recorder.event.ISemanticEventHandler;

/**
 * Recorder errors are internal errors that are reported to the host workspace for logging.
 */
public class RecorderErrorEvent implements IRecorderSemanticEvent {

  private static final long serialVersionUID = -5661725350112763499L;

  // TODO: move someplace central
  private static final String NEW_LINE = System.getProperty("line.separator", "\n");

  /**
   * The message describing this event
   *
   * @serial
   */
  private final String msg;

  /**
   * The throwable associated
   *
   * @serial
   */
  private final Throwable throwable;

  /**
   * Create an instance.
   *
   * @param msg       - a message describing the error
   * @param throwable - the associated throwable
   */
  public RecorderErrorEvent(String msg, Throwable throwable) {
    this.msg = msg;
    this.throwable = throwable;
  }

  @Override
  public void accept(ISemanticEventHandler handler) {
    handler.handleError(this);
  }

  /**
   * @return Returns the message.
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @return Returns the associated throwable.
   */
  public Throwable getThrowable() {
    return throwable;
  }

  @Override
  public String toString() {
    return "Internal recorder error (" + getMsg() + "):" + NEW_LINE + getThrowable().getMessage();
  }
}
