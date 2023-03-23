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
import org.eclipse.actf.visualization.eval.problem.ILowvisionProblemSubtype;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;
import org.w3c.dom.Element;

public interface ILowVisionProblem extends ILowvisionProblemSubtype{

	int UNSET_POSITION = -1;
	int DEFAULT_PRIORITY = 0;
	//061024
	short LOWVISION_PROBLEM = 0;

	short getType();

	LowVisionType getLowVisionType();

	// LowVision Error type (Color, Blur, etc.)
	int getLowVisionProblemType();

	String getDescription() throws LowVisionProblemException;

	IPageImage getPageImage();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	int getPriority();

	double getProbability();

	int getIntProbability();

	double getCharacterScore();

	LowVisionRecommendation[] getRecommendations();

	boolean isGroup();

	short getComponentType() throws LowVisionProblemException;

	PageComponent getPageComponent() throws LowVisionProblemException;

	PageElement getPageElement();

	String toString();

	void dump(PrintStream _ps, boolean _doRecommendations) throws LowVisionProblemException;

	void dump(PrintWriter _pw, boolean _doRecommendations) throws LowVisionProblemException;

	void drawSurroundingBox(Int2D _img);

	Element getElement();

	void setElement(Element element);

}