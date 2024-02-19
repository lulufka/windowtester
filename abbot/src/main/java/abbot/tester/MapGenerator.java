package abbot.tester;

import java.awt.Robot;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import abbot.Log;
import abbot.Platform;

/**
 * Provides read/write of locale-specific mappings for virtual keycode-based KeyStrokes to characters and vice versa.
 * <p>
 * If your locale's map is not present in src/abbot/tester/keymaps, please run this class's {@link #main(String[])}
 * method to generate them and
 * <a href="http://sourceforge.net/tracker/?group_id=50939&atid=461492">submit
 * them to the project</a> for inclusion.
 * <p>
 * Variations among locales and OSes are expected; if a map for a locale+OS is not found, the system falls back to the
 * locale map.
 */
public class MapGenerator extends KeyStrokeMap {
    private static boolean setModifiers(
            Robot robot,
            int mask,
            boolean press) {
        try {
            if ((mask & KeyEvent.SHIFT_MASK) != 0) {
                if (press) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                } else {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
            }
            if ((mask & KeyEvent.CTRL_MASK) != 0) {
                if (press) {
                    robot.keyPress(KeyEvent.VK_CONTROL);
                } else {
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                }
            }
            if ((mask & KeyEvent.ALT_MASK) != 0) {
                if (press) {
                    robot.keyPress(KeyEvent.VK_ALT);
                } else {
                    robot.keyRelease(KeyEvent.VK_ALT);
                }
            }
            if ((mask & KeyEvent.META_MASK) != 0) {
                if (press) {
                    robot.keyPress(KeyEvent.VK_META);
                } else {
                    robot.keyRelease(KeyEvent.VK_META);
                }
            }
            if ((mask & KeyEvent.ALT_GRAPH_MASK) != 0) {
                if (press) {
                    robot.keyPress(KeyEvent.VK_ALT_GRAPH);
                } else {
                    robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            // ignore these
        } catch (Exception e) {
            Log.warn(e);
        }
        return false;
    }

    private static class KeyWatcher extends KeyAdapter {
        public char keyChar;
        public boolean keyTyped;
        public boolean keyPressed;
        public String codeName;

        public void keyPressed(KeyEvent e) {
            keyPressed = true;
            // For debug only; activating this stuff tends to interfere with
            // key capture
            /*
            Document d = ((JTextComponent)e.getComponent()).getDocument();
            try {
                String insert = codeName != null
                    ? insert = "\n" + codeName + "=" : "";
                d.insertString(d.getLength(), insert, null);
            }
            catch(BadLocationException ble) {
            }
            */
        }

        public void keyTyped(KeyEvent e) {
            keyChar = e.getKeyChar();
            keyTyped = true;
            // For debug only; activating this stuff tends to interfere with
            // key capture
            /*
            Document d = ((JTextComponent)e.getComponent()).getDocument();
            char[] data = { keyChar };
            try {
                String insert = new String(data)
                    + " (" + String.valueOf((int)keyChar) + ")";
                d.insertString(d.getLength(), insert, null);
            }
            catch(BadLocationException ble) {
            }
            */
            codeName = null;
        }
    }

    private static KeyWatcher watcher = null;
    private static final int UNTYPED = -1;
    private static final int UNDEFINED = -2;
    private static final int ILLEGAL = -3;
    private static final int SYSTEM = -4;
    private static final int ERROR = -5;

    private static int generateKey(
            final Window w,
            final Component c,
            final Robot robot,
            Point p,
            String name,
            int code,
            final boolean refocus) {
        if (watcher == null) {
            watcher = new KeyWatcher();
            c.addKeyListener(watcher);
        }
        try {
            robot.waitForIdle();
            if (refocus) {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        w.show();
                        w.toFront();
                        c.requestFocus();
                        if (Platform.isWindows() || Platform.isMacintosh()) {
                            robot.mouseMove(w.getX() + w.getWidth() / 2,
                                    w.getY() + w.getHeight() / 2);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        }
                    }
                });
            }
            robot.mouseMove(p.x, p.y);
            robot.waitForIdle();
            try {
                watcher.codeName = name;
                watcher.keyTyped = watcher.keyPressed = false;
                robot.keyPress(code);
                robot.keyRelease(code);
                long start = System.currentTimeMillis();
                while (!watcher.keyPressed || !watcher.keyTyped) {
                    if (System.currentTimeMillis() - start > 500) {
                        break;
                    }
                    robot.waitForIdle();
                }
                if (!watcher.keyPressed) {
                    // alt-tab, alt-f4 and the like which get eaten by the OS
                    return SYSTEM;
                } else if (!watcher.keyTyped)
                // keys which result in KEY_TYPED event
                {
                    return UNTYPED;
                } else if (watcher.keyChar == KeyEvent.CHAR_UNDEFINED)
                // usually the same as UNTYPED, but just in case
                {
                    return UNDEFINED;
                } else {
                    return watcher.keyChar;
                }
            } catch (IllegalArgumentException e) {
                // not supported on this system
                return ILLEGAL;
            }
        } catch (Exception e) {
            // usually a core library bug
            Log.warn(e);
            return ERROR;
        }
    }

    private static boolean isFunctionKey(String name) {
        if (name.startsWith("VK_F")) {
            try {
                Integer.parseInt(name.substring(4));
                return true;
            } catch (NumberFormatException e) {
            }
        }
        return false;
    }

    private static final Comparator FIELD_COMPARATOR = new Comparator() {
        public int compare(
                Object o1,
                Object o2) {
            try {
                String n1 = ((Field) o1).getName();
                String n2 = ((Field) o2).getName();
                return n1.compareTo(n2);
            } catch (Exception e) {
                return 0;
            }
        }
    };

    // From a VK_ code + modifiers, produce a simluated KEY_TYPED
    // From a keychar, determine the necessary VK_ code + modifiers
    private static void generateKeyStrokeMap(
            Window w,
            JTextComponent c) {
        // TODO: invoke modifiers for multi-byte input sequences?
        // Skip known modifiers and locking keys
        Collection skip = Arrays.asList("VK_UNDEFINED",
                // modifiers
                "VK_SHIFT", "VK_CONTROL", "VK_META", "VK_ALT", "VK_ALT_GRAPH",
                // special-function keys
                "VK_CAPS_LOCK", "VK_NUM_LOCK", "VK_SCROLL_LOCK",
                // Misc other function keys
                "VK_KANA", "VK_KANJI", "VK_ALPHANUMERIC",
                "VK_KATAKANA", "VK_HIRAGANA", "VK_FULL_WIDTH", "VK_HALF_WIDTH",
                "VK_ROMAN_CHARACTERS",
                "VK_ALL_CANDIDATES", "VK_PREVIOUS_CANDIDATE", "VK_CODE_INPUT",
                "VK_JAPANESE_KATAKANA", "VK_JAPANESE_HIRAGANA",
                "VK_JAPANESE_ROMAN", "VK_KANA_LOCK", "VK_INPUT_METHOD_ON_OFF");
        System.out.println("Generating keystroke map");
        try {
            Robot robot = new Robot();
            // Make sure the window is ready for input
            if (!RobotVerifier.verify(robot)) {
                System.err.println("Robot non-functional, can't generate map");
                System.exit(1);
            }
            robot.delay(500);
            Field[] fields = KeyEvent.class.getDeclaredFields();
            Set codes = new TreeSet(FIELD_COMPARATOR);
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (name.startsWith("VK_")
                        && !skip.contains(name)
                        && !name.startsWith("VK_DEAD_")
                        && !isFunctionKey(name)) {
                    codes.add(fields[i]);
                }
            }
            System.out.println("Total VK_ fields read: " + codes.size());
            Point p = c.getLocationOnScreen();
            p.x += c.getWidth() / 2;
            p.y += c.getHeight() / 2;
            // for now, only do reasonable modifiers; add more if the need
            // arises 
            int[] modifierCombos = {
                    0,
                    KeyEvent.SHIFT_MASK,
                    KeyEvent.CTRL_MASK,
                    KeyEvent.META_MASK,
                    KeyEvent.ALT_MASK,
                    KeyEvent.ALT_GRAPH_MASK,
            };
            String[] MODIFIERS = {
                    "none", "shift", "control", "meta", "alt", "alt graph",
            };
            // These modifiers might trigger window manager functions
            int needRefocus = KeyEvent.META_MASK | KeyEvent.ALT_MASK;
            Map[] maps = new Map[modifierCombos.length];
            for (int m = 0; m < modifierCombos.length; m++) {
                Map map = new TreeMap(FIELD_COMPARATOR);
                int mask = modifierCombos[m];
                if (!setModifiers(robot, mask, true)) {
                    System.out.println("Modifier " + MODIFIERS[m]
                            + " is not currently valid");
                    continue;
                }
                System.out.println("Generating keys with mask="
                        + MODIFIERS[m]);
                Iterator iter = codes.iterator();
                // Always try to fix the focus; who knows what keys have
                // been mapped to the WM
                boolean focus = true;
                while (iter.hasNext()) {
                    Field f = (Field) iter.next();
                    int code = f.getInt(null);
                    //System.out.println(f.getName() + ".");
                    System.out.print(".");
                    int value = generateKey(w, c, robot, p, f.getName(), code,
                            focus
                                    || (mask & needRefocus) != 0);
                    map.put(f, new Integer(value));
                }
                setModifiers(robot, modifierCombos[m], false);
                System.out.println();
                maps[m] = map;
            }

            Properties props = new Properties();
            Iterator iter = maps[0].keySet().iterator();
            while (iter.hasNext()) {
                Field key = (Field) iter.next();
                for (int m = 0; m < modifierCombos.length; m++) {
                    Map map = maps[m];
                    if (map == null) {
                        continue;
                    }
                    String name = key.getName().substring(3);
                    name += "." + Integer.toHexString(modifierCombos[m]);
                    Integer v = (Integer) map.get(key);
                    int value = v.intValue();
                    String hex;
                    switch (value) {
                        case UNTYPED:
                            hex = "untyped";
                            break;
                        case UNDEFINED:
                            hex = "undefined";
                            break;
                        case ILLEGAL:
                            hex = "illegal";
                            break;
                        case SYSTEM:
                            hex = "system";
                            break;
                        case ERROR:
                            hex = "error";
                            break;
                        default:
                            hex = Integer.toHexString(value);
                            break;
                    }
                    props.setProperty(name, hex);
                }
            }
            String[] names = getMapNames();
            String[] desc = getMapDescriptions();
            for (int i = 0; i < names.length; i++) {
                String fn = getFilename(names[i]);
                System.out.println("Saving " + names[i] + " as " + fn);
                FileOutputStream fos = new FileOutputStream(fn);
                props.store(fos, "Key mappings for " + desc[i]);
            }
        } catch (AWTException e) {
            System.err.println("Robot not available, can't generate map");
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * Run this to generate the full set of mappings for a given locale.
     */
    public static void main(String[] args) {
        String language = System.getProperty("abbot.locale.language");
        if (language != null) {
            String country = System.getProperty("abbot.locale.country", "");
            String variant = System.getProperty("abbot.locale.variant", "");
            Locale.setDefault(new Locale(language, country, variant));
        }

        final JFrame frame = new JFrame("KeyStroke mapping generator");
        final JTextArea text = new JTextArea();
        // Remove all action mappings; we want to receive *all* keystrokes
        text.setInputMap(JTextArea.WHEN_FOCUSED, new InputMap());
        frame.getContentPane().add(new JScrollPane(text));
        frame.setLocation(100, 100);
        frame.setSize(250, 90);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        e.getWindow().show();
                    }
                });
            }
        });
        frame.show();
        if (Platform.isOSX()) {
//NOT Supported in Mac Java5+
//            // avoid exit on cmd-Q
//            com.apple.mrj.MRJApplicationUtils.registerQuitHandler(new com.apple.mrj.MRJQuitHandler() {
//                public void handleQuit() { }
//                });
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Thread("keymap generator") {
                    public void run() {
                        generateKeyStrokeMap(frame, text);
                        System.exit(0);
                    }

                }.start();
            }
        });
    }
}
