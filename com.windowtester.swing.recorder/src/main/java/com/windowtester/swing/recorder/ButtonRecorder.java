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
package com.windowtester.swing.recorder;

import java.awt.*;

import abbot.script.Action;
import abbot.script.ComponentReference;
import abbot.script.Resolver;
import abbot.script.Step;
import com.windowtester.recorder.event.IUISemanticEvent;
import com.windowtester.recorder.event.UISemanticEventFactory;

/**
 * Record simple clicks on a Button component. <p>
 */
public class ButtonRecorder extends ComponentRecorder {

    public ButtonRecorder(Resolver resolver) {
        super(resolver);
    }

    /**
     * Don't need to store any position or modifier information.
     */
    protected Step createClick(
            Component target,
            int x,
            int y,
            int mods,
            int count) {
        // windowtester semantic event generation
        IUISemanticEvent semanticEvent =
                UISemanticEventFactory.createWidgetSelectionEvent(target, x, y, count, getButton());
        notify(semanticEvent);

        ComponentReference cr = getResolver().addComponent(target);
        return new Action(getResolver(), null, "actionClick",
                new String[]{cr.getID()});
    }
}
