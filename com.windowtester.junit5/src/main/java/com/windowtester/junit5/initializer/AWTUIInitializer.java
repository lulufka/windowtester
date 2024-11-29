package com.windowtester.junit5.initializer;

import java.awt.Frame;

class AWTUIInitializer implements UIIinitializer<Frame> {

  @Override
  public Frame renderUI() {
    Frame frame = new Frame();
    frame.setLocationRelativeTo(null);
    frame.pack();

    return frame;
  }

  @Override
  public void closeUI(Frame frame) {
    if (frame != null) {
      frame.dispose();
    }
  }
}
