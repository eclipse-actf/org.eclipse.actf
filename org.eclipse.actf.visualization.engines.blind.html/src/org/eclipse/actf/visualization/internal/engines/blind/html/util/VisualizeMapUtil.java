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

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualizeMapUtil {

    // move from VisualizeEngine

    public static void createNode2NodeMap(Document orig, Document dest, VisualizeMapDataImpl mapData) {
        NodeList bodyNl = dest.getElementsByTagName("body"); //$NON-NLS-1$
        NodeList origBodyNl = orig.getElementsByTagName("body"); //$NON-NLS-1$

        if (origBodyNl.getLength() > 0) {

            Element bodyEl = (Element) bodyNl.item(0);
            Element origBodyEl = (Element) origBodyNl.item(0);

            if (origBodyEl.hasChildNodes()) {

                Stack<Node> stack = new Stack<Node>();
                Stack<Node> origStack = new Stack<Node>();

                stack.push(bodyEl);
                origStack.push(origBodyEl);

                Node curNode = bodyEl.getFirstChild();
                Node origCurNode = origBodyEl.getFirstChild();

                while ((origCurNode != null) && (origStack.size() > 0)) {
                    mapData.addOrigResultMapping(origCurNode, curNode);

                    try {

                        if (origCurNode.hasChildNodes()) {
                            stack.push(curNode);
                            origStack.push(origCurNode);

                            curNode = curNode.getFirstChild();
                            origCurNode = origCurNode.getFirstChild();
                        } else if (origCurNode.getNextSibling() != null) {
                            curNode = curNode.getNextSibling();
                            origCurNode = origCurNode.getNextSibling();
                        } else {
                            origCurNode = null;
                            while ((origCurNode == null) && (origStack.size() > 0)) {
                                curNode = stack.pop();
                                origCurNode = origStack.pop();

                                curNode = curNode.getNextSibling();
                                origCurNode = origCurNode.getNextSibling();
                            }
                        }
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    public static Map<String, String> createMapTextMap(Document doc) {
        Map<String, String> mapTextMap = new HashMap<String, String>();
        NodeList nl = doc.getElementsByTagName("area"); //$NON-NLS-1$
        int size = nl.getLength();

        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node.getParentNode().getNodeName().equals("map")) { //$NON-NLS-1$
                String curText = ((Element) node).getAttribute("alt"); //$NON-NLS-1$
                String mapName = ((Element) node.getParentNode()).getAttribute("name"); //$NON-NLS-1$
                String mapText = mapTextMap.get(mapName.toLowerCase());
                if (mapText != null) {
                    mapTextMap.put(mapName.toLowerCase(), mapText + ". " + curText); //$NON-NLS-1$
                } else {
                    mapTextMap.put(mapName.toLowerCase(), curText);
                }
            }
        }
        return (mapTextMap);
    }

}
