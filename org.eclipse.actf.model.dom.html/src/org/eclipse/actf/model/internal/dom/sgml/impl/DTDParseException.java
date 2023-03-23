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

package org.eclipse.actf.model.internal.dom.sgml.impl;

import org.eclipse.actf.model.dom.html.ParseException;

class DTDParseException extends ParseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1148837926968828952L;

	public DTDParseException() {
		super();
	}

	public DTDParseException(String msg) {
		super(msg);
	}
}
