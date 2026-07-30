/*******************************************************************************
 * Copyright (c) 2026 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Utility class to prepare secure DocumentBuilder.
 */
public class DocumentBuilderUtil {

	/**
	 * @return DocumentBuilder without DTD support
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder newDocumentBuilderWithoutDTD() throws ParserConfigurationException {
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();

		df.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		df.setXIncludeAware(false);

		//

		df.setFeature("http://xml.org/sax/features/external-general-entities", false);
		df.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		df.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		df.setNamespaceAware(true);
		df.setExpandEntityReferences(false);
		df.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		return df.newDocumentBuilder();
	}

	/**
	 * @return DocumentBuilder without external DTD/entity support
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder newSecureDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();

		df.setFeature("http://xml.org/sax/features/external-general-entities", false);
		df.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		df.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		df.setNamespaceAware(true);
		df.setXIncludeAware(false);
		df.setExpandEntityReferences(false);
		df.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		try {
			df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		} catch (IllegalArgumentException e) {
			// never expected on JDK17+, ignore log
		}

		System.setProperty("jdk.xml.entityExpansionLimit", "1000");
		System.setProperty("jdk.xml.totalEntitySizeLimit", "100000");

		DocumentBuilder db = df.newDocumentBuilder();
		db.setEntityResolver((publicId, systemId) -> {
			return new org.xml.sax.InputSource(new java.io.StringReader(""));
		});

		return db;
	}

}
