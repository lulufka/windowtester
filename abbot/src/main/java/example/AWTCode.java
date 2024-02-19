package example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import abbot.Log;
import abbot.tester.ComponentTester;

public class AWTCode {

    private static class PopupListener extends MouseAdapter {
        PopupMenu menu;

        public PopupListener(final PopupMenu menu) {
            this.menu = menu;
        }

        private void showPopup(final MouseEvent e) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
        }
    }

    public static void main(String[] args) {
        args = Log.init(args);
        final Frame frame = new Frame("AWT Code");
        final MenuBar mb = new MenuBar() {
            @Override
            protected void processEvent(final AWTEvent e) {
                Log.debug("Got " + ComponentTester.toString(e));
                super.processEvent(e);
            }
        };
        final Menu menu = new Menu("File") {
            @Override
            protected void processEvent(final AWTEvent e) {
                Log.debug("Got " + ComponentTester.toString(e));
                super.processEvent(e);
            }
        };
        MenuItem mi = new MenuItem("Open") {
            @Override
            protected void processEvent(final AWTEvent e) {
                Log.debug("Got " + ComponentTester.toString(e));
                super.processEvent(e);
            }
        };
        menu.add(mi);
        menu.add(new CheckboxMenuItem("Check Me"));
        mb.add(menu);
        final TextField tf = new TextField("Text Field");
        final TextArea ta = new TextArea("Text Area with wide/long text"
                + "\n\n\n\n\n\n\n");
        ta.setSize(200, 100);

        final Panel pane = new Panel();
        // Button, Canvas, Checkbox, Choice, Label, List, Scrollbar
        // TextComponent, TextField, TextArea
        // Container, Panel, ScrollPane, Window, Frame, Dialog
        final Choice choice = new Choice();
        choice.add("One");
        choice.add("Two");
        final List list = new List();
        list.add("One");
        list.add("Two");
        list.add("Three");
        final ScrollPane sp = new ScrollPane();
        final Canvas canvas = new Canvas();
        canvas.setSize(500, 500);
        sp.add(canvas);
        sp.setSize(100, 100);

        pane.add(new Button("Button"));
        pane.add(sp); // canvas within scrollpane
        pane.add(new Checkbox("Checkbox"));
        pane.add(choice);
        final Label label = new Label("Label");
        pane.add(label);
        pane.add(list);
        pane.add(tf);
        pane.add(new Scrollbar());
        pane.add(ta);

        final PopupMenu popup = new PopupMenu("MyPopupMenu");
        popup.add(mi = new MenuItem("first"));
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println("Got first popup item");
            }
        });
        popup.add(new MenuItem("second"));
        popup.add(new CheckboxMenuItem("check me"));
        pane.add(popup);
        pane.addMouseListener(new PopupListener(popup));

        frame.setMenuBar(mb);
        frame.add(pane);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setSize(300, 400);
        frame.show();
    }
}
