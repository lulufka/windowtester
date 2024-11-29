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

import abbot.Log;
import abbot.script.Action;
import abbot.script.ComponentReference;
import abbot.script.Resolver;
import abbot.script.Step;
import abbot.tester.JComboBoxTester;
import abbot.util.AWT;
import com.windowtester.recorder.event.IUISemanticEvent;
import com.windowtester.recorder.event.UISemanticEventFactory;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JList;

/**
 * Record basic semantic events you might find on an JComboBox. <p>
 * <ul>
 * <li>Select an item by value (toString representation)
 * <li>Enter a value (if editable) (not done)
 * </ul>
 * <p>
 * abbot.editor.recorder.JComboBoxRecorder
 */
public class JComboBoxRecorder extends JComponentRecorder {

  private final JComboBoxTester tester = new JComboBoxTester();
  private JComboBox<?> combo = null;
  private JList<?> list = null;
  private int index = -1;
  private ActionListener listener = null;

  public JComboBoxRecorder(Resolver resolver) {
    super(resolver);
  }

  @Override
  public boolean accept(AWTEvent event) {
    if (isClick(event) && getComboBox(event) == null) {
      return false;
    }
    return super.accept(event);
  }

  @Override
  protected void init(int recordingType) {
    super.init(recordingType);
    combo = null;
    list = null;
    index = -1;
    listener = null;
  }

  /**
   * Return the JComboBox for the given event, or null if none.
   */
  private JComboBox<?> getComboBox(AWTEvent event) {
    Component comp = (Component) event.getSource();
    // Depends somewhat on LAF; sometimes the combo box is itself the
    // button, sometimes a panel containing the button.
    if (comp instanceof javax.swing.JButton) {
      comp = comp.getParent();
    }
    if (comp instanceof JComboBox) {
      return (JComboBox<?>) comp;
    }
    return null;
  }

  @Override
  protected boolean canMultipleClick() {
    return false;
  }

  @Override
  protected boolean parseClick(AWTEvent event) {
    if (isFinished()) {
      return false;
    }

    // FIXME add key-based activation/termination?
    boolean consumed = true;
    if (combo == null) {
      combo = getComboBox(event);
      listener = ev -> {
        index = combo.getSelectedIndex();
        if (!combo.isPopupVisible()) {
          combo.removeActionListener(listener);
          setFinished(true);
        }
      };
      combo.addActionListener(listener);
      setStatus("Waiting for selection");
    } else if (event.getID() == KeyEvent.KEY_RELEASED
        && (((KeyEvent) event).getKeyCode() == KeyEvent.VK_SPACE
        || ((KeyEvent) event).getKeyCode() == KeyEvent.VK_ENTER)) {
      index = combo.getSelectedIndex();
      setFinished(true);
    }
    // Cancel via click somewhere else
    else if (event.getID() == MouseEvent.MOUSE_PRESSED
        && !AWT.isOnPopup((Component) event.getSource())
        && combo != getComboBox(event)) {
      setFinished(true);
      consumed = false;
    }
    // Cancel via ESC key
    else if (event.getID() == KeyEvent.KEY_RELEASED
        && ((KeyEvent) event).getKeyCode() == KeyEvent.VK_ESCAPE) {
      setStatus("Selection canceled");
      setFinished(true);
    } else {
      Log.debug("Event ignored");
    }
    if (list == null && combo.isPopupVisible()) {
      list = tester.findComboList(combo);
    }

    if (isFinished()) {
      combo.removeActionListener(listener);
      listener = null;
    }

    return consumed;
  }

  @Override
  protected Step createStep() {
    Step step = null;
    if (getRecordingType() == SE_CLICK) {
      step = createSelection(combo, index);
    } else {
      step = super.createStep();
    }
    return step;
  }

  protected Step createSelection(JComboBox<?> combo, int index) {
    Step step = null;
    if (combo != null && index != -1) {
      ComponentReference cr = getResolver().addComponent(combo);
      String value = tester.getValueAsString(combo, list, combo.getItemAt(index), index);

      if (value == null) {
        step =
            new Action(
                getResolver(),
                null,
                "actionSelectIndex",
                new String[]{cr.getID(), String.valueOf(index)},
                javax.swing.JComboBox.class);
      } else {
        step =
            new Action(
                getResolver(),
                null,
                "actionSelectItem",
                new String[]{cr.getID(), value},
                javax.swing.JComboBox.class);
        // Create semantic event
        IUISemanticEvent semanticEvent =
            UISemanticEventFactory.createComboSelectionEvent(combo, value);
        notify(semanticEvent);
      }
    }
    return step;
  }
}
