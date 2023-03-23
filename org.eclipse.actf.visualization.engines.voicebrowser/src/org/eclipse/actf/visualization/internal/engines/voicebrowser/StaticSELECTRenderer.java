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
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

public class StaticSELECTRenderer implements IElementRenderer {

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

		// get number of options	
		NodeList children = element.getChildNodes();
		int num = 0;
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			String nodeName = node.getNodeName().toLowerCase();
			if (nodeName.equals("option")) {
				num++;
			}
		}

		// check if multiple attribute is set (select type)
		NamedNodeMap attrs = element.getAttributes();
		Node multiNode = attrs.getNamedItem("multiple");
		String type = "select";
		if (multiNode != null)
			type = "multi-select";

		// build result string

		String result;
		if (num > 1) {
			result =
				OutLoud.buildResultString(
					mc,
					url,
					element,
					"in",
					"plural",
					"name=str1",
					type,
					"name=num1",
					Integer.toString(num));
			if (result == null && OutLoud.hprDefltMsg)
				result = "(Start of " + type + " menu with " + num + " items.)";
		} else {
			result =
				OutLoud.buildResultString(
					mc,
					url,
					element,
					"in",
					"singular",
					"name=str1",
					type);
			if (result == null && OutLoud.hprDefltMsg)
				result = "(Start of " + type + " menu with 1 item.)";
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
	@SuppressWarnings("nls")
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		// set `context in' flags
		setContextOut(element, curContext);

		// build result string
		String result =
			OutLoud.buildResultString(mc, url, element, "out", null);
		if (result == null && OutLoud.hprDefltMsg)
			result = "(End of select menu.)";

		if (result != null)
			result = result.trim();
		return new PacketCollection(
			new Packet(element, result, curContext, false));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setStartSelect(false);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}
}
