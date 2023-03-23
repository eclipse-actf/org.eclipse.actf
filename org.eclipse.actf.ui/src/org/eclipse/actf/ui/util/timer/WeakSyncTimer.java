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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.eclipse.swt.widgets.Display;

/**
 * WeakSyncTimer is a timer running on a thread. It has instances of
 * {@link SyncEventListener} which extends {@link Runnable} and has a interval
 * time. {@link SyncEventListener} will be executed at the interval until the
 * instance is disposed by the garbage collector.
 */
public class WeakSyncTimer extends Thread {
	private ArrayList<WeakReference<SyncEventListener>> listeners;
	private WeakHashMap<SyncEventListener, Long> entries;
	private long nextWakeup;
	private Display display;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public synchronized void run() {
		while (true) {
			long ctime = System.currentTimeMillis();
			if (nextWakeup > ctime) {
				try {
					long timeout = nextWakeup - System.currentTimeMillis() - 1;
					if (timeout > 0) {
						wait(timeout);
					} else {
						wait(1);
					}
				} catch (InterruptedException e) {
				}
				ctime = System.currentTimeMillis();
			}
			Iterator<WeakReference<SyncEventListener>> it = listeners
					.iterator();
			nextWakeup = Long.MAX_VALUE;
			while (it.hasNext()) {
				SyncEventListener listener = it.next().get();
				if (listener == null) {
					it.remove();
					continue;
				}
				Long timeObj = entries.get(listener);
				if (timeObj == null) {
					it.remove();
					continue;
				}
				long time = timeObj.longValue();
				if (time <= ctime) {
					if (!display.isDisposed()) {
						display.asyncExec(listener);
						manage(listener);
					} else {
						it.remove();
					}
				} else {
					if (nextWakeup > time) {
						nextWakeup = time;
					}
				}
			}
		}
	}

	private static WeakSyncTimer instance;

	private void manage(SyncEventListener listener) {
		long interval = (long) (listener.getInterval() * 1000);
		if (interval > 0) {
			long time = System.currentTimeMillis() + interval;
			entries.put(listener, time);
			if (time < nextWakeup) {
				nextWakeup = time;
			}
		} else {
			entries.remove(listener);
		}
	}

	/**
	 * Add an event listener to the timer.
	 * @param listener the event listener to be executed.
	 */
	public synchronized void addEventListener(SyncEventListener listener) {
		listeners.add(new WeakReference<SyncEventListener>(listener));
		manage(listener);
		// System.err.println("Size:" + entries.size());
		notify();
	}

	/**
	 * Remove an event explicitly.
	 * @param listener the event listener to be removed.
	 */
	public synchronized void removeEventListener(SyncEventListener listener) {
		entries.remove(listener);
	}

	/**
	 * @return the singleton instance of the timer.
	 */
	public static WeakSyncTimer getTimer() {
		if (instance == null) {
			instance = new WeakSyncTimer(Display.getCurrent());
			instance.start();
		}
		return instance;
	}

	private WeakSyncTimer(Display display) {
		super("WeakSyncTimer-scheduler"); //$NON-NLS-1$
		this.listeners = new ArrayList<WeakReference<SyncEventListener>>();
		this.entries = new WeakHashMap<SyncEventListener, Long>();
		this.nextWakeup = Long.MAX_VALUE;
		this.display = display;
	}
}
