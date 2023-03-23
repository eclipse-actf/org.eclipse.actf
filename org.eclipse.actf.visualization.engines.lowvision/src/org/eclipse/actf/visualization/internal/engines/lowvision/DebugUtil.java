/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision;

import java.io.PrintStream;

public class DebugUtil {

	// *************************************************************
	// to be changed according to the debugging phase
	// public static String DEBUG_STRING = "style-addcss-page-post-cache-diff";
	// private static final String DEBUG_STRING =
	// "post-genRE-infoSet-diff-cache";
	private static final String DEBUG_STRING = "TRACE"; //$NON-NLS-1$

	// private static final String DEBUG_STRING =
	// "TRACE_HISTOGRAM_CONTAINER_EXTRACT_PROBLEM_COMPONENT_REPORT";

	// (1)print utilities
	public static void errMsg(Object o, String s) {
		printMsg(o, s, System.err, 0);
	}

	public static void errMsg(Object o, String s, int level) {
		printMsg(o, s, System.err, level);
	}

	public static void outMsg(Object o, String s) {
		printMsg(o, s, System.out, 0);
	}

	public static void outMsg(Object o, String s, int level) {
		printMsg(o, s, System.out, level);
	}

	public static void debugMsg(Object o, String s, String debugType) {
		if (DEBUG_STRING.indexOf(debugType) > -1) {
			printMsg(o, s, System.out, 0);
		}
	}

	public static void debugMsg(Object o, String s, String debugType, int level) {
		if (DEBUG_STRING.indexOf(debugType) > -1) {
			printMsg(o, s, System.out, level);
		}
	}

	public static void printMsg(Object o, String s, PrintStream ps, int level) {
		if (level != 0) {
			ps.println("--------------------------------"); //$NON-NLS-1$
		}
		if (o != null) {
			ps.println(o.getClass().getName() + ": " + s); //$NON-NLS-1$
		} else {
			ps.println(s);
		}
		if (level != 0) {
			ps.println("--------------------------------"); //$NON-NLS-1$
		}
		ps.flush();
	}

}
