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
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.w3c.dom.Document;

/**
 * Interface to implement new blind usability visualization class. The
 * implementation class needs to be registered by using an extension point
 * "org.eclipse.actf.visualization.blind.blindVisualizer".
 */
public interface IBlindVisualizer {

	/**
	 * Visualization error code.
	 */
	public static final int ERROR = -1;

	/**
	 * Visualization success code.
	 */
	public static final int OK = 0;

	/**
	 * Execute the visualization (and accessibility evaluation if implemented).
	 * 
	 * @return visualization result status code. (OK, ERROR, etc.)
	 */
	public abstract int visualize();

	/**
	 * Get maximum reaching time within the target content.
	 * 
	 * @return maximum reaching time (second) within the target content.
	 */
	public abstract String getMaxReachingTime();

	/**
	 * Get accessibility evaluation result.
	 * 
	 * @return accessibility evaluation result
	 */
	public abstract IEvaluationResult getEvaluationResult();

	/**
	 * Get statistics data of the target page.
	 * 
	 * @return statistics data of the target page.
	 * @see PageData
	 */
	public abstract PageData getPageData();

	/**
	 * Get visualization result {@link Document}.
	 * 
	 * @return visualization result Document
	 */
	public abstract Document getResultDocument();

	/**
	 * Get visualization result {@link File}.
	 * 
	 * @return visualization result File
	 */
	public abstract File getResultFile();

	/**
	 * Set target {@link IModelService} for visualization
	 * 
	 * @param modelService
	 *            target {@link IModelService}
	 * @return true if the specified target is supported by the implementation
	 */
	public abstract boolean setModelService(IModelService modelService);

	/**
	 * Set target {@link IVisualizationView} to show visualization result
	 * 
	 * @param targetView
	 *            target {@link IVisualizationView} to show visualization
	 *            result.
	 */
	public abstract void setVisualizationView(IVisualizationView targetView);
}