/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml.modelgroup;

import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;
import org.w3c.dom.Node;


public interface IModelGroup {
	/**
	 * Checks if <code>child</code> matches to <code>parent</code>. And as
	 * a side effect, adds child to parent and sets parsers context to it if it
	 * matches.
	 * 
	 * @param parser
	 * @param parent
	 * @param child
	 *            node to be tested by this method
	 * @return true if <code>child</code> matches to <code>parent</code>
	 */
	boolean match(ISGMLParser parser, Node parent, Node child);

	public boolean optional();

	public void refer(boolean infinite);

	boolean match(int number);

	boolean[] rehash(int totalSize);
	
}
