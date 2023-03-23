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


public class MetricsItem {
    private String metricsName = ""; //$NON-NLS-1$
    
    private int score = 0;
    
    /**
     * @param metrics
     * @param score
     */
    public MetricsItem(String metricsName, int score) {
        this.metricsName = metricsName;
        this.score = score;
    }
    
    /**
     * @param metrics
     * @param score
     */
    public MetricsItem(String metricsName, String scoreStr) {
        this.metricsName = metricsName;
        try{
            int tmpScore = Integer.parseInt(scoreStr);
            if(tmpScore>-1&&tmpScore<=100){
                this.score = tmpScore;
            }
        }catch(Exception e){            
        }
    }
    
    
    
    public String getMetricsName() {
        return metricsName;
    }
    public void setMetricsName(String metricsName) {
        this.metricsName = metricsName;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    
    public String toString(){
        return(metricsName+" : "+score); //$NON-NLS-1$
    }
    
}
