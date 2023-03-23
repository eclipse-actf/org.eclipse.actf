/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.xpath.jaxp;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.actf.util.dom.EmptyNodeListImpl;
import org.eclipse.actf.util.xpath.XPathService;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XPathServiceImpl extends XPathService {
	private static final XPathFactory xf = XPathFactory.newInstance();

	private XPath getXPath() {
		return xf.newXPath();
	}

	@Override
	public Object compile(String path) {
		try {
			return getXPath().compile(path);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	@Override
	public NodeList evalForNodeList(Object compiled, Node ctx) {
		try {
			XPathExpression xpe = (XPathExpression) compiled;
			return (NodeList) xpe.evaluate(ctx, XPathConstants.NODESET);
		} catch (Exception e) {
			return EmptyNodeListImpl.getInstance();
		}
	}

	@Override
	public String evalForString(Object compiled, Node ctx) {
		try {
			XPathExpression xpe = (XPathExpression) compiled;
			return (String) xpe.evaluate(ctx, XPathConstants.STRING);
		} catch (Exception e) {
			return null;
		}
	}

	public static XPathService newInstance() {
		if (xf == null)
			return null;
		return new XPathServiceImpl();
	}

	private XPathServiceImpl() {
	}
}
