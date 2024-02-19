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
package com.windowtester.recorder;

import com.windowtester.recorder.event.IUISemanticEvent;
import com.windowtester.recorder.event.meta.RecorderErrorEvent;
import com.windowtester.recorder.event.meta.RecorderTraceEvent;

/**
 * An interface for UI event recorders.
 */
public interface IEventRecorder extends ISemanticEventProvider {

    /**
     * Start recording.
     */
    void start();

    /**
     * Stop recording.
     */
    void stop();

    /**
     * Write the recording.
     */
    void write();

    /**
     * Restart the recorder.
     */
    void restart();

    /**
     * Terminate the current recording session.
     */
    void terminate();

    /**
     * Pause the current recording session.
     */
    void pause();

    /**
     * Record the given semantic event.
     *
     * @param semanticEvent - the event to record
     */
    void record(IUISemanticEvent semanticEvent);

    /**
     * Report an internal error.
     *
     * @param event - the error event
     */
    void reportError(RecorderErrorEvent event);

    /**
     * Send a trace event (for debugging).
     *
     * @param event - the trace event
     */
    void trace(RecorderTraceEvent event);

    /**
     * Add an event filter to filter out events from recording.
     *
     * @param filter - the filter to add
     */
    void addEventFilter(IEventFilter filter);

    /**
     * Remove an event filter to filter out events from recording.
     *
     * @param filter - the filter to remove
     */
    void removeEventFilter(IEventFilter filter);

    /**
     * Add an assertion hook.
     *
     * @param hookName - the name of the assertion hook.
     */
    void addHook(String hookName);

    /**
     * Toggle spy mode.
     */
    void toggleSpyMode();

    boolean isRecording();

}
