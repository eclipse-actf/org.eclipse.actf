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

package org.eclipse.actf.visualization.internal.ui.report.action;

import java.util.List;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.ReportMessageDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;


public class ShowDescriptionAction extends Action {

    private TableViewer tableViewer;
    
    private IProblemItem curItem;
    
    /**
     * 
     */
    public ShowDescriptionAction(TableViewer tableViewer) {
        super(Messages.ProblemTable_6);
        this.tableViewer = tableViewer;
        
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent arg0) {
				@SuppressWarnings("rawtypes")
				List tmpList = ((IStructuredSelection) arg0.getSelection()).toList();
                if(tmpList==null||tmpList.size()>1||tmpList.size()==0){
                    setIProblemItem(null);
                }else{
                    try{
                        setIProblemItem((IProblemItem)tmpList.get(0));
                    }catch(Exception e){
                        setIProblemItem(null);
                    }
                }
            }
        });
        
        this.setEnabled(false);
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
    	Shell shell = tableViewer.getControl().getShell();
    	ReportMessageDialog.openReportMEssageDialog(shell, curItem);
    }
    
    public void setIProblemItem(IProblemItem target){
        if(target==null){
            this.setEnabled(false);
        }else{
            this.setEnabled(true);
            curItem = target;
        }        
    }
    
}
