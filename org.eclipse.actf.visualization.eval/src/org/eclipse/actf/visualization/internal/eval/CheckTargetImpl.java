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

import java.util.HashMap;

import org.eclipse.actf.visualization.eval.ICheckTarget;
import org.w3c.dom.Document;

/**
 * Default implementation of {@link ICheckTarget}
 */
public class CheckTargetImpl implements ICheckTarget {

	private HashMap<String, Document> documentMap = new HashMap<String, Document>();

	private Document target;

	private String targetUrl;

	/**
	 * Constructor of the class
	 * 
	 * @param target
	 *            evaluation target {@link Document}
	 * @param targetUrl
	 *            target URL
	 */
	public CheckTargetImpl(Document target, String targetUrl) {
		this.target = target;
		this.targetUrl = targetUrl;
	}

	public Document getTargetDocument() {
		return target;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setAdditionalDocument(String key, Document document) {
		documentMap.put(key, document);
	}

	public Document getAdditionalDocument(String key) {
		if (documentMap.containsKey(key)) {
			return documentMap.get(key);
		}
		return null;
	}

}
