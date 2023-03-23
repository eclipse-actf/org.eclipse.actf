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

package org.eclipse.actf.visualization.internal.eval;

import org.eclipse.actf.visualization.eval.IGuidelineItem;


public class GuidelineItemImpl implements IGuidelineItem {

    private String guidelineName;

    private String level;

    private String id;

    private String url;
    
    private boolean isEnabled = false;

    /**
     * @param id
     * @param guidelineName
     */
    public GuidelineItemImpl(String guidelineName) {
        this.guidelineName = guidelineName;
        this.id = ""; //$NON-NLS-1$
        this.level = ""; //$NON-NLS-1$
        this.url = ""; //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#getGuidelineName()
     */
    public String getGuidelineName() {
        return guidelineName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#getLevel()
     */
    public String getLevel() {
        return level;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#getUrl()
     */
    public String getUrl() {
        return url;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#setLevel(java.lang.String)
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineItem#setUrl(java.lang.String)
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return (guidelineName + ": " + id + ",\t" + level + ",\t" + url); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}
}
