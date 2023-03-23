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
 * The interface for implementing an event handler that periodically check if
 * action can be executed right now. If canRun becomes true, the run method will
 * be executed. The implementation will be set to
 * {@link WaitExecSyncEventListener}.
 * 
 * @see WaitExecSyncEventListener
 */
public interface WaitExecSyncEventHandler extends Runnable {

	/**
	 * Get scheduling interval in second
	 * 
	 * @return scheduling interval in second
	 */
	public double getInterval();

	/**
	 * Check if action can be executed right now
	 * 
	 * @param elapsed
	 *            elapsed time
	 * @return true if run method can be executed
	 */
	public boolean canRun(double elapsed);
}
