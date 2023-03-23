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

package org.eclipse.actf.model.internal.dom.sgml.errorhandler;

import java.io.IOException;

import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.internal.dom.sgml.ISGMLParser;


/**
 * Token-levle error handler interface.
 */
public interface ITokenErrorHandler {
	/**
	 * Handles error whose type is specified by <code>code</code>
	 * 
	 * @param code
	 *            error type. This parameter represents tokenizer's state. In
	 *            other words, where the tokenizer read. <code>Code</code>
	 *            must be {@link IParserError#ATTR_VALUE} (Error occurs when a
	 *            tokenizer is to read attribute's value),
	 *            {@link IParserError#BEFORE_ATTRNAME} (to read attribute's
	 *            name) or {@link IParserError#TAG_NAME} (to read tag name).
	 *            If string to substitute is diecovered, that can be inserted to
	 *            current reading position.
	 * @param parser
	 *            caller of this handler.
	 * @param errorStr
	 *            string that causes the error.
	 * @return true if error was handled. Otherwise false.
	 * @see ISGMLParser#insert(java.lang.String)
	 */
	public boolean handleError(int code, ISGMLParser parser, String errorStr)
			throws IOException;
}
