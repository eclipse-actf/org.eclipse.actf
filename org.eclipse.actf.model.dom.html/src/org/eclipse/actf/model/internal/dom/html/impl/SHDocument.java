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

package org.eclipse.actf.model.internal.dom.html.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import org.eclipse.actf.model.internal.dom.html.util.UnsynchronizedHashtable;
import org.eclipse.actf.model.internal.dom.sgml.impl.ElementDefinition;
import org.eclipse.actf.model.internal.dom.sgml.impl.SGMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTitleElement;

@SuppressWarnings("nls")
public class SHDocument extends SGMLDocument implements HTMLDocument {
	private static final String NAME = "name";

	private static final String ID = "id";

	private static final long serialVersionUID = 3204066329029237578L;

	public SHDocument() {
		this((SHDOMImpl) SHDOMImpl.getDOMImplementation());
	}

	SHDocument(SHDOMImpl imple) {
		super(imple);
	}

	/**
	 * @serial
	 */
	private String cookie;

	public void close() { /* do not support */
	}

	HTMLCollection createCollection(final NodeList list) {
		return new HTMLCollection() {
			public int getLength() {
				return list.getLength();
			}

			public Node item(int index) {
				return list.item(index);
			}

			public Node namedItem(String name) {
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (!(node instanceof Element))
						continue;
					if (name
							.equalsIgnoreCase(((Element) node).getAttribute(ID))) {
						return node;
					}
				}
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (!(node instanceof Element))
						continue;
					if (name.equalsIgnoreCase(((Element) node)
							.getAttribute(NAME))) {
						ElementDefinition def = getDTD().getElementDefinition(
								node.getNodeName());
						if (def != null && def.getAttributeDef(NAME) != null) {
							return node;
						}
					}
				}
				return null;
			}

			public String toString() {
				return list.toString();
			}
		};
	}

	HTMLCollection createCollection(final Node nodes[], final int len) {
		return new HTMLCollection() {
			public int getLength() {
				return len;
			}

			public Node item(int index) {
				return 0 <= index && index < len ? nodes[index] : null;
			}

			public Node namedItem(String name) {
				for (int i = 0; i < len; i++) {
					Node node = nodes[i];
					if (!(node instanceof Element))
						continue;
					if (name
							.equalsIgnoreCase(((Element) node).getAttribute(ID))) {
						return node;
					}
				}
				for (int i = 0; i < len; i++) {
					Node node = nodes[i];
					if (!(node instanceof Element))
						continue;
					if (name.equalsIgnoreCase(((Element) node)
							.getAttribute(NAME))) {
						ElementDefinition def = getDTD().getElementDefinition(
								node.getNodeName());
						if (def != null && def.getAttributeDef(NAME) != null) {
							return node;
						}
					}
				}
				return null;
			}
		};
	}

	private static UnsynchronizedHashtable constructors;
	static {
		constructors = new UnsynchronizedHashtable();
		constructors.put("COL", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableColElement(tagName, doc);
			}
		});
		constructors.put("COLGROUP", constructors.get("COL"));
		constructors.put("THEAD", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableSectionElement(tagName, doc);
			}
		});
		constructors.put("TFOOT", constructors.get("THEAD"));
		constructors.put("CAPTION", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableCaptionElement(tagName, doc);
			}
		});
		constructors.put("TH", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableCellElement(tagName, doc);
			}
		});
		constructors.put("HTML", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHHtmlElement(tagName, doc);
			}
		});
		constructors.put("BASE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHBaseElement(tagName, doc);
			}
		});
		constructors.put("STYLE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHStyleElement(tagName, doc);
			}
		});
		constructors.put("TITLE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTitleElement(tagName, doc);
			}
		});
		constructors.put("BODY", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHBodyElement(tagName, doc);
			}
		});
		constructors.put("H6", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHHeadingElement(tagName, doc);
			}
		});
		constructors.put("HEAD", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHHeadElement(tagName, doc);
			}
		});
		constructors.put("SCRIPT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHScriptElement(tagName, doc);
			}
		});
		constructors.put("LINK", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHLinkElement(tagName, doc);
			}
		});
		constructors.put("FRAMESET", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHFrameSetElement(tagName, doc);
			}
		});
		constructors.put("BASEFONT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHBaseFontElement(tagName, doc);
			}
		});
		constructors.put("INS", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHModElement(tagName, doc);
			}
		});
		constructors.put("DEL", constructors.get("INS"));
		constructors.put("IFRAME", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHIFrameElement(tagName, doc);
			}
		});
		constructors.put("FIELDSET", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHFieldSetElement(tagName, doc);
			}
		});
		constructors.put("DIR", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHDirectoryElement(tagName, doc);
			}
		});
		constructors.put("META", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHMetaElement(tagName, doc);
			}
		});
		constructors.put("HR", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHHRElement(tagName, doc);
			}
		});
		constructors.put("MENU", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHMenuElement(tagName, doc);
			}
		});
		constructors.put("OBJECT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHObjectElement(tagName, doc);
			}
		});
		constructors.put("APPLET", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHAppletElement(tagName, doc);
			}
		});
		constructors.put("PARAM", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHParamElement(tagName, doc);
			}
		});
		constructors.put("MAP", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHMapElement(tagName, doc);
			}
		});
		constructors.put("AREA", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHAreaElement(tagName, doc);
			}
		});
		constructors.put("LEGEND", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHLegendElement(tagName, doc);
			}
		});
		constructors.put("ISINDEX", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHIsIndexElement(tagName, doc);
			}
		});
		constructors.put("OPTGROUP", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHOptGroupElement(tagName, doc);
			}
		});
		constructors.put("TEXTAREA", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTextAreaElement(tagName, doc);
			}
		});
		constructors.put("FORM", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHFormElement(tagName, doc);
			}
		});
		constructors.put("SELECT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHSelectElement(tagName, doc);
			}
		});
		constructors.put("LABEL", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHLabelElement(tagName, doc);
			}
		});
		constructors.put("H5", constructors.get("H6"));
		constructors.put("BUTTON", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHButtonElement(tagName, doc);
			}
		});
		constructors.put("INPUT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHInputElement(tagName, doc);
			}
		});
		constructors.put("OPTION", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHOptionElement(tagName, doc);
			}
		});
		constructors.put("QUOTE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHQuoteElement(tagName, doc);
			}
		});
		constructors.put("H4", constructors.get("H6"));
		constructors.put("PRE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHPreElement(tagName, doc);
			}
		});
		constructors.put("H3", constructors.get("H6"));
		constructors.put("BR", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHBRElement(tagName, doc);
			}
		});
		constructors.put("TABLE", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableElement(tagName, doc);
			}
		});
		constructors.put("TBODY", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableSectionElement(tagName, doc);
			}
		});
		constructors.put("DL", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHDListElement(tagName, doc);
			}
		});
		constructors.put("H2", constructors.get("H6"));
		constructors.put("UL", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHUListElement(tagName, doc);
			}
		});
		constructors.put("OL", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHOListElement(tagName, doc);
			}
		});
		constructors.put("TR", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHTableRowElement(tagName, doc);
			}
		});
		constructors.put("LI", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHLIElement(tagName, doc);
			}
		});
		constructors.put("IMG", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHImageElement(tagName, doc);
			}
		});

		constructors.put("DIV", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHDivElement(tagName, doc);
			}
		});
		constructors.put("H1", constructors.get("H6"));
		constructors.put("P", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHParagraphElement(tagName, doc);
			}
		});
		constructors.put("FONT", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHFontElement(tagName, doc);
			}
		});
		constructors.put("A", new Constructor() {
			public SHElement newInstance(String tagName, SHDocument doc) {
				return new SHAnchorElement(tagName, doc);
			}
		});
		constructors.put("TD", constructors.get("TH"));
	}

	public Element createElement(String tagName) {
		Constructor constructor = constructors.get(tagName);
		return constructor != null ? constructor.newInstance(tagName, this)
				: new SHElement(tagName, this);
	}

	public HTMLCollection getLinks() {
		int len = 0;
		Node links[] = new Node[32];
		Node tmp1, tmp2;
		Node root = getDocumentElement();
		tmp1 = root;
		outer: while (tmp1 != null) {
			if (tmp1 instanceof Element) {
				String name = tmp1.getNodeName();
				if (name.equalsIgnoreCase("AREA")
						|| (name.equalsIgnoreCase("A") && ((Element) tmp1)
								.getAttributeNode("href") != null)) {
					if (len == links.length) {
						Node buf[] = new Node[len * 2];
						System.arraycopy(links, 0, buf, 0, len);
						links = buf;
					}
					links[len++] = tmp1;
				}
			}
			if ((tmp2 = tmp1.getFirstChild()) == null) {
				if (tmp1 == root) {
					break outer;
				} else {
					tmp2 = tmp1.getNextSibling();
				}
			}
			while (tmp2 == null) {
				tmp1 = tmp2 = tmp1.getParentNode();
				if (tmp1 != root) {
					tmp2 = tmp1.getNextSibling();
				} else {
					break outer;
				}
			}
			tmp1 = tmp2;
		}
		return createCollection(links, len);
	}

	public HTMLCollection getApplets() {
		return createCollection(getElementsByTagName("APPLET"));
	}

	public String getCookie() {
		return cookie;
	}

	public String getDomain() {
		return null;
	}

	/*
	replaced for performance reason @2009/06/25 by dsato@jp.ibm.com
	public Element getElementById(String elementId) {
		Node tmp1, tmp2;
		Node root = getDocumentElement();
		tmp1 = root;
		while (tmp1 != null) {
			if (tmp1 instanceof Element
					&& elementId.equals(((Element) tmp1).getAttribute(ID))) {
				return (Element) tmp1;
			}
			if ((tmp2 = tmp1.getFirstChild()) == null) {
				if (tmp1 == root) {
					return null;
				} else {
					tmp2 = tmp1.getNextSibling();
				}
			}
			while (tmp2 == null) {
				tmp1 = tmp2 = tmp1.getParentNode();
				if (tmp1 != root) {
					tmp2 = tmp1.getNextSibling();
				} else {
					return null;
				}
			}
			tmp1 = tmp2;
		}
		return null;
	}*/

	public NodeList getElementsByName(String elementName) {
		if (elementName.equals("*")) {
			return new NodeList() {
				public int getLength() {
					return 0;
				}

				public Node item(int index) {
					return null;
				}
			};
		} else {
			return getElementsByTagName(elementName);
		}
	}

	public HTMLCollection getForms() {
		return createCollection(getElementsByName("FORM"));
	}

	public HTMLCollection getImages() {
		return createCollection(getElementsByName("IMG"));
	}

	public HTMLCollection getAnchors() {
		int len = 0;
		Node links[] = new Node[32];
		Node tmp1, tmp2;
		Node root = getDocumentElement();
		tmp1 = root;
		outer: while (tmp1 != null) {
			if (tmp1 instanceof Element) {
				String name = tmp1.getNodeName();
				if ((name.equalsIgnoreCase("A") && ((Element) tmp1)
						.getAttributeNode(NAME) != null)) {
					if (len == links.length) {
						Node buf[] = new Node[len * 2];
						System.arraycopy(links, 0, buf, 0, len);
						links = buf;
					}
					links[len++] = tmp1;
				}
			}
			if ((tmp2 = tmp1.getFirstChild()) == null) {
				if (tmp1 == root) {
					break outer;
				} else {
					tmp2 = tmp1.getNextSibling();
				}
			}
			while (tmp2 == null) {
				tmp1 = tmp2 = tmp1.getParentNode();
				if (tmp1 != root) {
					tmp2 = tmp1.getNextSibling();
				} else {
					break outer;
				}
			}
			tmp1 = tmp2;
		}
		return createCollection(links, len);
	}

	public String getReferrer() {
		return null;
	}

	public String getTitle() {
		if (null == getDocumentElement())
			return null;
		NodeList titles = getElementsByTagName("TITLE");
		if (titles.getLength() == 0)
			return null;
		HTMLTitleElement te = (HTMLTitleElement) titles.item(0);
		Node title = te.getFirstChild();
		if (title == null || !(title instanceof Text))
			return null;
		return title.getNodeValue();
	}

	private URL url;

	public String getURL() {
		return url != null ? url.toString() : null;
	}

	/**
	 * @param url
	 *            URL of this document.
	 */
	public void setURL(URL url) {
		this.url = url;
	}

	public void open() { /* do not support */
	}

	/**
	 * @return if this document does not have <code>BODY</code> or
	 *         <code>FRAMESET
	 * </code>, null.
	 */
	public HTMLElement getBody() {
		if (getDocumentElement() == null)
			return null;
		NodeList bodies = getElementsByTagName("FRAMESET");
		if (bodies.getLength() == 0) {
			bodies = getElementsByTagName("BODY");
		}
		if (bodies.getLength() == 0)
			return null;
		return (HTMLElement) bodies.item(0);
	}

	public void setBody(HTMLElement body) {
		HTMLElement oldBody = getBody();
		if (oldBody == null) {
			getDocumentElement().insertBefore(body, null);
		} else {
			oldBody.getParentNode().replaceChild(body, oldBody);
		}
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public void setTitle(String title) {
		NodeList titles = getElementsByTagName("TITLE");
		if (titles.getLength() == 0)
			return;
		HTMLTitleElement te = (HTMLTitleElement) titles.item(0);
		te.setText(title);
	}

	public void write(String text) { /* do not support */
	}

	public void writeln(String text) { /* do not support */
	}

	/**
	 * Prints this document as XHTML format. At the beginning of the document
	 * this method generates is a DOCTYPE declaration. Its contents is
	 * determined as follows.
	 * <ul>
	 * <li>if this document's doctype is "-//W3C//DTD HTML 4.0 Frameset//EN",
	 * then "-//W3C//DTD XHTML 1.0 Frameset//EN"
	 * <li>else if "-//W3C//DTD HTML 4.0//EN" then "-//W3C//DTD XHTML 1.0
	 * Strict//EN"
	 * <li>Otherwise, "-//W3C//DTD XHTML 1.0 Transitional//EN"
	 * </ul>
	 * 
	 * @param pw
	 *            PrintWriter to write
	 * @param indent
	 *            if <code>true</code>, indents
	 * @param enc
	 *            encoding of output
	 */
	public void printAsXHTML(PrintWriter pw, boolean indent, String enc)
			throws IOException {
		String dtdID = getDTD().toString();
		if (dtdID.equals("-//W3C//DTD HTML 4.0 Frameset//EN")) {
			printAsXML("-//W3C//DTD XHTML 1.0 Frameset//EN", new URL(
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd"), pw,
					indent, enc);
		} else if (dtdID.equals("-//W3C//DTD HTML 4.0//EN")) {
			printAsXML("-//W3C//DTD XHTML 1.0 Strict//EN", new URL(
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"), pw,
					indent, enc);
		} else {
			printAsXML("-//W3C//DTD XHTML 1.0 Transitional//EN", new URL(
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"),
					pw, indent, enc);
		}
	}
}
