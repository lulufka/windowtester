package abbot.editor;

import abbot.script.Step;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepTransferable implements Transferable {

  public static final DataFlavor STEP_FLAVOR =
      new DataFlavor(
          "application/x-java-serialized-object;class=abbot.script.Step", "Abbot script step");
  public static final DataFlavor STEP_LIST_FLAVOR =
      new DataFlavor(
          "application/x-java-serialized-object;class=java.util.ArrayList",
          "List of Abbot script steps");

  // A single step is available as itself or as a list
  private static final DataFlavor[] FLAVORS = {
    STEP_FLAVOR, STEP_LIST_FLAVOR,
  };

  // Can't get a list as a single step
  private static final DataFlavor[] LIST_FLAVORS = {STEP_LIST_FLAVOR};

  private static final List FLAVOR_LIST = Arrays.asList(FLAVORS);
  private static final List LIST_FLAVOR_LIST = Arrays.asList(LIST_FLAVORS);

  private final Step step;
  private final List steps;
  private final List flavorList;
  private final DataFlavor[] flavors;

  public StepTransferable(Step step) {
    this.step = step;
    this.steps = new ArrayList();
    steps.add(step);
    flavorList = FLAVOR_LIST;
    flavors = FLAVORS;
  }

  public StepTransferable(List steps) {
    this.step = null;
    this.steps = steps;
    flavorList = LIST_FLAVOR_LIST;
    flavors = LIST_FLAVORS;
  }

  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavorList.contains(flavor);
  }

  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    if (flavor.isMimeTypeEqual(STEP_FLAVOR.getMimeType())) {
      if (step != null) {
        return step;
      }
    } else if (flavor.isMimeTypeEqual(STEP_LIST_FLAVOR.getMimeType())) {
      return steps;
    }
    throw new UnsupportedFlavorException(flavor);
  }

  public String toString() {
    return "Transferable " + (step != null ? step.toString() : "List of Steps");
  }
}
