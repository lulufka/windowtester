package com.windowtester.internal.swing.matcher;

import com.windowtester.internal.runtime.matcher.AdapterFactory;
import com.windowtester.runtime.locator.IWidgetMatcher;

public class TreeMatcher {

  public static IWidgetMatcher create(String treePath) {
    return new AdapterFactory()
        .adapt(new com.windowtester.internal.finder.matchers.swing.TreePathMatcher(treePath));
  }
}
