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


public class StaticTRRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc) {
		setContextIn(element, ctx);
		return null;
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

		if (element.getNodeName().toLowerCase().equals("tr") //$NON-NLS-1$
			&& OutLoud.jwat_mode == IVoiceBrowserController.SCREEN_READER_MODE) {
			String result =
				OutLoud.buildResultString(mc, url, element, "out", null); //$NON-NLS-1$

			if (result != null)
				result = result.trim();
			return new PacketCollection(
				new Packet(element, result, ctx, false));
		} else
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
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}
}
