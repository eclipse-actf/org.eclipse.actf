/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind;

import java.io.File;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IChecker;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.w3c.dom.Document;

import com.ibm.icu.text.MessageFormat;

/**
 * Abstract implementation of {@link IBlindVisualizer}. Contributors need to
 * implement <samp>visualize</samp> method to suit to their purpose.
 */
public abstract class BlindVisualizerBase implements IBlindVisualizer {

	protected IChecker[] checkers = EvaluationUtil.getCheckers();
	protected String tmpDirS = BlindVizResourceUtil.getTempDirectory()
			.getAbsolutePath()
			+ File.separator;
	protected String targetUrl = ""; //$NON-NLS-1$
	protected IModelService modelService;

	// for reuslt
	protected IEvaluationResult checkResult = new EvaluationResultBlind();
	protected Document resultDocument;
	protected PageData pageData;
	protected int maxReachingTime = 0;
	protected File resultFile;
	protected File variantFile;

	private IVisualizationView vizView;

	/**
	 * Check if the target {@link IModelService} is supported by the
	 * implementation. Contributors must implement this method.
	 * 
	 * @param modelService
	 *            target {@link IModelService}
	 * @return true if the target {@link IModelService} is supported by the
	 *         implementation
	 */
	protected abstract boolean isTarget(IModelService modelService);

	public boolean setModelService(IModelService targetModel) {
		modelService = null;
		targetUrl = ""; //$NON-NLS-1$
		if (!isTarget(targetModel)) {
			return false;
		}

		modelService = targetModel;
		targetUrl = targetModel.getURL();
		if(targetUrl==null){
			targetUrl="";
		}
		return true;
	}

	public void setVisualizationView(IVisualizationView targetView) {
		vizView = targetView;
	}

	public String getMaxReachingTime() {
		return MessageFormat.format(Messages.BlindView_Maximum_Time,
				new Object[]{maxReachingTime});
	}

	public IEvaluationResult getEvaluationResult() {
		return checkResult;
	}

	public PageData getPageData() {
		return pageData;
	}

	public Document getResultDocument() {
		return resultDocument;
	}

	public File getResultFile() {
		return resultFile;
	}

	protected void setStatusMessage(String message) {
		if (null != vizView) {
			vizView.setStatusMessage(message);
		}
	}

	protected void setInfoMessage(String message) {
		if (null != vizView) {
			vizView.setInfoMessage(message);
		}
	}

}