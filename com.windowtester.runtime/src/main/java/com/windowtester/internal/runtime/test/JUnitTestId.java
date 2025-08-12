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
package com.windowtester.internal.runtime.test;

import com.windowtester.internal.runtime.junit.core.ITestIdentifier;

/**
 * An id for a JUnit3 test.
 */
public class JUnitTestId implements ITestIdentifier {

  private final String id;
  private final String name;

  public JUnitTestId(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  private String getTestCaseID() {
    return id;
  }
}
