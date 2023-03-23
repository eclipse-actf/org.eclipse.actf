/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import java.util.EventListener;

/**
 * Interface to listen voice browser cursor moved event.
 * 
 * @see CursorMovedEvent
 */
public interface CursorListener extends EventListener {
	/**
	 * This method is called when a voice browser cursor is moved.
	 * 
	 * @param event
	 *            The {@link CursorMovedEvent} contains information about the
	 *            event.
	 */
	public void doCursorMoved(CursorMovedEvent event);

}
