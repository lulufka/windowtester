package example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Select a font
 */
public class FontChooser extends JPanel implements PropertyChangeListener, ItemListener {

  protected Font specifiedFont = new Font("Serif", Font.PLAIN, 10);
  protected JComboBox name;
  protected JCheckBox bold, italic;
  protected NumberChooser size;
  protected static String[] availableFonts = null;

  public FontChooser() {
    setLayout(new GridLayout(2, 2));
    if (availableFonts == null) {
      final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      availableFonts = ge.getAvailableFontFamilyNames();
    }
    name = new JComboBox(availableFonts);
    name.setSelectedItem(specifiedFont.getName());
    name.addItemListener(this);
    add(name);
    size = new NumberChooser(1, 128, specifiedFont.getSize());
    add(size);
    size.setColumns(3);
    size.addPropertyChangeListener(this);
    add(bold = new JCheckBox("bold"));
    bold.setSelected(specifiedFont.isBold());
    bold.addItemListener(this);
    add(italic = new JCheckBox("italic"));
    italic.setSelected(specifiedFont.isItalic());
    italic.addItemListener(this);
  }

  public void setSpecifiedFont(final Font f) {
    name.setSelectedItem(f.getName());
    bold.setSelected(f.isBold());
    italic.setSelected(f.isItalic());
    size.setValue(f.getSize());
    fireStateChange();
  }

  public Font getSpecifiedFont() {
    return new Font(
        (String) name.getSelectedItem(),
        (bold.isSelected() ? Font.BOLD : 0) | (italic.isSelected() ? Font.ITALIC : 0),
        size.getValue());
  }

  @Override
  public void itemStateChanged(final ItemEvent e) {
    fireStateChange();
  }

  @Override
  public void propertyChange(final PropertyChangeEvent e) {
    fireStateChange();
  }

  protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

  @Override
  public void addPropertyChangeListener(final PropertyChangeListener l) {
    listeners.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(final PropertyChangeListener l) {
    listeners.removePropertyChangeListener(l);
  }

  /**
   * update our internal font, then tell everyone about it
   */
  protected void fireStateChange() {
    final Font oldFont = specifiedFont;
    specifiedFont = getSpecifiedFont();
    listeners.firePropertyChange("style", oldFont, specifiedFont);
  }

  /**
   * Put up a frame containing a font chooser to make it easy for a script to play with.
   */
  public static void main(final String[] args) {
    final JFrame frame = new JFrame("Font Chooser unit test");
    frame.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(final WindowEvent e) {
            frame.dispose();
          }
        });
    final JPanel panel = new JPanel(new BorderLayout());
    // panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    final String text = "The quick brown fox jumped over the lazy dog";
    final FontChooser chooser = new FontChooser();
    panel.add(chooser, BorderLayout.NORTH);
    final JLabel label = new JLabel(text);
    panel.add(label, BorderLayout.CENTER);
    label.setFont(chooser.getSpecifiedFont());
    frame.getContentPane().add(panel);
    ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(4, 4, 4, 4));

    // Position the frame away from the screen edge to avoid stupid
    // toolbars and such
    frame.setLocation(new Point(50, 50));
    frame.setSize(400, 300);
    frame.pack();

    final Dimension s1 = panel.getPreferredSize();
    final Dimension s2 = frame.getPreferredSize();
    final int hoff = s2.height - s1.height;
    final int width = s2.width;
    chooser.addPropertyChangeListener(
        new PropertyChangeListener() {
          @Override
          public void propertyChange(final PropertyChangeEvent ev) {
            label.setFont((Font) ev.getNewValue());
            if (frame != null) {
              final Dimension size = panel.getPreferredSize();
              size.height += hoff;
              size.width = width;
              frame.setSize(size);
            }
          }
        });
    frame.show();
  }
}
