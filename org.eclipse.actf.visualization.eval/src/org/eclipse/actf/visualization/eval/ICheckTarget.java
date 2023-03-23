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

package org.eclipse.actf.visualization.eval;

import org.w3c.dom.Document;

/**
 * Interface for evaluation target information
 */
public interface ICheckTarget {

	/**
	 * Get evaluation target {@link Document}
	 * 
	 * @return target {@link Document}
	 */
	Document getTargetDocument(); // for all

	/**
	 * Get evaluation target URL
	 * 
	 * @return target URL
	 */
	String getTargetUrl(); // for all

	/**
	 * Set additional {@link Document} for evaluation with key
	 * 
	 * @param key
	 *            key
	 * @param document
	 *            target {@link Document}
	 */
	void setAdditionalDocument(String key, Document document);

	/**
	 * Get additional {@link Document}
	 * 
	 * @param key
	 *            target key
	 * @return target {@link Document} associated with target key
	 */
	Document getAdditionalDocument(String key);

}
