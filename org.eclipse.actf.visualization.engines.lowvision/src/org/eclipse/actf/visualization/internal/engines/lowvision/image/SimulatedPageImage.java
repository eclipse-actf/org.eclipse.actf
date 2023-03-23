/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.awt.image.BufferedImage;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.LowVisionFilter;

/*
 * Contain simulation result
 */
public class SimulatedPageImage extends PageImage {
	private IPageImage original = null; // original image

	private LowVisionType type = null; // type of simulation

	public SimulatedPageImage(IPageImage _pi, LowVisionType _type)
			throws ImageException {
		original = _pi;
		type = _type;
		LowVisionFilter lvFilter = new LowVisionFilter(type);
		BufferedImage bi = null;
		try {
			BufferedImage tmpBufIm = original.getBufferedImage();
			bi = lvFilter.filter(tmpBufIm, null);
			tmpBufIm = null;
		} catch (LowVisionException lve) {
			// lve.printStackTrace();
			throw new ImageException(
					"The original PageImage cannot be filtered."); //$NON-NLS-1$
		}
		init(bi);
		bi = null;
	}

}
