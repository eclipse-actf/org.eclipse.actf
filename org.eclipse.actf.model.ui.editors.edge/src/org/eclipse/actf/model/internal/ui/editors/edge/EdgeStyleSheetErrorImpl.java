/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import org.eclipse.actf.model.dom.dombyjs.IRules;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.w3c.dom.DOMException;

public class EdgeStyleSheetErrorImpl extends EdgeStyleSheetImpl {

	private final String error;

	public EdgeStyleSheetErrorImpl(String title, String href, String error) {
		super(title, href, new String[0], null);
		this.error = error;
	}

	@Override
	public IStyleSheets getImports() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, error);
	}

	@Override
	public IRules getRules() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, error);
	}

	@Override
	public String getCssText() throws DOMException {
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, error);
	}

}
