/*******************************************************************************
 * Copyright (c) 2008, 2019 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util.win32;

import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.ole.win32.Variant;

public class VariantUtil {

	/**
	 * Create Variant from specified address of IDispatch
	 * 
	 * @param address
	 *            address of IDispatch
	 * @return Variant object which represents an IDispatch interface
	 */
	public static Variant createVariantFromIDispatchAddress(long address) {
		return new Variant(new IDispatch(address));
	}

}
