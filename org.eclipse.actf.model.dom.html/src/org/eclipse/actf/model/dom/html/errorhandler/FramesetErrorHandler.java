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

package org.eclipse.actf.model.dom.html.errorhandler;

/**
 * If a parser using Strict or some DTD which does not define FRAMESET, this
 * error handler make the parser use FRAMESET while parsing.
 */
public class FramesetErrorHandler extends UnknownElementErrorHandler {
	/**
	 * Default constructor of the class
	 */
	public FramesetErrorHandler() {
		super("FRAMESET", "-//W3C//DTD HTML 4.01 Frameset//EN"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
