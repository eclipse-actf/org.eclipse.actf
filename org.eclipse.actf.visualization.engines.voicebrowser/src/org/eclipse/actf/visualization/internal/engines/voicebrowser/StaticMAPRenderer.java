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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class StaticMAPRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc) {

		// set `context in' flags
		setContextIn(element, ctx);

		// get number of options	
		NodeList children = element.getChildNodes();
		int num = 0;
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			String nodeName = node.getNodeName().toLowerCase();
			if (nodeName.equals("area")) { //$NON-NLS-1$
				num++;
			}
		}

		// select type
		//String nodeName = element.getNodeName().toLowerCase();
		String result = null;
		if (num > 1) {
			result =
				OutLoud.buildResultString(
					mc,
					url,
					element,
					"in", //$NON-NLS-1$
					"plural", //$NON-NLS-1$
					"<name=num1>", //$NON-NLS-1$
					Integer.toString(num));
			if (result == null && OutLoud.hprDefltMsg)
				result = "(Start of map with " + num + " items.)";
		} else {
			result =
				OutLoud.buildResultString(
					mc,
					url,
					element,
					"in", //$NON-NLS-1$
					"singular", //$NON-NLS-1$
					"<name=num1>", //$NON-NLS-1$
					Integer.toString(num));
			if (result == null && OutLoud.hprDefltMsg)
				result = "(Start of map with 1 item.)";
		}
		if (result != null)
			result = result.trim();
		return new PacketCollection(new Packet(element, result, ctx, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionOut(Element, Context)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc) {
		setContextOut(element, ctx);

		String result =
			OutLoud.buildResultString(mc, url, element, "out", null); //$NON-NLS-1$
		if (result == null && OutLoud.hprDefltMsg)
			result = "(End of Map.)";

		if (result != null)
			result = result.trim();
		return new PacketCollection(new Packet(element, result, ctx, false));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context ctx) {
		ctx.setStartSelect(true);
		ctx.setGoChild(true);
		ctx.setLineDelimiter(true);
		ctx.setLinkTag(false);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context ctx) {
		ctx.setStartSelect(false);
		ctx.setGoChild(true);
		//		ctx.setLineDelimiter(false);
		ctx.setLineDelimiter(true);
		ctx.setLinkTag(false);
	}

}
