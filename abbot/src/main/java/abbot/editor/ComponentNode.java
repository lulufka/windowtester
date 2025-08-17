package abbot.editor;

import abbot.finder.Hierarchy;
import abbot.i18n.Strings;
import abbot.tester.Robot;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuContainer;
import java.awt.MenuItem;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Provides a JTree-compatible node model for displaying a given hierarchy.
 */
public class ComponentNode extends DefaultMutableTreeNode {

  private Hierarchy hierarchy;
  private final Map map;
  private boolean loaded;

  public ComponentNode(Hierarchy hierarchy) {
    super(null, true);
    this.hierarchy = hierarchy;
    map = new WeakHashMap();
  }

  protected ComponentNode(ComponentNode parent, Object obj) {
    super(obj, (obj == null || obj instanceof Container || obj instanceof MenuContainer));
    hierarchy = parent.hierarchy;
    map = parent.map;
    map.put(obj, this);
  }

  public ComponentNode(ComponentNode parent, Component comp) {
    this(parent, (Object) comp);
  }

  public ComponentNode(ComponentNode parent, MenuComponent comp) {
    this(parent, (Object) comp);
  }

  public ComponentNode(ComponentNode parent, MenuItem comp) {
    this(parent, (Object) comp);
  }

  public TreeNode getChildAt(int index) {
    load();
    return super.getChildAt(index);
  }

  public int getChildCount() {
    load();
    return super.getChildCount();
  }

  public void reload() {
    reload(hierarchy);
  }

  public void reload(Hierarchy hierarchy) {
    this.hierarchy = hierarchy;
    map.clear();
    loaded = false;
  }

  private void load() {
    if (loaded) {
      return;
    }

    loaded = true;
    removeAllChildren();
    Object obj = getUserObject();
    if (isRoot()) {
      Iterator iter = hierarchy.getRoots().iterator();
      while (iter.hasNext()) {
        add(new ComponentNode(this, (Component) iter.next()));
      }
    } else if (obj instanceof Container) {
      // Specially handle AWT MenuBar
      if (obj instanceof Frame) {
        Frame f = (Frame) obj;
        if (f.getMenuBar() != null) {
          add(new ComponentNode(this, f.getMenuBar()));
        }
      }
      Collection children = hierarchy.getComponents(getComponent());
      Iterator iter = children.iterator();
      while (iter.hasNext()) {
        add(new ComponentNode(this, (Component) iter.next()));
      }
    }
    // Specially handle AWT menus
    else if (obj instanceof MenuBar) {
      MenuBar mb = (MenuBar) obj;
      for (int i = 0; i < mb.getMenuCount(); i++) {
        add(new ComponentNode(this, mb.getMenu(i)));
      }
    } else if (obj instanceof Menu) {
      Menu menu = (Menu) obj;
      for (int i = 0; i < menu.getItemCount(); i++) {
        add(new ComponentNode(this, menu.getItem(i)));
      }
    }
  }

  Component getParent(Component c) {
    return hierarchy.getParent(c);
  }

  public Component getComponent() {
    if (getUserObject() instanceof Component) {
      return (Component) getUserObject();
    }
    return null;
  }

  public int hashCode() {
    return (isRoot() ? super.hashCode() : getUserObject().hashCode());
  }

  public boolean equals(Object other) {
    return this == other
        || ((other instanceof ComponentNode)
            && (getUserObject() == ((ComponentNode) other).getUserObject()));
  }

  public String toString() {
    if (isRoot()) {
      return getChildCount() == 0 ? Strings.get("NoComponents") : Strings.get("AllFrames");
    }
    return Robot.toString(getUserObject());
  }

  public ComponentNode getNode(Component comp) {
    if (comp == null) {
      return (ComponentNode) getRoot();
    }
    ComponentNode node = (ComponentNode) map.get(comp);
    if (node == null) {
      Component parentComp = getParent(comp);
      ComponentNode parent = getNode(parentComp);
      if (parent == null) {
        return getNode(parentComp);
      }
      // Fall back to parent if no child matches.
      node = parent;
      for (int i = 0; i < parent.getChildCount(); i++) {
        ComponentNode child = (ComponentNode) parent.getChildAt(i);
        if (child.getComponent() == comp) {
          node = child;
          break;
        }
      }
    }
    return node;
  }

  public TreePath getPath(Component comp) {
    ComponentNode node = getNode(comp);
    return new TreePath(node.getPath());
  }
}
