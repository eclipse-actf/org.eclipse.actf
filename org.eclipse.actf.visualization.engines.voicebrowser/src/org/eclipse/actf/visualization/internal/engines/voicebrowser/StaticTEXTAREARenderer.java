/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and Others
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
import org.w3c.dom.Node;

public class StaticTEXTAREARenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element,
	 *      Context)
	 */
	@SuppressWarnings("nls")
	public PacketCollection getPacketCollectionIn(Element element, Context curContext, String url,
			MessageCollection mc) {

		// set `context in' flags
		setContextIn(element, curContext);

		// build result string
		String result = null;

		Node node = element.getFirstChild();
		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
			String nodeValue = node.getNodeValue();
			nodeValue = nodeValue.trim();
			if (nodeValue.length() > 0) {
				result = OutLoud.buildResultString(mc, url, element, null, "hasstr", "name=str1", nodeValue);
				if (result == null && OutLoud.hprDefltMsg)
					result = "[TextArea: " + nodeValue + "]";
			}
		}
		if (result == null) {
			result = OutLoud.buildResultString(mc, url, element, null, "nostr");
			if (result == null && OutLoud.hprDefltMsg)
				result = "[TextArea.]";
		}
		if (result != null)
			result = result.trim();

		if(DomUtil.isDisabled(element)){
			result = "(disabled)"+result;
		}

		return new PacketCollection(new Packet(element, result, curContext, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionOut(Element,
	 *      Context)
	 */
	public PacketCollection getPacketCollectionOut(Element element, Context curContext, String url,
			MessageCollection mc) {
		setContextOut(element, curContext);

		return null;
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setGoChild(true);
		// curContext.setLineDelimiter(false);
		// curContext.setLinkTag(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}
}
