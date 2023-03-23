/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.visualization.engines.blind.ui.preferences.IBlindPreferenceConstants;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * Utility class to check alternative text
 */
public class TextChecker {

	private static final String NULL_STRING = ""; //$NON-NLS-1$

	private static final String KIGOU = "(\\p{InMathematicalOperators}|\\p{InGeometricShapes}|\\p{InMiscellaneousSymbols}|\\p{InBoxDrawing}|\\p{InGeneralPunctuation}|\\p{InCJKSymbolsandPunctuation}|\\p{InArrows})"; //$NON-NLS-1$

	private static final String NIHONGO = "(\\p{InCJKUnifiedIdeographs}|\\p{InHiragana}|\\p{InKatakana})"; //$NON-NLS-1$

	private static final String KANJI = "(\\p{InCJKUnifiedIdeographs})"; //$NON-NLS-1$

	private static final String ALT_TEXT_PROPERTIES_FILE = "altText.properties"; //$NON-NLS-1$

	private static final String INAPP_ALT = "blindViz.inappropriateAlt_"; //$NON-NLS-1$

	private static final String POSSIBLE_INAPP_ALT = "blindViz.possible_inappAlt_"; //$NON-NLS-1$

	private static TextChecker INSTANCE;

	private Set<String> ngwordset = new TreeSet<String>();

	private Set<String> ngwordset2 = new TreeSet<String>();

	private Set<String> ngPatterns = new HashSet<String>();

	private IPreferenceStore pref = BlindVizEnginePlugin.getDefault().getPreferenceStore();

	// TODO spell out check

	// separated from VisualizeEngine
	private TextChecker() {

		if (!pref.getBoolean(IBlindPreferenceConstants.NOT_FIRST_TIME)) {

			Properties prop = new Properties();
			try {
				InputStream prefIS = FileLocator.openStream(Platform.getBundle(BlindVizEnginePlugin.PLUGIN_ID),
						new Path("config/" + ALT_TEXT_PROPERTIES_FILE), false); //$NON-NLS-1$
				if (prefIS != null) {
					prop.load(prefIS);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Object key : prop.keySet()) {
				String keyS = (String) key;
				String value;
				if (keyS.startsWith(INAPP_ALT)) {
					value = prop.getProperty(keyS);
					if (value.length() > 0) {
						ngwordset.add(value);
					}
				} else if (keyS.startsWith(POSSIBLE_INAPP_ALT)) {
					value = prop.getProperty(keyS);
					if (value.length() > 0) {
						ngwordset2.add(value);
					}
				}
			}

			resetPreferences();

		} else {
			for (int i = 0; pref.contains(INAPP_ALT + i); i++) {
				String value = pref.getString(INAPP_ALT + i);
				if (value.length() > 0) {
					ngwordset.add(value);
				}
			}
			for (int i = 0; pref.contains(POSSIBLE_INAPP_ALT + i); i++) {
				String value = pref.getString(POSSIBLE_INAPP_ALT + i);
				if (value.length() > 0) {
					ngwordset2.add(value);
				}
			}
		}

		ngPatterns.add("\u753B\u50CF\\s*\\d+");
	}

	/**
	 * Get instance of {@link TextChecker}
	 * 
	 * @return instance of {@link TextChecker}
	 */
	synchronized public static TextChecker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextChecker();
		}
		return INSTANCE;
	}

	/**
	 * Check redundancy of text
	 * 
	 * @param prevText
	 *            previous text
	 * @param curText
	 *            target text to check
	 * @return true if the target text is redundant with previous text
	 */
	public boolean isRedundantText(String prevText, String curText) {

		if ((prevText != null) && (prevText.length() > 1) && (curText.length() > 1)) {

			String prevText2 = prevText.replaceAll("\\[|\\]|\\.|\\!|\\>|\\n", NULL_STRING); //$NON-NLS-1$
			prevText2 = prevText2.trim();

			String curText2 = curText.replaceAll("\\[|\\]|\\.|\\!|\\>|\\n", NULL_STRING); //$NON-NLS-1$
			curText2 = curText2.trim();

			if (curText2.equalsIgnoreCase(prevText2)) {
				return true;
			}
		}
		return (false);
	}

	/**
	 * Check appropriateness of alternative text.
	 * 
	 * @param alt
	 *            target alternative text
	 * @return true if the alternative text matches with inappropriate
	 *         alternative text {@link Set}
	 */
	public boolean isInappropriateAlt(String alt) {
		String tmpAlt = alt.trim();
		tmpAlt = tmpAlt.toLowerCase();

		if (ngwordset.contains(tmpAlt) || isEndWithImageExt(tmpAlt)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check possibly inappropriate alternative text.
	 * 
	 * @param alt
	 *            target alternative text
	 * @return
	 * 		<ul>
	 *         <li>0: OK</li>
	 *         <li>1: possibly inappropriate</li>
	 *         <li>2: inappropriate</li>
	 *         <li>3: space separated chars</li>
	 *         </ul>
	 */
	public int checkInappropriateAlt(final String alt) {
		String[] tmpSA = alt.toLowerCase().split("(" + KIGOU + "|\\p{Punct}|\\p{Space})"); //$NON-NLS-1$ //$NON-NLS-2$
		int count = 0;
		int all = 0;
		int charLength = 0;
		for (int i = 0; i < tmpSA.length; i++) {
			if (tmpSA[i].length() > 0) {
				all++;
			}
			charLength += tmpSA[i].length();
			if (ngwordset2.contains(tmpSA[i])) {
				// System.out.println("alt: "+tmpSA[i]);
				count++;
			}
		}

		int org = alt.length();

		// TODO combination
		if (org > 0 && alt.matches(".*(\\p{Alpha}\\p{Space}){3,}.*")) {// TODO //$NON-NLS-1$
			// 4 is
			// appropriate?
			return 3;
		}

		// TODO Japanese check
		if (org > 0 && ((double) charLength / (double) org) < 0.5) {
			// TODO divide error (use ratio)

			// spaces
			if (!alt.matches("\\p{Space}*")) { //$NON-NLS-1$
				return 1;
			}
		}

		// System.out.println(count+" "+all+":"+(double)count/(double)all);
		if ((double) count / (double) all > 0.6) {
			return 2;
		} else if ((double) count / (double) all > 0.3) {
			return 1;
		}
		return 0;
	}

	/**
	 * Returns true if ALT text ends with one of the extensions which is used
	 * for image files. It detects common error where content author sets ALT to
	 * the name of the image file itself.
	 * 
	 * @param alt
	 * @return
	 */
	private boolean isEndWithImageExt(String alt) {
		String tmpS = alt.trim().toLowerCase();
		String regexp3 = "\\p{Print}*\\.(jpg|jpeg|gif|png|bmp|tiff)"; //$NON-NLS-1$
		return (tmpS.matches(regexp3));
	}

	/**
	 * Check if the target String is space separated Japanese chars or not
	 * 
	 * @param target
	 *            target String
	 * @return true if the target String is space separated Japanese chars
	 */
	public boolean isSeparatedJapaneseChars(String target) {
		String tmpS = target.trim();
		tmpS = tmpS.toLowerCase();

		// TODO rewrite regexp (combination of Japanese and English)
		// \u3000 = double byte space
		// \u00A0 = &nbsp;
		@SuppressWarnings("unused")
		String regexp1 = ".*" + KANJI + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
		String regexp2 = ".*\\b" + NIHONGO + "[\\p{Space}\u3000\u00A0]+" + NIHONGO + "\\b.*";

		if (tmpS.matches(regexp2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get inappropriate alternative text {@link Set}
	 * 
	 * @return {@link Set} of inappropriate alternative text
	 */
	public Set<String> getInappropriateALTSet() {
		Set<String> tmpSet = new HashSet<String>();
		tmpSet.addAll(ngwordset);
		return tmpSet;
	}

	private void resetPreferences() {
		int i = 0;
		for (String value : ngwordset) {
			pref.setValue(INAPP_ALT + i, value);
			i++;
		}
		for (int j = i; pref.contains(INAPP_ALT + j); j++) {
			pref.setValue(INAPP_ALT + j, NULL_STRING);
		}

		i = 0;
		for (String value : ngwordset2) {
			pref.setValue(POSSIBLE_INAPP_ALT + i, value);
			i++;
		}
		for (int j = i; pref.contains(POSSIBLE_INAPP_ALT + j); j++) {
			pref.setValue(POSSIBLE_INAPP_ALT + j, NULL_STRING);
		}
	}

	/**
	 * Set inappropriate alternative text {@link Set}. The new {@link Set} will
	 * be stored into {@link PreferenceStore}.
	 * 
	 * @param inappAltSet
	 *            {@link Set} of inappropriate alternative text
	 */
	public void setInappropriateAltSet(Set<String> inappAltSet) {
		if (inappAltSet != null) {
			ngwordset = inappAltSet;
		}
		resetPreferences();
	}

	//
	// TODO Constants and methods called from CheckEngine class. For new JIS.
	//
	public TextCheckResult checkAlt(String alt) {
		return checkAlt(alt, null, new TreeSet<String>());
	}

	public TextCheckResult checkAlt(String alt, String src) {
		return checkAlt(alt, src, new TreeSet<String>());
	}

	public TextCheckResult checkAlt(String alt, Set<String> ngWords) {
		return checkAlt(alt, null, ngWords);
	}

	/**
	 * Check alt attribute string and returns result in one of the constants
	 * defined above.
	 * 
	 * @param alt
	 *            The value of the alt attributes of images, buttons, area
	 *            elements, etc. It MUST NOT be null.
	 * @param src
	 *            The value of the src attribute. Set null if it does not exist.
	 * @param ngWords
	 *            additional NG words set; when we check ALT text of area
	 *            element, we want to add "area" as additional NG word.
	 * @return Check result as TextCheckerResult instance.
	 */
	public TextCheckResult checkAlt(String alt, String src, Set<String> ngWords) {
		// First do not trim the given ALT string!
		String origAlt = alt;
		int origLength = alt.length();

		alt = alt.toLowerCase();

		if (alt.equals(NULL_STRING))
			return TextCheckResult.NULL;

		assert origLength > 0;
		if (alt.matches("[\\p{Space}\\u3000\\u00A0]+")) {
			if (alt.matches(".*\\u00A0.*"))
				return TextCheckResult.BLANK_NBSP;
			else
				return TextCheckResult.BLANK;
		}

		alt = alt.trim();

		// exact match with entire ALT string
		if (ngwordset.contains(alt) || ngWords.contains(alt))
			return TextCheckResult.NG_WORD;

		for (String patterns : ngPatterns) {
			if (alt.matches(patterns))
				return TextCheckResult.NG_WORD;
		}

		if (src != null) {
			Matcher m = Pattern.compile(".*?([^/]+)").matcher(src.toLowerCase());
			if (m.matches()) {
				String fileName = m.group(1);
				if (alt.equals(fileName)) {
					return TextCheckResult.SAME_AS_SRC;
				}
			}
		}

		if (isEndWithImageExt(alt))
			return TextCheckResult.IMG_EXT;

		if (alt.matches(".*(\\p{Alpha}\\p{Space}){3,}.*"))
			return TextCheckResult.SPACE_SEPARATED; // case 3

		if (isSeparatedJapaneseChars(alt))
			return TextCheckResult.SPACE_SEPARATED_JP;

		// Counts number of NG "word"s in ALT
		List<String> wordList = Arrays.asList(alt.toLowerCase().split("(" + KIGOU + "|\\p{Punct}|\\p{Space})")); //$NON-NLS-1$ //$NON-NLS-2$
		int wordCountNG = 0;
		int wordCountAll = 0;

		@SuppressWarnings("unused")
		int charLength = 0;

		for (String word : wordList) {
			charLength += word.length();
			if (word.length() > 0)
				wordCountAll++;
			if (ngwordset2.contains(word)) {
				wordCountNG++;
			}
		}

		if (isAsciiArtString(origAlt))
			return TextCheckResult.ASCII_ART; // case 1

		if (((double) wordCountNG / (double) wordCountAll) > 0.6)
			return TextCheckResult.INCLUDING_MANY_NG_WORD; // case 2
		else if ((double) wordCountNG / (double) wordCountAll > 0.3) {
			return TextCheckResult.INCLUDING_NG_WORD; // case 1
		}

		return TextCheckResult.OK;
	}

	public boolean isAsciiArtString(String str) {
		int origLength = str.length();
		str = str.toLowerCase();
		List<String> wordList = Arrays.asList(str.toLowerCase().split("(" + KIGOU + "|\\p{Punct}|\\p{Space})")); //$NON-NLS-1$ //$NON-NLS-2$
		int charLength = 0;
		for (String word : wordList) {
			charLength += word.length();
		}

		boolean isBlank = str.matches("[\\p{Space}\\u3000\\u00A0]*");

		return (origLength > 0 && ((double) charLength / (double) origLength) < 0.5 && !isBlank);
	}
}
