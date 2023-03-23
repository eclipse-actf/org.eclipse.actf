/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.util;

import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;

/**
 * Utility class for mapping information between HTML IDs and line number of
 * source HTML. This class uses ACTF_ID to create the mapping information.
 */
public class Id2LineViaActfId {
	private Map<Integer, Integer> id2ActfId;

	private Vector<Html2ViewMapData> html2ViewMapDataV;

	// TODO
	private boolean is1base = true;

	/**
	 * Constructor of the class.
	 * 
	 * @param id2AccId
	 *            mapping information between ID and ACTF_ID
	 * @param html2ViewMapDataV
	 *            mapping information between ACTF_ID and line number. ACTF_ID
	 *            is used as index of Vector.
	 */
	public Id2LineViaActfId(Map<Integer, Integer> id2AccId,
			Vector<Html2ViewMapData> html2ViewMapDataV) {
		this(id2AccId, html2ViewMapDataV, true);
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param id2AccId
	 *            mapping information between ID and ACTF_ID
	 * @param html2ViewMapDataV
	 *            mapping information between ACTF_ID and line number. ACTF_ID
	 *            is used as index of Vector.
	 * @param is1base
	 *            true if line number starts from 1<br>
	 *            false if line number starts from 0
	 */
	public Id2LineViaActfId(Map<Integer, Integer> id2AccId,
			Vector<Html2ViewMapData> html2ViewMapDataV, boolean is1base) {
		this.id2ActfId = id2AccId;
		this.html2ViewMapDataV = html2ViewMapDataV;
		this.is1base = is1base;
	}

	/**
	 * Get corresponding line number from ID
	 * 
	 * @param nodeId
	 *            target ID
	 * @return line number
	 */
	public int getLine(int nodeId) {
		int result = -1;
		Integer id = new Integer(nodeId);

		if (id2ActfId.containsKey(id)) {
			int accId = id2ActfId.get(id).intValue();
			if (accId > -1 && accId < html2ViewMapDataV.size()) {
				Html2ViewMapData tmpData = html2ViewMapDataV.get(accId);
				result = tmpData.getStartLine();// ? +1 ?
			}
		}

		return (result);
	}

	/**
	 * Get corresponding {@link Html2ViewMapData} of target ID
	 * 
	 * @param nodeId
	 *            target ID
	 * @return corresponding {@link Html2ViewMapData}
	 */
	public Html2ViewMapData getViewMapData(int nodeId) {
		return (getViewMapData(new Integer(nodeId)));
	}

	/**
	 * Get corresponding {@link Html2ViewMapData} of target ID
	 * 
	 * @param nodeId
	 *            target ID
	 * @return corresponding {@link Html2ViewMapData}
	 */
	public Html2ViewMapData getViewMapData(Integer nodeId) {
		Html2ViewMapData result = null;
		if (id2ActfId.containsKey(nodeId)) {
			int accId = id2ActfId.get(nodeId).intValue();
			if (accId > -1 && accId < html2ViewMapDataV.size()) {
				result = html2ViewMapDataV.get(accId);
			}
		}
		return (result);
	}

	/**
	 * @return true if line number starts from 1
	 */
	public boolean is1base() {
		return is1base;
	}

}
