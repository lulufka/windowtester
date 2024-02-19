package example;

import abbot.tester.ComponentTester;
import junit.extensions.abbot.ComponentTestFixture;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

/**
 * Source code for Tutorial 1.
 * Simple unit tests for example.ArrowButton.
 * Also demonstrates the use of ComponentTestFixture.
 */
public class ArrowButtonTest extends ComponentTestFixture {

    public ArrowButtonTest(final String name) {
        super(name);
    }

    private ComponentTester tester;
    private String gotClick;
    private ArrowButton left;
    private ArrowButton right;
    private ArrowButton up;
    private ArrowButton down;
    private JPanel panel;

    private int count = 0;

    @Override
    protected void setUp() {
        tester = ComponentTester.getTester(ArrowButton.class);
        panel = createPanel();
    }

    public void testClickLeft() {
        // This method provided by ComponentTestFixture
        showFrame(panel);

        gotClick = null;
        tester.actionClick(left);

        assertEquals("Action click LEFT failed", ArrowButton.LEFT, gotClick);
    }

    public void testClickRight() {
        // This method provided by ComponentTestFixture
        showFrame(panel);

        gotClick = null;
        tester.actionClick(right);

        assertEquals("Action click RIGHT failed", ArrowButton.RIGHT, gotClick);
    }

    public void testClickUp() {
        // This method provided by ComponentTestFixture
        showFrame(panel);

        gotClick = null;
        tester.actionClick(up);

        assertEquals("Action click UP failed", ArrowButton.UP, gotClick);
    }

    public void testClickDown() {
        // This method provided by ComponentTestFixture
        showFrame(panel);

        gotClick = null;
        tester.actionClick(down);

        assertEquals("Action click DOWN failed", ArrowButton.DOWN, gotClick);
    }

    public void testRepeatedFire() {
        final ArrowButton arrow = new ArrowButton(ArrowButton.LEFT);
        final ActionListener al = ev -> ++count;
        arrow.addActionListener(al);
        showFrame(arrow);

        // Hold the button down for 5 seconds
        tester.mousePress(arrow);
        tester.actionDelay(5000);
        tester.mouseRelease();

        assertTrue("Didn't get any repeated events", count > 1);
    }

    private JPanel createPanel() {
        this.left = new ArrowButton(ArrowButton.LEFT);
        left.addActionListener(ev -> gotClick = ev.getActionCommand());

        this.right = new ArrowButton(ArrowButton.RIGHT);
        right.addActionListener(ev -> gotClick = ev.getActionCommand());

        this.up = new ArrowButton(ArrowButton.UP);
        up.addActionListener(ev -> gotClick = ev.getActionCommand());

        this.down = new ArrowButton(ArrowButton.DOWN);
        down.addActionListener(ev -> gotClick = ev.getActionCommand());

        JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pane.add(right);
        pane.add(up);
        pane.add(down);
        pane.add(left);
        return pane;
    }

}
