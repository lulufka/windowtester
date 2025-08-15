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

public class DefaultCodeGenerator implements ICodeGenerator {

  private final StringBuilder builder = new StringBuilder();

  @Override
  public JavaVersion getJavaVersion() {
    return ICodeGenerator.JAVA4;
  }

  @Override
  public ICodeGenerator append(String body) {
    builder.append(body);
    return this;
  }

  @Override
  public ICodeGenerator addImport(String importString) {
    return this;
  }

  @Override
  public String toCodeString() {
    return builder.toString();
  }
}
