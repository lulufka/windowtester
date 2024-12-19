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
package com.windowtester.internal.finder.matchers.swing;

import abbot.finder.matchers.ClassMatcher;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextComponent;
import javax.swing.AbstractButton;
import javax.swing.JLabel;

public class TextClsMatcher extends ClassMatcher {

  private final Class<?> cls;
  private final String text;
  private String componentText = null;

  /**
   * Constructs a Matcher for the text given.
   * <p/>
   * The component must be visible.
   *
   * @param text the text to match.
   */
  public TextClsMatcher(String text) {
    this(text, true, Component.class);
  }

  /**
   * Constructs a Matcher with the text and the visibility given.
   * <p/>
   *
   * @param text          the text to match.
   * @param mustBeShowing true if the widget must be visible.
   */
  public TextClsMatcher(String text, boolean mustBeShowing) {
    this(text, mustBeShowing, Component.class);
  }

  /**
   * Constructs a matcher with the text and the class given.
   * <p/>
   * The component must be visible. Note that searches are considerably faster when a class is
   * provided to the matcher.
   *
   * @param text the text to match
   * @param cls  the Class to match
   */
  public TextClsMatcher(String text, Class<?> cls) {
    this(text, true, cls);
  }

  /**
   * Constructs a Matcher with the text, visibility and class given.
   * <p/>
   * Note that searches are considerably faster when a class is provided to the matcher.
   *
   * @param text          the text to match.
   * @param mustBeShowing true if the widget must be visible.
   * @param cls           the class to match.
   */
  public TextClsMatcher(String text, boolean mustBeShowing, Class<?> cls) {
    super(cls);
    this.cls = cls;
    this.text = text;
  }

  @Override
  public boolean matches(Component component) {
    if (cls != null) {
      boolean superResult = super.matches(component);
      if (!superResult) {
        return false;
      }
    }

    // AWT Components
    if (component instanceof Button button) {
      componentText = button.getLabel();
    }
    if (component instanceof Checkbox checkbox) {
      componentText = checkbox.getLabel();
    }
    if (component instanceof Label label) {
      componentText = label.getText();
    }
    if (component instanceof TextComponent textComponent) {
      componentText = textComponent.getText();
    }
    if (component instanceof Dialog dialog) {
      componentText = dialog.getTitle();
    }
    if (component instanceof Frame frame) {
      componentText = frame.getTitle();
    }

    // Swing Components
    if (component instanceof AbstractButton abstractButton) {
      // button, menuitem, toggle button
      componentText = abstractButton.getText();
    }
    if (component instanceof JLabel jLabel) {
      componentText = jLabel.getText();
    }

    if (componentText == null) {
      return false;
    }
    if (text == null) {
      return false;
    }

    return stringsMatch(text, componentText);
  }

  /**
   * Retrieve the text of this matcher.
   *
   * @return Returns the text.
   */
  public String getText() {
    return text;
  }
}
