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


public class StaticTABLERenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionIn(Element, Context, MessageCollection)
	 */
	@SuppressWarnings("nls")
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc) {

		// Jaws mode : ex) table with 3 columns and 7 rows
		// 	tr - rows count
		// 	td - columns count
		int rcnt = getRowsCount(element);
		int ccnt = getColumnsCount(element);
		String result =
			OutLoud.buildResultString(
				mc,
				url,
				element,
				"in",
				null,
				"name=num1",
				Integer.toString(ccnt),
				"name=num2",
				Integer.toString(rcnt));
		setContextIn(element, ctx);
		if (result != null)
			result = result.trim();
		return new PacketCollection(new Packet(element, result, ctx, true));
	}

	/**
	 * Method getRowsCount.
	 * @param element
	 * @return int
	 */
	private static int getRowsCount(Element element) {
		Node startNode = element;
		Node node = startNode;
		int rowscount = 0;
		while (node != null) {

			if (node.getNodeType() == Node.ELEMENT_NODE
				&& node.getNodeName().toLowerCase().equals("tr")) { //$NON-NLS-1$
				rowscount++;
			}
			node = goNext(node, startNode);
		}
		return rowscount;
	}

	/**
	 * Method getColumnsCount.
	 * @param element
	 * @return int
	 */
	private static int getColumnsCount(Element element) {
		Node startNode = element;
		Node node = startNode;
		int ccnt = 0;
		int maxcnt = 0;
		while (node != null) {

			if (node.getNodeType() == Node.ELEMENT_NODE
				&& node.getNodeName().toLowerCase().equals("tr")) { //$NON-NLS-1$
				if (maxcnt < ccnt)
					maxcnt = ccnt;
				ccnt = 0;
			}
			if (node.getNodeType() == Node.ELEMENT_NODE
				&& node.getNodeName().toLowerCase().equals("td")) { //$NON-NLS-1$
				ccnt++;
			}
			node = goNext(node, startNode);
			if (node == null) {
				if (maxcnt < ccnt)
					maxcnt = ccnt;
			}
		}
		return maxcnt;
	}

	/**
	 * Method goNext.
	 * @param node
	 * @param sNode
	 * @return Node
	 */
	private static Node goNext(Node node, Node sNode) {
		if (node == null)
			return null;
		// get next node
		if (node.hasChildNodes()) {
			// Has Child
			node = node.getFirstChild();
		} else if (node.getNextSibling() != null) {
			// Has Brother
			node = node.getNextSibling();
		} else { // Check Parent
			for (;;) {
				Node pnode = node.getParentNode();
				if (pnode == null || pnode == sNode) {
					node = null;
					break;
				} else {
					node = pnode.getNextSibling();
					if (node == null)
						node = pnode;
					else
						break;
				}
			}
		}
		return node;
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#getPacketCollectionOut(Element, Context, MessageCollection)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context ctx,
		String url,
		MessageCollection mc) {
		// set `context out' flags
		setContextOut(element, ctx);
		// build result string
		String result =
			OutLoud.buildResultString(mc, url, element, "out", null); //$NON-NLS-1$
		if (result != null)
			result = result.trim();
		return new PacketCollection(new Packet(element, result, ctx, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.internal.engines.voicebrowser.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
	}

}
