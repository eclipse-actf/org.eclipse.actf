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

public class MessageCollection extends ArrayList<Message> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1072510518088567847L;

	/**
	 * Method MessageCollection.
	 * @param text
	 */
	public MessageCollection(Message text) {
		super();
		add(text);
	}

	/**
	 * @see java.lang.Object#Object()
	 */
	public MessageCollection() {
		super();
	}

	/**
	 * Method getText.
	 * @return String
	 */
	public String getText() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Method add.
	 * @param p
	 * @return boolean
	 */
	public boolean add(Message p) {
		return super.add(p);
	}

	/**
	 * Method addAll.
	 * @param c
	 * @return boolean
	 */
	public boolean addAll(MessageCollection c) {
		return super.addAll(c);
	}

	/**
	 * Method getMessage.
	 * @param mc
	 * @param nodename
	 * @param type
	 * @param state
	 * @return Message
	 */
	public Message getMessage(
		MessageCollection mc,
		String nodename,
		String type,
		String state) {
		Message msg = null;
		if (mc != null && mc.size() > 0) {
			for (int i = 0; i < mc.size(); i++) {
				msg = mc.get(i);
				if (nodename.toLowerCase().equals(msg.getTagName())) {
					if ((type == null && msg.getTagType() == null)
						|| (type != null
							&& type.toLowerCase().equals(msg.getTagType()))) {
						if ((state == null && msg.getTagState() == null)
							|| (state != null
								&& state.toLowerCase().equals(
									msg.getTagState()))) {
							return msg;
						}
					}
				}
			}
		}
		return null;
	}
}
