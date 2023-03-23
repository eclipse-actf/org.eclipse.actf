/*******************************************************************************
 * Copyright (c) 2004, 2012 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.util;

import java.util.Vector;

import org.eclipse.actf.model.dom.html.IErrorLogListener;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;

/**
 * Implementation of {@link IErrorLogListener} to correct HTML parse errors.
 */
public class HtmlErrorLogListener implements IErrorLogListener {

	// TODO move to eval plugin (eval.html) (& move IErrorLogListener to
	// common/util)

	private boolean isNoDoctype = false;

	private boolean isNonPublic = false;

	private boolean isInvalidDoctype = false;

	private Vector<IProblemItem> problemV = new Vector<IProblemItem>();

	private String orgDoctype = ""; //$NON-NLS-1$

	private boolean flag = true;

	private int doctype_line = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.dom.html.IErrorLogListener#errorLog(int,
	 * java.lang.String)
	 */
	@SuppressWarnings("nls")
	public void errorLog(int arg0, String arg1) {
		if (arg0 != IParserError.ILLEGAL_ATTRIBUTE || arg1.indexOf(Html2ViewMapData.ACTF_ID) < 0) {
			// TODO create HTML problems
			switch (arg0) {
			case IParserError.DOCTYPE_MISSED:
				isNoDoctype = true;
				break;
			case IParserError.ILLEGAL_DOCTYPE:
				if (arg1.indexOf("Invalid DOCTYPE declaration.") > -1) {
					isNonPublic = true;
				} else if (arg1.matches(".*Instead of \".*\" use \".*\" as a DTD.")) {
					orgDoctype = arg1.substring(arg1.indexOf("\"") + 1);
					orgDoctype = orgDoctype.substring(0, orgDoctype.indexOf("\""));
					if (orgDoctype.matches(
							"-//W3C//DTD XHTML ((1.0 (Strict|Transitional|Frameset))|1.1|Basic 1.0|Basic 1.1)//EN")) {
						orgDoctype = "";
					} else {
						isInvalidDoctype = true;
					}
				} else if (arg1.matches(".*Instead of SYSTEM \".*\" use PUBLIC \".*\" as a DTD.")) {
					orgDoctype = arg1.substring(arg1.indexOf("\"") + 1);
					orgDoctype = orgDoctype.substring(0, orgDoctype.indexOf("\""));
					if (orgDoctype.equalsIgnoreCase("about:legacy-compat")) {
						orgDoctype = "";
					} else {
						isInvalidDoctype = true;
					}
				}
				String tmpS[] = arg1.split(":"); //$NON-NLS-1$
				if (tmpS.length > 0) {
					try {
						doctype_line = Integer.parseInt(tmpS[0].trim());
					} catch (Exception e) {
					}
				}
				break;
			case IParserError.ILLEGAL_CHILD:
				// TBD "li" case (C_1000.7)
				// System.out.println(arg0+" : "+arg1);
				if (arg1.matches(".*<head.*> must be before <body.*")) {
					addHtmlProblem("C_1000.1", arg1);
				} else if (arg1.matches(".*<html.*> is not allowed as a child of <.*")) {
					addHtmlProblem("C_1000.2", arg1);
				} else if (arg1.matches(".*<body.*> is not allowed as a child of <.*")) {
					addHtmlProblem("C_1000.3", arg1);
				} else if (arg1.matches(".*Order of <html.*>'s children is wrong.*")) {
					addHtmlProblem("C_1000.5", arg1);
				}
				break;
			case IParserError.BOM:
				addHtmlProblem("C_1000.8", "");
			default:
			}
		}
	}

	private void addHtmlProblem(String id, String target) {
		IProblemItem tmpCP = new ProblemItemImpl(id);
		int line = -1;
		String tmpS[] = target.split(":"); //$NON-NLS-1$
		if (tmpS.length > 0) {
			try {
				line = Integer.parseInt(tmpS[0].trim());
			} catch (Exception e) {
			}
		}
		if (line > -1) {
			tmpCP.setLine(line);
		}
		problemV.add(tmpCP);
	}

	/**
	 * @return true if there is no DOCTYPE declaration
	 * @see IParserError#DOCTYPE_MISSED
	 */
	public boolean isNoDoctypeDeclaration() {
		return (isNoDoctype || isNonPublic || isInvalidDoctype);
	}

	/**
	 * @return true if the specified DOCTYPE is not public
	 */
	public boolean isNonPublicDoctype() {
		return (isNonPublic);
	}

	/**
	 * @return true if Invalid DOCTYPE declaration error is reported
	 * @see IParserError#ILLEGAL_DOCTYPE
	 */
	public boolean isInvalidDoctype() {
		return (isInvalidDoctype);
	}

	/**
	 * Get DOCTYPE declaration that causes the Invalid DOCTYPE declaration error
	 * 
	 * @return DOCTYPE declaration
	 */
	public String getDeclaratedDoctype() {
		return (orgDoctype);
	}

	/**
	 * Get HTML parse errors as {@link Vector} of {@link IProblemItem}
	 * 
	 * @return HTML parse errors
	 */
	@SuppressWarnings("nls")
	public Vector<IProblemItem> getHtmlProblemVector() {
		if (flag) {
			// (IE based LIVE DOM)->DOCTYPE was removed by IE
			if (EvaluationUtil.isOriginalDOM() && isNoDoctypeDeclaration()) {
				if (isInvalidDoctype() || isNonPublicDoctype()) {
					IProblemItem item = new ProblemItemImpl("C_1000.6");
					if (doctype_line > -1) {
						item.setLine(doctype_line);
					}
					problemV.add(item);
				} else {
					problemV.add(new ProblemItemImpl("C_1000.7"));
				}
			}
			flag = false;
		}
		return (problemV);
	}
}
