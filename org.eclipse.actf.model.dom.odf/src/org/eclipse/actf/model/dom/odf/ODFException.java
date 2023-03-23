/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf;

/**
 * When a ODF parser meets an unexpected and unrecoverable error, ODFException is thrown
 */
public class ODFException extends Exception {
	private static final long serialVersionUID = -5189990975068451718L;

	/**
	 * 
	 */
	public ODFException() {
		super();
	}

	/**
	 * @param message
	 */
	public ODFException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ODFException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ODFException(Throwable cause) {
		super(cause);
	}

}
