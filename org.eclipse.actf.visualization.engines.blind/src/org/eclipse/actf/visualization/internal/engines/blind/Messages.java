/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.blind;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String AltDialog_TITLE;
	public static String AltDialog_Column_Name;
	public static String AltDialog_MSG_No_String;
	public static String AltDialog_MSG_Existed;
	public static String PageEvaluation_Bad;
	public static String PageEvaluation_Excellent;
	public static String PageEvaluation_Good;
	public static String PageEvaluation_Poor;
	public static String Eval_redundant_img_alt;
	public static String Eval_wrong_img_alt;
	public static String Eval_no_img_alt;
	public static String Eval_redundant_img_alt_error_msg;
	public static String Eval_wrong_img_alt_error_msg;
	public static String Eval_no_img_alt_error_msg;
	public static String Eval_navigability_low_score_error_msg;
	public static String Eval_navigability_long_time_error_msg;
	public static String Eval_navigability_good_msg;
	public static String Eval_confirm_errors_detailed_report;
	public static String Eval_confirm_alt_attributes_first;
	public static String Eval_many_accessibility_issues;
	public static String Eval_some_accessibility_issues;
	public static String Eval_completely_compliant_with_user_check_items;
	public static String Eval_completely_compliant;
	public static String Eval_some_errors_on_metrics;
	public static String Eval_some_errors_on_metrics1;
	public static String Eval_user_check1;
	public static String Eval_user_check2;	
	public static String Eval_compliant_with_some_other_errors;
	public static String Eval_excellent;
	public static String Eval_easy_for_blind_user_to_navigate;
	public static String Eval_page_has_skiplinks_headings;
	public static String Eval_darkcolored_visualization_view;
	public static String DialogSettingBlind_NG_Word___Wrong_Text_5;
	public static String DialogSettingBlind_NG_Word_Wrong_Text_Edit____25;
	public static String DialogSettingBlind_Language_4;
	public static String DialogSettingBlind_English_15;
	public static String DialogSettingBlind_Japanese_16;
	public static String DialogSettingBlind_SimpliedChinese_17;
	public static String DialogSettingBlind_LayoutModeSetting;
	public static String DialogSettingBlind_Visualization_mode_3;
	public static String DialogSettingBlind_Voice_browser_output_mode_8;
	public static String DialogSettingBlind_Layout_mode_9;
	public static String DialogSettingBlind_Maximum_time_17;
	public static String DialogSettingBlind_Color_for_maximum_time_19;
	public static String DialogSettingBlind_Table_headers_20;
	public static String DialogSettingBlind_Heading_tags_21;
	public static String DialogSettingBlind_Input_tags_22;
	public static String DialogSettingBlind_Label_tags_23;
	public static String DialogSettingBlind_Tabel_border_24;
	public static String DialogSettingBlind_Caption_tags_25;
	
	public static String VisualizeBrowserMode;
	public static String VisualizeBrowserModeTooltip;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}