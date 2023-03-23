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


public interface IElementRenderer {

	/**
	 * Method getPacketCollectionIn.
	 * @param element
	 * @param ctx
	 * @return PacketCollection
	 */
	PacketCollection getPacketCollectionIn(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc);

	/**
	 * Method getPacketCollectionOut.
	 * @param element
	 * @param ctx
	 * @return PacketCollection
	 */
	PacketCollection getPacketCollectionOut(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc);

	/**
	 * Method setContextIn.
	 * @param curContext
	 */
	void setContextIn(Element element, Context curContext);

	/**
	 * Method setContextOut.
	 * @param curContext
	 */
	void setContextOut(Element element, Context curContext);
}
