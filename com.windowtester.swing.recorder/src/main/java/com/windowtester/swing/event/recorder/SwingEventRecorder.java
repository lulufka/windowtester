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
package com.windowtester.swing.event.recorder;

import abbot.editor.recorder.RecordingFailedException;
import abbot.script.Resolver;
import abbot.script.Step;
import com.windowtester.recorder.IEventFilter;
import com.windowtester.recorder.IEventRecorder;
import com.windowtester.recorder.event.ISemanticEventListener;
import com.windowtester.recorder.event.IUISemanticEvent;
import com.windowtester.recorder.event.meta.RecorderErrorEvent;
import com.windowtester.recorder.event.meta.RecorderTraceEvent;
import com.windowtester.swing.event.spy.SpyEventHandler;
import com.windowtester.swing.recorder.ComponentRecorder;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/***
 *  Extend the Abbot EventRecorder for use with windowtester
 *  implement the IEventRecorder interface
 */
public class SwingEventRecorder extends abbot.editor.recorder.EventRecorder
    implements IEventRecorder {

  /**
   * A flag to indicate record state
   */
  protected boolean isRecording;

  /**
   * A flag to indicate pause state
   */
  protected boolean isPaused;

  /**
   * A list of primitive event filters
   */
  private List<IEventFilter> filters;

  private final SpyEventHandler spyHandler = new SpyEventHandler();

  public SwingEventRecorder(Resolver resolver, boolean captureMotion) {
    super(resolver, captureMotion);
  }

  @Override
  public void addListener(ISemanticEventListener listener) {
    // Install existing semantic recorders
    for (Class<?> recorderClass : recorderClasses) {
      ((ComponentRecorder) getSemanticRecorder(recorderClass)).addListener(listener);
    }
  }

  @Override
  protected String getRecorderName(String cname) {
    return "com.windowtester.swing.recorder." + cname + "Recorder";
  }

  @Override
  public void terminate() throws RecordingFailedException {
    isRecording = false;
    super.terminate();
    // get the list of listeners
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyDispose();
    }
  }

  @Override
  public void toggleSpyMode() {
    for (ISemanticEventListener iSemanticEventListener : getListeners()) {
      iSemanticEventListener.notifySpyModeToggle();
    }
  }

  @Override
  public void stop() {
    //	System.out.println("Stopping recorder");
    try {
      isRecording = false;
      super.terminate();
      List<ISemanticEventListener> listeners = getListeners();
      for (ISemanticEventListener listener : listeners) {
        listener.notifyStop();
      }
    } catch (RecordingFailedException e) {
      Throwable error = e.getReason();
      System.out.println("Recording stop failure: " + error.toString());
    }
    // note: this does a create...
    Step step = getStep();
  }

  @Override
  public void start() {
    super.start();
    isRecording = true;
    isPaused = false;
    //	get the list of listeners
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyStart();
    }
  }

  @Override
  public void write() {
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyWrite();
    }
  }

  @Override
  public void restart() {
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyRestart();
    }
  }

  @Override
  public void pause() {
    isRecording = false;
    isPaused = true;
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyPause();
    }
  }

  @Override
  public void removeListener(ISemanticEventListener listener) {
    // remove listener from semantic recorders
    for (Class<?> recorderClass : recorderClasses) {
      ((ComponentRecorder) getSemanticRecorder(recorderClass)).removeListener(listener);
    }
  }

  @Override
  public void record(IUISemanticEvent semanticEvent) {
    //	get the list of listeners
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notify(semanticEvent);
    }
  }

  @Override
  public void reportError(RecorderErrorEvent event) {
    // get the list of listeners
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyError(event);
    }
  }

  @Override
  public void trace(RecorderTraceEvent event) {
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyTrace(event);
    }
  }

  @Override
  public void addEventFilter(IEventFilter filter) {
    List<IEventFilter> filters = getEventFilters();
    if (filters.contains(filter)) {
      debug("multiple adds of filter: : " + filter);
    } else {
      filters.add(filter);
    }
  }

  @Override
  public void removeEventFilter(IEventFilter filter) {
    List<IEventFilter> filters = getEventFilters();
    if (filters.contains(filter)) {
      debug("filter removed that was not registered: " + filter);
    } else {
      filters.remove(filter);
    }
  }

  @Override
  public void addHook(String hookName) {
    List<ISemanticEventListener> listeners = getListeners();
    for (ISemanticEventListener listener : listeners) {
      listener.notifyAssertionHookAdded(hookName);
    }
  }

  /**
   * @return list of listeners attached to the recorder
   */
  private List<ISemanticEventListener> getListeners() {
    return ((ComponentRecorder) getSemanticRecorder(recorderClasses[0])).getListeners();
  }

  public void notify(IUISemanticEvent semanticEvent) {
    for (ISemanticEventListener listener : getListeners()) {
      listener.notify(semanticEvent);
    }
  }

  /**
   * @return the list of event filters
   */
  private List<IEventFilter> getEventFilters() {
    if (filters == null) {
      filters = new ArrayList<>();
    }
    return filters;
  }

  /**
   * Send this debug message to the tracer
   */
  private static void debug(String msg) {
    //        DebugHandler.trace(TRACE_OPTION, msg);
  }

  @Override
  public boolean isRecording() {
    return isRecording;
  }

  @Override
  public void insertStep(Step step) {
    super.insertStep(step);
    if (capturedEvent != null && capturedEvent.getID() == MouseEvent.MOUSE_ENTERED) {
      IUISemanticEvent event = spyHandler.interpretHover(capturedEvent);
      if (event != null) {
        notify(event);
      }
    }
  }
}
