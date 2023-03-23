/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.html.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.w3c.dom.Element;

public class CssBeforeAfterChecker {

	private static final int CONTENT_UNKNOWN = -1;
	private static final int CONTENT_NONE = 0;
	private static final int CONTENT_EMPTY_TEXT = 1;
	private static final int CONTENT_PUNCT_TEXT = 2;
	private static final int CONTENT_TEXT = 3;
	private static final int CONTENT_ATTR = 4;
	private static final int CONTENT_COUNTER = 5;
	private static final int CONTENT_URL = 6;

	private static final String ERROR_ID_BEFORE = ".0";
	private static final String ERROR_ID_AFTER = ".1";

	private static final Pattern BEFORE_PATTERN = Pattern.compile(
			":?:before(\\s)*\\{[^\\}]*content(\\s)*:(\\s)*((\"[^\"]*\")|(\'[^\']*\')|([^;\\}\"\']*))(\\s)*[;\\}]",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern AFTER_PATTERN = Pattern.compile(
			":?:after(\\s)*\\{[^\\}]*content(\\s)*:(\\s)*((\"[^\"]*\")|(\'[^\']*\')|([^;\\}\"\']*))(\\s)*[;\\}]",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern CONTENT_PATTERN = Pattern
			.compile(":?:((before)|(after))(\\s)*\\{[^\\}]*content(\\s})*:", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern CONTENT_PATTERN_SPACE = Pattern.compile("(\"(\\s|\\h)*\")|(\'(\\s|\\h)*\')");

	private static final Pattern CONTENT_PATTERN_PUNCT = Pattern
			.compile("(\"(\\s|\\h|\\p{Punct})*\")|(\'(\\s|\\h|\\p{Punct})*\')");

	private static final Pattern CONTENT_PATTERN_TEXT = Pattern.compile("(\"[^\"]*\")|(\'[^\']*\')");

	public CssBeforeAfterChecker() {
	}

	private List<IProblemItem> checkStyle(boolean isBefore, String style, Element e, String targetString) {
		ArrayList<IProblemItem> result = new ArrayList<IProblemItem>();

		StringBuffer textContents = new StringBuffer();
		StringBuffer urlContents = new StringBuffer();

		String errorId;
		Matcher matcher;
		IProblemItem prob;

		if (isBefore) {
			matcher = BEFORE_PATTERN.matcher(style);
			errorId = ERROR_ID_BEFORE;
		} else {
			matcher = AFTER_PATTERN.matcher(style);
			errorId = ERROR_ID_AFTER;
		}

		while (matcher.find()) {
			Matcher matcher2 = CONTENT_PATTERN.matcher(matcher.group());
			String contentS = matcher2.replaceFirst("");
			contentS = contentS.substring(0, contentS.length() - 1).trim();
			int contentType = checkContentType(contentS);

			switch (contentType) {
			case CONTENT_PUNCT_TEXT:
			case CONTENT_TEXT:
			case CONTENT_ATTR:
			case CONTENT_COUNTER:
				textContents.append(", " + contentS);
				// has some text
				break;
			case CONTENT_URL:
				urlContents.append(", \"" + contentS + "\"");
				break;
			default:
				// none/normal, empty text or unknown
				break;
			}
		}

		if (textContents.length() > 2) {
			prob = new ProblemItemImpl("C_90" + errorId);
			prob.setTargetString(
					targetString + " (content: " + textContents.subSequence(2, textContents.length()) + ")");
			prob.setTargetNode(e);
			result.add(prob);
		}

		if (urlContents.length() > 2) {
			prob = new ProblemItemImpl("C_91" + errorId);
			prob.setTargetString(targetString + " (content: " + urlContents.subSequence(2, urlContents.length()) + ")");
			prob.setTargetNode(e);
			result.add(prob);
		}
		return result;
	}

	public List<IProblemItem> check(String style, Element e) {
		ArrayList<IProblemItem> result = new ArrayList<IProblemItem>();
		result.addAll(checkStyle(true, style, e, ""));
		result.addAll(checkStyle(false, style, e, ""));
		return result;
	}

	public List<IProblemItem> check(String style, String cssUrl) {
		ArrayList<IProblemItem> result = new ArrayList<IProblemItem>();
		result.addAll(checkStyle(true, style, null, "(" + cssUrl + ")"));
		result.addAll(checkStyle(false, style, null, "(" + cssUrl + ")"));
		return result;
	}

	private int checkContentType(String content) {
		try {
			String targetS = content.toLowerCase().trim();
			// not for CSS syntax check
			if ("none".equals(targetS) || "normal".equals(targetS)) {
				return CONTENT_NONE;
			} else if (targetS.matches("attr(\\s)*\\([^\\)]+\\)")) {
				return CONTENT_ATTR;
			} else if (targetS.matches("((counter)|(counters))(\\s)*\\([^\\)]+\\)")) {
				return CONTENT_COUNTER;
			} else if (targetS.matches("url(\\s)*\\([^\\)]+\\)")) {
				return CONTENT_URL;
			}
			// String check
			if (CONTENT_PATTERN_SPACE.matcher(targetS).matches()) {
				return CONTENT_EMPTY_TEXT;
			} else if (CONTENT_PATTERN_PUNCT.matcher(targetS).matches()) {
				return CONTENT_PUNCT_TEXT;
			} else if (CONTENT_PATTERN_TEXT.matcher(targetS).matches()) {
				return CONTENT_TEXT;
			}
			DebugPrintUtil.devOrDebugPrintln("uncategolized content: " + targetS);
		} catch (Exception e) {
			DebugPrintUtil.devOrDebugPrintStackTrace(e);
		}
		return CONTENT_UNKNOWN;
	}

}
