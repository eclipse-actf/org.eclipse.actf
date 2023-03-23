/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker.caption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.actf.examples.michecker.smil.ClockValueParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CaptionDataFactory {
	/**
	 * Flag that turns verbose output on.
	 */
	private static final boolean DEBUG = false;

	/**
	 * creates one or more {@link CaptionData} instances included in the file
	 * with the given {@link URL}.
	 * 
	 * @return
	 */
	public static Vector<CaptionData> createCaptionData(double time,
			URL captionSrc) {
		try {
			if (DEBUG)
				System.out.println("--- checking caption file " + captionSrc
						+ "...");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					captionSrc.openStream()));
			String line = br.readLine();
			br.close();
			if (line.startsWith("<window")) {
				// RealText file
				if (DEBUG)
					System.out.println("--- It seems to be a RealText file.");
				Vector<CaptionData> cdv = createRealTextCaptions(time,
						captionSrc);
				return cdv;
			} else {
				// simple text file
				if (DEBUG)
					System.out
							.println("--- It seems to be a simple text file.");
				CaptionData cd = new CaptionData(time, captionSrc);
				Vector<CaptionData> cdv = new Vector<CaptionData>();
				cdv.add(cd);
				return cdv;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates a {@link Vector} of {@link CaptionData} included in the given
	 * RealText file
	 * 
	 * @return
	 */
	private static Vector<CaptionData> createRealTextCaptions(double time,
			URL url) {
		System.out.println("--- parsing...");
		Vector<CaptionData> captions = new Vector<CaptionData>();
		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(url.toExternalForm());
			NodeList nl = doc.getDocumentElement().getElementsByTagName("time");
			for (int i = 0; i < nl.getLength(); i++) {
				Element t = (Element) nl.item(i);
				double tt = ClockValueParser.parseDuration(t
						.getAttribute("begin"));
				System.out.print(tt + " -> " + (tt + time) + ": ");
				String content = getTextContent(t);
				System.out.println(content);
				captions.add(new CaptionData(tt + time, content));
			}
			System.out.println("--- done.");
			return captions;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Obtain a text after the given <time> element and before the next <time>
	 * element.
	 * 
	 * @param t
	 *            <code>time</code> element
	 */
	private static String getTextContent(Element t) {
		StringBuffer content = new StringBuffer();
		Node n = t.getNextSibling();
		while (n != null
				&& (n.getNodeType() != Node.ELEMENT_NODE || ((Element) n)
						.getTagName() != "time")) {
			if (n.getNodeType() == Node.TEXT_NODE) {
				content.append(n.getTextContent().trim());
			} else if (n.getNodeType() == Node.ELEMENT_NODE
					&& ((Element) n).getTagName() == "br") {
				content.append(" ");
			}
			n = nextNode(n);
		}
		return content.toString();
	}

	private static Node nextNode(Node n) {
		Node next;
		if ((next = n.getFirstChild()) != null)
			return next;

		// no child
		while (n != null && n.getNextSibling() == null) {
			n = n.getParentNode();
		}
		return (n != null) ? n.getNextSibling() : null;
	}

	public static void main(String[] args) {
		try {
			/*
			 * CaptionDataFactory.createCaptionData(100, new URL(
			 * "file:///C:/home/ar/JIS/testcases/smil/CSUN/CESDemo.rt"));
			 */
			CaptionDataFactory
					.createCaptionData(
							100,
							new URL(
									"file:///C:/home/ar/JIS/testcases/smil/MAGpie/magpie2_demo.en_US.real.rt"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
