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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerAccessKeyInfo;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerIdInfo;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerInfoProvider;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl.ElementInfoProviderExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualizationAttributeInfo {
	String attributeName;

	String attributeValue;

	String tagName;

	String category = ""; //$NON-NLS-1$

	String helpUrl = ""; //$NON-NLS-1$

	String description = ""; //$NON-NLS-1$

	int nodeId;

	private static IElementViewerInfoProvider[] provider = ElementInfoProviderExtension
			.getProviders();

	VisualizationAttributeInfo(Element target, IVisualizeMapData mapData,
			String targetAttribute) {
		attributeName = targetAttribute;
		tagName = target.getNodeName();
		attributeValue = target.getAttribute(targetAttribute);

		try {
			nodeId = mapData.getIdOfOrigNode(target).intValue();
		} catch (Exception e) {
			//
			nodeId = -1;
		}

		// TODO use Properties
		if (attributeName.equalsIgnoreCase("id")) { //$NON-NLS-1$
			for (int i = 0; i < provider.length; i++) {
				IElementViewerIdInfo idInfo = provider[i]
						.getIdInfo(attributeValue);
				if (idInfo != null) {
					category = idInfo.getCategory();
					helpUrl = idInfo.getHelpUrl();
				}
			}
		}

		if (attributeName.equalsIgnoreCase("accesskey")) { //$NON-NLS-1$

			for (int i = 0; i < provider.length; i++) {
				IElementViewerAccessKeyInfo keyInfo = provider[i]
						.getAccessKeyInfo(attributeValue);
				if (keyInfo != null) {
					description = keyInfo.getDescription();
					helpUrl = keyInfo.getHelpUrl();
				}
			}
			attributeValue = "Alt+ " + attributeValue; //$NON-NLS-1$
		}
	}

	/**
	 * @return
	 */
	public String getAttribtueName() {
		return attributeName;
	}

	/**
	 * @return
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @return
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @return
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return
	 */
	public String getHelpUrl() {
		return helpUrl;
	}

	/**
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String toString() {
		return (tagName + " : " + attributeValue + " : " + nodeId); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static List<VisualizationAttributeInfo> listUp(Document target,
			IVisualizeMapData mapData, String targetAttribute) {
		List<VisualizationAttributeInfo> result = new ArrayList<VisualizationAttributeInfo>();

		// TODO use XPath
		NodeList bodyNl = target.getElementsByTagName("body"); //$NON-NLS-1$

		if (bodyNl.getLength() > 0) {
			Node tmpN = bodyNl.item(0);
			Stack<Node> stack = new Stack<Node>();
			while (tmpN != null) {
				if (tmpN.getNodeType() == Node.ELEMENT_NODE) {
					Element currentE = (Element) tmpN;
					if (currentE.hasAttribute(targetAttribute)) {
						result.add(new VisualizationAttributeInfo(currentE,
								mapData, targetAttribute));
					}
				}

				if (tmpN.hasChildNodes()) {
					stack.push(tmpN);
					tmpN = tmpN.getFirstChild();
				} else if (tmpN.getNextSibling() != null) {
					tmpN = tmpN.getNextSibling();
				} else {
					tmpN = null;
					while ((tmpN == null) && (stack.size() > 0)) {
						tmpN = stack.pop();
						tmpN = tmpN.getNextSibling();
					}
				}
			}
		}

		return (result);
	}

}
