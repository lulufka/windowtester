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
package com.windowtester.internal.runtime;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A collector of diagnostic information via the
 * {@link IDiagnosticParticipant#diagnose(IDiagnostic)} method.
 */
public class DiagnosticWriter implements IDiagnostic {

  private final StringWriter stringWriter = new StringWriter();
  private final PrintWriter printWriter = new PrintWriter(stringWriter);
  private int depth = 0;

  @Override
  public void diagnose(String key, Object value) {
    if (!stringWriter.getBuffer().isEmpty()) {
      printWriter.println();
      for (int i = 0; i < depth; i++) {
        printWriter.print("  ");
      }
    }

    printWriter.print("DiagnosticWriter ");
    printWriter.print(key);
    printWriter.print(": ");

    if (value == null) {
      printWriter.println("null");
    } else if (value instanceof IDiagnosticParticipant participant) {
      depth++;
      participant.diagnose(this);
      depth--;
    } else {
      attribute("DiagnosticWriter: toString", String.valueOf(value));
      attribute("DiagnosticWriter: class", value.getClass().getName());
    }
  }

  @Override
  public void attribute(String key, String value) {
    printWriter.println();
    printWriter.print(key);
    printWriter.print("=");
    printWriter.print(value);
  }

  @Override
  public void attribute(String key, int value) {
    attribute(key, String.valueOf(value));
  }

  @Override
  public void attribute(String key, boolean value) {
    attribute(key, String.valueOf(value));
  }

  @Override
  public String toString() {
    return stringWriter.toString();
  }
}
