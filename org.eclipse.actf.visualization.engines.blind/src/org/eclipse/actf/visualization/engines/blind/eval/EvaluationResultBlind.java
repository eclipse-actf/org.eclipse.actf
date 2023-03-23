/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.eval;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;

/**
 * Implementation class of {@link IEvaluationResult}
 */
public class EvaluationResultBlind extends EvaluationResultImpl {
	private int count = 0;
	private PageEvaluation pageEvaluation = null;

	public void addProblemItems(Collection<IProblemItem> c) {
		stripProblem(c);
		super.addProblemItems(c);
	}

	public void setProblemList(List<IProblemItem> problemList) {
		count = 0;
		stripProblem(problemList);
		super.setProblemList(problemList);
	}

	private void stripProblem(Collection<IProblemItem> c) {
		GuidelineHolder holder = GuidelineHolder.getInstance();
		for (Iterator<IProblemItem> i = c.iterator(); i.hasNext();) {
			try {
				IProblemItem tmpItem = i.next();
				if (holder.isMatchedCheckItem(tmpItem.getEvaluationItem())) {
					tmpItem.setSerialNumber(count);
					if (tmpItem.isCanHighlight()
							&& tmpItem.getTargetNode() != null) {
						
						if (HtmlTagUtil.hasAncestor(tmpItem.getTargetNode(), "script")) {
							tmpItem.setCanHighlight(false);
						}
					}
					count++;
				} else {
					i.remove();
				}
			} catch (Exception e) {
				// e.printStackTrace();
				i.remove();
			}
		}
	}
	
	/**
	 * Get {@link PageEvaluation} related to the evaluation
	 * 
	 * @return {@link PageEvaluation}
	 */
	public PageEvaluation getPageEvaluation() {
		return pageEvaluation;
	}

	/**
	 * Set {@link PageEvaluation} related to the evaluation
	 * 
	 * @param pageEvaluation target {@link PageEvaluation}
	 */
	public void setPageEvaluation(PageEvaluation pageEvaluation) {
		this.pageEvaluation = pageEvaluation;
	}
		
}
