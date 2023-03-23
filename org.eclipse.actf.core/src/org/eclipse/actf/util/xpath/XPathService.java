/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.xpath;

import org.eclipse.actf.util.dom.EmptyNodeListImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class for XPath evaluation
 */
public abstract class XPathService {

	/**
	 * Compile an XPath expression for later evaluation.
	 * 
	 * @param path
	 *            the target XPath expression
	 * @return compiled XPath expression
	 */
	public abstract Object compile(String path);

	/**
	 * Evaluate the compiled XPath expression in the specified context and
	 * return the result as NodeList.
	 * 
	 * @param compiled
	 *            the compiled XPath expression
	 * @param ctx
	 *            the target context
	 * @return evaluation result as NodeList
	 */
	public abstract NodeList evalForNodeList(Object compiled, Node ctx);

	/**
	 * Evaluate the compiled XPath expression in the specified context and
	 * return the result as String.
	 * 
	 * @param compiled
	 *            the compiled XPath expression
	 * @param ctx
	 *            the target context
	 * @return evaluation result as String
	 */
	public abstract String evalForString(Object compiled, Node ctx);

	/**
	 * Evaluate the XPath expression in the specified context and return the
	 * result as NodeList.
	 * 
	 * @param path
	 *            the target XPath expression
	 * @param ctx
	 *            the target context
	 * @return evaluation result as NodeList
	 */
	public NodeList evalPathForNodeList(String path, Node ctx) {
		Object compiled = compile(path);
		if (compiled == null)
			return EmptyNodeListImpl.getInstance();
		return evalForNodeList(compiled, ctx);
	}

	/**
	 * Evaluate the XPath expression in the specified context and return the
	 * result as NodeList.
	 * 
	 * @param path
	 *            the target XPath expression
	 * @param ctx
	 *            the target context
	 * @return evaluation result as String
	 */
	public String evalPathForString(String path, Node ctx) {
		Object compiled = compile(path);
		if (compiled == null)
			return null;
		return evalForString(compiled, ctx);
	}
}
