/*******************************************************************************
 * Copyright (c) 1998, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.html.parser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.actf.model.dom.html.DocumentTypeUtil;
import org.eclipse.actf.model.dom.html.IErrorLogListener;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.dom.html.IParser;
import org.eclipse.actf.model.dom.html.IParserError;
import org.eclipse.actf.model.dom.html.ParseException;
import org.eclipse.actf.model.dom.html.dochandler.PREHandler;
import org.eclipse.actf.model.dom.html.errorhandler.FramesetErrorHandler;
import org.eclipse.actf.model.dom.html.errorhandler.HTMLErrorHandler;
import org.eclipse.actf.model.internal.dom.html.impl.SHDOMImpl;
import org.eclipse.actf.model.internal.dom.html.impl.SHDocument;
import org.eclipse.actf.model.internal.dom.html.util.JapaneseEncodingDetector;
import org.eclipse.actf.model.internal.dom.html.util.RereadableInputStream;
import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDOMImplementation;
import org.xml.sax.SAXException;

/**
 * HTML Parser. To use this class,
 * <ol>
 * <li>Construct HTMLParser instance with a file to read.
 * <li>call parse(...) method. If an error occurred, <code>ParseException</code>
 * or IOException raises.
 * <li>get <code>Document</code> instance by calling {@link #getDocument()}. If
 * a <code>ParseException</code> was thrown, the returned instance holds sub DOM
 * created from sub document to the error point.
 * </ol>
 * For example,
 * 
 * <pre>
 * &lt;code&gt;
 * HTMLParser parser = new HTMLParser();
 * try {
 * 	parser.parse(new FileInputStream(&quot;xxx.html&quot;));
 * } catch (ParseException e) {
 * 	e.printStackTrace();
 * } catch (IOException e) {
 * 	e.printStackTrace();
 * }
 * org.w3c.Document doc = parser.getDocument();
 * &lt;/code&gt;
 * </pre>
 * 
 */
public class HTMLParser extends SGMLParser implements IHTMLParser {
	static {
		InputStream is = null;
		is = HTMLParser.class.getResourceAsStream("public_entities.properties"); //$NON-NLS-1$
		if (is != null) {
			Properties map = new Properties();
			try {
				map.load(is);
				// pubEntityMap.putAll(map);
				Enumeration<Object> keys = map.keys(); // CRS
				while (keys.hasMoreElements()) { // CRS
					String aKey = (String) keys.nextElement(); // CRS
					String replaceKey = aKey.replace('@', ' '); // CRS
					pubEntityMap.put(replaceKey, (String) map.get(aKey)); // CRS
				} // CRS
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public RereadableInputStream ris;

	public InputStreamReader isReader;

	InputStreamReader getISReader() {
		return isReader;
	}

	/**
	 * Constructs HTMLParser instance whose defaultDTD is
	 * <code>"-//W3C//DTD HTML 4.01 Transitional//EN"</code>. And also,
	 * {@link HTMLErrorHandler} and {@link FramesetErrorHandler} instances are
	 * added.
	 */
	@SuppressWarnings("deprecation")
	public HTMLParser() {
		defaultDTD = "-//W3C//DTD HTML 4.01 Transitional//EN"; //$NON-NLS-1$
		addErrorHandler(new FramesetErrorHandler());
		addErrorHandler(new HTMLErrorHandler());
		setDocumentHandler(new PREHandler(this));
		setDOMImplementation(SHDOMImpl.getDOMImplementation());
		if (getDOMImplementation() == null) {
			setDocument(new SHDocument());
		}
	}

	protected Reader getResource(String resourceName) throws IOException {
		InputStream is = IHTMLParser.class.getResourceAsStream("dtd/" //$NON-NLS-1$
				+ resourceName);
		if (is != null) {
			return new InputStreamReader(is);
		} else {
			return super.getResource(resourceName);
		}
	}

	/**
	 * Reads files and print their top elements. This method is just for test.
	 * usage: java org.eclipse.actf.model.dom.html.HTMLParser [options] files...
	 * <br>
	 * options:
	 * <DL>
	 * <DT>-e encoding
	 * <DD>specify character encoding to <code>encoding</code>
	 * <DT>-c
	 * <DD>If it meets
	 * <code> &lt;META http-equiv="Content-Type" content="text/html;
	 * charset=xxx"&gt;</code> tag, change encoding to <code>xxx</code>
	 * <DT>-d
	 * <DD>Dump results.
	 * <DT>-o output file
	 * <DT>-x [dtd]
	 * <DD>Dump as xml format.
	 * <DT>-w?
	 * <DD>warning
	 * 
	 * @param args
	 *            command line argument.
	 */
	@SuppressWarnings("nls")
	public static void main(String args[]) {
		boolean dump = false;
		String targetFileName = null;
		int length = args.length;
		String encoding = null;
		boolean ce = false;
		boolean indent = false;
		int warning = -1;
		boolean physical = false;
		boolean xml = false;
		String list = null;
		boolean keepUnknown = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-d")) {
				dump = true;
				length--;
			} else if (args[i].equals("-o")) {
				i++;
				targetFileName = args[i];
				length -= 2;
			} else if (args[i].equals("-e")) {
				if (++i == args.length)
					usage();
				encoding = args[i];
				length -= 2;
			} else if (args[i].equals("-c")) {
				ce = true;
				length--;
			} else if (args[i].equals("-x")) {
				xml = true;
				length--;
			} else if (args[i].equals("-i")) {
				indent = true;
				length--;
			} else if (args[i].startsWith("-w")) {
				if (args[i].length() == 2) {
					warning = 0;
				} else {
					try {
						warning = Integer.parseInt(args[i].substring(2));
					} catch (NumberFormatException e) {
						usage();
					}
				}
				length--;
			} else if (args[i].startsWith("-ku")) {
				keepUnknown = true;
				length--;
			} else if (args[i].equals("-physical")) {
				physical = true;
				length--;
			} else if (args[i].startsWith("-l") && args[i].length() > 2) {
				list = args[i].substring(2);
				length--;
			} else {
				break;
			}
		}
		if (length == 0) {
			usage();
		}
		HTMLParser parser;
		for (int i = args.length - length; i < args.length; i++) {
			try {
				parser = new HTMLParser();
				parser.keepUnknownElements(keepUnknown);
				if (warning != -1) {
					if (warning == 0) {
						parser.addErrorLogListener(new IErrorLogListener() {
							public void errorLog(int code, String msg) {
								System.err.println(msg);
							}
						});
					} else {
						final int errorKind = warning;
						parser.addErrorLogListener(new IErrorLogListener() {
							public void errorLog(int code, String msg) {
								if (code == errorKind) {
									System.err.println(msg);
								}
							}
						});
					}
				}
				if (physical) {
					parser.elementHandle(false);
				}
				if (xml) {
					parser.setDefaultTagCase(IParser.LOWER_CASE);
					parser.setTagCase(IParser.LOWER_CASE);
					parser.setAttrNameCase(IParser.LOWER_CASE);
				}
				InputStream is;
				if (args[i].equals("-u")) {
					i++;
					java.net.URL url = new java.net.URL(args[i]);
					is = url.openStream();
				} else {
					is = new FileInputStream(args[i]);
				}
				System.err.println("parsing " + args[i] + " ...");
				try {
					if (ce) {
						parser.parseSwitchEnc(is, encoding);
						System.out.println("Encoding: " + parser.getEncoding());
					} else {
						parser.parse(is, encoding);
						System.err.println("Doctype:" + parser.getDocument().getDoctype());
						System.err.println(
								"Org Doctype:" + DocumentTypeUtil.getOriginalID(parser.getDocument().getDoctype()));
						System.err.println();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
				parser.close();
				if (dump) {
					PrintWriter pw;
					OutputStream os = targetFileName == null ? (OutputStream) System.out
							: new FileOutputStream(targetFileName);
					if (encoding != null) {
						pw = new PrintWriter(new OutputStreamWriter(os, encoding));
					} else {
						pw = new PrintWriter(os);
					}
					if (!xml) {
						((SHDocument) parser.getDocument()).printAsSGML(pw, indent);
					} else {
						((SHDocument) parser.getDocument()).printAsXHTML(pw, indent, encoding);
					}
				} else if (list != null) {
					NodeList nodeList = parser.getDocument().getElementsByTagName(list);
					for (i = 0; i < nodeList.getLength(); i++) {
						System.out.println(nodeList.item(i));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Parses an HTML document and return its top element.
	 * 
	 * @param is
	 *            inputstream to parse with default encoding.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, thrown
	 * @exception IOException
	 */
	public Node parse(InputStream is) throws ParseException, IOException, SAXException {

		JapaneseEncodingDetector JED = new JapaneseEncodingDetector(is);
		try {
			encoding = JED.detect();
			// TODO check if html5 or not
			if (JED.hasBOM()) {
				error(IParserError.BOM, "Byte-Order Mark (BOM) found in UTF-8 HTML file.");
			}
		} catch (IOException e) {
			throw (e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}

		try {
			return parse(JED.getInputStream(), encoding);
		} catch (Exception e) {
			isReader = new InputStreamReader(JED.getInputStream());
			return parse(isReader);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Parses a HTML document and return its top element.
	 * 
	 * @param is
	 *            inputstream to parse.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, throwed
	 * @exception IOException
	 */
	public Node parse(InputStream is, String charEncoding) throws SAXException, ParseException, IOException {
		if (charEncoding == null) {
			isReader = new InputStreamReader(is);
		} else {
			this.encoding = charEncoding;
			isReader = new InputStreamReader(is, charEncoding);
		}
		Node result = parse(isReader);
		try {
			is.close();
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Parses a HTML document and return its top element. This method is almost
	 * same as {@link #parse(InputStream) parse(InputStream)}. If it meets
	 * <code> &lt;META http-equiv="Content-Type" 
	 * content="text/html; charset=xxx"&gt;</code> tag in a document, it tries
	 * to change encoding to <code>xxx</code>.
	 * 
	 * @param is
	 *            inputstream to parse
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, throwed
	 * @exception IOException
	 */
	public Node parseSwitchEnc(InputStream is) throws ParseException, IOException, SAXException {
		return parseSwitchEnc(is, null);
	}

	/**
	 * Parses a HTML document and return its top element. This method is the
	 * same as {@link #parse(InputStream,String) parse(InputStream,String)} If
	 * it meets <code> &lt;META http-equiv="Content-Type"
	 * content="text/html; charset=xxx"&gt;</code> tag in a document, it tries
	 * to change encoding to <code>xxx</code>.
	 * 
	 * @param is
	 *            inputstream to parse
	 * @param defaultEncoding
	 *            default encoding before switching encoding.
	 * @return Top element.
	 * @exception PaserException
	 *                If unrecoverable syntax or token error occurred, throwed
	 * @exception IOException
	 */
	public Node parseSwitchEnc(InputStream is, String defaultEncoding)
			throws SAXException, ParseException, IOException {
		ris = new RereadableInputStream(is);
		setDocumentHandler(new CharsetHandler(this, ris));
		try {
			if (defaultEncoding == null) {
				isReader = new InputStreamReader(ris);
			} else {
				encoding = defaultEncoding;
				isReader = new InputStreamReader(ris, defaultEncoding);
			}
			return parse(isReader);
		} catch (EncodingException e) {
			// clear created child nodes.
			if (setDOMImplementation(getDOMImplementation()) == null) {
				Document doc = getDocument();
				while (doc.hasChildNodes()) {
					doc.removeChild(doc.getFirstChild());
				}
			}
			init();
			encoding = e.getNewReader().getEncoding();
			return parse(e.getNewReader());
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
	}

	protected Document createDocument(DocumentType docType) {
		DOMImplementation domImpl = getDOMImplementation();
		try {
			if (!(domImpl instanceof HTMLDOMImplementation)) {
				return super.createDocument(docType);
			}
		} catch (Error e) {
			return super.createDocument(docType);
		}
		Document ret = ((HTMLDOMImplementation) domImpl).createHTMLDocument("dummy"); //$NON-NLS-1$
		if (ret.getDocumentElement() != null) {
			ret.removeChild(ret.getDocumentElement());
		}
		if (docType != null) {
			try {
				ret.insertBefore(docType, null);
			} catch (DOMException e) {
			}
		}
		return ret;
	}

	public String encoding = null;

	/**
	 * Character Encoding parsed a document with.
	 * 
	 * @return If null parsed a document with default encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	void setEncoding(String enc) {
		this.encoding = enc;
	}

	private static void usage() {
		System.out.println(
				"usage java org.eclipse.actf.model.dom.html.parser.HTMLParser [-w[#]] [-d] [-ku] [-e encoding] [-c] files..."); //$NON-NLS-1$
		System.exit(1);
	}

	/**
	 * Always returns "HTML"
	 * 
	 * @return "HTML"
	 */
	protected String getDefaultTopElement() {
		return "HTML"; //$NON-NLS-1$
	}
}
