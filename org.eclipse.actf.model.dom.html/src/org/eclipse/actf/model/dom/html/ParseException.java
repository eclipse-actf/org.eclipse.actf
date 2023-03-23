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

/**
 * When a parser meets an unexpected and unrecoverable error, ParseException is
 * thrown
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 940413074059724617L;

	/**
	 * Constructor of the Exception.
	 */
	public ParseException() {
		super();
	}

	/**
	 * Constructor of the Exception.
	 * 
	 * @param msg
	 *            Exception message
	 */
	public ParseException(String msg) {
		super(msg);
	}
}
