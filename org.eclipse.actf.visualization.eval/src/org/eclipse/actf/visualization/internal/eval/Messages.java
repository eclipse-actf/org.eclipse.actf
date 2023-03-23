/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.eval;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String ProblemConst_All_Errors_1;
	public static String ProblemConst_Warning;
	public static String ProblemConst_Essential_2;
	public static String ProblemConst_Advanced_3;
	public static String ProblemConst_Basic_4;
	public static String ProblemConst_User_Check_5;
	public static String ProblemConst_Compliance_6;
	public static String ProblemConst_Trash_7;
	public static String ProblemConst_Type_11;
	public static String ProblemConst_Line_12;
	public static String ProblemConst_Highlight;
	public static String ProblemConst_Problem_Description_17;
	public static String ProblemConst_Foreground_18;
	public static String ProblemConst_Background_19;
	public static String ProblemConst_Severity_20;
	public static String ProblemConst_X_21;
	public static String ProblemConst_Y_22;
	public static String ProblemConst_0;
	public static String ProblemConst_Area_23;
	public static String ProblemConst_Detailed_report;
	public static String ProblemConst_Summary_report;
	public static String ProblemConst_Info;
	public static String NavigabilityWarningDialog_Message1;
	public static String NavigabilityWarningDialog_Message2;
	public static String NavigabilityWarningDialog_EnableWCAG;
	public static String NavigabilityWarningDialog_DisableNav;
	public static String NavigabilityWarningDialog_Continue;
	public static String adesigner_preference_guideline_list_group_text;
	public static String adesigner_preference_guideline_criteria_group_text;
	public static String adesigner_preference_guideline_properties_group_text;
	public static String DialogCheckerOption_Line_Number_Information_19;
	public static String DialogCheckerOption_Add_line_number_20;
	public static String DialogCheckerOption_LIVE_DOM;
	public static String GuidelinePreferencePage_0;
	public static String GuidelinePreferencePage_1;
	public static String Techniques;
	public static String Type;
	public static String Help;
	public static String Perceivable;
	public static String Operable;
	public static String Understandable;
	public static String Robust;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}