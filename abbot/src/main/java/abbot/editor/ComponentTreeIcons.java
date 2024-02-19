package abbot.editor;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * Provides JTree icons for different Components.
 */

class ComponentTreeIcons {
    private final Map icons = new HashMap();

    public Icon getIcon(Class cls) {
        String className = cls.getName();
        int lastdot = className.lastIndexOf(".");
        String simpleName = lastdot != -1
                ? className.substring(lastdot + 1).toLowerCase() : className;
        Icon icon = (Icon) icons.get(className);
        if (icon == null) {
            URL url = getClass().getResource("icons/" + className + ".gif");
            if (url == null) {
                url = getClass().getResource("icons/" + simpleName + ".gif");
            }
            if (url == null) {
                if (Component.class.equals(cls)) {
                    return null;
                }
                icon = getIcon(cls.getSuperclass());
            } else {
                icon = new ImageIcon(url);
            }
            if (icon != null) {
                icons.put(className, icon);
            }
        }
        return icon;
    }
}
