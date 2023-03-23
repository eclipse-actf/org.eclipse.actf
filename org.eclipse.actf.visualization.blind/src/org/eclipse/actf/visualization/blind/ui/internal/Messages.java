/*******************************************************************************
 * Copyright (c) 2006, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.ui.internal;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.actf.visualization.blind.ui.internal.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String BlindView_Maximum_Time;
	public static String BlindView_Done;
	public static String BlindView_Now_preparing;
	public static String BlindView_Now_processing;
	public static String BlindView_Now_rendering;
	public static String BlindView_saving_file;
	public static String BlindView_end_saving_file;
	public static String BlindView_Visualize_4;
	public static String BlindVisualizationAction_0;
	public static String BlindVisualizerHtml_0;
	public static String BlindVisualizerHtml_1;
	public static String BlindVisualizerHtml_15;
	public static String BlindVisualizerHtml_16;
	public static String BlindVisualizerHtml_2;
	public static String Visualization_Error;
	public static String Report;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}