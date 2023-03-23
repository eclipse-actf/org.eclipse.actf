/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util.timer;

/**
 * Utility to periodically check if action defined in
 * {@link WaitExecSyncEventHandler} can be executed. If canRun becomes true, it
 * execute the run method of {@link WaitExecSyncEventHandler}.
 * 
 * @see WaitExecSyncEventHandler
 * @see SyncEventListener
 */
public class WaitExecSyncEventListener implements SyncEventListener {

	private WeakSyncTimer timer = WeakSyncTimer.getTimer();

	private WaitExecSyncEventHandler handler;

	private long start;

	private double interval;

	/**
	 * Constructor of the class
	 * 
	 * @param handler
	 *            target {@link WaitExecSyncEventHandler}
	 */
	public WaitExecSyncEventListener(WaitExecSyncEventHandler handler) {
		super();
		this.handler = handler;
		startListener();
	}

	private void startListener() {
		start = System.currentTimeMillis();
		interval = handler.getInterval();
		timer.addEventListener(this);
	}

	private void stopListener() {
		interval = 0;
		timer.removeEventListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.ui.util.timer.SyncEventListener#getInterval()
	 */
	public double getInterval() {
		return interval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (0 != interval
				&& handler
						.canRun((double) (System.currentTimeMillis() - start) / 1000)) {
			stopListener();
			handler.run();
		}
	}
}
