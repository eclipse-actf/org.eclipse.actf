/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class StaticABBRRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, CurrentCursor, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		// set `context in' flags
		setContextIn(element, curContext);

		// get alt attribute
		NamedNodeMap attrs = element.getAttributes();
		Node node = attrs.getNamedItem("title"); //$NON-NLS-1$
		if (node == null)
			return null;

		// get title string
		String str = node.getNodeValue();
		str = TextUtil.trim(str);
		if (str.length() == 0)
			return null;

		// build result string
		String result =
			OutLoud.buildResultString(
				mc,
				url,
				element,
				null,
				null,
				"<name=str1>", //$NON-NLS-1$
				str);
		if (result == null && OutLoud.hprDefltMsg) {
			if (element.getNodeName().toLowerCase().equals("abbr")) //$NON-NLS-1$
				result = "(Abbreviation: " + str + ".)"; //$NON-NLS-1$ //$NON-NLS-2$
			else
				result = "(Acronym: " + str + ".)"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (result != null)
			result = result.trim();
		return new PacketCollection(
			new Packet(element, result, curContext, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionOut(Element, CurrentCursor, Context)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		setContextOut(element, curContext);

		return null;
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
	}
}
