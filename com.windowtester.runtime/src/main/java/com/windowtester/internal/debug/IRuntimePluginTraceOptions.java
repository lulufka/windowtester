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
package com.windowtester.internal.debug;

import com.windowtester.internal.runtime.Platform;

/**
 * Trace option flag constants for use when running in trace mode.
 */
public interface IRuntimePluginTraceOptions {

    String ID = "com.windowtester.runtime";

    /**
     * The <code>BASIC</code> option enables basic lifecycle event tracing.
     */
    String BASIC = ID + "/basic";

    /**
     * The <code>WIDGET_SELECTION</code> option enables verbose tracing of widget selection events.
     */
    String WIDGET_SELECTION = ID + "/runtime/widget/selection";

    /**
     * The <code>HIERARCHY_INFO</code> option enables verbose tracing of hierarchy info creation.
     */
    String HIERARCHY_INFO = ID + "/runtime/hierarchy";

    /**
     * The <code>CONDITIONS</code> option enables verbose tracing of condition handling.
     */
    String CONDITIONS = ID + "/runtime/conditions";

    /**
     * The <code>UI_THREAD_MONITOR</code> option enables verbose tracing of condition handling.
     */
    String UI_THREAD_MONITOR = ID + "/runtime/uiThreadMonitor";
}