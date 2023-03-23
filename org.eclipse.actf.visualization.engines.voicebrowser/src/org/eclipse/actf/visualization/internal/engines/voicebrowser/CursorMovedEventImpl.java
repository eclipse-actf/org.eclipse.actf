/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.util.EventObject;

import org.eclipse.actf.visualization.engines.voicebrowser.CursorMovedEvent;

/**
 *
 */
public class CursorMovedEventImpl extends EventObject implements CursorMovedEvent {

	private static final long serialVersionUID = -7281723465717975039L;

	private int cCursorPos;

	/**
	 * Constructor for CursorChangedEvent.
	 * 
	 * @param source
	 * @param pos
	 */
	public CursorMovedEventImpl(Object source, int pos) {
		super(source);
		cCursorPos = pos;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.CursorMovedEvent#getCurrentCursorPosision()
	 */
	public int getCurrentCursorPosision() {
		return cCursorPos;
	}

	@SuppressWarnings("nls")
	public String toString() {
		return (getClass().getName() + "[source=" + getSource()
				+ ",cCursorPos=" + cCursorPos + "]");
	}

}
