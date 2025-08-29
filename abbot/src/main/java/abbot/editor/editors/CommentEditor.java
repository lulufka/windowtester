package abbot.editor.editors;

import abbot.script.Comment;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
 * A Comment only has its description available for editing.
 */
public class CommentEditor extends StepEditor {

  private final Comment comment;
  private final JTextArea description;

  public CommentEditor(Comment comment) {
    super(comment);
    this.comment = comment;
    // remove the default description
    remove(getComponentCount() - 1);
    description = addTextArea(null, comment.getDescription());
  }

  public void actionPerformed(ActionEvent ev) {
    if (ev.getSource() == description) {
      comment.setDescription(description.getText());
      fireStepChanged();
    } else {
      super.actionPerformed(ev);
    }
  }
}
