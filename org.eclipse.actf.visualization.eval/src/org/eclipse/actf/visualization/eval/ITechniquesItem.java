/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval;

/**
 * Interface to hold Techniques item information
 */
public interface ITechniquesItem extends Comparable<ITechniquesItem> {

	/**
	 * @return name of guideline
	 */
	public abstract String getGuidelineName();

	/**
	 * @return ID of Techniques item (e.g., H1, G1, etc.)
	 */
	public abstract String getId();

	/**
	 * @return URL of public introduction page of Techniques item
	 */
	public abstract String getUrl();

}
