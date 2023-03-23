/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.internal;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String report_small_url;
	public static String report_middle_url;
	public static String report_large_url;
	public static String RoomSimulationAction_Large;
	public static String RoomSimulationAction_Middle;
	public static String RoomSimulationAction_Small;
	public static String PartRightRoom_begin_to_make_PageImage__2;
	public static String PartRightRoom_simulation_of_current_page_is_over__8;
	public static String PartRightRoom_dump_the_image_in_the_web_browser__26;
	public static String PartRightRoom_prepare_Simulation_Image__29;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}