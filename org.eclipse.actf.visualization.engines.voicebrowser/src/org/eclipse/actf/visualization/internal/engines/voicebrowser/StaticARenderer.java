/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
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

public class StaticARenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		setContextIn(element, curContext);
		try {
			// check if `a' element has `href' attribute
			NamedNodeMap attrs = element.getAttributes();
			Node hrefNode = attrs.getNamedItem("href"); //$NON-NLS-1$
			String result = null;
			if (hrefNode == null) {
				curContext.setLinkTag(false);
			} else {
				String link = hrefNode.getNodeValue();				
				curContext.setHref(link);				
				if (link != null
					&& link.length() > 0
					&& link.charAt(0) == '#') {
					result =
						OutLoud.buildResultString(
							mc,
							url,
							element,
							"internallink", //$NON-NLS-1$
							null);
				} else {
					result =
						OutLoud.buildResultString(mc, url, element, null, null);
				}
				if (result != null)
					result = result.trim();
			}

			return new PacketCollection(
				new Packet(element, result, curContext, true));
		} catch (NullPointerException npe) {
			return null;
		}
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionOut(Element, Context)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {
		setContextOut(element, curContext);
		return new PacketCollection(new Packet(element, "", curContext, true)); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setInsideAnchor(true);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setInsideAnchor(false);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(false);
		curContext.setHref(null);
	}
}
