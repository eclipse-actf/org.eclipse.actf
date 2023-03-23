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

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizationAttributeInfo;
import org.w3c.dom.Document;

public class VisualizeStyleInfo {

	private Set<String> importedCssSet = new HashSet<String>();

	private List<VisualizationAttributeInfo> origIdList; // original HTML
	// elements' ID

	private List<VisualizationAttributeInfo> accesskeyList; // accesskey

	private List<VisualizationAttributeInfo> classList; // class

	public VisualizeStyleInfo() {
		origIdList = new ArrayList<VisualizationAttributeInfo>();
		accesskeyList = new ArrayList<VisualizationAttributeInfo>();
		classList = new ArrayList<VisualizationAttributeInfo>();
	}

	/**
	 * 
	 */
	@SuppressWarnings("nls")
	public VisualizeStyleInfo(Document orig, IVisualizeMapData mapData) {
		origIdList = VisualizationAttributeInfo.listUp(orig, mapData, "id");
		accesskeyList = VisualizationAttributeInfo.listUp(orig, mapData,
				"accesskey");
		classList = VisualizationAttributeInfo.listUp(orig, mapData, "class");
	}

	/**
	 * @return
	 */
	public List<VisualizationAttributeInfo> getOrigIdList() {
		return origIdList;
	}

	/**
	 * @return
	 */
	public List<VisualizationAttributeInfo> getAccesskeyList() {
		return accesskeyList;
	}

	/**
	 * @return
	 */
	public List<VisualizationAttributeInfo> getClassList() {
		return classList;
	}

	/**
	 * @return Returns the importedCssSet.
	 */
	public Set<String> getImportedCssSet() {
		return importedCssSet;
	}

	/**
	 * @param importedCssSet
	 *            The importedCssSet to set.
	 */
	public void setImportedCssSet(Set<String> importedCssSet) {
		this.importedCssSet = importedCssSet;
	}
}
