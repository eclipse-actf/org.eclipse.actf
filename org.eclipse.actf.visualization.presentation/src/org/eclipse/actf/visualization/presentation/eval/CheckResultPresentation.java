/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.presentation.eval;

import org.eclipse.actf.visualization.eval.EvaluationResultImpl;


public class CheckResultPresentation extends EvaluationResultImpl {

    //serial number
	
	public CheckResultPresentation() {
        setSummaryReportUrl("about:blank"); //$NON-NLS-1$
        setShowAllGuidelineItems(true);
    }
    
}
