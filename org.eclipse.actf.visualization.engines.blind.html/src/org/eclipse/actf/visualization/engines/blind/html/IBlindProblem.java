/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind.html;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;

/**
 * Interface for detected problems through blind visualization.
 */
public interface IBlindProblem extends IProblemItem {

	public static final int NO_ALT_IMG = 0;
	public static final int NO_ALT_INPUT = 1;
	public static final int NO_ALT_AREA = 2;
	public static final int WRONG_ALT_IMG = 4;
	public static final int WRONG_ALT_INPUT = 5;// TODO
	public static final int WRONG_ALT_AREA = 6;
	public static final int NO_DEST_LINK = 8;
	public static final int REDUNDANT_ALT = 9;
	public static final int NO_SKIPTOMAIN_LINK = 10;// without structure
	public static final int TOO_LESS_STRUCTURE = 12;
	public static final int NO_TEXT_INTRAPAGELINK = 14;
	public static final int WRONG_TEXT = 15;
	public static final int NO_ID_INPUT = 16; // TODO
	public static final int TOOFAR_SKIPTOMAIN_LINK = 17;
	public static final int NO_DEST_SKIP_LINK = 18;
	public static final int WRONG_SKIP_LINK_TEXT = 19;
	public static final int NO_SKIPTOMAIN_WITH_STRUCTURE = 20;
	public static final int ALERT_NO_SKIPTOMAIN_NO_STRUCTURE = 21;
	public static final int LESS_STRUCTURE_WITH_SKIPLINK = 22;
	public static final int LESS_STRUCTURE_WITH_HEADING = 23;
	public static final int LESS_STRUCTURE_WITH_BOTH = 24;
	public static final int NO_TEXT_WITH_TITLE_INTRAPAGELINK = 25;
	public static final int WRONG_SKIP_LINK_TITLE = 26;
	public static final int ALERT_WRONG_ALT = 27;
	public static final int ALERT_REDUNDANT_TEXT = 28; // TODO
	public static final int SEPARATE_DBCS_ALT_IMG = 29;
	public static final int SEPARATE_DBCS_ALT_INPUT = 30; // TODO
	public static final int SEPARATE_DBCS_ALT_AREA = 31;
	public static final int ALERT_NO_DEST_INTRA_LINK = 33;
	public static final int ALERT_SPELL_OUT = 34;
	public static final int INVISIBLE_INTRAPAGE_LINK = 35;
	public static final int NO_VALUE_INPUT_BUTTON = 36;
	public static final int SEPARATE_DBCS_INPUT_VALUE = 37;
	public static final int WRONG_NBSP_ALT_IMG = 38;
	public static final int NUM_PROBLEMS = 39;// max id+1
}