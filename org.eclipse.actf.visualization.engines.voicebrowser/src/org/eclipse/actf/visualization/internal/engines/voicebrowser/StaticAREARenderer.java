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


public class StaticAREARenderer implements IElementRenderer {

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
		ctx.setStringOutput(false);
		if (OutLoud.jwat_mode == IVoiceBrowserController.SCREEN_READER_MODE)
			ctx.setStringOutput(true);

		// get alt attribute
		NamedNodeMap attrs = element.getAttributes();
		Node altNode = attrs.getNamedItem("alt"); //$NON-NLS-1$
		if (altNode == null)
			return null;

		// get alt string
		String altstr = altNode.getNodeValue();
		altstr = TextUtil.trim(altstr);
		if (altstr.length() == 0)
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
				altstr);
		if (result == null && OutLoud.hprDefltMsg)
			result = "[" + altstr + ".]"; //$NON-NLS-1$ //$NON-NLS-2$

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
		return null;
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context ctx) {
		ctx.setGoChild(true);
		ctx.setLineDelimiter(true);
		ctx.setLinkTag(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context ctx) {
		ctx.setGoChild(true);
		ctx.setLineDelimiter(false);
		ctx.setLinkTag(false);
	}

}
