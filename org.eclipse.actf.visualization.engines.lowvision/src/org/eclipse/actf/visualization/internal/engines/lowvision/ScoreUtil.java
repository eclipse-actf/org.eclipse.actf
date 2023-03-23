/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision;

import org.eclipse.actf.visualization.IVisualizationConst;



public class ScoreUtil {

	static final int VERY_GOOD = 500;

	static final int GOOD = 2000;

	static final int POOR = 3000;
        
	public static String getScoreString(int _score) {
		if (_score <= VERY_GOOD) {
			return (Messages.PageEvaluation_Excellent);
		} else if (_score <= GOOD) {
			return (Messages.PageEvaluation_Good);
		} else if (_score <= POOR) {
			return (Messages.PageEvaluation_Poor);
		} else {
			return (Messages.PageEvaluation_Bad);
		}

	}

	public static String getScoreImageString(int _score) {
		if (_score <= VERY_GOOD) {
			return (IVisualizationConst.RATING_V_GOOD );
		} else if (_score <= GOOD) {
			return (IVisualizationConst.RATING_GOOD );
		} else if (_score <= POOR) {
			return (IVisualizationConst.RATING_POOR );
		} else {
			return (IVisualizationConst.RATING_BAD);
		}
	}


}
