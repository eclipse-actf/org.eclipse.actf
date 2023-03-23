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
import org.eclipse.actf.visualization.engines.voicebrowser.IContext;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.w3c.dom.Node;

public class Packet implements IPacket {
	private Node node;
	private String text;
	private IContext context;
	private boolean isStartTag;

	/**
	 * Constructor for RenderedText.
	 * @param ctx packet's context
	 * @param curNode node
	 * @param curText default text for this packet
	 */
	public Packet(
		Node curNode,
		String curText,
		IContext ctx,
		boolean isstarttag) {
		context = new Context(ctx);
		node = curNode;
		text = curText;
		isStartTag = isstarttag;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacket#getText()
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacket#getContext()
	 */
	public IContext getContext() {
		return context;
	}


//	 * Sets the text.
//	 * @param text The text to set
//	 */
//	public void setText(String text) {
//		this.text = text;
//	}
//	/**
//	 * Sets the context.
//	 * @param context The context to set
//	 */
//	public void setContext(IContext context) {
//		this.context = context;
//	}
//	/**
//	 * Sets the node.
//	 * @param node The node to set
//	 */
//	public void setNode(Node node) {
//		this.node = node;
//	}


	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacket#getNode()
	 */
	public Node getNode() {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacket#isStartTag()
	 */
	public boolean isStartTag() {
		return isStartTag;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("nls")
	public String toString() {
		StringBuffer sbuf = new StringBuffer();

		try {
			sbuf.append(getNode().getNodeName());
			sbuf.append("\n");
			sbuf.append("\t");
			sbuf.append(getText());
			sbuf.append("\n");
			sbuf.append("\t");
			sbuf.append(getContext().toString());
			sbuf.append("\n");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

		return sbuf.toString();
	}

}
