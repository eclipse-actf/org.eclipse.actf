/*******************************************************************************
 * Copyright (c) 2008, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	  Junji MAEDA - initial API and implementation
 * 	  IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.checker;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.DecisionMaker;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterMS;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSM;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSS;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Container;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Topology;
import org.eclipse.actf.visualization.internal.engines.lowvision.operator.LowVisionFilter;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.BlurProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemGroup;

public class CharacterChecker {

	// separated from PageImage

	/*
	 * Extract method for SM char is not perfect. (e.g., extract chars with
	 * shadow, etc...) So, set strict threshold.
	 */
	public static final double THRESHOLD_MIN_SM_COLOR_PROBLEM_RATIO = 0.8;

	private PageImage pageImage;

	public CharacterChecker(PageImage pageImage) {
		this.pageImage = pageImage;
	}

	private class CompareByPriority implements Comparator<LowVisionProblem> {
		public int compare(LowVisionProblem _o1, LowVisionProblem _o2) {
			return (_o2.getPriority() - _o1.getPriority());
		}
	}

	// handle PageImage as an image (without considering HTML char/image char)
	public LowVisionProblemGroup[] checkAllCharacters(LowVisionType _lvType)
			throws ImageException, LowVisionProblemException {

		if (_lvType.countTypes() == 0) {
			return (new LowVisionProblemGroup[0]);
		}

		Vector<LowVisionProblem> problemVec = new Vector<LowVisionProblem>();

		// container
		for (int k = 0; k < pageImage.getNumContainers(); k++) {

			Container cont = pageImage.getContainers()[k];
			Vector<LowVisionProblem> tmpProblemVec = new Vector<LowVisionProblem>();

			// Problem -> ProblemGroup
			for (int l = 0; l < cont.getNumSSCharacters(); l++) {
				LowVisionProblem prob = checkOneSSCharacter(cont
						.getSsCharacters()[l], _lvType);
				if (prob != null) {
					tmpProblemVec.addElement(prob);
				}
			}
			for (int l = 0; l < cont.getNumMSCharacters(); l++) {
				LowVisionProblem prob = checkOneMSCharacter(cont
						.getMsCharacters()[l], _lvType);
				if (prob != null) {
					tmpProblemVec.addElement(prob);
				}
			}
			for (int l = 0; l < cont.getNumSMCharacters(); l++) {
				LowVisionProblem prob = checkOneSMCharacter(cont
						.getSmCharacters()[l], _lvType);
				if (prob != null) {
					tmpProblemVec.addElement(prob);
				}
			}

			Vector<LowVisionProblemGroup> curProblemVec = collectProblems(tmpProblemVec);
			if (curProblemVec == null) {
				continue;
			}
			int curSize = curProblemVec.size();
			for (int m = 0; m < curSize; m++) {
				problemVec.addElement(curProblemVec.elementAt(m));
			}

			// /* generate problems for each character*/
			// for( int l=0; l<cont.numSSCharacters; l++ ){
			// Problem prob = checkOneSSCharacter( cont.ssCharacters[l], _lvType
			// );
			// if( prob != null ){
			// problemVec.addElement( prob );
			// }
			// }
			// for( int l=0; l<cont.numMSCharacters; l++ ){
			// Problem prob = checkOneMSCharacter( cont.msCharacters[l], _lvType
			// );
			// if( prob != null ){
			// problemVec.addElement( prob );
			// }
			// }
			// for( int l=0; l<cont.numSMCharacters; l++ ){
			// Problem prob = checkOneSMCharacter( cont.smCharacters[l], _lvType
			// );
			// if( prob != null ){
			// problemVec.addElement( prob );
			// }
			// }
		}

		// SM Character (outside container)
		Vector<LowVisionProblem> tmpProblemVec = new Vector<LowVisionProblem>();
		for (int k = 0; k < pageImage.getNumNonContainedCharacters(); k++) {
			LowVisionProblem prob = checkOneSMCharacter(pageImage
					.getNonContainedCharacters()[k], _lvType);
			if (prob != null) {
				tmpProblemVec.addElement(prob);
			}
		}
		Vector<LowVisionProblemGroup> curProblemVec = collectProblems(tmpProblemVec);
		if (curProblemVec != null) {
			int curSize = curProblemVec.size();
			for (int m = 0; m < curSize; m++) {
				problemVec.addElement(curProblemVec.elementAt(m));
			}
		}

		Collections.sort(problemVec, new CompareByPriority());
		LowVisionProblemGroup[] problemArray = null;
		int problemVecSize = problemVec.size();
		if (problemVecSize > 0) {
			problemArray = new LowVisionProblemGroup[problemVecSize];
			for (int k = 0; k < problemVecSize; k++) {
				problemArray[k] = (LowVisionProblemGroup) (problemVec
						.elementAt(k));
			}
			problemVec.removeAllElements();
			return (problemArray);
		} else {
			return (new LowVisionProblemGroup[0]);
		}
	}

	// Simulate and check (MS Char)
	private LowVisionProblem simulateAndCheckMSCharacter(CharacterMS _msc,
			LowVisionType _lvType, int _bg) throws ImageException,
			LowVisionProblemException {
		/* Color change-> already simulated & checked */

		if (!_lvType.doBlur()) {
			return (null);
		}

		// add margin (for Blur filter)
		int margin = 0;
		if (_lvType.doEyesight()) {
			margin = _lvType.getEyesightRadius();
		}
		IInt2D beforeI2d = _msc.makeMarginedImage(margin * 2);

		// simulation
		LowVisionFilter lvFilter = new LowVisionFilter(_lvType);
		Int2D afterI2d = null;
		try {
			afterI2d = new Int2D(lvFilter.filter(beforeI2d.toBufferedImage(),
					null));
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while simulating an MSCharacter."); //$NON-NLS-1$
		}

		/*
		 * Create BinaryImage from simulated image (with 1 margin) (cut most
		 * outside margin <- blacken by BlurOp)
		 */
		int afterWidth = afterI2d.getWidth() - 2 * margin;
		int afterHeight = afterI2d.getHeight() - 2 * margin;
		BinaryImage bin = new BinaryImage(afterWidth, afterHeight);
		HashMap<Integer, Boolean> answerMap = new HashMap<Integer, Boolean>();
		try {
			for (int j = 0; j < afterHeight; j++) {
				for (int i = 0; i < afterWidth; i++) {
					int pixel = afterI2d.getData()[j + margin][i + margin];
					if (pixel == _bg) {
						continue;
					} else {
						Integer pixelInt = new Integer(pixel);
						Boolean answerBool = answerMap.get(pixelInt);
						if (answerBool == null) {
							if (DecisionMaker.distinguishableTextColors(pixel,
									_bg)) {
								bin.data[j][i] = 1;
								answerMap.put(pixelInt, new Boolean(true));
							} else {
								answerMap.put(pixelInt, new Boolean(false));
							}
						} else {
							if (answerBool.booleanValue()) {
								bin.data[j][i] = 1;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while making binary image."); //$NON-NLS-1$
		}
		int ccLeft = _msc.cc.getLeft() - margin;
		if (ccLeft < 0)
			ccLeft = 0;
		int ccTop = _msc.cc.getTop() - margin;
		if (ccTop < 0)
			ccTop = 0;
		ConnectedComponent cc = new ConnectedComponent(ccLeft, ccTop, bin,
				ConnectedComponent.CONNECTIVITY_8);
		Topology simTopo = cc.thinning().calcTopology();
		if (simTopo.match(_msc.getTopology())) {
			return (null);
		} else {
			double probability = 1.0;
			if (_msc.getTopology().getCount() > 0) {
				probability = (double) (Math.abs(_msc.getTopology().getCount()
						- simTopo.getCount()))
						/ (double) (_msc.getTopology().getCount());
				if (probability > 1.0) {
					probability = 1.0;
				}
			}
			return (new BlurProblem(_msc, _lvType, probability));
		}
	}

	// Simulate and check (SM Char)
	private LowVisionProblem simulateAndCheckSMCharacter(CharacterSM _smc,
			LowVisionType _lvType, int _fg) throws ImageException,
			LowVisionProblemException {
		/* Color change-> already simulated & checked */

		if (!_lvType.doBlur()) {
			return (null);
		}

		// add margin (for Blur filter)
		int margin = 0;
		if (_lvType.doEyesight()) {
			margin = _lvType.getEyesightRadius();
		}
		IInt2D beforeI2d = _smc.makeMarginedImage(margin * 2);

		// simulation
		LowVisionFilter lvFilter = new LowVisionFilter(_lvType);
		Int2D afterI2d = null;
		try {
			afterI2d = new Int2D(lvFilter.filter(beforeI2d.toBufferedImage(),
					null));
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while simulating an SMCharacter."); //$NON-NLS-1$
		}

		/*
		 * Create BinaryImage from simulated image (with 1 margin) (cut most
		 * outside margin <- blacken by BlurOp)
		 */
		int afterWidth = afterI2d.getWidth() - 2 * margin;
		int afterHeight = afterI2d.getHeight() - 2 * margin;
		BinaryImage bin = new BinaryImage(afterWidth, afterHeight);
		HashMap<Integer, Boolean> answerMap = new HashMap<Integer, Boolean>();
		try {
			for (int j = 0; j < afterHeight; j++) {
				for (int i = 0; i < afterWidth; i++) {
					int pixel = afterI2d.getData()[j + margin][i + margin];
					if (pixel == _fg) {
						bin.data[j][i] = 1;
						continue;
					} else {
						Integer pixelInt = new Integer(pixel);
						Boolean answerBool = (answerMap.get(pixelInt));
						if (answerBool == null) {
							if (!(DecisionMaker.distinguishableTextColors(
									pixel, _fg))) {
								bin.data[j][i] = 1;
								answerMap.put(pixelInt, new Boolean(false));
							} else {
								answerMap.put(pixelInt, new Boolean(true));
							}
						} else {
							if (!(answerBool.booleanValue())) {
								bin.data[j][i] = 1;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while making binary image."); //$NON-NLS-1$
		}
		int ccLeft = _smc.cc.getLeft() - margin;
		if (ccLeft < 0)
			ccLeft = 0;
		int ccTop = _smc.cc.getTop() - margin;
		if (ccTop < 0)
			ccTop = 0;
		ConnectedComponent cc = new ConnectedComponent(ccLeft, ccTop, bin,
				ConnectedComponent.CONNECTIVITY_8);
		Topology simTopo = cc.thinning().calcTopology();
		if (simTopo.match(_smc.getTopology())) {
			return (null);
		} else {
			double probability = 1.0;
			if (_smc.getTopology().getCount() > 0) {
				probability = (double) (Math.abs(_smc.getTopology().getCount()
						- simTopo.getCount()))
						/ (double) (_smc.getTopology().getCount());
				if (probability > 1.0) {
					probability = 1.0;
				}
			}
			return (new BlurProblem(_smc, _lvType, probability));
		}
	}

	// Simulate and check (SS Char)
	private LowVisionProblem simulateAndCheckSSCharacter(CharacterSS _ssc,
			LowVisionType _lvType, int _bg) throws ImageException,
			LowVisionProblemException {

		// add margin
		int margin = 0;
		if (_lvType.doEyesight()) {
			margin = _lvType.getEyesightRadius();
		}
		IInt2D beforeI2d = _ssc.makeMarginedImage(margin * 2);

		// simulation
		LowVisionFilter lvFilter = new LowVisionFilter(_lvType);
		Int2D afterI2d = null;
		try {
			afterI2d = new Int2D(lvFilter.filter(beforeI2d.toBufferedImage(),
					null));
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while simulating an SSCharacter."); //$NON-NLS-1$
		}

		/*
		 * Create BinaryImage from simulated image (with 1 margin) (cut most
		 * outside margin <- blacken by BlurOp)
		 */
		int afterWidth = afterI2d.getWidth() - 2 * margin;
		int afterHeight = afterI2d.getHeight() - 2 * margin;
		BinaryImage bin = new BinaryImage(afterWidth, afterHeight);
		HashMap<Integer, Boolean> answerMap = new HashMap<Integer, Boolean>();
		try {
			for (int j = 0; j < afterHeight; j++) {
				for (int i = 0; i < afterWidth; i++) {
					int pixel = afterI2d.getData()[j + margin][i + margin];
					if (pixel == _bg) {
						continue;
					} else {
						Integer pixelInt = new Integer(pixel);
						Boolean answerBool = (answerMap.get(pixelInt));
						if (answerBool == null) {
							if (DecisionMaker.distinguishableTextColors(pixel,
									_bg)) {
								bin.data[j][i] = 1;
								answerMap.put(pixelInt, new Boolean(true));
							} else {
								answerMap.put(pixelInt, new Boolean(false));
							}
						} else if (answerBool.booleanValue()) {
							bin.data[j][i] = 1;
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ImageException(
					"Error occurred while making binary image."); //$NON-NLS-1$
		}
		int ccLeft = _ssc.cc.getLeft() - margin;
		if (ccLeft < 0)
			ccLeft = 0;
		int ccTop = _ssc.cc.getTop() - margin;
		if (ccTop < 0)
			ccTop = 0;
		ConnectedComponent cc = new ConnectedComponent(ccLeft, ccTop, bin,
				ConnectedComponent.CONNECTIVITY_8);
		Topology simTopo = cc.thinning().calcTopology();
		if (simTopo.match(_ssc.getTopology())) {
			return (null);
		} else {
			double probability = 1.0;
			if (_ssc.getTopology().getCount() > 0) {
				probability = (double) (Math.abs(_ssc.getTopology().getCount()
						- simTopo.getCount()))
						/ (double) (_ssc.getTopology().getCount());
				if (probability > 1.0) {
					probability = 1.0;
				}
			}
			return (new BlurProblem(_ssc, _lvType, probability));
		}
	}

	private LowVisionProblem checkOneMSCharacter(CharacterMS _msc,
			LowVisionType _lvType) throws ImageException,
			LowVisionProblemException {
		int simBgColor = _msc.getBackgroundColor();
		// Int2D i2d = _msc.getInt2D();

		if (_lvType.doChangeColors()) {
			try {
				simBgColor = _lvType.convertColor(_msc.getBackgroundColor());
				int simFgColor = _lvType
						.convertColor(_msc.getForegroundColor());

				double sev = W3CColorChecker.calcSeverity(new ColorIRGB(
						simFgColor), new ColorIRGB(simBgColor));
				if (sev > 0.0) {
					return (new ColorProblem(_msc, _lvType, sev));
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		if (_lvType.doBlur()) {
			return (simulateAndCheckMSCharacter(_msc, _lvType, simBgColor));
		}
		return (null);
	}

	private LowVisionProblem checkOneSMCharacter(CharacterSM _smc,
			LowVisionType _lvType) throws ImageException,
			LowVisionProblemException {
		int simFgColor = _smc.getForegroundColor();
		// Int2D i2d = _smc.getInt2D();

		if (_lvType.doChangeColors()) {
			try {
				simFgColor = _lvType.convertColor(_smc.getForegroundColor());

				// count bad bg pixels
				int badCount = 0;
				int w = _smc.getWidth();
				int h = _smc.getHeight();
				int[][] im = _smc.getImage();
				byte[][] data = _smc.cc.getShape().getData();
				HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
				for (int j = 0; j < h; j++) {
					for (int i = 0; i < w; i++) {
						if (data[j][i] == 0) {
							int simBg = _lvType.convertColor(im[j][i]);
							Integer bgInt = new Integer(simBg);
							Boolean bgBool = (map.get(bgInt));
							if (bgBool == null) { // first time
								if (!(DecisionMaker.distinguishableTextColors(
										simFgColor, simBg))) {
									badCount++;
									map.put(bgInt, new Boolean(false));
								} else {
									map.put(bgInt, new Boolean(true));
								}
							} else {
								if (!(bgBool.booleanValue())) {
									badCount++;
								}
							}
						}
					}
				}

				double badRatio = (double) badCount
						/ (double) (w * h - _smc.cc.getCount());
				if (badRatio >= CharacterChecker.THRESHOLD_MIN_SM_COLOR_PROBLEM_RATIO) {
					double probability = (badRatio - CharacterChecker.THRESHOLD_MIN_SM_COLOR_PROBLEM_RATIO)
							/ (1.0 - CharacterChecker.THRESHOLD_MIN_SM_COLOR_PROBLEM_RATIO);
					return (new ColorProblem(_smc, _lvType, probability));
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		if (_lvType.doBlur()) {
			return (simulateAndCheckSMCharacter(_smc, _lvType, simFgColor));
		}
		return (null);
	}

	private LowVisionProblem checkOneSSCharacter(CharacterSS _ssc,
			LowVisionType _lvType) throws ImageException,
			LowVisionProblemException {
		int simBgColor = _ssc.getBackgroundColor();

		if (_lvType.doChangeColors()) {
			try {
				int simFgColor = _lvType
						.convertColor(_ssc.getForegroundColor());
				simBgColor = _lvType.convertColor(_ssc.getBackgroundColor());

				double sev = W3CColorChecker.calcSeverity(new ColorIRGB(
						simFgColor), new ColorIRGB(simBgColor));
				if (sev > 0.0) {
					return (new ColorProblem(_ssc, _lvType, sev));
				}
			} catch (Exception e) {
				// e.printStackTrace();
				throw new ImageException(
						"An error occurred while checking colors of an SSCharacter."); //$NON-NLS-1$
			}
		}

		if (_lvType.doBlur()) {
			return (simulateAndCheckSSCharacter(_ssc, _lvType, simBgColor));
		}
		return (null);
	}

	/* problem grouping */
	private Vector<LowVisionProblemGroup> collectProblems(
			Vector<LowVisionProblem> _tmpVec) throws ImageException {
		int size = _tmpVec.size();
		if (size == 0) {
			return (null);
		}
		int[] idMap = new int[size];
		int id = 1;
		for (int i = 0; i < size; i++) {
			LowVisionProblem curProb = _tmpVec.elementAt(i);
			if (idMap[i] == 0) { // not yet
				idMap[i] = id;
				assignProblemGroupID(curProb, id, _tmpVec, size, idMap);
				id++;
			}
		}

		Vector<LowVisionProblemGroup> resultVec = new Vector<LowVisionProblemGroup>();
		for (int i = 1; i < id; i++) {
			makeProblemGroupByID(resultVec, i, _tmpVec, size, idMap);
		}

		return (resultVec);
	}

	private void assignProblemGroupID(LowVisionProblem _curProb, int _id,
			Vector<LowVisionProblem> _tmpVec, int _size, int[] _idMap)
			throws ImageException {
		try {
			Stack<LowVisionProblem> searchStack = new Stack<LowVisionProblem>();
			searchStack.push(_curProb);
			while (!searchStack.empty()) {
				ILowVisionProblem sProb = searchStack.pop();
				for (int i = 0; i < _size; i++) {
					if (_idMap[i] == 0) {
						LowVisionProblem tmpProb = _tmpVec.elementAt(i);
						if (DecisionMaker.areSameGroupProblems(sProb, tmpProb)) {
							_idMap[i] = _id;
							searchStack.push(tmpProb);
						}
					}
				}
			}
		} catch (LowVisionProblemException lvpe) {
			// lvpe.printStackTrace();
			throw new ImageException(
					"Error occurred while making problem group."); //$NON-NLS-1$
		}
	}

	private void makeProblemGroupByID(Vector<LowVisionProblemGroup> _resultVec,
			int _id, Vector<LowVisionProblem> _tmpVec, int _size, int[] _idMap)
			throws ImageException {
		Vector<ILowVisionProblem> groupVector = new Vector<ILowVisionProblem>();

		for (int i = 0; i < _size; i++) {
			if (_idMap[i] == _id) {
				LowVisionProblem curProb = _tmpVec.elementAt(i);
				groupVector.addElement(curProb);
			}
		}
		int groupSize = groupVector.size();
		if (groupSize <= 0) {
			throw new ImageException("No instance belongs to the group. id = " //$NON-NLS-1$
					+ _id);
		}
		LowVisionProblemGroup pg = null;
		try {
			pg = new LowVisionProblemGroup(groupVector);
		} catch (LowVisionProblemException lvpe) {
			// lvpe.printStackTrace();
			throw new ImageException(
					"LowVisionProblemGroup cannot be constracted."); //$NON-NLS-1$
		}
		_resultVec.addElement(pg);
	}

}
