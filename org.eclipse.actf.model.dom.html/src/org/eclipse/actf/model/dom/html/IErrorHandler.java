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

package org.eclipse.actf.model.dom.html;

import java.io.IOException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Node-level error handler interface.
 */
public interface IErrorHandler extends IParserError {
	/**
	 * Handles error whose type is specified by <code>code</code>
	 * 
	 * @param code
	 *            error type.
	 * @param parser
	 *            caller of this handler. This parser's state is easily changed
	 *            by the referenced methods.
	 * @param errorNode
	 *            a node that causes the error.
	 * @return <code>true</code> if error was handled. Otherwise <code>false
	 * </code>.
	 * @see IParser#getNode()
	 * @see IParser#pushBackNode(org.w3c.dom.Node)
	 * @see IParser#getExtraErrInfo()
	 * @see IParser#getContext()
	 * @see IParser#setContext(org.w3c.dom.Element)
	 */
	public boolean handleError(int code, IParser parser, Node errorNode)
			throws ParseException, IOException, SAXException;
}
