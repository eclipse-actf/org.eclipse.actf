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

import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class StaticIMGRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		setContextIn(element, curContext);

		// get alt attribute
		NamedNodeMap attrs = element.getAttributes();
		Node altNode = attrs.getNamedItem("alt"); //$NON-NLS-1$
		String altstr = null;
		if (altNode == null)
			return null;

		// get alt string
		altstr = altNode.getNodeValue();
		altstr = TextUtil.trim(altstr);
		if (altstr.length() == 0) {
			if (OutLoud.jwat_mode == IVoiceBrowserController.SCREEN_READER_MODE) {

				Node node = element.getParentNode();
				for (;;) {
					if (node == null)
						return null;

					if (node.getNodeType() == Node.ELEMENT_NODE
						&& node.getNodeName().toLowerCase().equals("body")) //$NON-NLS-1$
						return null;
					if (node.getNodeType() == Node.ELEMENT_NODE
						&& node.getNodeName().toLowerCase().equals("a")) //$NON-NLS-1$
						break;
					node = node.getParentNode();
				}

				Node srcNode = attrs.getNamedItem("src"); //$NON-NLS-1$
				if (srcNode != null) {
					String srcstr = srcNode.getNodeValue();
					srcstr = TextUtil.trim(srcstr);
					altstr = srcstr;
				} else
					return null;
			} else
				return null;
		}

		// build result string
		String result =
			OutLoud.buildResultString(
				mc,
				url,
				element,
				null,
				null,
				"<name=str1>", //$NON-NLS-1$
				altstr);
		if (result == null && OutLoud.hprDefltMsg)
			result = "[" + altstr + ".]"; //$NON-NLS-1$ //$NON-NLS-2$

		if (result != null)
			result = result.trim();
		return new PacketCollection(
			new Packet(element, result, curContext, true));
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
		return null;
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(false);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(false);
	}
}
