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
import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;

public class EdgeStyleSheetImpl implements IStyleSheet {

	private final String title, href, cssText;
	private final IStyleSheets imports;
	private final IStyleSheet parentStyleSheet = null;
	private final IRules rules = null;

	public EdgeStyleSheetImpl(String title, String href, String[] rules, IStyleSheets imports) {
		this.title = title;
		this.href = href;
		this.cssText = String.join("\n", rules);
		this.imports = imports;
	}

	@Override
	public String getHref() {
		return href;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public IStyleSheet getParentStyleSheet() {
		return parentStyleSheet;
	}

	@Override
	public IStyleSheets getImports() {
		return imports;
	}

	@Override
	public IRules getRules() {
		return rules;
	}

	@Override
	public String getCssText() {
		return cssText;
	}

}
