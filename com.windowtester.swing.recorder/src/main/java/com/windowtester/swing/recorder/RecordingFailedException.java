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
package com.windowtester.swing.recorder;

public class RecordingFailedException extends RuntimeException {

  private final Throwable reason;

  public RecordingFailedException(String msg) {
    super(msg);
    reason = null;
  }

  public RecordingFailedException(Throwable thr) {
    super(thr.getMessage());
    reason = thr;
  }

  public Throwable getReason() {
    return reason != null ? reason : this;
  }
}
