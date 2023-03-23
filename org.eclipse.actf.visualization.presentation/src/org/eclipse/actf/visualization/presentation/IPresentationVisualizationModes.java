/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation;

import org.eclipse.actf.visualization.presentation.util.ParamRoom;

/**
 * Interface for specifying mode of presentation visualization.
 */
public interface IPresentationVisualizationModes {

	/**
	 * The mode id to simulate a presentation in a small room.
	 */
	public static final int SMALL_ROOM = ParamRoom.ROOM_SMALL;
	/**
	 * The mode id to simulate a presentation in a large room.
	 */
	public static final int LARGE_ROOM = ParamRoom.ROOM_MIDDLE;
	/**
	 * The mode id to simulate a presentation in an auditorium.
	 */
	public static final int AUDITORIUM = ParamRoom.ROOM_LARGE;

}
