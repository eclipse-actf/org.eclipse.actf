/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report.action;

import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.program.Program;



public class ShowTechniquesAction extends Action {

    private ITechniquesItem targetItem;

    /**
     * 
     */
    public ShowTechniquesAction(ITechniquesItem targetItem) {
        super(targetItem.getGuidelineName() + ": " + targetItem.getId()); //$NON-NLS-1$
        this.targetItem = targetItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {

        // launch default web browser
        Program program = Program.findProgram(".html"); //$NON-NLS-1$
        if (null != program) {
            program.execute(targetItem.getUrl());
        }
    }

}
