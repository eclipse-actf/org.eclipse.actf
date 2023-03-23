/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFNode;
import org.eclipse.actf.model.dom.odf.base.impl.ODFDocumentImpl;
import org.eclipse.actf.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * ODFParser is used when parsing ODF documents.
 */
public class ODFParser {

	private InputStream getFileInputStream(String odfName, String fileName) {
		URL url = null;
		try {
			url = new URL(odfName);
		} catch (MalformedURLException e) {
		}

		ZipFile zipFile = null;
		ZipEntry zipEntry = null;
		try {
			if (url != null) {
				zipFile = new ZipFile(new File(url.toURI()));
			} else {
				zipFile = new ZipFile(odfName);
			}
			String zipFileName = fileName.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			zipEntry = zipFile.getEntry(zipFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		InputStream is = null;
		if (zipEntry != null) {
			try {
				is = zipFile.getInputStream(zipEntry);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	private void createODFNode(Node parent, ODFDocument odfDoc,
			ODFNode odfNodeParent) {
		if (parent instanceof Element) {
			NamedNodeMap attrs = ((Element) parent).getAttributes();
			if ((attrs != null) && (attrs.getLength() > 0)) {
				for (int i = 0; i < attrs.getLength(); i++) {
					((ODFDocumentImpl) odfDoc).getODFNode(attrs.item(i));
				}
			}
		}

		NodeList nl = parent.getChildNodes();
		if ((nl != null) && (nl.getLength() != 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);

				ODFNode odfChild = ((ODFDocumentImpl) odfDoc).getODFNode(child);
				createODFNode(child, odfDoc, odfChild);
			}
		}
	}

	private ODFDocument parse(InputSource is) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		Document document = null;
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			document = documentBuilder.parse(is);
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		if (document == null)
			return null;

		Node root = document.getDocumentElement();
		ODFDocument odfDoc = new ODFDocumentImpl(document);
		ODFNode odfDocRoot = (ODFNode) odfDoc.getDocumentElement();
		createODFNode(root, odfDoc, odfDocRoot);

		return odfDoc;
	}

	/**
	 * Parses a ODF document and return document. 
	 *
	 * @param odfName
	 *            file path of ODF document to parse
	 * @param xmlName
	 *            XML file name compressed in ODF file
	 * @return Document.
	 */	
	public ODFDocument getDocument(String odfName, String xmlName) {
		ODFDocument odfDoc = null;
		try {
			InputStream is = getFileInputStream(odfName, xmlName);
			if (is != null) {
				odfDoc = parse(new InputSource(is));
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return odfDoc;
	}

	/**
	 * Parses a ODF document and return document interface of "content.xml". 
	 *
	 * @param odfName
	 *            file path of ODF document to parse
	 * @return Document.
	 */	
	public ODFDocument getDocument(String odfName) {
		ODFDocument contentDoc = getDocument(odfName,
				ODFConstants.ODF_CONTENT_FILENAME);
		ODFDocument styleDoc = getDocument(odfName,
				ODFConstants.ODF_STYLE_FILENAME);

		if (contentDoc != null) {
			contentDoc.setStyleDocument(styleDoc);
			contentDoc.setURL(odfName);
		}

		return contentDoc;
	}

	/**
	 * Returns file name list compressed in ODF document. 
	 *
	 * @param odfName
	 *            file path of ODF document to parse
	 * @return file name list.
	 */		
	public String[] getFileEntries(String odfName) {
		ZipFile zipFile = null;

		URL url = null;
		try {
			url = new URL(odfName);
		} catch (MalformedURLException e) {
		}

		try {
			if (url != null) {
				zipFile = new ZipFile(url.getPath());
			} else {
				zipFile = new ZipFile(odfName);
			}
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			List<String> list = new ArrayList<String>();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String name = entry.getName();
				list.add(name);
			}
			zipFile.close();
			return list.toArray(new String[list.size()]);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * Extract and copy file compressed in ODF document. 
	 *
	 * @param odfName
	 *            file path of ODF document to parse
	 * @param entry
	 *            file name compressed in ODF document
	 * @param outputFileName
	 *            file path to copy file extracted from ODF document
	 */			
	public void copyFile(String odfName, String entry, String outputFileName) {
		InputStream is = getFileInputStream(odfName, entry);
		FileUtils.saveToFile(is, outputFileName, true);
	}
}
