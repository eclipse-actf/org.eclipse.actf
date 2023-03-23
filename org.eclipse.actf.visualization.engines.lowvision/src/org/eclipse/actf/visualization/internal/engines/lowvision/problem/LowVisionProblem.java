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

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;
import org.w3c.dom.Element;

public abstract class LowVisionProblem implements ILowVisionProblem {
	IPageImage pageImage = null;
	LowVisionType lowVisionType = null;
	int problemType;
	short componentType = PageComponent.UNDEFINED_TYPE;
	PageComponent pageComponent = null;
	PageElement pageElement = null;
	String description;
	int left = UNSET_POSITION;
	int top = UNSET_POSITION;
	int width = 0;
	int height = 0;
	int priority;

	double probability = 0.0; //
	double characterScore = 0.0; //
	// int numRecommendations = 0; // recommendations.length;
	LowVisionRecommendation[] recommendations = null;
	boolean isGroupFlag = false; // is LowVisionProblemGroup?

	Element element;

	protected LowVisionProblem() {
	}

	public LowVisionProblem(int _type, LowVisionType _lvType, String _description, PageComponent _com, double _proba)
			throws LowVisionProblemException {
		problemType = _type;
		lowVisionType = _lvType;
		description = _description;
		pageComponent = _com;
		componentType = pageComponent.getType();
		pageImage = pageComponent.getPageImage();
		ConnectedComponent cc = pageComponent.getConnectedComponent();
		if (cc != null) {
			left = cc.getLeft();
			top = cc.getTop();
			width = cc.getWidth();
			height = cc.getHeight();
		}
		setPriority();
		probability = _proba;
		characterScore = probability * width * height;
	}

	public LowVisionProblem(int _type, LowVisionType _lvType, String _description, PageElement _pe, double _proba) {
		problemType = _type;
		lowVisionType = _lvType;
		description = _description;
		pageElement = _pe;
		if (pageElement != null) {
			left = pageElement.getX();
			top = pageElement.getY();
			width = pageElement.getWidth();
			height = pageElement.getHeight();
		}
		setPriority();
		probability = _proba;
	}

	private void setPriority() {
		if (left == UNSET_POSITION || top == UNSET_POSITION) {
			priority = DEFAULT_PRIORITY;
		} else {
			priority = 0x7fffffff - top * 0xffff - left;
		}
		/*
		 * PageImage pi = component.getPageImage(); if( pi != null ){ int
		 * pageWidth = pi.getWidth(); int pageHeight = pi.getHeight(); priority
		 * = pageWidth*pageHeight - top*pageWidth - left; } else{ priority =
		 * DEFAULT_PRIORITY; }
		 */
	}

	protected abstract void setRecommendations() throws LowVisionProblemException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getType()
	 */
	public short getType() {
		return (LOWVISION_PROBLEM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getLowVisionType()
	 */
	public LowVisionType getLowVisionType() {
		return (lowVisionType);
	}

	// LowVision Error type (Color, Blur, etc.)
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getLowVisionProblemType()
	 */
	public int getLowVisionProblemType() {
		return (problemType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getDescription()
	 */
	public String getDescription() throws LowVisionProblemException {
		return (description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getPageImage()
	 */
	public IPageImage getPageImage() {
		return (pageImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getX()
	 */
	public int getX() {
		return (left);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getY()
	 */
	public int getY() {
		return (top);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getWidth()
	 */
	public int getWidth() {
		return (width);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getHeight()
	 */
	public int getHeight() {
		return (height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getPriority()
	 */
	public int getPriority() {
		return (priority);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getProbability()
	 */
	public double getProbability() {
		return (probability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getIntProbability()
	 */
	public int getIntProbability() {
		return ((int) (Math.rint(probability * 100.0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getCharacterScore()
	 */
	public double getCharacterScore() {
		return (characterScore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getRecommendations()
	 */
	public LowVisionRecommendation[] getRecommendations() {
		return (recommendations);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#isGroup()
	 */
	public boolean isGroup() {
		return (isGroupFlag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getComponentType()
	 */
	public short getComponentType() throws LowVisionProblemException {
		if (!isGroupFlag) {
			return (componentType);
		} else {
			throw new LowVisionProblemException("componentType cannot be gotten from a ProblemGroup."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getPageComponent()
	 */
	public PageComponent getPageComponent() throws LowVisionProblemException {
		if (!isGroupFlag) {
			return (pageComponent);
		} else {
			throw new LowVisionProblemException("component cannot be gotten from a ProblemGroup."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getPageElement()
	 */
	public PageElement getPageElement() {
		return (pageElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#toString()
	 */
	@SuppressWarnings("nls")
	public String toString() {
		String compTypeString = null;
		if (componentType == PageComponent.SS_CHARACTER_TYPE) {
			compTypeString = "(SS)";
		} else if (componentType == PageComponent.MS_CHARACTER_TYPE) {
			compTypeString = "(MS)";
		} else if (componentType == PageComponent.SM_CHARACTER_TYPE) {
			compTypeString = "(SM)";
		} else {
			compTypeString = "" + componentType;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("Description=" + description);
		sb.append(compTypeString);
		sb.append(", ");
		sb.append("(x,y)=(" + left + "," + top + ")");
		sb.append(", ");
		sb.append("[WIDTH x HEIGHT]=[" + width + " x " + height + "]");
		sb.append(", ");
		sb.append("Probability=" + (int) (Math.rint(probability * 100.0)));
		sb.append(", ");
		sb.append("#Recommendations=" + recommendations.length);
		return (sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#dump(java.io.PrintStream, boolean)
	 */
	public void dump(PrintStream _ps, boolean _doRecommendations) throws LowVisionProblemException {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw, _doRecommendations);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#dump(java.io.PrintWriter, boolean)
	 */
	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw, boolean _doRecommendations) throws LowVisionProblemException {
		_pw.println("----------");
		_pw.println("dumping a problem");
		_pw.println("problemType = " + problemType);
		_pw.println("componentType = " + componentType);
		_pw.println("description = " + getDescription());
		_pw.println("(x,y) = ( " + getX() + ", " + getY() + ")");
		_pw.println("width, height = " + getWidth() + ", " + getHeight());
		_pw.println("priority = " + getPriority());
		LowVisionRecommendation[] recs = getRecommendations();
		if (recs != null) {
			_pw.println("# of Recommendations = " + recs.length);
		} else {
			_pw.println("Recommendations are null.");
		}
		if (_doRecommendations && recs != null) {
			for (int i = 0; i < recs.length; i++) {
				_pw.println("Recommendation #" + i);
				recs[i].dump(_pw);
			}
		}
		_pw.println("----------");
	}

	private static final int[] PROBLEM_COLORS = { 0x00ffffff, 0x00ff0000, 0x0000ff00 };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#drawSurroundingBox(org.eclipse.actf.visualization.
	 * internal.engines.lowvision.image.Int2D)
	 */
	public void drawSurroundingBox(Int2D _img) {
		int x0 = getX();
		int y0 = getY();
		int x1 = x0 + getWidth();
		int y1 = y0 + getHeight();
		int color = PROBLEM_COLORS[problemType];
		for (int i = x0; i < x1; i++) {
			_img.getData()[y0][i] = color;
			_img.getData()[y1 - 1][i] = color;
		}
		for (int j = y0; j < y1; j++) {
			_img.getData()[j][x0] = color;
			_img.getData()[j][x1 - 1] = color;
		}
	}

	public static void drawAllSurroundingBoxes(ILowVisionProblem[] _problems, Int2D _img) {
		for (int k = 0; k < _problems.length; k++) {
			_problems[k].drawSurroundingBox(_img);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#getElement()
	 */
	public Element getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.internal.engines.lowvision.problem.
	 * ILowVisionProblem#setElement(org.w3c.dom.Element)
	 */
	public void setElement(Element element) {
		this.element = element;
	}
}
