/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.dom.html.dochandler;

import org.eclipse.actf.model.internal.dom.html.parser.HTMLParser;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;

public class PREHandler extends DefaultDocumentHandler {
	private boolean previousPrsvWS;

	public PREHandler(HTMLParser parser) {
		super(parser);
	}

	public void startElement(String name, AttributeList atts)
			throws SAXException {
		if (name.equalsIgnoreCase("PRE")) { //$NON-NLS-1$
			previousPrsvWS = parser.getPreserveWhitespace();
			parser.setPreserveWhitespace(true);
		}
		super.startElement(name, atts);
	}

	public void endElement(String name) throws SAXException {
		if (name.equalsIgnoreCase("PRE")) { //$NON-NLS-1$
			parser.setPreserveWhitespace(previousPrsvWS);
		}
		super.endElement(name);
	}
}
