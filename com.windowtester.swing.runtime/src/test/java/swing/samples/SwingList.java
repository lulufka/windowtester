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

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class SwingList extends JPanel {

    public SwingList() {
        super(new FlowLayout());

        Box box = Box.createHorizontalBox();
        box.add(new JScrollPane(createList1()));
        box.add(Box.createRigidArea(new Dimension(15, 0)));
        box.add(new JScrollPane(createList2()));
        box.add(Box.createRigidArea(new Dimension(15, 0)));
        box.add(new JScrollPane(createList3()));

        add(box);
    }

    private JList<String> createList1() {
        JList<String> list = new JList<>(createListModel());
        list.setName("list1");
        list.setSelectedIndex(0);
        list.setVisibleRowCount(15);
        return list;
    }

    private JList<String> createList2() {
        JList<String> list = new JList<>(createListModel());
        list.setName("list2");
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(15);
        return list;
    }

    private JList<String> createList3() {
        JList<String> list = new JList<>(createListModel());
        list.setName("list3");
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(15);
        return list;
    }

    private ListModel<String> createListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("/three/two/one");
        listModel.addElement("four");
        listModel.addElement("five");
        listModel.addElement("six");
        listModel.addElement("seven");
        return listModel;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
        createAndShowGUI();
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Swing List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SwingList newContentPane = new SwingList();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
