/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.eval;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;



public class CheckResultLowVision extends EvaluationResultImpl {
	
    //serial number
	private int count = 0;
		
    public CheckResultLowVision() {
        setSummaryReportUrl("about:blank"); //$NON-NLS-1$
        setShowAllGuidelineItems(true);
    }
    
    public void setFrameOffsetToProblems(IPageImage[] framePageImages){
        int frameOffset[];

        if (null != framePageImages) {

            frameOffset = new int[framePageImages.length];
            frameOffset[0] = 0;

            for (int i = 1; i < framePageImages.length; i++) {
                frameOffset[i] = frameOffset[i - 1] + framePageImages[i - 1].getHeight();
            }

            for (Iterator<IProblemItem> i = getProblemList().iterator(); i.hasNext();) {
                try {
                	IProblemItemImage tmpP = (IProblemItemImage) i.next();
                    int frameId = tmpP.getFrameId();
                    if (frameId > -1 && frameId < frameOffset.length) {
                        tmpP.setFrameOffset(frameOffset[frameId]);
                        tmpP.setY(tmpP.getY() + frameOffset[frameId]);
                    }
                } catch (Exception e) {
                }
            }
        }
        
//        for (Iterator<IProblemItem> i = getProblemList().iterator(); i.hasNext();) {
//            ProblemItemLV tmpP = (ProblemItemLV) i.next();
//            System.out.println(tmpP.getFrameId()+":"+tmpP.getFrameOffset());
//        }
    }

	public void addProblemItems(Collection<IProblemItem> c) {
		stripProblem(c);
		super.addProblemItems(c);
	}

	public void setProblemList(List<IProblemItem> problemList) {
		count = 0;
		stripProblem(problemList);
		super.setProblemList(problemList);
	}
    
	private void stripProblem(Collection<IProblemItem> c) {
		GuidelineHolder holder = GuidelineHolder.getInstance();
		for (Iterator<IProblemItem> i = c.iterator(); i.hasNext();) {
			try {
				IProblemItem tmpItem = i.next();
				if (holder.isMatchedCheckItem(tmpItem.getEvaluationItem())) {
					tmpItem.setSerialNumber(count);
					count++;
				} else {
					i.remove();
				}
			} catch (Exception e) {
				i.remove();
			}
		}
	}
    
    
    
}
