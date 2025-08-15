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
 * Events that correspond to tracing messages.
 */
public class RecorderTraceEvent implements IRecorderSemanticEvent {

  private static final long serialVersionUID = -747178880240708873L;

  /**
   * The associated trace option.
   *
   * @serial
   */
  private final String traceOption;

  /**
   * The associated trace message.
   *
   * @serial
   */
  private final String msg;

  /**
   * Create an instance.
   *
   * @param traceOption trace option
   * @param msg         message
   */
  public RecorderTraceEvent(String traceOption, String msg) {
    this.traceOption = traceOption;
    this.msg = msg;
  }

  /**
   * @return Returns the msg.
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @return Returns the traceOption.
   */
  public String getTraceOption() {
    return traceOption;
  }

  @Override
  public void accept(ISemanticEventHandler handler) {
    handler.handleTrace(this);
  }
}
