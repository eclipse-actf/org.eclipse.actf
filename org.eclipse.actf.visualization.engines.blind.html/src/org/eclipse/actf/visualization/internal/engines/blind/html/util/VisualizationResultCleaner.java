/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualizationResultCleaner {

	// TODO merge into DocumentCleaner

	private static final String TITLE = "title"; //$NON-NLS-1$

	@SuppressWarnings("nls")
	//TODO
	public static void clean(Document _result, String url) {
		// base tag for image/css
		/*
		 * NodeList nl = result.getElementsByTagName("head"); Element head =
		 * (Element) nl.item(0); Element base = result.createElement("base");
		 * base.setAttribute("href",
		 * testApp.getPartLeftWebBrowser().getCurURL());
		 * //head.appendChild(base); head.insertBefore(base,
		 * head.getFirstChild());
		 */

		// remove tricky comment for IE encoding detection
		NodeList htmlNl = _result.getElementsByTagName("html"); //$NON-NLS-1$
		int size = htmlNl.getLength();
		for (int i = 0; i < size; i++) {
			Element htmlE = (Element) htmlNl.item(0);
			NodeList childNl = htmlE.getChildNodes();
			for (int j = childNl.getLength() - 1; j > -1; j--) {
				if (childNl.item(j).getNodeType() == Node.COMMENT_NODE) {
					htmlE.removeChild(childNl.item(j));
				}
			}
			if (i == 0) {
				// TODO file protocol
				htmlE.insertBefore(htmlE.getOwnerDocument().createComment(
						" saved from url=(0014)about:internet "), htmlE //$NON-NLS-1$
						.getFirstChild());
			}
		}

		NodeList framesetNl = _result.getElementsByTagName("frameset"); //$NON-NLS-1$
		if (framesetNl.getLength() > 0) {
			try {
				NodeList frameNL = _result.getElementsByTagName("noframes"); //$NON-NLS-1$
				for (int i = 0; i < frameNL.getLength(); i++) {
					Node tmpN = frameNL.item(i);
					Node parentN = tmpN.getParentNode();
					NodeList childNL = tmpN.getChildNodes();
					for (int j = 0; j < childNL.getLength(); j++) {
						_result.getDocumentElement().appendChild(
								childNL.item(j).cloneNode(true));
					}
					parentN.removeChild(tmpN);
				}

				// TODO noframe message
				if (frameNL.getLength() < 1) {
					_result.getDocumentElement().appendChild(
							_result.createElement("body")); //$NON-NLS-1$
				}

				for (int i = 0; i < framesetNl.getLength(); i++) {
					Node tmpN = framesetNl.item(i);
					try {
						tmpN.getParentNode().removeChild(tmpN);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e2) {
			}
		}

		NodeList headNl = _result.getElementsByTagName("head"); //$NON-NLS-1$
		if (headNl.getLength() > 0) {
			Element meta = _result.createElement("meta"); //$NON-NLS-1$
			meta.setAttribute("http-equiv", "Content-type"); //$NON-NLS-1$  //$NON-NLS-2$
			meta.setAttribute("content", "text/html; charset=UTF-8"); //$NON-NLS-1$  //$NON-NLS-2$
			Element first = (Element) headNl.item(0);
			if (first.hasChildNodes()) {
				first.insertBefore(meta, first.getFirstChild());
			} else {
				first.appendChild(meta);
			}

			NodeList titleNl = first.getElementsByTagName(TITLE);
			if (titleNl.getLength() > 0) {
				Element title = (Element) titleNl.item(0);
				Node titleText = title.getFirstChild();
				if (titleText != null
						&& titleText.getNodeType() == Node.TEXT_NODE) {
					titleText.setNodeValue("Visualization result of "
							+ titleText.getNodeValue());
				} else {
					titleText = _result.createTextNode("Visualization result");
					title.insertBefore(titleText, title.getFirstChild());
				}
			} else {
				Element title = _result.createElement(TITLE);
				title.appendChild(_result
						.createTextNode("Visualization result"));
				first.appendChild(title);
			}

		}
	}

}
