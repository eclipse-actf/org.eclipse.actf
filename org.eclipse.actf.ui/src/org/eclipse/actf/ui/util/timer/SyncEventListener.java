/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util.timer;

/**
 * SyncEventListener interface extends {@link Runnable} interface.
 * {@link Runnable#run()} will be called by the {@link WeakSyncTimer}.
 * {@link WeakSyncTimer} will call the {@link Runnable#run()} methods at the
 * interval specified by the {@link #getInterval()} methods.
 */
public interface SyncEventListener extends Runnable {
	/**
	 * @return the interval time in form of second. For example, 100 millisecond is 0.1.
	 */
	double getInterval();
}
