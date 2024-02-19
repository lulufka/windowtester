package abbot.editor.editors;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import abbot.i18n.Strings;
import abbot.script.Expression;

/**
 * A Expression only has its description available for editing.
 */

public class ExpressionEditor extends StepEditor {

    private final Expression step;
    private final JTextArea expr;
    private final JButton run;
    private final Color DEFAULT_FG;
    private final JTextArea status;

    public ExpressionEditor(Expression step) {
        super(step);
        this.step = step;
        remove(getComponentCount() - 1);
        expr = addTextArea(null, step.getExpression());
        expr.setName("expression.text");
        expr.setColumns(80);
        expr.setToolTipText(Strings.get("expression.text.tip"));
        expr.setLineWrap(false);
        run = addButton(Strings.get("expression.eval"));
        run.setToolTipText(Strings.get("expression.eval.tip"));
        DEFAULT_FG = expr.getForeground();
        expr.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        && (e.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    run.doClick();
                }
            }
        });
        // TODO: properly format for readability
        status = new JTextArea();
        status.setBorder(null);
        status.setForeground(Color.red);
        status.setBackground(getBackground());
        status.setLineWrap(true);
        status.setEditable(false);
        add(status);
    }

    public Dimension getPreferredSize() {
        Insets insets = getInsets();
        Dimension size = super.getPreferredSize();
        size.width = expr.getPreferredSize().width;
        if (insets != null) {
            size.width += insets.left + insets.right;
        }
        return size;
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == expr) {
            expr.setForeground(DEFAULT_FG);
            expr.setToolTipText(Strings.get("expression.text.tip"));
            step.setExpression(expr.getText());
            status.setText("");
            fireStepChanged();
        } else if (ev.getSource() == run) {
            expr.setForeground(DEFAULT_FG);
            expr.setToolTipText(Strings.get("expression.text.tip"));
            status.setText("");
            new Thread("expression runner") {
                public void run() {
                    try {
                        step.run();
                    } catch (Throwable e) {
                        //   if (e instanceof bsh.EvalError)
                        //       expr.setForeground(Color.red);
                        status.setText(e.getMessage());
                    }
                    fireStepChanged();
                }
            }.start();
        } else {
            super.actionPerformed(ev);
        }
    }
}
