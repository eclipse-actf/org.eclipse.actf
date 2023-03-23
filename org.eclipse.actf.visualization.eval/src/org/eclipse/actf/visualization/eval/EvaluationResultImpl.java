/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.ui.util.HighlightStringListener;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.eclipse.swt.custom.LineStyleListener;

/**
 * Default implementation of {@link IEvaluationResult}
 */
public class EvaluationResultImpl implements IEvaluationResult {

	// TODO viz, modelService?

	private String _summaryReportUrl;

	private List<IProblemItem> _problemList = new ArrayList<IProblemItem>();

	private String _summaryReportText;

	private String _targetUrl;

	private File _sourceFile = null;

	private boolean showAllGuidelineItems = false;

	private Vector<File> associateFileV = new Vector<File>();

	private LineStyleListener lsl = new HighlightStringListener();

	public EvaluationResultImpl() {
	}

	public void setProblemList(List<IProblemItem> problemList) {
		if (problemList == null) {
			problemList = new ArrayList<IProblemItem>();
		}
		this._problemList = problemList;
	}

	public void addProblemItems(Collection<IProblemItem> c) {
		_problemList.addAll(c);
	}

	public void addProblemItems(IProblemItem[] items) {
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				_problemList.add(items[i]);
			}
		}
	}

	public List<IProblemItem> getProblemList() {
		return this._problemList;
	}

	public void setSummaryReportText(String summaryReportText) {
		this._summaryReportText = summaryReportText;
	}

	public String getSummaryReportText() {
		if (_summaryReportText == null) {
			return ""; //$NON-NLS-1$
		}
		return this._summaryReportText;
	}

	public String getSummaryReportUrl() {
		if (_summaryReportUrl == null) {
			return ""; //$NON-NLS-1$
		}
		return _summaryReportUrl;
	}

	public void setSummaryReportUrl(String reportUrl) {
		_summaryReportUrl = reportUrl;
	}

	public void accept(IProblemItemVisitor visitor) {
		for (IProblemItem i : _problemList) {
			i.accept(visitor);
		}
	}

	public String getTargetUrl() {
		if (_targetUrl == null) {
			return ""; //$NON-NLS-1$
		}
		return _targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this._targetUrl = targetUrl;
	}

	public File getSourceFile() {
		return _sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this._sourceFile = sourceFile;
	}

	public boolean isShowAllGuidelineItems() {
		return showAllGuidelineItems;
	}

	public void setShowAllGuidelineItems(boolean b) {
		showAllGuidelineItems = b;
	}

	public LineStyleListener getLineStyleListener() {
		return lsl;
	}

	public void setLineStyleListener(LineStyleListener lsl) {
		if (lsl != null) {
			this.lsl = lsl;
		}
	}

	public boolean addAssociateFile(File target) {
		if (target != null) {
			return (associateFileV.add(target));
		}
		return false;
	}

	public File[] getAssociateFiles() {
		return associateFileV.toArray(new File[associateFileV.size()]);
	}

	public boolean removeAssociatedFile(File target) {
		return associateFileV.remove(target);
	}

}
