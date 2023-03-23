/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *    Kentarou Fukuda - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@SuppressWarnings("nls")
public class StaticBUTTONRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element,
	 *      Context)
	 */
	// @SuppressWarnings("nls")
	public PacketCollection getPacketCollectionIn(Element element, Context curContext, String url,
			MessageCollection mc) {
		try {
			setContextIn(element, curContext);
			NamedNodeMap attrs = element.getAttributes();

			String type = null, result = null;
			Node typeNode = attrs.getNamedItem("type");
			if (typeNode != null) {
				type = typeNode.getNodeValue();
				if (type.length() > 0) {
					if (type.equals("submit")) {
						result = inputTypeSubmit(element, attrs, mc, url);
					} else if (type.equals("reset")) {
						result = inputTypeReset(element, attrs, mc, url);
					} else if (type.equals("button")) {
						result = inputTypeButton(element, attrs, mc, url);
					}
					// other??
				}
			} else {
				result = inputTypeButton(element, attrs, mc, url);
			}
			if (OutLoud.jwat_mode == IVoiceBrowserController.SCREEN_READER_MODE) {
				curContext.setLineDelimiter(true);
				curContext.setStartSelect(true);
			}
			if (result != null) {
				result = result.trim();

				if (DomUtil.isDisabled(element)) {
					result = "(disabled)" + result;
				}

				return new PacketCollection(new Packet(element, result, curContext, true));
			} else {
				return null;
			}
		} catch (NullPointerException npe) {
			return null;
		}
	}

	/**
	 * Method inputTypeSubmit.
	 * 
	 * @param nodeName
	 * @param attrs
	 * @param mc
	 * @return String
	 */
	private String inputTypeSubmit(Element element, NamedNodeMap attrs, MessageCollection mc, String url) {
		String result = null;
		String value = null;
		Node valueNode = attrs.getNamedItem("VALUE");
		if (valueNode != null)
			value = valueNode.getNodeValue();
		if (value != null && value.length() > 0) {
			result = OutLoud.buildResultString(mc, url, element, "submit", "hasstr", "name=str1", value);
			if (result == null)
				result = "[" + value + ": Submit button.]";
		} else {
			result = OutLoud.buildResultString(mc, url, element, "submit", "nostr");
			if (result == null)
				result = "[Submit button.]";
		}
		return result;
	}

	/**
	 * Method inputTypeReset.
	 * 
	 * @param nodeName
	 * @param attrs
	 * @param mc
	 * @return String
	 */
	private String inputTypeReset(Element element, NamedNodeMap attrs, MessageCollection mc, String url) {
		String result = null;
		String value = null;
		Node valueNode = attrs.getNamedItem("VALUE");
		if (valueNode != null)
			value = valueNode.getNodeValue();
		if (value != null && value.length() > 0) {
			result = OutLoud.buildResultString(mc, url, element, "reset", "hasstr", "name=str1", value);
			if (result == null)
				result = "[" + value + ": Reset button.]";
		} else {
			result = OutLoud.buildResultString(mc, url, element, "reset", "nostr");
			if (result == null)
				result = "[Reset button.]";
		}
		return result;
	}

	/**
	 * Method inputTypeButton.
	 * 
	 * @param nodeName
	 * @param attrs
	 * @param mc
	 * @return String
	 */
	private String inputTypeButton(Element element, NamedNodeMap attrs, MessageCollection mc, String url) {
		String result = null;
		String value = null;
		Node valueNode = attrs.getNamedItem("VALUE");
		if (valueNode != null)
			value = valueNode.getNodeValue();
		if (value != null && value.length() > 0) {
			result = OutLoud.buildResultString(mc, url, element, "button", "hasstr", "name=str1", value);
			if (result == null)
				result = "[" + value + ": Button.]";
		} else {
			result = OutLoud.buildResultString(mc, url, element, "button", "nostr");
			if (result == null)
				result = "[Button.]";
		}
		return result;
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
		/*
		 * try { NamedNodeMap attrs = element.getAttributes();
		 * 
		 * String type = null; Node typeNode = attrs.getNamedItem("type");
		 * //$NON-NLS-1$ if (typeNode != null) { type = typeNode.getNodeValue();
		 * if (type.length() > 0) { if (type.equals("submit") ||
		 * type.equals("reset")) { //$NON-NLS-1$ //$NON-NLS-2$
		 * curContext.setLineDelimiter(true); } else {
		 * curContext.setLineDelimiter(false); } } else {
		 * curContext.setLineDelimiter(false); } } else {
		 * curContext.setLineDelimiter(false); } } catch (NullPointerException
		 * npe) { npe.printStackTrace(); }
		 */
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}
}
