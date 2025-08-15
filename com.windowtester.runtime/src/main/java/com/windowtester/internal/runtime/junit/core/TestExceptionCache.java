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

import java.lang.reflect.InvocationTargetException;

/**
 * A cache for exceptions caught in test execution.
 */
public class TestExceptionCache {

  // Cached exceptions for re-throwing
  private InvocationTargetException ite;
  private IllegalAccessException iae;

  /**
   * Check if there is an exception cached.
   *
   * @return <code>true</code> if there is a cached exception, <code>false</code> otherwise
   */
  public boolean hasException() {
    return ite != null || iae != null;
  }

  /**
   * Cache the given exception for later throwing.
   */
  public void cache(Throwable e) {
    if (e instanceof InvocationTargetException ex) {
      e.fillInStackTrace();
      ite = ex;
    } else if (e instanceof IllegalAccessException ex) {
      e.fillInStackTrace();
      iae = ex;
    } else {
      ite = new InvocationTargetException(e);
    }
  }

  /**
   * Throw the cached exception (if there is one).
   */
  public void throwException() throws Throwable {
    if (ite != null) {
      // Extract the wrapped exception as appropriate
      if (ite.getCause() != null) {
        throw ite.getCause();
      }
      throw ite;
    }
    if (iae != null) {
      throw iae;
    }
  }

  /**
   * Clear the exception cache.
   */
  public void clear() {
    iae = null;
    ite = null;
  }
}
