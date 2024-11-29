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
package com.windowtester.internal.swing.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for tokenizing path Strings.
 */
public class PathStringTokenizerUtil {

  private PathStringTokenizerUtil() {
    // do nothing
  }

  /**
   * Take a path string (elements delimited by the '\' character) and break it into tokens. If an element contains the
   * delimiter, it must be escaped.
   * <p>
   * <p>
   * For example: "Edit/Find\\/Replace" tokenizes to "Edit", "Find/Replace".
   *
   * @param pathString - the path string to tokenize
   * @return an array of string tokens.
   */
  public static String[] tokenize(String pathString) {
    if (pathString == null) {
      throw new IllegalArgumentException("path string must be non-null");
    }
    return new PathTokenizer(pathString).tokenize();
  }

  /**
   * A helper class for tokenizing.
   */
  public static class PathTokenizer {

    private final String pathString;
    private final char delimiter;
    private final char escape;
    private StringBuilder builder = new StringBuilder();
    private final List<String> items = new ArrayList<>();

    public PathTokenizer(String pathString, char delimiter, char escape) {
      this.pathString = pathString;
      this.delimiter = delimiter;
      this.escape = escape;
    }

    public PathTokenizer(String pathString) {
      this(pathString, '/', '\\');
    }

    boolean isDelimeter(char c) {
      return c == delimiter;
    }

    boolean isEscape(char c) {
      return c == escape;
    }

    public String[] tokenize() {
      for (int i = 0; inBounds(i); ++i) {
        // System.out.println(getChar(i));
        if (isDelimeter(getChar(i))) {
          addItem();
        } else if (isEscapeCase(i)) {
          addChar(++i); // advance and add escaped char
        } else {
          addChar(i); // just add
        }
      }
      // add last item
      addItem();

      return getItems();
    }

    private String[] getItems() {
      return items.toArray(new String[] {});
    }

    private void addChar(int i) {
      builder.append(getChar(i));
    }

    private boolean isEscapeCase(int i) {
      return inBounds(i + 1) && isEscape(getChar(i)) && isDelimeter(getChar(i + 1));
    }

    private void addItem() {
      if (builder.length() == 0) {
        return;
      }
      items.add(builder.toString());
      builder.delete(0, builder.length() -1);
    }

    private char getChar(int i) {
      return pathString.charAt(i);
    }

    private boolean inBounds(int i) {
      return i < pathString.length();
    }
  }
}
