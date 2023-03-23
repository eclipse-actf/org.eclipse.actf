/*******************************************************************************
 * Copyright (c) 2007 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

/**
 * Visitor class can be used to visit each {@link IProblemItem} and export
 * problem list, filter some problem items, etc.
 * 
 */
public interface IProblemItemVisitor {

	/**
	 * Visit {@link IProblemItem} and do something.
	 * 
	 * @param item
	 *            target {@link IProblemItem} to visit
	 */
	public void visit(IProblemItem item);
}
