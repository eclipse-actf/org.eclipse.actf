/*******************************************************************************
 * Copyright (c) 2003, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.DecisionMaker;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.LowVisionFilter;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ImageColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemGroup;
import org.w3c.dom.Element;

public class InteriorImage extends PageComponent {

	public static final int UNSET = -1;

	// check color combination (image)
	// See InteriorImage.java
	public static final double THRESHOLD_MIN_IMAGE_COLOR_PROBLEM_PROBABILITY = 0.2;

	// // InteriorImage
	// minimum pixels/occupation to be considered as LargeComponent
	public static final int THRESHOLD_MIN_LARGE_COMPONENT_PIXELS = 100;

	public static final double THRESHOLD_MIN_LARGE_COMPONENT_OCCUPATION = 0.0005;

	int left = UNSET; // relative position

	int top = UNSET; // relative position

	Int2D pixel = null;

	int numLargeComponents = 0;

	InteriorImageComponent[] largeComponents = null;

	String url = null;
	
	Element image;

	public Element getImage() {
		return image;
	}

	public void setImageElement(Element image) {
		this.image = image;
	}

	// a part of PageImage
	public InteriorImage(PageImage _pi, int _x, int _y, int _width, int _height) {
		super(INTERIOR_IMAGE_TYPE, _pi);

		left = _x;
		top = _y;
		pixel = new Int2D(_width, _height);
		for (int j = 0; j < _height; j++) {
			for (int i = 0; i < _width; i++) {
				pixel.getData()[j][i] = _pi.pixel.getData()[j + top][i + left];
			}
		}
	}

	// a part of PageImage
	public InteriorImage(PageImage _pi, ImagePositionInfo _pos) {
		this(_pi, _pos.getX(), _pos.getY(), _pos.getWidth(), _pos.getHeight());
		url = _pos.getUrl();
	}

	InteriorImage(Int2D _i2d) {
		super(INTERIOR_IMAGE_TYPE, null);
		pixel = _i2d.deepCopy();
	}

	public InteriorImage(BufferedImage _bi) throws ImageException {
		super(INTERIOR_IMAGE_TYPE, null);
		pixel = ImageUtil.bufferedImageToInt2D(_bi);
	}

	public int getLeft() {
		return (left);
	}

	public int getTop() {
		return (top);
	}

	public String getUrl() {
		return (url);
	}

	// content type detection
	/*
	 * private static final int MIN_CHARACTER_IMAGE_HEIGHT = 20; private static
	 * final int MIN_FIGURE_IMAGE_HEIGHT = 150;
	 * 
	 * public void setContentType(){ int width = getWidth(); int height =
	 * getHeight(); short fileType = IoUtil.getFileType( this.url ); //null ->
	 * TYPE_UNKNOWN if( height < MIN_CHARACTER_IMAGE_HEIGHT ){ contentType =
	 * OTHER_TYPE; return; } // if( fileType != IoUtil.TYPE_GIF ){ // //TBD
	 * consider gradation (edge) in JPEG graph// contentType = OTHER_TYPE; //
	 * return; // } if( height < MIN_FIGURE_IMAGE_HEIGHT ){ contentType =
	 * OTHER_TYPE; return; } contentType = FIGURE_TYPE; }
	 */

	public int getWidth() {
		return (pixel.getWidth());
	}

	public int getHeight() {
		return (pixel.getHeight());
	}

	@SuppressWarnings("nls")
	public String dumpLargeComponents() {
		if (numLargeComponents == 0) {
			return ("There are no large components.");
		}
		String msg = "There are " + numLargeComponents + " large components.\n";
		for (int i = 0; i < numLargeComponents; i++) {
			InteriorImageComponent curComponent = largeComponents[i];
			ColorIRGB curColor = new ColorIRGB(curComponent.getColor());
			msg += i + ": ( " + curColor.getR() + ", " + curColor.getG() + ", "
					+ curColor.getB() + ")\n";
		}
		return (msg);
	}

	public void extractLargeComponents() {
		if (largeComponents != null) {
			return;
		}

		Vector<InteriorImageComponent> largeComponentVector = new Vector<InteriorImageComponent>();

		ColorHistogram histo = ColorHistogram.makeColorHistogram(pixel);

		int histoSize = histo.getSize();
		int numProcessedColors = histoSize;
		ColorHistogramBin[] histoArray = histo.getSortedArrayByOccurrence();
		for (int i = 0; i < histoSize; i++) {
			if (histoArray[i].occurrence < InteriorImage.THRESHOLD_MIN_LARGE_COMPONENT_PIXELS) {
				numProcessedColors = i;
				break;
			}
		}

		for (int k = 0; k < numProcessedColors; k++) {
			Vector<ConnectedComponent> currentVector = new Vector<ConnectedComponent>();

			int curColor = histoArray[k].color;
			BinaryImage binaryByColor = new BinaryImage(pixel,
					BinaryImage.METHOD_SPECIFY_FOREGROUND, curColor);

			LabeledImage curLabeledImage = new LabeledImage(binaryByColor,
					LabeledImage.METHOD_8_CONNECTIVITY);
			int numComponents = curLabeledImage.numComponents;
			ConnectedComponent[] components = curLabeledImage.components;

			for (int l = 0; l < numComponents; l++) {
				ConnectedComponent curCc = components[l];
				if (curCc.getCount() >= InteriorImage.THRESHOLD_MIN_LARGE_COMPONENT_PIXELS) {
					currentVector.addElement(curCc);
				}
			}

			if (currentVector.size() > 0) {
				InteriorImageComponent iic = new InteriorImageComponent(this,
						curColor, currentVector);
				if (iic.occupation >= InteriorImage.THRESHOLD_MIN_LARGE_COMPONENT_OCCUPATION) {
					largeComponentVector.addElement(iic);
				}
				currentVector.removeAllElements();
			}
		}

		// sort by pixelsize order
		Collections.sort(largeComponentVector, new CompareByCount());

		numLargeComponents = largeComponentVector.size();
		if (numLargeComponents > 0) {
			largeComponents = new InteriorImageComponent[numLargeComponents];
			for (int k = 0; k < numLargeComponents; k++) {
				largeComponents[k] = largeComponentVector.elementAt(k);
			}
			largeComponentVector.removeAllElements();
		}
	}

	private class CompareByCount implements Comparator<InteriorImageComponent> {
		public int compare(InteriorImageComponent _o1,
				InteriorImageComponent _o2) {
			return (_o2.count - _o1.count);
		}
	}

	public LowVisionProblemGroup[] checkColors(LowVisionType _lvType)
			throws ImageException {
		if (!_lvType.doChangeColors()) {
			return (null);
		}

		extractLargeComponents();

		if (numLargeComponents <= 1) {
			return (null);
		}

		Vector<ILowVisionProblem> problemVector = new Vector<ILowVisionProblem>();
		try {
			for (int k = 0; k < numLargeComponents - 1; k++) {
				for (int l = k + 1; l < numLargeComponents; l++) {
					InteriorImageComponent iic1 = largeComponents[k];
					InteriorImageComponent iic2 = largeComponents[l];
					int origColor1 = iic1.color;
					int origColor2 = iic2.color;

					// do not handle similar color parts in original image (to
					// avoide false positive)
					if (!(DecisionMaker.distinguishableImageColors(origColor1,
							origColor2))) {
						continue;
					}

					int convColor1 = _lvType.convertColor(origColor1);
					int convColor2 = _lvType.convertColor(origColor2);

					if (!(DecisionMaker.distinguishableImageColors(convColor1,
							convColor2))) {

						double probability = 1.0 - DecisionMaker
								.calcColorDistanceForImage(convColor1,
										convColor2);
						if (probability < InteriorImage.THRESHOLD_MIN_IMAGE_COLOR_PROBLEM_PROBABILITY) {
							continue;
						}

						ImageColorProblem probl = new ImageColorProblem(this,
								_lvType, probability, iic1, iic2);
						probl.setElement(image);
						problemVector.addElement(probl);
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Exception occurred while checking colors."); //$NON-NLS-1$
		}

		// (A) assume ImageProblem only
		if (problemVector.size() > 0) {
			Collections.sort(problemVector, new CompareByProbability());
			LowVisionProblemGroup group = null;
			try {
				group = new LowVisionProblemGroup(problemVector);
			} catch (LowVisionProblemException lvpe) {
				// lvpe.printStackTrace();
				throw new ImageException(
						"Error occurred while making an instance of LowVisionProblemGroup."); //$NON-NLS-1$
			}
			LowVisionProblemGroup[] groupArray = new LowVisionProblemGroup[1];
			groupArray[0] = group;
			return (groupArray);
		} else {
			return (null);
		}

		// (B) several types of problems
		/*
		 * Vector problemGroupVector = collectProblems( problemVector );
		 * 
		 * int vecSize = problemGroupVector.size(); if( vecSize > 0 ){
		 * LowVisionProblemGroup[] probGroupArray = new
		 * LowVisionProblemGroup[vecSize]; for( int k=0; k<vecSize; k++ ){
		 * probGroupArray[k] =
		 * (LowVisionProblemGroup)(problemGroupVector.elementAt(k)); } return(
		 * probGroupArray ); } else{ return( null ); }
		 */
	}

	@SuppressWarnings("unused")
	private Vector<LowVisionProblemGroup> collectProblems(
			Vector<LowVisionProblem> _vec) throws ImageException {
		if (_vec == null || _vec.size() == 0) {
			return (null);
		}

		Vector<LowVisionProblemGroup> answerVec = new Vector<LowVisionProblemGroup>();

		int curSize = _vec.size();
		while (curSize > 0) {
			// search problems should be merged with curProb
			LowVisionProblem curProb = _vec.elementAt(curSize - 1);
			_vec.removeElementAt(curSize - 1);
			Vector<ILowVisionProblem> curVec = new Vector<ILowVisionProblem>();
			curVec.addElement(curProb);

			short curType = curProb.getType();

			for (int k = curSize - 2; k >= 0; k--) {
				LowVisionProblem tmpProb = _vec.elementAt(k);
				if (tmpProb.getType() == curType) {
					_vec.removeElementAt(k);
					curVec.addElement(tmpProb);
				}
			}

			// sort by probability
			Collections.sort(curVec, new CompareByProbability());

			LowVisionProblemGroup curGroup = null;
			try {
				curGroup = new LowVisionProblemGroup(curVec);
			} catch (LowVisionProblemException lvpe) {
				// lvpe.printStackTrace();
				throw new ImageException(
						"Error occurred while making an instance of LowVisionProblemGroup."); //$NON-NLS-1$
			}
			// if (curGroup != null) {
			answerVec.addElement(curGroup);
			// }
			curSize = _vec.size();
		}
		return (answerVec);
	}

	private class CompareByProbability implements Comparator<ILowVisionProblem> {
		public int compare(ILowVisionProblem _o1, ILowVisionProblem _o2) {
			double diff = _o2.getProbability() - _o1.getProbability();
			if (diff > 0) {
				return (1);
			} else if (diff < 0) {
				return (-1);
			} else {
				return (0);
			}
		}
	}

	public Int2D simulate(LowVisionType _lvType) throws ImageException {
		LowVisionFilter lvFilter = new LowVisionFilter(_lvType);
		try {
			Int2D simulatedPixel = ImageUtil.bufferedImageToInt2D(lvFilter
					.filter(pixel.toBufferedImage(), null));
			return (simulatedPixel);
		} catch (LowVisionException lve) {
			// lve.printStackTrace();
			throw new ImageException(
					"Exception occurred while simulating an interiorImage"); //$NON-NLS-1$
		}
	}
}
