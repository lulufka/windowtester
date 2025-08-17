package abbot.editor.editors;

import abbot.i18n.Strings;
import abbot.script.Sample;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

/**
 * Provide convenient editing of a Sample step.
 */
public class SampleEditor extends PropertyCallEditor {

  private final Sample step;
  private final JTextField property;

  public SampleEditor(Sample step) {
    super(step);
    this.step = step;
    property = addTextField(Strings.get("PropertyName"), step.getPropertyName());
  }

  public void actionPerformed(ActionEvent ev) {
    Object src = ev.getSource();
    if (src == property) {
      String name = property.getText().trim();
      if (!"".equals(name)) {
        step.setPropertyName(name);
        fireStepChanged();
      }
    } else {
      super.actionPerformed(ev);
    }
  }
}
