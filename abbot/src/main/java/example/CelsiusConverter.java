/**
 * A simple Swing UI demonstrating the use of JButton, JTextField and JLabel. Code contributed by Satadip Dutta was
 * labeled <code>v 1.3</code>, and was extended/revised by Tom Roche & Tim Wall.
 *
 * @author Satadip Dutta
 * @version $Id: CelsiusConverter.java,v 1.1 2006-11-03 18:52:42 pq Exp $
 */
package example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class CelsiusConverter extends JPanel {
  private static final int NPRECISIONS = 5;
  private int precision;
  private final JLabel celsiusLabel;
  private final JLabel fahrLabel;
  private final JTextField inputText;

  // Constructor
  public CelsiusConverter() {
    // Create the container.
    final int MARGIN = 2;
    setLayout(new GridLayout(0, 2, MARGIN, MARGIN));
    setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));

    final JPanel left = new JPanel(new BorderLayout());
    left.setBorder(BorderFactory.createEtchedBorder());
    add(left);

    final JPanel right = new JPanel(new BorderLayout());
    right.setBorder(BorderFactory.createEtchedBorder());
    add(right);

    // Create widgets.
    inputText = new JTextField(2);
    final JButton convertTemp = new JButton(lookupString("conversion.button.text")); // $NON-NLS-1$
    celsiusLabel = new JLabel(lookupString("input.label.text"), SwingConstants.LEFT); // $NON-NLS-1$
    fahrLabel = new JLabel(lookupString("output.label.text"), SwingConstants.LEFT); // $NON-NLS-1$

    // Add widgets to container.
    left.add(inputText, BorderLayout.NORTH);
    left.add(convertTemp, BorderLayout.SOUTH);

    right.add(celsiusLabel, BorderLayout.NORTH);
    right.add(fahrLabel, BorderLayout.SOUTH);

    celsiusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    fahrLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Listen to events from Convert button.
    convertTemp.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent event) {
            updateLabels();
          }
        });
    inputText.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            convertTemp.doClick();
          }
        });
  }

  /**
   * Convert the given Celsius value to Fahrenheit.
   */
  public static double convert(final double celsius) {
    return celsius * 9 / 5 + 32;
  }

  // convenience
  public static String lookupString(final String key) {
    return CelsiusConverterStrings.getString(key);
  }

  // convenience, reused in tests
  public static String formatOutput(final String format, final double value, int precision) {
    final MessageFormat f = new MessageFormat(format);
    String pattern = "#";
    if (precision > 0) {
      pattern += ".";
    }
    while (precision-- > 0) {
      pattern += "#";
    }
    final DecimalFormat dfmt = new DecimalFormat(pattern);
    return f.format(new Object[] {dfmt.format(value)});
  }

  // convenience, reused in tests
  public static String fahrenheitOutput(final double value, final int precision) {
    return formatOutput(lookupString("F"), value, precision);
  }

  // convenience, reused in tests
  public static String celsiusOutput(final double value, final int precision) {
    return formatOutput(lookupString("C"), value, precision);
  }

  private void updateLabels() {
    final String in = inputText.getText();
    try {
      // Convert degrees Celsius to Fahrenheit.
      final double celsius = Double.parseDouble(in);
      celsiusLabel.setText(formatOutput(lookupString("C"), celsius, precision)); // $NON-NLS-1$
      final double fahr = convert(celsius);
      fahrLabel.setText(formatOutput(lookupString("F"), fahr, precision)); // $NON-NLS-1$
    } catch (final Exception e) {
      inputText.selectAll();
    }
  }

  private JMenuBar createMenuBar() {

    final JMenuBar menuBar = new JMenuBar();
    final JMenu menu = new JMenu(lookupString("menu.options"));
    menuBar.add(menu);
    final JMenu submenu = new JMenu(lookupString("menu.precision"));
    menu.add(submenu);
    final ActionListener listener =
        new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
              precision = Integer.parseInt(e.getActionCommand());
              updateLabels();
            }
          }
        };
    final ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < NPRECISIONS; i++) {
      final JRadioButtonMenuItem item = new JRadioButtonMenuItem(String.valueOf(i), i == precision);
      item.setActionCommand(String.valueOf(i));
      item.addActionListener(listener);
      group.add(item);
      submenu.add(item);
    }

    return menuBar;
  }

  /**
   * Stick us in a <code>JFrame</code>. Reused in tests.
   */
  public void enframe(final JFrame frame) {
    frame.setTitle(lookupString("frame.title")); // $NON-NLS-1$
    // Add the panel to the frame.
    frame.setContentPane(this);
    frame.setJMenuBar(createMenuBar());

    // Exit when the window is closed.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.pack();
    final Dimension d = frame.getSize();
    d.width = Math.max(d.width, 350);
    frame.setSize(d);
  }

  // main method
  public static void main(final String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (final Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    final CelsiusConverter converter = new CelsiusConverter();
    final JFrame frame = new JFrame();
    converter.enframe(frame);
    frame.setVisible(true);
  }
}
