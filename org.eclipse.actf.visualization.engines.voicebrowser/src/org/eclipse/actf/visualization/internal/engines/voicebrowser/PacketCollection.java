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

import java.util.ArrayList;

import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection;
import org.w3c.dom.Node;

public class PacketCollection extends ArrayList<IPacket> implements
		IPacketCollection {

	private static final long serialVersionUID = 673397303905161652L;

	/**
	 * Method PacketCollection.
	 * 
	 * @param text
	 */
	public PacketCollection(Packet text) {
		super();
		add(text);
	}

	/**
	 * @see java.lang.Object#Object()
	 */
	public PacketCollection() {
		super();
	}

	/**
	 * Method add.
	 * 
	 * @param p
	 * @return boolean
	 */
	public boolean add(Packet p) {
		return super.add(p);
	}

	/**
	 * Method addAll.
	 * 
	 * @param c
	 * @return boolean
	 */
	public boolean addAll(PacketCollection c) {
		return super.addAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#isLineDelimiter(int)
	 */
	public boolean isLineDelimiter(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isLineDelimiter();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#isLinkTag(int)
	 */
	public boolean isLinkTag(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isLinkTag();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#isInsideForm(int)
	 */
	public boolean isInsideForm(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isInsideForm();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#isInsideAnchor(int)
	 */
	public boolean isInsideAnchor(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isInsideAnchor();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isStartSelect(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isStartSelect();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isStringOutput(int i) {
		try {
			if (i < this.size())
				return (get(i)).getContext().isStringOutput();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#getFirstNode()
	 */
	public Node getFirstNode() {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				if (((Packet) this.get(i)).isStartTag()) {
					return (this.get(i)).getNode();
				}
			}
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#getLastNode()
	 */
	public Node getLastNode() {
		try {
			int size = this.size();
			for (int i = size - 1; i >= 0; i--) {
				if (((Packet) this.get(i)).isStartTag()) {
					return (this.get(i)).getNode();
				}
			}
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#getTopNodePosition()
	 */
	public int getTopNodePosition() {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				String str = (this.get(i)).getText();
				if (str != null && str.length() > 0) {
					if (this.isInsideAnchor(i)) {
						for (int j = i; j >= 0; j--)
							if (this.isLinkTag(j))
								return j;
					}
					return i;
				}
			}
			return -1;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return -1;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#getBottomNodePosition()
	 */
	public int getBottomNodePosition() {
		try {
			int size = this.size();
			boolean found = false;
			for (int i = size - 1; i >= 0; i--) {
				String str = (this.get(i)).getText();
				if (((str != null) && (str.length() > 0)))
					found = true;
				if (found) {
					if (this.isLineDelimiter(i)) {
						if (i == size - 1)
							return i;
						else {
							if (this.isLinkTag(i + 1)
									&& (i + 2 < size && this.isLinkTag(i + 2)))
								return i + 2;
							else
								return i + 1;
						}
					}
				}
			}
			return -1;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return -1;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return -1;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		int size = this.size();
		for (int i = 0; i < size; i++) {
			try {
				sbuf.append((this.get(i)).toString());
				sbuf.append("\n"); //$NON-NLS-1$
			} catch (ClassCastException cce) {
				sbuf.append("error: " + i + "the object is not a packet.\n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return sbuf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection#getNodePosition(org.w3c.dom.Node)
	 */
	public int getNodePosition(Node node) {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				if (node == (this.get(i)).getNode()) {
					// System.out.println ("found: " + i + "/" + size);
					return i;
				}
			}
			return 0;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return 0;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return 0;
		}
	}
}
