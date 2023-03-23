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
package org.eclipse.actf.model.dom.odf.range.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class ITextElementContainerUtil {

	private static List<ITextElementContainer> getChildList(Element el) {
		List<ITextElementContainer> list = new Vector<ITextElementContainer>();
		NodeList nl = el.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (child instanceof ITextElementContainer) {
				list.add((ITextElementContainer) child);
			}
		}
		return list;
	}

	public static long getContentSize(Element el) {
		return getChildList(el).size();
	}

	public static Iterator<ITextElementContainer> getChildIterator(Element el) {
		return getChildList(el).iterator();
	}
}