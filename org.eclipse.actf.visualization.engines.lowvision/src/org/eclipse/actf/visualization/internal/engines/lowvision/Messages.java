/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String BlurProblem_It_is_difficult_for_weak_sighted_to_read_these_characters__1;
	public static String ChangableFontRecommendation_Do_not_use_fixed_size_font__1;
	public static String ColorProblem_Foreground_and_background_colors_are_too_close__1;
	public static String DontRelyOnColorRecommendation_Don__t_rely_only_on_color__1;
	public static String EnlargeLineRecommendation_Enlarge_the_line_height__1;
	public static String EnlargeTextRecommendation_Enlarge_the_text__1;
	public static String EnoughContrastRecommendation_Provide_enough_contrast_between_foreground_and_background_colors__1;
	public static String FixedSizeFontProblem_Fixed_size_font_is_used__1;
	public static String FixedSmallFontProblem_This_text_is_too_small_and_its_font_size_is_fixed__1;
	public static String ImageColorProblem_This_image_has_two_or_more_components_whose_colors_are_too_close__1;
	public static String SmallFontProblem_This_text_is_too_small__1;
	public static String UseAllowedColorRecommendation_Use_a_color_allowed_by_the_design_guideline__1;
	public static String ProhibitedBothColorsProblem_Both_of_the_foreground_and_background_colors_are_not_allowed_by_the_design_guideline__1;
	public static String ProhibitedForegroundColorProblem_The_foreground_color_is_not_allowed_by_the_design_guideline__1;
	public static String ProhibitedBackgroundColorProblem_The_background_color_is_not_allowed_by_the_design_guideline__1;
	public static String PageEvaluation_Bad;
	public static String PageEvaluation_Excellent;
	public static String PageEvaluation_Good;
	public static String PageEvaluation_Poor;
	public static String ImageDumpUtil_TrueColor;
	public static String ContrastRatio;
	public static String TargetString;
	public static String BackgroundImage;
	public static String FixedSizeFontWarning;
	public static String FixedSmallFontWarning;
	public static String FontAlphaWarning;
	public static String BackgroundAlphaWarning;
	public static String BothAlphaWarning;
	
	public static String StyleAttribute;
	public static String StyleElement;
	public static String Selector;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}