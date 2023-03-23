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

package org.eclipse.actf.visualization.internal.engines.lowvision;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterMS;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSM;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSS;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorLAB;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Container;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.LineSegment;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Topology;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Vector3D;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;

/**
 * Check color combination by using threshold (in LowvisionCommon)
 * 
 */
@SuppressWarnings("nls")
public class DecisionMaker {

	// thresholds for char check
	public static final double THRESHOLD_MIN_CHAR_RATIO = 0.1;
	public static final double THRESHOLD_MAX_CHAR_RATIO = 10.0;
	public static final double THRESHOLD_MIN_CONTAINER_DENSITY = 0.4;
	public static final double THRESHOLD_MAX_CHARACTER_DENSITY = 0.75;
	public static final double THRESHOLD_MIN_THINNING_RATIO = 0.25; // 
	public static final double THRESHOLD_MIN_UNDERLINE_POSITION = 0.85;
	public static final double THRESHOLD_MIN_UNDERLINE_WIDTH_RATIO = 0.95;
	public static final double THRESHOLD_MIN_UNDERLINE_RATIO = 3.0;
	public static final int THRESHOLD_MIN_CHAR_WIDTH = 5;
	public static final int THRESHOLD_MAX_CHAR_WIDTH = 72;
	public static final int THRESHOLD_MIN_CHAR_HEIGHT = 5;
	public static final int THRESHOLD_MAX_CHAR_HEIGHT = 100;
	public static final int THRESHOLD_MAX_THINNED_BRANCHES = 8;
	public static final int THRESHOLD_MAX_THINNED_CROSSES = 8;
	public static final int THRESHOLD_MIN_MSCHAR_WIDTH = 10;
	public static final int THRESHOLD_MIN_MSCHAR_HEIGHT = 10;
	public static final int THRESHOLD_MAX_MSCHAR_HEIGHT = 100;
	public static final int THRESHOLD_MIN_SMCHAR_WIDTH = 10;
	public static final int THRESHOLD_MIN_SMCHAR_HEIGHT = 10;
	public static final int THRESHOLD_MAX_SMCHAR_HEIGHT = 100;

	// check fg/bg color
	public static final double THRESHOLD_FOREGROUND_RATIO = 0.25;
	// error margin
	public static final double THRESHOLD_FOREGROUND_ERROR_MARGIN = 1.5;
	public static final float THRESHOLD_LIMIT_BLURRED_WIDTH_RATIO = 3.0f;
	public static final float THRESHOLD_LIMIT_BLURRED_HEIGHT_RATIO = 3.0f;
	// Problem grouping
	public static final int THRESHOLD_MAX_CHARACTER_SPACE = 7; // word
	public static final int THRESHOLD_MAX_REGION_ELEMENT_SPACE = 80;
	public static final int THRESHOLD_MAX_GROUPED_CONTAINER_WIDTH = 300;
	public static final int THRESHOLD_MAX_GROUPED_CONTAINER_HEIGHT = 300;

	// severity/color mapping
	public static final double SCORE_ORANGE = 0.5;
	public static final double SCORE_RED = 1.0;

	// color combination check (image)
	public static final float ENOUGH_DELTA_E_FOR_IMAGE = 40.0f;
	// color combination check (text)
	public static final float MIN_ENOUGH_DELTA_L_FOR_TEXT = 20.0f; // enough L
	// (L*a*b*)
	public static final float MAX_ENOUGH_DELTA_L_FOR_TEXT = 40.0f; // (L*a*b*)
	public static final float ENOUGH_DELTA_E_FOR_TEXT = 100.0f; // (L*a*b*)
	public static final float ENOUGH_DELTA_H_FOR_TEXT = 100.0f; // (L*a*b*)

	// // for PageImage
	//
	// check connected component or not
	public static short judgeComponentType(ConnectedComponent _cc,
			IPageImage _pi) throws ImageException {
		return (judgeComponentType(_cc, _pi, false));
	}

	public static short judgeComponentType(ConnectedComponent _cc,
			IPageImage _pi, boolean _test) throws ImageException {
		// int pWidth = _pi.getWidth();
		// int pHeight = _pi.getHeight();
		BinaryImage shape = _cc.getShape();
		int cWidth = shape.getWidth();
		int cHeight = shape.getHeight();

		// very small
		if (cWidth < DecisionMaker.THRESHOLD_MIN_CHAR_WIDTH
				|| cHeight < DecisionMaker.THRESHOLD_MIN_CHAR_HEIGHT) {
			return (PageComponent.OTHER_TYPE);
		}

		double density = _cc.getDensity();

		// underlined part (start)
		LineSegment horSeg = shape.detectLongestHorizontalLine();
		if (horSeg.isVertical() || horSeg.isDiagonal()) {
			throw new ImageException(
					"detectLongestHorizontalLine() does not work.");
		}
		double widthRatio = horSeg.getLength() / cWidth;
		double position = (double) (horSeg.getLeftPoint().getY())
				/ (double) cHeight;
		double ratio = (double) cWidth / (double) cHeight;
		if (widthRatio >= DecisionMaker.THRESHOLD_MIN_UNDERLINE_WIDTH_RATIO
				&& position >= DecisionMaker.THRESHOLD_MIN_UNDERLINE_POSITION
				&& ratio >= DecisionMaker.THRESHOLD_MIN_UNDERLINE_RATIO
				&& density < DecisionMaker.THRESHOLD_MIN_CONTAINER_DENSITY) {
			return (PageComponent.CANDIDATE_UNDERLINED_CHARACTER_TYPE);
		}

		// container
		if ((cWidth > DecisionMaker.THRESHOLD_MAX_CHAR_WIDTH || cHeight > DecisionMaker.THRESHOLD_MAX_CHAR_HEIGHT)) {
			return (PageComponent.CONTAINER_TYPE);
		}

		// small (might be line)
		if (ratio < DecisionMaker.THRESHOLD_MIN_CHAR_RATIO
				|| DecisionMaker.THRESHOLD_MAX_CHAR_RATIO < ratio) {
			return (PageComponent.OTHER_TYPE);
		}

		// test
		/*
		 * Topology topo = new Topology( _cc ); Topology thinTopo = new
		 * Topology( _cc.thinning() ); int count1 = topo.getCount(); int count2
		 * = thinTopo.getCount(); int interior1 = topo.getNumInterior(); int
		 * interior2 = thinTopo.getNumInterior(); int edge1 =
		 * topo.getNumEdges(); int edge2 = thinTopo.getNumEdges(); int branch1 =
		 * topo.getNumBranches(); int branch2 = thinTopo.getNumBranches(); int
		 * cross1 = topo.getNumCrosses(); int cross2 = thinTopo.getNumCrosses();
		 * 
		 * double countDouble = (double)(topo.getCount()); double interiorRatio
		 * = (double)(topo.getNumInterior())/countDouble; double edgeRatio =
		 * (double)(topo.getNumEdges())/countDouble; double branchRatio =
		 * (double)(topo.getNumBranches())/countDouble; double crossRatio =
		 * (double)(topo.getNumCrosses())/countDouble;
		 */

		// test (density)
		/*
		 * if( density > THRESHOLD_MAX_CHARACTER_DENSITY ){ return(
		 * PageComponent.OTHER_TYPE ); }
		 */

		// thinning
		ConnectedComponent thinCc = _cc.thinning();
		if ((double) (thinCc.getCount()) / (double) (_cc.getCount()) < DecisionMaker.THRESHOLD_MIN_THINNING_RATIO) {
			return (PageComponent.OTHER_TYPE);
		}

		Topology thinTopo = new Topology(thinCc);
		if (thinTopo.getNumBranches() > DecisionMaker.THRESHOLD_MAX_THINNED_BRANCHES) {
			return (PageComponent.OTHER_TYPE);
		}
		if (thinTopo.getNumCrosses() > DecisionMaker.THRESHOLD_MAX_THINNED_CROSSES) {
			return (PageComponent.OTHER_TYPE);
		}

		// debug(from here)
		/*
		 * if( _test ){ int ccX = _cc.getLeft(); int ccY = _cc.getTop(); int ccW
		 * = _cc.getWidth(); int ccH = _cc.getHeight(); DebugUtil.debugMsg(
		 * null, "Judging container at (" + ccX + "," + ccY + ") [" + ccW +
		 * " x " + ccH + "] : count=(" + count1 + "," + count2 + ") interior=("
		 * + interior1 + "," + interior2 + ") edge=(" + edge1 + "," + edge2 + ")
		 * branch=(" + branch1 + "," + branch2 + ") cross=(" + cross1 + "," +
		 * cross2 + ")", "COMPONENT" ); // DebugUtil.debugMsg( null, "Judging
		 * container at (" + ccX + "," + ccY + ") [" + ccW + " x " + ccH + "] :
		 * count = " + topo.getCount() + ", interiorRatio =
		 * " + interiorRatio + ", edgeRatio = " + edgeRatio + ", branchRatio =
		 * " + branchRatio + ", crossRatio = " + crossRatio, "COMPONENT" ); //
		 * DebugUtil.debugMsg( null, "Density = " + density, "COMPONENT" ); //
		 * debug(to here) }
		 */

		return (PageComponent.CANDIDATE_CHARACTER_TYPE);
	} // judgeComponentType( ConnectedComponent )

	// check connected component is MS character or not
	public static boolean isMSCharacter(ConnectedComponent _cc)
			throws ImageException {
		BinaryImage shape = _cc.getShape();
		int cWidth = shape.getWidth();
		int cHeight = shape.getHeight();

		// too small
		if (cWidth < DecisionMaker.THRESHOLD_MIN_MSCHAR_WIDTH
				|| cHeight < DecisionMaker.THRESHOLD_MIN_MSCHAR_HEIGHT) {
			return (false);
		}
		if (cHeight > DecisionMaker.THRESHOLD_MAX_MSCHAR_HEIGHT) {
			return (false);
		}
		/*
		 * do not use thinning ConnectedComponent thinCc = _cc.thinning(); //
		 * debug // DebugUtil.outMsg( null, "thinCc.count = " +
		 * thinCc.getCount() + ", _cc.count = " + _cc.getCount() + ", Ratio = "
		 * + (double)(thinCc.getCount())/(double)(_cc.getCount()) ); if(
		 * (double)(thinCc.getCount())/(double)(_cc.getCount()) <
		 * THRESHOLD_MIN_THINNING_RATIO ){ return( false ); }
		 */
		return (true);
	}

	public static boolean isTooSmallThinedMSCharacter(CharacterMS _msc)
			throws ImageException {
		ConnectedComponent curCc = _msc.getConnectedComponent();
		ConnectedComponent thinCc = curCc.thinning();
		double ratio = (double) (thinCc.getCount())
				/ (double) (curCc.getCount());
		if (ratio < DecisionMaker.THRESHOLD_MIN_THINNING_RATIO) {
			return (true);
		}
		return (false);
	}

	public static boolean isSMCharacter(CharacterSM _smc) {
		int foregroundColor = _smc.getForegroundColor();
		int sumR = 0;
		int sumG = 0;
		int sumB = 0;
		int w = _smc.getWidth();
		int h = _smc.getHeight();
		if (w < DecisionMaker.THRESHOLD_MIN_SMCHAR_WIDTH
				|| h < DecisionMaker.THRESHOLD_MIN_SMCHAR_HEIGHT
				|| h > DecisionMaker.THRESHOLD_MAX_SMCHAR_HEIGHT) {
			return (false);
		}
		ConnectedComponent cc = _smc.getConnectedComponent();
		byte[][] data = cc.getShape().getData();
		int[][] img = _smc.getImage();
		int count = 0;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (data[j][i] == 0) {
					int color = img[j][i];
					sumR += (color >> 16) & 0xff;
					sumG += (color >> 8) & 0xff;
					sumB += color & 0xff;
					count++;
				}
			}
		}
		sumR /= count;
		sumG /= count;
		sumB /= count;

		int sumColor = ((sumR & 0xff) << 16) | ((sumG & 0xff) << 8)
				| (sumB & 0xff);
		try {
			if (distinguishableTextColors(foregroundColor, sumColor)) {
				return (true);
			} else {
				return (false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return (true);
		}
	}

	// // for SimulatedPageImage

	/*
	 * Color combination check (for text)
	 * 
	 * criteria for text/image text (bg/fg connected) <- brightness image
	 * (components are separated in some cases) <- chroma, hue
	 */
	public static boolean distinguishableTextColors(ColorLAB _c1, ColorLAB _c2) {
		double deltaL = ColorLAB.deltaL(_c1, _c2);
		double deltaE = ColorLAB.deltaE(_c1, _c2);
		// double deltaH = ColorLAB.deltaH( _c1, _c2 );

		// minimum delta of brightness
		if (deltaL < DecisionMaker.MIN_ENOUGH_DELTA_L_FOR_TEXT) {
			return (false);
		}

		// enough chroma/hue difference
		// (required delta of brightness becomes small)
		if (deltaE >= DecisionMaker.ENOUGH_DELTA_E_FOR_TEXT) {
			return (true);
		}
		// if( deltaH >= ENOUGH_DELTA_H_FOR_TEXT ){
		// return( true );
		// }

		double thresholdL = calcThresholdLforText(_c1, _c2);
		if (deltaL < thresholdL) {
			return (false);
		} else {
			return (true);
		}
	}

	// Color combination check (for text)
	public static boolean distinguishableTextColors(int _c1, int _c2)
			throws LowVisionException {
		// debug(from here)
		/*
		 * ColorIRGB i2_ = new ColorIRGB(_c2); ColorIRGB i1_ = new
		 * ColorIRGB(_c1); int c1R = i1_.getR(); int c1G = i1_.getG(); int c1B =
		 * i1_.getB(); try{ if( _c2 == ((90<<16)+(93<<8)+90) && (c1R>c1G) &&
		 * (c1R>c1B) ){ DebugUtil.outMsg( null, "C1: R = " + i1_.getR() +
		 * ", G = " + i1_.getG() + ", B = " + i1_.getB() ); ColorLAB l1_ = (new
		 * ColorIRGB(_c1)).toXYZ().toLAB(); ColorLAB l2_ = (new
		 * ColorIRGB(_c2)).toXYZ().toLAB(); DebugUtil.outMsg( null, "Delta L = "
		 * + ColorLAB.deltaL(l1_,l2_) ); DebugUtil.outMsg( null, "Delta E = " +
		 * ColorLAB.deltaE(l1_,l2_) ); } }catch( Exception e ){
		 * e.printStackTrace(); }
		 */
		// debug(to here)
		ColorLAB lab1 = null;
		ColorLAB lab2 = null;
		try {
			lab1 = (new ColorIRGB(_c1)).toXYZ().toLAB();
			lab2 = (new ColorIRGB(_c2)).toXYZ().toLAB();
		} catch (ColorException ce) {
			ce.printStackTrace();
			throw new LowVisionException(
					"ColorException occurred while converting int into ColorLAB");
		}
		return (distinguishableTextColors(lab1, lab2));
	}

	// obtain threshold of brightness from chroma/hue
	public static double calcThresholdLforText(ColorLAB _c1, ColorLAB _c2) {
		double deltaE = ColorLAB.deltaE(_c1, _c2);
		double thresholdL = (DecisionMaker.MIN_ENOUGH_DELTA_L_FOR_TEXT - DecisionMaker.MAX_ENOUGH_DELTA_L_FOR_TEXT)
				/ DecisionMaker.ENOUGH_DELTA_E_FOR_TEXT
				* deltaE
				+ DecisionMaker.MAX_ENOUGH_DELTA_L_FOR_TEXT;
		if (thresholdL < 0.0) {
			thresholdL = 0.0;
		}
		return (thresholdL);
		/*
		 * double deltaH = ColorLAB.deltaH( _c1, _c2 ); double thresholdL =
		 * (MIN_ENOUGH_DELTA_L_FOR_TEXT
		 * -MAX_ENOUGH_DELTA_L_FOR_TEXT)/ENOUGH_DELTA_H_FOR_TEXTdeltaH +
		 * MAX_ENOUGH_DELTA_L_FOR_TEXT; if( thresholdL < 0.0 ){ thresholdL =
		 * 0.0; } return( thresholdL );
		 */
	}

	public static double calcThresholdLforText(int _c1, int _c2)
			throws LowVisionException {
		ColorLAB lab1 = null;
		ColorLAB lab2 = null;
		try {
			lab1 = (new ColorIRGB(_c1)).toXYZ().toLAB();
			lab2 = (new ColorIRGB(_c2)).toXYZ().toLAB();
		} catch (ColorException ce) {
			ce.printStackTrace();
			throw new LowVisionException(
					"ColorException occurred while converting int into ColorLAB");
		}
		return (calcThresholdLforText(lab1, lab2));
	}

	// Color conbimation check (for image)
	public static boolean distinguishableImageColors(ColorLAB _c1, ColorLAB _c2) {
		if (calcColorDistanceForImage(_c1, _c2) >= 1.0) {
			return (true);
		} else {
			return (false);
		}

	}

	public static boolean distinguishableImageColors(int _c1, int _c2)
			throws LowVisionException {
		ColorLAB lab1 = null;
		ColorLAB lab2 = null;
		try {
			lab1 = (new ColorIRGB(_c1)).toXYZ().toLAB();
			lab2 = (new ColorIRGB(_c2)).toXYZ().toLAB();
		} catch (ColorException ce) {
			ce.printStackTrace();
			throw new LowVisionException(
					"ColorException occurred while converting int into ColorLAB");
		}
		return (distinguishableImageColors(lab1, lab2));
	}

	public static double calcColorDistanceForImage(ColorLAB _c1, ColorLAB _c2) {
		return (ColorLAB.deltaE(_c1, _c2) / DecisionMaker.ENOUGH_DELTA_E_FOR_IMAGE);
	}

	public static double calcColorDistanceForImage(int _c1, int _c2)
			throws LowVisionException {
		ColorLAB lab1 = null;
		ColorLAB lab2 = null;
		try {
			lab1 = (new ColorIRGB(_c1)).toXYZ().toLAB();
			lab2 = (new ColorIRGB(_c2)).toXYZ().toLAB();
		} catch (ColorException ce) {
			ce.printStackTrace();
			throw new LowVisionException(
					"ColorException occurred while converting int into ColorLAB");
		}
		return (calcColorDistanceForImage(lab1, lab2));
	}

	/*
	 * check the color can be considered as foreground color?
	 */
	public static boolean isForegroundColor(int _color, int _fore, int _back) {
		Vector3D colV = colorToVector3D(_color);
		Vector3D foreV = colorToVector3D(_fore);
		Vector3D backV = colorToVector3D(_back);

		Vector3D back2colV = Vector3D.subtract(colV, backV);
		Vector3D back2foreV = Vector3D.subtract(foreV, backV);

		if (back2foreV.isZeroVector() || back2colV.isZeroVector()) {

			double distance = Vector3D
					.magnitude(Vector3D.subtract(foreV, colV));
			if (distance <= DecisionMaker.THRESHOLD_FOREGROUND_ERROR_MARGIN) {
				return (true);
			} else {
				return (false);
			}
		}

		double rCol = back2colV.magnitude();
		double rFore = back2foreV.magnitude();
		double sinTheta = 0.0;
		double cosTheta = 0.0;
		try {
			sinTheta = Vector3D.sine(back2foreV, back2colV);
			cosTheta = Vector3D.cosine(back2foreV, back2colV);
		} catch (ImageException e) {
			e.printStackTrace();
		}

		if (rCol * sinTheta > DecisionMaker.THRESHOLD_FOREGROUND_ERROR_MARGIN) {
			return (false);
		}

		double ratio = rCol * cosTheta / rFore;
		if (ratio < DecisionMaker.THRESHOLD_FOREGROUND_RATIO || 1.0 < ratio) {
			return (false);
		}

		return (true);
	}

	private static Vector3D colorToVector3D(int _color) {
		ColorIRGB ci = new ColorIRGB(_color);
		return (new Vector3D(ci.getR(), ci.getG(), ci.getB()));
	}

	/*
	 * calc search range for blurred text
	 */
	public static int calcSearchMinX(int _origLeft, int _origWidth,
			int _pageWidth) {
		int tmpX = _origLeft
				- Math
						.round((DecisionMaker.THRESHOLD_LIMIT_BLURRED_WIDTH_RATIO - 1.0f)
								/ 2.0f * _origWidth);
		if (tmpX < 0) {
			tmpX = 0;
		}
		return (tmpX);
	}

	public static int calcSearchMaxX(int _origLeft, int _origWidth,
			int _pageWidth) {
		int tmpX = _origLeft
				+ _origWidth
				+ Math
						.round((DecisionMaker.THRESHOLD_LIMIT_BLURRED_WIDTH_RATIO - 1.0f)
								/ 2.0f * _origWidth);
		if (tmpX > _pageWidth - 1) {
			tmpX = _pageWidth - 1;
		}
		return (tmpX);
	}

	public static int calcSearchMinY(int _origTop, int _origHeight,
			int _pageHeight) {
		int tmpY = _origTop
				- Math
						.round((DecisionMaker.THRESHOLD_LIMIT_BLURRED_HEIGHT_RATIO - 1.0f)
								/ 2.0f * _origHeight);
		if (tmpY < 0) {
			tmpY = 0;
		}
		return (tmpY);
	}

	public static int calcSearchMaxY(int _origTop, int _origHeight,
			int _pageHeight) {
		int tmpY = _origTop
				+ _origHeight
				+ Math
						.round((DecisionMaker.THRESHOLD_LIMIT_BLURRED_HEIGHT_RATIO - 1.0f)
								/ 2.0f * _origHeight);
		if (tmpY > _pageHeight - 1) {
			tmpY = _pageHeight - 1;
		}
		return (tmpY);
	}

	// // for Problems

	/*
	 * merge problems in the same container
	 */
	public static boolean areSameGroupProblems(ILowVisionProblem _p1,
			ILowVisionProblem _p2) throws LowVisionProblemException {
		if (_p1.isGroup() || _p2.isGroup()) {
			throw new LowVisionProblemException(
					"ProblemGroup cannot be grouped any more.");
		}

		// check component type
		PageComponent component1 = _p1.getPageComponent();
		PageComponent component2 = _p2.getPageComponent();
		short componentType1 = component1.getType();
		short componentType2 = component2.getType();
		if (componentType1 != componentType2) {
			return (false);
		}

		// check problem type
		int type1 = _p1.getLowVisionProblemType();
		if (type1 != _p2.getLowVisionProblemType()) {
			return (false);
		}

		// color
		// SS -> same fg/bg
		// MS -> same bg
		// SM -> same fg
		if (type1 == LowVisionProblem.LOWVISION_COLOR_PROBLEM) {
			ColorProblem cp1 = (ColorProblem) _p1;
			ColorProblem cp2 = (ColorProblem) _p2;

			if (componentType1 == PageComponent.SS_CHARACTER_TYPE) {
				if (cp1.getBackgroundColor() != cp2.getBackgroundColor()) {
					return (false);
				}
				if (cp1.getForegroundColor() != cp2.getForegroundColor()) {
					return (false);
				}
			} else if (componentType1 == PageComponent.MS_CHARACTER_TYPE) {
				if (cp1.getBackgroundColor() != cp2.getBackgroundColor()) {
					return (false);
				}
			} else if (componentType1 == PageComponent.SM_CHARACTER_TYPE) {
				if (cp1.getForegroundColor() != cp2.getForegroundColor()) {
					return (false);
				}
			}
		}

		// blur
		else if (type1 == LowVisionProblem.LOWVISION_BLUR_PROBLEM) {
			if (componentType1 == PageComponent.SS_CHARACTER_TYPE) {
				CharacterSS ssc1 = (CharacterSS) component1;
				CharacterSS ssc2 = (CharacterSS) component2;
				if (ssc1.getForegroundColor() != ssc2.getForegroundColor()) {
					return (false);
				}
				if (ssc1.getBackgroundColor() != ssc2.getBackgroundColor()) {
					return (false);
				}
			} else if (componentType1 == PageComponent.MS_CHARACTER_TYPE) {
				if (((CharacterMS) component1).getBackgroundColor() != ((CharacterMS) component2)
						.getBackgroundColor()) {
					return (false);
				}
			} else if (componentType1 == PageComponent.SM_CHARACTER_TYPE) {
				if (((CharacterSM) component1).getForegroundColor() != ((CharacterSM) component2)
						.getForegroundColor()) {
					return (false);
				}
			}
		} else {
			throw new LowVisionProblemException("Unknown problem type :"
					+ type1);
		}

		// small container
		Container cont = null;
		cont = _p1.getPageComponent().getContainer();
		if (cont != null) {
			if (cont.getWidth() <= DecisionMaker.THRESHOLD_MAX_GROUPED_CONTAINER_WIDTH
					&& cont.getHeight() <= DecisionMaker.THRESHOLD_MAX_GROUPED_CONTAINER_HEIGHT) {
				return (true);
			}
		}

		int left1 = _p1.getX();
		int right1 = left1 + _p1.getWidth() - 1;
		int top1 = _p1.getY();
		int bottom1 = top1 + _p1.getHeight() - 1;
		int left2 = _p2.getX();
		int right2 = left2 + _p2.getWidth() - 1;
		int top2 = _p2.getY();
		int bottom2 = top2 + _p2.getHeight() - 1;

		if (left1 - right2 > DecisionMaker.THRESHOLD_MAX_REGION_ELEMENT_SPACE
				|| left2 - right1 > DecisionMaker.THRESHOLD_MAX_REGION_ELEMENT_SPACE) {
			return (false);
		}
		if (top1 - bottom2 > DecisionMaker.THRESHOLD_MAX_REGION_ELEMENT_SPACE
				|| top2 - bottom1 > DecisionMaker.THRESHOLD_MAX_REGION_ELEMENT_SPACE) {
			return (false);
		}

		return (true);
	}

	// /*
	// * same Y region, near (X) -> word
	// */
	// public static boolean areSameGroupProblems(LowVisionProblem _p1,
	// LowVisionProblem _p2) throws ImageException { //
	// if (_p1.isGroup() || _p2.isGroup()) {
	// throw new ImageException("ProblemGroup cannot be grouped any more.");
	// }
	// if (_p1.getLowVisionProblemType() != _p2.getLowVisionProblemType()) {
	// return (false);
	// }
	//
	// int left1 = _p1.getX();
	// int right1 = left1 + _p1.getWidth() - 1;
	// int top1 = _p1.getY();
	// int bottom1 = top1 + _p1.getHeight() - 1;
	// int left2 = _p2.getX();
	// int right2 = left2 + _p2.getWidth() - 1;
	// int top2 = _p2.getY();
	// int bottom2 = top2 + _p2.getHeight() - 1;
	// if (left1 - right2 > THRESHOLD_MAX_CHARACTER_SPACE
	// || left2 - right1 > THRESHOLD_MAX_CHARACTER_SPACE) {
	// return (false);
	// }
	// if (bottom1 < top2 || bottom2 < top1) {
	// return (false);
	// }
	// return (true);
	// }

	// return color for the score map
	public static int getScoreMapColor(double _score) {
		if (_score <= 0.0) {
			return (0x00aaaaaa);
		} else if (_score < DecisionMaker.SCORE_ORANGE) {
			return (0x00ffff00);
		} else if (_score < DecisionMaker.SCORE_RED) {
			return (0x00ff7700);
		} else {
			return (0x00ff0000);
		}
	}

}
