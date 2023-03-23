/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.util;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class SimulateLowVision {

	// separated from Lowvision part

	private static synchronized IPageImage getSimulatedPageImage(
			IPageImage target, ParamLowVision currentSetting) {

		try {
			return PageImageFactory.createSimulationPageImage(target,
					currentSetting.getLowVisionType());
		} catch (ImageException ie) {
			ie.printStackTrace();
		}

		return PageImageFactory.createPageImage();
	}

	public static synchronized ImageData[] doSimulate(IPageImage target,
			ParamLowVision currentSetting, String fileName) {

		ImageData[] imageDataArray = new ImageData[0];

		// TODO use memory
		try {
			getSimulatedPageImage(target, currentSetting).writeToBMPFile(
					fileName);
			ImageLoader loaderAfterSimulate = new ImageLoader();
			imageDataArray = loaderAfterSimulate.load(fileName);
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return imageDataArray;
	}

	public static synchronized Image doSimulate(IPageImage target,
			ParamLowVision currentSetting, Display display, String fileS) {

		Image image = null;

		try {
			PageImageFactory.createSimulationPageImage(target,
					currentSetting.getLowVisionType()).writeToBMPFile(fileS);
			image = new Image(display, fileS);

		} catch (ImageException ie) {
			ie.printStackTrace();
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return image;
	}

}
