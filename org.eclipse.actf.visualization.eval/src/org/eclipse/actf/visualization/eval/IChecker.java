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

import java.util.List;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;

/**
 * Interface for content evaluator
 */
public interface IChecker {
	/**
	 * Check target content and return detected issues
	 * 
	 * @param checkTarget
	 *            target content information
	 * @return detected issues as list of {@link IProblemItem}
	 */
	List<IProblemItem> check(ICheckTarget checkTarget);

	/**
	 * Check if MIME type is supported by this checker
	 * 
	 * @param mimeType
	 *            target MIME type
	 * @return true, if target MIME type is supported
	 */
	boolean isTargetFormat(String mimeType);

	/**
	 * Check if evaluation item related to this checker is enabled by user
	 * 
	 * @return true, if evaluation item related to this checker is enabled
	 */
	boolean isEnabled();
}
