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
package org.eclipse.actf.visualization.internal.ui.report.table;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;



public class ResultTableFilter extends ViewerFilter {

    public static final int ALL = Integer.MAX_VALUE;
    
    private int severity = ALL;

    /**
     *  
     */
    public ResultTableFilter() {
        super();
    }

    public void setSeverity(int type) {
        this.severity = type;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
     *      java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer arg0, Object arg1, Object arg2) {
        try {
            IProblemItem tmpItem = (IProblemItem) arg2;
            if((severity&tmpItem.getSeverity())>0){
                return true;
            }            
        } catch (Exception e) {
        }
        return false;
    }

}
