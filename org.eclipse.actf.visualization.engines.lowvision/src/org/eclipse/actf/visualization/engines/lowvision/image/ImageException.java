/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.lowvision.image;

/**
 * Exception relates to images used in low vision simulation engine.
 */
public class ImageException extends Exception {
	private static final long serialVersionUID = -3796816367042170808L;

	/**
	 * Constructor of Exception
	 */
	public ImageException() {
		super();
	}

	/**
	 * Constructor of Exception
	 * 
	 * @param msg
	 *            exception message
	 */
	public ImageException(String msg) {
		super(msg);
	}
}
