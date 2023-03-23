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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;

public class EdgeStyleSheetsImpl implements IStyleSheets {

	private final List<IStyleSheet> styleSheets = new ArrayList<IStyleSheet>();

	public EdgeStyleSheetsImpl(Object[] jsObjects) {
		for (Object obj : jsObjects) {
			Object[] o = (Object[]) obj;
			if (o.length == 3) {
				styleSheets.add(new EdgeStyleSheetErrorImpl((String) o[0], (String) o[1], (String) o[2]));
			} else if (o.length == 4) {
				String[] rules = Stream.of((Object[]) o[2]).toArray(String[]::new);
				IStyleSheets imports = new EdgeStyleSheetsImpl((Object[]) o[3]);
				styleSheets.add(new EdgeStyleSheetImpl((String) o[0], (String) o[1], rules, imports));
			}
		}
	}

	@Override
	public int getLength() {
		return styleSheets.size();
	}

	@Override
	public IStyleSheet item(int index) {
		return styleSheets.get(index);
	}

}
