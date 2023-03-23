/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.internal;

import java.util.List;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.visualization.ui.IPositionSize;
import org.eclipse.actf.visualization.ui.VisualizationCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class LowVisionSimulationView {

	private VisualizationCanvas _imageCanvas;

	// private LowVisionToolbar _lowVisionToolbar;

	// separate from PartRightLowVision
	public LowVisionSimulationView(Composite parent) {

		GridData gridData;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		parent.setLayout(gridLayout);

		// this._lowVisionToolbar = new LowVisionToolbar(parent, SWT.NONE,
		// lowVisionCtrl);

		Composite compositeLowVisionHalf2 = new Composite(parent, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		compositeLowVisionHalf2.setLayoutData(gridData);

		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		compositeLowVisionHalf2.setLayout(gridLayout);

		// Canvas to show the image.
		_imageCanvas = new VisualizationCanvas(compositeLowVisionHalf2);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		_imageCanvas.setLayoutData(gridData);

	}

	protected void highlight(List<IPositionSize> target) {
		_imageCanvas.highlight(target);
	}

	protected void clearImage() {
		_imageCanvas.clear();
	}

	protected void displayImage(ImageData newImageData, IModelService target, boolean wholePage) {
		_imageCanvas.showImage(newImageData, target);
		_imageCanvas.setSync(wholePage && target.getScrollManager().getScrollType() != IModelServiceScrollManager.NONE);
	}

	protected void setCurrentModelService(IModelService current) {
		_imageCanvas.setCurrentModelService(current);
	}

	protected void setScale(double x, double y) {
		_imageCanvas.setScale(x, y);
	}

}
