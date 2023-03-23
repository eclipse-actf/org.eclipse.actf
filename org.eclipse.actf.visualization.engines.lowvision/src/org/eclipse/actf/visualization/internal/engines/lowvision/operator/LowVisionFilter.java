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

package org.eclipse.actf.visualization.internal.engines.lowvision.operator;

import java.awt.image.BufferedImage;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorException;

/*
 * filter to generate simulation image
 */
public class LowVisionFilter {
	LowVisionType type = null;

	Vector<String> simVector = new Vector<String>();

	private static final String TYPE_SENIOR_FILTER = "senior_color"; //$NON-NLS-1$

	private static final String TYPE_CVD = "color"; //$NON-NLS-1$

	private static final String TYPE_EYESIGHT = "focus"; //$NON-NLS-1$

	private static final String TYPE_GLARE = "glare"; //$NON-NLS-1$

	public LowVisionFilter(LowVisionType _t) {
		type = _t;
		if (type.countTypes() > 0) {
			if (type.doColorFilter()) {
				simVector.addElement(TYPE_SENIOR_FILTER);
			}
			if (type.doGlare()) {
				simVector.addElement(TYPE_GLARE);
			}
			if (type.doCVD()) {
				simVector.addElement(TYPE_CVD);
			}
			if (type.doEyesight()) {
				simVector.addElement(TYPE_EYESIGHT);
			}
		}
	}

	/*
	 * generate simulation image
	 */
	public BufferedImage filter(BufferedImage _src, BufferedImage _dest)
			throws LowVisionException {
		if (type == null) {
			throw new LowVisionException("LowVisionType must be provided."); //$NON-NLS-1$
		}
		try {
			int size = simVector.size();
			if (size > 1) {
				BufferedImage tmpImg = oneFilter(simVector.elementAt(0), _src,
						null);
				// keep ColorModel as in src
				for (int i = 1; i < size - 1; i++) {
					String curType = simVector.elementAt(i);
					BufferedImage tmpImg2 = oneFilter(curType, tmpImg, null);
					tmpImg = tmpImg2;
				}
				// change ColorModel to that of _dest
				String curType = simVector.elementAt(size - 1);
				return (oneFilter(curType, tmpImg, _dest));
			} else if (size == 1) {
				return (oneFilter(simVector.elementAt(0), _src, _dest));
			} else { // count == 0
				return (_src);
			}
		} catch (ColorException ce) {
			ce.printStackTrace();
			throw new LowVisionException(
					"ColorException occurred while filtering."); //$NON-NLS-1$
		}

	}

	// do image simulation for _type
	private BufferedImage oneFilter(String _type, BufferedImage _src,
			BufferedImage _dest) throws LowVisionException, ColorException {
		if (_type.equals(LowVisionFilter.TYPE_SENIOR_FILTER)) {
			ColorFilterOp cfop = new ColorFilterOp();
			cfop.setRatio(type.getColorFilterRGB());
			return (cfop.filter(_src, _dest));
		} else if (_type.equals(LowVisionFilter.TYPE_GLARE)) {
			// debug
			// DebugUtil.errMsg( this, "Glare is invoked." );
			GlareOp gop = new GlareOp(type.getGlareDegree());
			return (gop.filter(_src, _dest));
		} else if (_type.equals(LowVisionFilter.TYPE_CVD)) {
			CVDOp cop = new CVDOp(type.getCVDType());
			return (cop.filter(_src, _dest));
		} else if (_type.equals(LowVisionFilter.TYPE_EYESIGHT)) {
			BlurOp bop = new BlurOp(type);
			return (bop.filter(_src, _dest));
		} else {
			throw new LowVisionException("Unknown type: " + _type); //$NON-NLS-1$
		}
	}
}
