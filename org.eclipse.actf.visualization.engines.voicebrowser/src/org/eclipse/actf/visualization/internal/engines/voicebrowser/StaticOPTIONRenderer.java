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

import org.w3c.dom.*;


public class StaticOPTIONRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	@SuppressWarnings("nls")
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		// set `context in' flags
		setContextIn(element, curContext);

		// build result string
		String result = null;
		String selstr = null;

		Node node = element.getFirstChild();
		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
			selstr = node.getNodeValue();
			if (selstr.length() > 0) {
				selstr = TextUtil.substitute(selstr, "\n", "");
				selstr = TextUtil.trim(selstr);
			}

			int selected = 0;
			NamedNodeMap attList = element.getAttributes();
			for (int j = 0; j < attList.getLength(); j++) {
				Attr att = (Attr) attList.item(j);
				if (att.getName().equals("selected")) {
					result =
						OutLoud.buildResultString(
							mc,
							url,
							element,
							null,
							"on",
							"name=str1",
							selstr);
					if (result == null && OutLoud.hprDefltMsg) {
						result = selstr + " [Selected.]";
					}
					selected++;
					break;
				}
			}
			if (selected == 0) {
				curContext.setStringOutput(false);
				result =
					OutLoud.buildResultString(
						mc,
						url,
						element,
						null,
						"off",
						"name=str1",
						selstr);
				if (result == null && OutLoud.hprDefltMsg) {
					result = selstr + " [Off.]";
				}
			}
		}
		if (result != null)
			result = result.trim();
		
		if(DomUtil.isDisabled(element)){
			result = "(disabled)"+result;
		}

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
		curContext.setGoChild(false);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(true);
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
