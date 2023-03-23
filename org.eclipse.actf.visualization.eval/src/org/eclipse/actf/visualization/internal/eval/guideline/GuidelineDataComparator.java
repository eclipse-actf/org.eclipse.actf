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

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.util.Comparator;




public class GuidelineDataComparator implements Comparator<GuidelineData> {

    public int compare(GuidelineData o1, GuidelineData o2) {
        int result = 0;
        try{
            result = o1.getId() - o2.getId();
            
            if(result==0){
                return o1.getGuidelineName().compareTo(o2.getGuidelineName());
            }
            return result;
        }catch (Exception e){
            return(result);            
        }
    }

}
