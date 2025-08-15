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
package com.windowtester.internal.runtime.mapper;

import com.windowtester.runtime.WidgetLocator;
import java.util.HashMap;
import java.util.Map;

/**
 * A (somewhat) smart key generator that generates keys based on widget type and label. These
 * generated keys are intended to simulate the kinds of keys users might themselves choose to
 * identify widgets. For example, suppose we have a Button labeled "on".  The first generated key
 * for such a button would be "on.button".  The second generated key for such a button would be
 * "on.button1" and so on.
 * <br><br>
 * Each instance of this generator will create unique keys for each call to generate.
 */
public class SmartKeyGenerator implements IKeyGenerator {

  /**
   * A map of witnessed keys to their counts
   */
  private final Map<String, Integer> seen = new HashMap<>();

  @Override
  public String generate(WidgetLocator info) {
    var base = getBase(info);
    var index = getIndex(base.toString());
    if (index != 0) {
      base.append(index);
    }
    return base.toString();
  }

  /**
   * Get the index that numbers this base string.
   *
   * @param base - the String key
   * @return an index that describes how many such keys have been encountered
   */
  private int getIndex(String base) {
    var index = seen.get(base);
    if (index == null) {
      index = 0;
    } else {
      index = index + 1;
    }
    seen.put(base, index);
    return index;
  }

  /**
   * Extract a basic identifying String to label this info object.
   *
   * @param info - the info to describe
   * @return a base StringBuffer for use in key generation
   */
  private StringBuilder getBase(WidgetLocator info) {
    var builder = new StringBuilder();

    var label = info.getNameOrLabel();
    if (label != null && !label.isEmpty()) {
      builder.append(label).append('.');
    }

    // get the simple name of the class
    var className = info.getTargetClass().getName();
    var lastPeriod = className.lastIndexOf('.');
    var simpleName = (lastPeriod >= 0) ? className.substring(lastPeriod + 1) : className;
    builder.append(simpleName.toLowerCase());

    return builder;
  }
}
