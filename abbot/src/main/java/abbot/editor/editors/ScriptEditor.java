package abbot.editor.editors;

import abbot.Log;
import abbot.editor.EditorConstants;
import abbot.editor.widgets.Mnemonic;
import abbot.i18n.Strings;
import abbot.script.Fixture;
import abbot.script.Script;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 * Provide convenient editing of a Script step.
 */
public class ScriptEditor extends SequenceEditor {

  private final Script script;

  private final JTextField path;
  private JCheckBox fork;
  private JTextField vmargs;

  public ScriptEditor(Script script) {
    super(script);
    this.script = script;

    path = addTextField(Strings.get("FilePath"), script.getFilename());

    if (!(script instanceof Fixture)) {
      fork = addCheckBox("", script.isForked());
      String key = EditorConstants.ACTION_PREFIX + EditorConstants.ACTION_TOGGLE_FORKED;
      Mnemonic mnemonic = Mnemonic.getMnemonic(Strings.get(key));
      mnemonic.setMnemonic(fork);
      addVMArgs();
    }
  }

  private void addVMArgs() {
    if (script.isForked()) {
      vmargs = addTextField(Strings.get("VMArgs"), script.getVMArgs());
    } else if (vmargs != null) {
      while (getComponent(getComponentCount() - 1) != fork) {
        remove(getComponentCount() - 1);
      }
      vmargs = null;
    }
    revalidate();
    repaint();
  }

  public void actionPerformed(ActionEvent ev) {
    Object src = ev.getSource();
    if (src == path) {
      String filename = path.getText().trim();
      File file = new File(script.getRelativeTo(), filename);
      script.setFile(file);
      try {
        script.load();
      } catch (Exception exc) {
        Log.warn(exc);
      }
      fireStepChanged();
    } else if (src == fork) {
      script.setForked(!script.isForked());
      addVMArgs();
      fireStepChanged();
    } else if (src == vmargs) {
      String text = vmargs.getText();
      if ("".equals(text)) {
        text = null;
      }
      script.setVMArgs(text);
      fireStepChanged();
    } else {
      super.actionPerformed(ev);
    }
  }
}
