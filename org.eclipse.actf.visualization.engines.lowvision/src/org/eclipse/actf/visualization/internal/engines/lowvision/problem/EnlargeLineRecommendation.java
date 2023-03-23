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

import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;

public class EnlargeLineRecommendation extends LowVisionRecommendation {
	public EnlargeLineRecommendation(ILowVisionProblem _prob)
			throws LowVisionProblemException {
		super(
				ENLARGE_LINE_RECOMMENDATION,
				_prob,
				Messages.EnlargeLineRecommendation_Enlarge_the_line_height__1);
	}
}
