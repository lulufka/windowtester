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
package swing.samples;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SwingText extends JPanel {

  private final JTextField textField;
  private String text;

  private boolean choice1;
  private boolean choice2;

  public SwingText() {
    super();

    textField = new JTextField("", 20);
    textField.setName("textField");
    textField.setText("");
    textField.addActionListener(
        evt -> {
          textField.setText(((JTextField) evt.getSource()).getText());
          text = textField.getText();
        });
    add(textField);

    JLabel label2 = new JLabel("Name");
    add(label2);
    JTextField textField2 = new JTextField("", 20);
    textField2.setText("Jane");
    add(textField2);

    JMenuItem menuItem1 = new JMenuItem("choice1");
    menuItem1.addActionListener(e -> choice1 = !choice1);

    JMenuItem menuItem2 = new JMenuItem("choice2");
    menuItem2.addActionListener(e -> choice2 = !choice2);

    JPopupMenu popup = new JPopupMenu();
    popup.add(menuItem1);
    popup.add(menuItem2);

    //      Add listener to the text area so the popup menu can come up.
    MouseListener popupListener = new PopupListener(popup);
    textField.addMouseListener(popupListener);
  }

  public String getText() {
    return text;
  }

  public JTextField getTextComponent() {
    return textField;
  }

  private static class PopupListener extends MouseAdapter {

    private final JPopupMenu popup;

    PopupListener(JPopupMenu popupMenu) {
      popup = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
      if (SwingUtilities.isRightMouseButton(e) || e.isPopupTrigger()) {
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
