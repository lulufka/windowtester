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
package com.windowtester.runtime;

import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
import java.awt.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Thrown when multiple widgets are found.
 */
public class MultipleWidgetsFoundException extends WidgetSearchException {

  public MultipleWidgetsFoundException(IWidgetLocator[] locators) {
    super(String.format("Multiple widgets found: %s", createLocatorClassDetails(locators)));
  }

  private static String createLocatorClassDetails(IWidgetLocator[] locators) {
    return Arrays.stream(locators)
        .filter(WidgetReference.class::isInstance)
        .map(WidgetReference.class::cast)
        .map(ref -> ref.getWidget().getClass().getCanonicalName() + "("
            + ((Component) ref.getWidget()).getName() + "-" + ref.getWidget()
            .hashCode() + ")")
        .collect(Collectors.joining(","));
  }

}
