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

package org.eclipse.actf.examples.michecker.smil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.actf.examples.michecker.caption.CaptionData;
import org.eclipse.actf.examples.michecker.caption.CaptionDataFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * SMIL viewer front end. See main() for usage.
 * 
 */
public class SMILReader {
	private final boolean DEBUG = false;
	private URL fileUrl;
	private Vector<CaptionData> captions;

	/**
	 * reads and parses a file with given <code>filePath</code>.
	 * 
	 * @param filePath
	 *            {@link URL} and local file path are allowed
	 * @return true if it successes reading given file.
	 */
	public boolean read(String filePath) {
		try {
			fileUrl = new URL(filePath);
		} catch (MalformedURLException e1) {
			if (DEBUG)
				System.err
						.println("- failed to treat given path as URL. try to interpret as local file path...");
			URI uri = (new File(filePath)).toURI();
			try {
				fileUrl = uri.toURL();
			} catch (MalformedURLException e) {
				if (DEBUG)
					System.err
							.println("- failed to treat as local file. I give it up. abort.");
				return false;
			}
		}
		assert fileUrl != null;
		if (DEBUG)
			System.out.println("-- reading " + filePath + "...");
		captions = new Vector<CaptionData>();
		try {
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setExpandEntityReferences(false);
			df.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			DocumentBuilder db = df.newDocumentBuilder();
			if (DEBUG) {
				System.out.println(new SimpleDateFormat("HH:mm:ss.SSS")
						.format(new Date()) + " parsing start");
			}
			Document doc = db.parse(filePath);
			if (DEBUG) {
				System.out.println(new SimpleDateFormat("HH:mm:ss.SSS")
						.format(new Date()) + " parsing end");
				System.out.println("--- checking file format...");
			}
			Element root = doc.getDocumentElement();
			if (DEBUG) {
				System.out.println(root.getLocalName());
				System.out.println(root.getTagName());
				System.out.println(root.getNamespaceURI());
				System.out.println(root.getNodeName());
			}
			NodeList headNl = root.getElementsByTagName("head");
			NodeList bodyNl = root.getElementsByTagName("body");
			if (headNl.getLength() == 1 && bodyNl.getLength() == 1) {
				if (DEBUG)
					System.out.println("  OK");
			} else {
				if (DEBUG)
					System.out.println("  NG");
				return false;
			}
			if (DEBUG)
				System.out.println("--- reading metadata...");
			NodeList metaNl = ((Element) headNl.item(0))
					.getElementsByTagName("meta");
			for (int i = 0; i < metaNl.getLength(); i++) {
				Element meta = (Element) (metaNl.item(i));
				if (DEBUG)
					System.out.println("  " + meta.getAttribute("name") + ": "
							+ meta.getAttribute("content"));
			}
			if (DEBUG)
				System.out.println("--- reading structure...");
			Element body = (Element) bodyNl.item(0);
			parseElement(0.0, 1, body);
			if (DEBUG)
				System.out.println("-- reading completed.");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Parses a par, seq, or excl container and returns a relative end time.
	 * 
	 * @param time
	 *            The global time when this media starts.
	 * @param container
	 * @returns The global when this media ends.
	 */
	private double parseElement(double time, int indent, Element e) {
		String name = e.getTagName().toLowerCase();
		double dur = Double.NaN;

		// indentation
		for (int j = 0; j < indent; j++) {
			// System.out.print("  ");
		}
		// System.out.print(time + ": " + name);

		// if not supported, warn and return
		if (!isSupported(name)) {
			// System.err.println("- unsupported element: " + name);
			// System.out.println("");
			return time;
		}

		if (hasSynchAttributes(name)) {
			String durStr = e.getAttribute("dur");
			// System.out.print(" [" + e.getAttribute("begin") + "]");
			// System.out.print(" [" + durStr + "]");
			if (durStr.length() > 0)
				dur = ClockValueParser.parseDuration(e.getAttribute("dur"));
		}
		// System.out.println("");

		// create a CaptionData instance
		if (name.matches("text(stream)?")) {
			try {
				Vector<CaptionData> v = CaptionDataFactory.createCaptionData(
						time, new URL(fileUrl, e.getAttribute("src")));
				if (v != null) {
					captions.addAll(v);
				}
			} catch (MalformedURLException e1) {
				// System.err.println("- invalid path for text file. skipped.");
			}
		}

		// show children
		NodeList nl = e.getChildNodes();
		double childBegin = time;
		double childEnd = time;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				double endTime = parseElement(childBegin, indent + 1,
						(Element) nl.item(i));
				if (name.matches("body|par|switch")) {
					childEnd = Math.max(childEnd, endTime);
				} else if (name.equals("seq")) {
					childBegin = childEnd = endTime;
				} else { // media element
					; // do nothing
				}
			}
		}
		if (Double.isNaN(dur))
			return childEnd;
		else
			return time + dur;
	}

	@SuppressWarnings("unused")
	private boolean isTimeContainer(String tagName) {
		return tagName.matches("body|par|seq");
	}

	private boolean isSupported(String tagName) {
		return tagName
				.matches("body|seq|par|img|audio|text|param|video|switch|textstream");
	}

	private boolean hasSynchAttributes(String tagName) {
		return tagName.matches("body|seq|par|img|audio|text|video|textstream"); // param
																				// is
		// excluded
	}

	public Vector<CaptionData> getCaptions() {
		return captions;
	}

	public static void main(String[] args) {
		SMILReader viewer = new SMILReader();
		boolean b = viewer.read(args[0]);
		System.out.println(b);
		if (b) {
			System.out.println("\n-- showing captions...");
			Vector<CaptionData> cp = viewer.getCaptions();
			for (CaptionData captionData : cp) {
				System.out.println(captionData.getTime() + ": "
						+ captionData.getCaptionText());
			}
		}
		System.out.println("-- done.");
	}
}
