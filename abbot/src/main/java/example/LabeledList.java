package example;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Source code for Tutorial 2.
 */
public class LabeledList extends JPanel {

  private final JList<String> list;
  private final JLabel label;

  public LabeledList(String[] initialContents) {
    setLayout(new BorderLayout());

    list = new JList<>(initialContents);
    add(list, BorderLayout.CENTER);

    label = new JLabel("Selected: ");
    add(label, BorderLayout.SOUTH);

    // Update the label whenever the list selection changes
    list.addListSelectionListener(event -> label.setText("Selected: " + list.getSelectedValue()));
  }
}
