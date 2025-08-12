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
package com.windowtester.runtime.internal.junit4.runners;

import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import com.windowtester.internal.runtime.junit.core.ISequenceRunner.IRunnable;
import com.windowtester.runtime.internal.junit4.mirror.runners.TestMethodRunner;
import com.windowtester.runtime.internal.junit4.runner.IExecutionContextProvider;
import com.windowtester.runtime.internal.junit4.runner.ITestRunnerDelegate;
import com.windowtester.runtime.internal.junit4.runner.RunManager;


/**
 *
 * @author Phil Quitslund
 *
 */
public class UITestMethodRunner extends TestMethodRunner {

	private final ITestRunnerDelegate _runner;
	private final IExecutionContextProvider _provider;


	public UITestMethodRunner(Object test, Method method, RunNotifier notifier,
			Description description, ITestRunnerDelegate runner, IExecutionContextProvider _contextProvider) {
		super(test, method, notifier, description);
		_runner = runner;
		_provider = _contextProvider;
	}

	
	/* (non-Javadoc)
	 * @see org.junit.mirror.internal.runners.BeforeAndAfterRunner#runProtected()
	 */
	@Override
	public void runProtected() {
		try {
			new RunManager(getRunner(), getExecutionContextProvider()).run(new IRunnable() {
				public void run() throws Throwable {
					UITestMethodRunner.super.runProtected();
				}
			}, fDescription);
		} catch (Throwable e) {
			addFailure(e);
		}
	}

	private IExecutionContextProvider getExecutionContextProvider() {
		return _provider;
	}

	private ITestRunnerDelegate getRunner() {
		return _runner;
	}
	
}
