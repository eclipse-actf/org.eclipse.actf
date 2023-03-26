/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.odf;

import java.util.Vector;

import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class OdfCheckerEngineBase implements
		OdfCheckerEngine {
	private static final String PREFIX_O = "O_"; //$NON-NLS-1$

	protected boolean detectVersion = false;

	protected Document targetODF;

	protected Document target;

	protected Vector<IProblemItem> resultV;

	// protected TrlGuideCheckData browserData;

	/**
	 * @param target
	 * @param result
	 * @param curUrlS
	 * @param document2IdMap
	 */
	public OdfCheckerEngineBase(Document targetODF, Document target) {
		super();
		this.targetODF = targetODF;
		this.target = target;

		resultV = new Vector<IProblemItem>();
	}

	public void setDetectOdfVersion(boolean bDetectVersion) {
		this.detectVersion = bDetectVersion;
	}

	protected IProblemItem addProblem(int id, Node targetNode, String midDesc) {
		IProblemItem problem = new ProblemItemImpl(PREFIX_O + id);
		problem.setTargetNode(targetNode);
		problem.setTargetString(midDesc);
		problem.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(
				targetNode));
		resultV.add(problem);
		return (problem);
	}

	protected IProblemItem addProblem(int id, String midDesc,
			Element startTarget, Element endTarget) {
		IProblemItem problem = new ProblemItemImpl(PREFIX_O + id);
		problem.setTargetNode(startTarget);
		problem.setTargetString(midDesc);
		problem.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(
				startTarget, endTarget));
		resultV.add(problem);
		return (problem);
	}

	protected IProblemItem addProblem(int id, String midDesc, Vector<Node> targetV) {
		IProblemItem problem = new ProblemItemImpl(PREFIX_O + id);
		problem.setTargetString(midDesc);
		problem
				.setHighlightTargetNodeInfo(new HighlightTargetNodeInfo(targetV));

		resultV.add(problem);
		return (problem);
	}

}