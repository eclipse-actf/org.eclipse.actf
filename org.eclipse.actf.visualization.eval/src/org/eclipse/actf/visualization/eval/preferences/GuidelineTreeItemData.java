/*******************************************************************************
 * Copyright (c) 2006, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.swt.graphics.Image;




public class GuidelineTreeItemData implements IGuidelineTreeItem {

    private static final Image ICON_OASIS = EvaluationPlugin.getImageDescriptor("icons/media/oasis.png").createImage();

	private static final Image ICON_IBM = EvaluationPlugin.getImageDescriptor("icons/media/ibm.png").createImage();

	private static final Image ICON_JSA = EvaluationPlugin.getImageDescriptor("icons/media/jsa.png").createImage();

	private static final Image ICON_508 = EvaluationPlugin.getImageDescriptor("icons/media/508.png").createImage();

	private static final Image ICON_W3C = EvaluationPlugin.getImageDescriptor("icons/media/w3c.png").createImage();

	private static final int GUIDELINE_WCAG_P1 = 30;

    private static final int GUIDELINE_WCAG_P2 = 31;

    private static final int GUIDELINE_WCAG_P3 = 32;
    
    private static final int GUIDELINE_SECTION508 = 33;

    private static final int GUIDELINE_JIS = 34;

    private static final int GUIDELINE_IBMGUIDELINE = 35;

    private static final int GUIDELINE_IBMW3V8 = 36;

    private static final int GUIDELINE_ODF = 37;

    private static final int GUIDELINE_PII = 38;

    private static final int GUIDELINE_WACAG_2 = 50;

    
    private int _guideline = -1;

    private IGuidelineTreeItem _parent = null;

    private List<IGuidelineTreeItem> _children = null;

    private boolean _isEnabled = false;
    
    private boolean[] _correspondingCriteria;

    private int _index;
    
    private String _name = "";     //$NON-NLS-1$
    
    private String _category = ""; //$NON-NLS-1$

    private String _description = ""; //$NON-NLS-1$
    
    //TODO use extension (CheckerInfoProvider)
    
    public GuidelineTreeItemData(String name) {
        setName(name);
        this._children = new ArrayList<IGuidelineTreeItem>();
    }

    public IGuidelineTreeItem getParent() {
        return this._parent;
    }

    public void add(IGuidelineTreeItem guidelineTreeItem) {
        this._children.add(guidelineTreeItem);
        this._parent = this;
    }

    public List<IGuidelineTreeItem> getChildren() {
        return this._children;
    }

    private int getGuideline() {
        return this._guideline;
    }

    @SuppressWarnings("nls")
	public void setName(String name) {
        this._name = name;

        if (null != this._name) {
            if (this._name.equals("WCAG (P1)")) {
                this._guideline = GUIDELINE_WCAG_P1;
            } else if (this._name.equals("WCAG (P2)")) {
                this._guideline = GUIDELINE_WCAG_P2;
            } else if (this._name.equals("WCAG (P3)")) {
                this._guideline = GUIDELINE_WCAG_P3;
            } else if (this._name.equals("Section508")) {
                this._guideline = GUIDELINE_SECTION508;
            } else if (this._name.startsWith("JIS")) {
                this._guideline = GUIDELINE_JIS;
            } else if (this._name.equals("IBMGuideline")) {
                this._guideline = GUIDELINE_IBMGUIDELINE;
            } else if (this._name.equals("IBM w3 v8")) {
                this._guideline = GUIDELINE_IBMW3V8;
            } else if (this._name.equals("ODF Guide")) {
                this._guideline = GUIDELINE_ODF;
            } else if (this._name.equals("PII")) {
                this._guideline = GUIDELINE_PII;
            } else if (this._name.startsWith("WCAG 2.0")){
            	this._guideline = GUIDELINE_WACAG_2;
            }
        }
    }
    

    @SuppressWarnings("nls")
	public Image getGuidelineImage() {

        switch (getGuideline()) {
        case GUIDELINE_WCAG_P1:
        case GUIDELINE_WCAG_P2:
        case GUIDELINE_WCAG_P3:
        case GUIDELINE_WACAG_2:
            return ICON_W3C;
        case GUIDELINE_SECTION508:
            return ICON_508;
        case GUIDELINE_JIS:
            return ICON_JSA;
        case GUIDELINE_IBMGUIDELINE:
        case GUIDELINE_IBMW3V8:
        case GUIDELINE_PII:
            return ICON_IBM;
        case GUIDELINE_ODF:
            return ICON_OASIS;
        }

        return null;
    }

    public void setEnabled(boolean isEnabled) {
        this._isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return this._isEnabled;
    }

    public void setCorrespondingCriteria(boolean[] correspondingCriteria) {
        this._correspondingCriteria = correspondingCriteria;
    }

    public boolean[] getCorrespondingCriteria() {
        return this._correspondingCriteria;
    }

    public void setIndex(int index) {
        this._index = index;
    }

    public int getIndex() {
        return this._index;
    }

    public void setCategory(String category) {
        this._category = category;
    }

    public String getCategory() {
        return this._category;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getDescription() {
        return this._description;
    }
}
