/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

/**
 * The event sent by a voice browser controller. This event is occurred when the
 * cursor position in voice browser is changed.
 * 
 * @see CursorListener
 * @see IVoiceBrowserController
 */
public interface CursorMovedEvent {

	/**
	 * Returns the current cursor position.
	 * 
	 * @return current cursor position
	 */
	public abstract int getCurrentCursorPosision();

}