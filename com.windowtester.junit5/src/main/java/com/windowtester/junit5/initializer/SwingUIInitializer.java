package com.windowtester.junit5.initializer;

import javax.swing.JFrame;

class SwingUIInitializer implements UIIinitializer<JFrame> {

  @Override
  public JFrame renderUI() {
    JFrame frame = new JFrame();
    frame.setLocationRelativeTo(null);
    frame.pack();

    return frame;
  }

  @Override
  public void closeUI(JFrame frame) {
    if (frame != null) {
      frame.dispose();
    }
  }
}
