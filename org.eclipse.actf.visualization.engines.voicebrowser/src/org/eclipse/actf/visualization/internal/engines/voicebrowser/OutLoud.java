/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


public class OutLoud {

	private static final String DEFAULT = "default"; //$NON-NLS-1$
	private static final String GREATER = ">"; //$NON-NLS-1$
	private static final String ATTRIBUTE_EQUAL = "<attribute="; //$NON-NLS-1$
	private static final String VAR = "<var>"; //$NON-NLS-1$
	
	public static int jwat_mode = IVoiceBrowserController.HPR_MODE;
	public static boolean hprDefltMsg = true;
	public static String name = DEFAULT;
	public static String description = null;
	public static String topofpage = null;
	public static String endofpage = null;
	public static String nocurlink = null;
	public static String noprevlink = null;
	public static String nonextlink = null;
	public static String nostring = null;
	public static String notonalink = null;

	/**
	 * Method createMessageCollection.
	 * @param mode
	 * @param xmlpath
	 * @return MessageCollection
	 */
	public static MessageCollection createMessageCollection(
		int mode,
		String xmlpath) {
		MessageCollection mc = null;
		if (xmlpath != null && xmlpath.length() > 0) {
			mc = createSiteMessageCollection(xmlpath);
			mc = createApplMessageCollection(mc, mode, xmlpath);
		}
		return mc;
	}

	/**
	 * Method createSiteMessageCollection.
	 * @param xmlpath
	 * @return MessageCollection
	 */
	@SuppressWarnings("nls")
	private static MessageCollection createSiteMessageCollection(String xmlpath) {
		MessageCollection mc = new MessageCollection();

		String path = xmlpath + File.separator;
		String uriPrefix = null;
		try {
			File dir = new File(xmlpath);
			String ls[] = dir.list();
			for (int i = 0; i < ls.length; i++) {
				if (ls[i].toLowerCase().startsWith("jwat_site_")
					&& ls[i].toLowerCase().endsWith(".xml")) {
					String f = path + ls[i];

					InputSource inputSource = new InputSource(new FileReader(f));
					DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = dbfactory.newDocumentBuilder();
					Document document = builder.parse(inputSource);

					Node curReadNode = document;

					while (curReadNode != null) {
						if (curReadNode.getNodeType() == Node.ELEMENT_NODE) {
							if (curReadNode
								.getNodeName()
								.toLowerCase()
								.equals("outloud-message")) {
								int num =
									curReadNode.getAttributes().getLength();
								if (num > 0) {
									Attr attribute =
										(Attr) curReadNode
											.getAttributes()
											.item(
											0);
									uriPrefix = attribute.getNodeValue();
								}

							} else if (
								curReadNode.getNodeName().toLowerCase().equals(
									"tag")) {
								if (curReadNode.getAttributes() != null
									&& curReadNode.getAttributes().getLength()
										> 0) {
									int num =
										curReadNode.getAttributes().getLength();
									for (int j = 0; j < num; j++) {
										Attr attribute =
											(Attr) curReadNode
												.getAttributes()
												.item(
												j);
										if (attribute
											.getNodeName()
											.toLowerCase()
											.equals("name")) {
											MessageCollection tmp =
												buildMessageCollection(
													curReadNode,
													uriPrefix);
											if (tmp != null)
												mc.addAll(tmp);
											break;
										}
									}
								}
							}
						}
						curReadNode = goNext(curReadNode);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}

		return mc;
	}

	/**
	 * Method createMessageCollection.
	 * @param filename
	 * @return MessageCollection
	 */
	private static MessageCollection createApplMessageCollection(
		MessageCollection result,
		int mode,
		String xmlpath) {

		String path = xmlpath + File.separator;
		Document document;
		try {
			String dtdpath = path + "jwat.dtd"; //$NON-NLS-1$
			if (mode == IVoiceBrowserController.HPR_MODE) {
				path += "jwat_hpr.xml"; //$NON-NLS-1$
			} else if (mode == IVoiceBrowserController.SCREEN_READER_MODE) {
				path += "jwat_jaws.xml"; //$NON-NLS-1$
			}
			File f = new File(path);
			File d = new File(dtdpath);
			if (f.exists() && d.exists()) {
				InputSource inputSource = new InputSource(new FileReader(path));
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				document = builder.parse(inputSource);
			} else {
				name = DEFAULT;
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			name = DEFAULT;
			return null;
		}

		jwat_mode = mode;
		Node curReadNode = document;
		hprDefltMsg = true;

		while (curReadNode != null) {
			if (curReadNode.getNodeType() == Node.ELEMENT_NODE) {
				if (curReadNode.getNodeName().toLowerCase().equals("name")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					name = getString(curReadNode);
				} else if (
					curReadNode.getNodeName().toLowerCase().equals(
						"description")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					description = getString(curReadNode);
				} else if (
					curReadNode.getNodeName().equals(
						"disableDefaultMessage")) { //$NON-NLS-1$
					hprDefltMsg = false;
				} else if (curReadNode.getNodeName().equals("TopOfPage")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					topofpage = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("EndOfPage")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					endofpage = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("NoCurLink")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					nocurlink = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("NoPrevLink")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					noprevlink = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("NoNextLink")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					nonextlink = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("NoString")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					nostring = getString(curReadNode);
				} else if (curReadNode.getNodeName().equals("NotOnALink")) { //$NON-NLS-1$
					curReadNode = goNext(curReadNode);
					notonalink = getString(curReadNode);
				} else if (
					curReadNode.getNodeName().toLowerCase().equals("tag")) { //$NON-NLS-1$
					if (curReadNode.getAttributes() != null
						&& curReadNode.getAttributes().getLength() > 0) {
						int num = curReadNode.getAttributes().getLength();
						for (int i = 0; i < num; i++) {
							Attr attribute =
								(Attr) curReadNode.getAttributes().item(i);
							if (attribute
								.getNodeName()
								.toLowerCase()
								.equals("name")) { //$NON-NLS-1$
								MessageCollection tmp =
									buildMessageCollection(curReadNode, null);
								if (tmp != null)
									result.addAll(tmp);
								break;
							}
						}
					}
				}
			}
			curReadNode = goNext(curReadNode);
		}
		return result;
	}

	/**
	 * Method getString.
	 * @param node
	 * @return String
	 */
	private static String getString(Node node) {
		String str = null;
		if (node.getNodeType() == Node.TEXT_NODE)
			str = node.getNodeValue();
		return str;
	}

	/**
	 * Method buildMessageCollection.
	 * @param node
	 * @return MessageCollection
	 */
	@SuppressWarnings("nls")
	private static MessageCollection buildMessageCollection(
		Node node,
		String uri) {
		String tagName = null;
		String tagType = null;
		String tagState = null;
		int num = node.getAttributes().getLength();
		for (int i = 0; i < num; i++) {
			Attr attribute = (Attr) node.getAttributes().item(i);
			if (attribute.getNodeName().toLowerCase().equals("name")) {
				tagName = attribute.getNodeValue();
			} else if (attribute.getNodeName().toLowerCase().equals("type")) {
				tagType = attribute.getNodeValue();
			} else if (attribute.getNodeName().toLowerCase().equals("state")) {
				tagState = attribute.getNodeValue();
			}
		}

		ArrayList<String> msgs = new ArrayList<String>();
		ArrayList<String> conditions = new ArrayList<String>();

//		if (msgs != null)
//			msgs.clear();
//
//		if (conditions != null)
//			conditions.clear();

		node = goNext(node);
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().toLowerCase().equals("condition")) {
					conditions.add(uri);
					node = goNext(node);
					while (node != null) {
						if (node.getNodeType() == Node.ELEMENT_NODE
							&& node.getNodeName().toLowerCase().equals("msg")) {
							break;
						} else if (
							node.getNodeType() == Node.ELEMENT_NODE
								&& node.getNodeName().toLowerCase().equals(
									"attribute")) {
							num = node.getAttributes().getLength();
							if (num == 1) {
								Attr attribute =
									(Attr) node.getAttributes().item(0);
								String s = attribute.getNodeValue();
								if (s != null && s.length() > 0) {
									node = goNext(node);
									if (node.getNodeType() == Node.TEXT_NODE) {
										String t = node.getNodeValue().trim();
										if (t != null && t.length() > 0) {
											String r = s + "=" + t;
											conditions.add(r);
										}
									}
								}
							}
						}
						node = goNext(node);
					}
				} else if (node.getNodeName().toLowerCase().equals("msg")) {
					// msgs
					node = goNext(node);
					while (node != null) {
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							if (node.getNodeName().toLowerCase().equals("msg"))
								break;
							else if (
								node.getNodeName().toLowerCase().equals(
									"var")) {
								String s = VAR;
								num = node.getAttributes().getLength();
								for (int i = 0; i < num; i++) {
									Attr attribute =
										(Attr) node.getAttributes().item(i);
									String value = attribute.getNodeValue();
									s += ",<"
										+ attribute.getNodeName().toLowerCase()
										+ "="
										+ value
										+ GREATER;
								}
								if (s.length() > 0) {
									msgs.add(s);
								}
							}
						} else if (node.getNodeType() == Node.TEXT_NODE) {
							// String t = node.getNodeValue().trim();
							String t = node.getNodeValue();
							if (t.length() > 0) {
								msgs.add(t);
							}
							if (node.getNextSibling() == null)
								break;
						}
						node = goNext(node);
					}
				} else if (node.getNodeName().toLowerCase().equals("tag")) {
					break;
				}
			}
			if (!node.getNodeName().toLowerCase().equals("msg"))
				node = goNext(node);
		}
		return new MessageCollection(
			new Message(tagName, conditions, tagType, tagState, msgs));
	}

	/**
	 * Method goNext.
	 * @param curReadNode
	 * @return Node
	 */
	private static Node goNext(Node curReadNode) {
		if (curReadNode == null)
			return null;

		if (curReadNode.hasChildNodes()) {
			// Has Child
			return curReadNode.getFirstChild();

		} else if (curReadNode.getNextSibling() != null) {
			// Has Brother
			return curReadNode.getNextSibling();
		} else {
			// Check Parent
			if (curReadNode.getParentNode() != null) {
				Node node = curReadNode.getParentNode();
				curReadNode = node.getNextSibling();
			} else
				curReadNode = null;
			return curReadNode;
		}
	}

	/**
	 * Method dumpMessages.
	 * @param mc
	 */
	@SuppressWarnings("nls")
	public static void dumpMessages(MessageCollection mc) {
		if (mc == null)
			return;

		System.out.println("Mode name is [" + OutLoud.name + "]");
		System.out.println("Mode description [" + OutLoud.description + "]");
		System.out.println("	TopOfPage is [" + OutLoud.topofpage + "]");
		System.out.println("	EndOfPage is [" + OutLoud.endofpage + "]");
		System.out.println("	NoCurLink is [" + OutLoud.nocurlink + "]");
		System.out.println("	NoPrevLink is [" + OutLoud.noprevlink + "]");
		System.out.println("	NoNextLink is [" + OutLoud.nonextlink + "]");
		System.out.println("	NoString is [" + OutLoud.nostring + "]");
		System.out.println("	NotOnALink is [" + OutLoud.notonalink + "]");

		if (mc.size() > 0) {
			for (int i = 0; i < mc.size(); i++) {
				String n = mc.get(i).getTagName();
				String t = mc.get(i).getTagType();
				String s = mc.get(i).getTagState();
				System.out.println(
					"tag[" + n + "] - type[" + t + "]  - state[" + s + "]");
				ArrayList<String> conditions = mc.get(i).getConditions();
				for (int j = 0; j < conditions.size(); j++) {
					System.out.println(" C[" + conditions.get(j) + "]");
				}
				ArrayList<String> msgs = mc.get(i).getMessages();
				for (int j = 0; j < msgs.size(); j++) {
					System.out.println(" T[" + msgs.get(j) + "]");
				}
			}
		}
	}

	/**
	 * Method buildResultString.
	 * @param mc
	 * @param element
	 * @param type
	 * @param state
	 * @return String
	 */
	public static String buildResultString(
		MessageCollection mc,
		String url,
		Element element,
		String type,
		String state) {
		String nodeName = element.getNodeName().toLowerCase();
		String result = null;
		ArrayList<String> msgs = null;
		ArrayList<String> conditions = null;

		if (mc == null)
			return null;

		result = getSiteSpecificMessage(mc, element, url);
		if (result != null && result.length() > 0)
			return result;

		Message msg = mc.getMessage(mc, nodeName, type, state);

		if (msg != null) {
			msgs = msg.getMessages();
			conditions = msg.getConditions();
		}
		if (msg != null) {
			String s = null;
			for (int i = 0; i < msgs.size(); i++) {
				if (conditions.size() > 0)
					continue;
				s = msgs.get(i);
				if (s.startsWith(VAR)) {
					String pattern0 = ATTRIBUTE_EQUAL;
					int pos = s.lastIndexOf(pattern0);
					if (pos > 0) {
						s = s.substring(pos + pattern0.length());
						s = s.substring(0, s.indexOf(GREATER));
						if (s.length() > 0) {
							NamedNodeMap attrs = element.getAttributes();
							String value = null;
							Node altNode = attrs.getNamedItem(s);
							value = altNode.getNodeValue();
							if (value != null && value.length() > 0) {
								if (result == null)
									result = value;
								else
									result += value;
							}
						}
					}
				} else {
					if (result == null)
						result = s;
					else
						result += s;
				}
			}
		}
		return result;
	}

	/**
	 * Method buildResultString.
	 * @param mc
	 * @param element
	 * @param type
	 * @param state
	 * @param pattern1
	 * @param replstr1
	 * @return String
	 */
	public static String buildResultString(
		MessageCollection mc,
		String url,
		Element element,
		String type,
		String state,
		String pattern1,
		String replstr1) {
		String nodeName = element.getNodeName().toLowerCase();
		String result = null;
		ArrayList<String> msgs = null;
		ArrayList<String> conditions = null;

		if (mc == null)
			return null;

		result = getSiteSpecificMessage(mc, element, url);
		if (result != null && result.length() > 0)
			return result;

		Message msg = mc.getMessage(mc, nodeName, type, state);

		if (msg != null) {
			msgs = msg.getMessages();
			conditions = msg.getConditions();
		}
		if (msg != null) {
			String s = null;
			for (int i = 0; i < msgs.size(); i++) {
				if (conditions.size() > 0)
					continue;
				s = msgs.get(i);
				if (s.startsWith(VAR)) {
					String pattern0 = ATTRIBUTE_EQUAL;
					int pos = s.lastIndexOf(pattern0);
					if (pos > 0) {
						s = s.substring(pos + pattern0.length());
						s = s.substring(0, s.indexOf(GREATER));
						if (s.length() > 0) {
							NamedNodeMap attrs = element.getAttributes();
							String value = null;
							Node altNode = attrs.getNamedItem(s);
							value = altNode.getNodeValue();
							if (value != null && value.length() > 0) {
								if (result == null)
									result = value;
								else
									result += value;
							}
						}
					}
					pos = s.lastIndexOf(pattern1);
					if (pos > 0) {
						if (result == null)
							result = replstr1;
						else
							result += replstr1;
					}
				} else {
					if (result == null)
						result = s;
					else
						result += s;
				}
			}
		}
		return result;
	}

	/**
	 * Method buildResultString.
	 * @param mc
	 * @param element
	 * @param type
	 * @param state
	 * @param pattern1
	 * @param replstr1
	 * @param pattern2
	 * @param replstr2
	 * @return String
	 */
	public static String buildResultString(
		MessageCollection mc,
		String url,
		Element element,
		String type,
		String state,
		String pattern1,
		String replstr1,
		String pattern2,
		String replstr2) {
		String nodeName = element.getNodeName().toLowerCase();
		String result = null;
		ArrayList<String> msgs = null;
		ArrayList<String> conditions = null;

		if (mc == null)
			return null;

		result = getSiteSpecificMessage(mc, element, url);
		if (result != null && result.length() > 0)
			return result;

		Message msg = mc.getMessage(mc, nodeName, type, state);

		if (msg != null) {
			msgs = msg.getMessages();
			conditions = msg.getConditions();
		}
		if (msg != null) {
			String s = null;
			for (int i = 0; i < msgs.size(); i++) {
				if (conditions.size() > 0)
					continue;
				s = msgs.get(i);
				if (s.startsWith(VAR)) {
					String pattern0 = ATTRIBUTE_EQUAL;
					int pos = s.lastIndexOf(pattern0);
					if (pos > 0) {
						s = s.substring(pos + pattern0.length());
						s = s.substring(0, s.indexOf(GREATER));
						if (s.length() > 0) {
							NamedNodeMap attrs = element.getAttributes();
							String value = null;
							Node altNode = attrs.getNamedItem(s);
							value = altNode.getNodeValue();
							if (value != null && value.length() > 0) {
								if (result == null)
									result = value;
								else
									result += value;
							}
						}
					}
					pos = s.lastIndexOf(pattern1);
					if (pos > 0) {
						if (result == null)
							result = replstr1;
						else
							result += replstr1;
					}
					pos = s.lastIndexOf(pattern2);
					if (pos > 0) {
						if (result == null)
							result = replstr2;
						else
							result += replstr2;
					}
				} else {
					if (result == null)
						result = s;
					else
						result += s;
				}
			}
		}
		return result;
	}

	/**
	 * Method buildResultString.
	 * @param mc
	 * @param element
	 * @param type
	 * @param state
	 * @param pattern1
	 * @param replstr1
	 * @param pattern2
	 * @param replstr2
	 * @param pattern3
	 * @param replstr3
	 * @return String
	 */
	public static String buildResultString(
		MessageCollection mc,
		String url,
		Element element,
		String type,
		String state,
		String pattern1,
		String replstr1,
		String pattern2,
		String replstr2,
		String pattern3,
		String replstr3) {
		String nodeName = element.getNodeName().toLowerCase();
		String result = null;
		ArrayList<String> msgs = null;
		ArrayList<String> conditions = null;

		if (mc == null)
			return null;

		result = getSiteSpecificMessage(mc, element, url);
		if (result != null && result.length() > 0)
			return result;

		Message msg = mc.getMessage(mc, nodeName, type, state);

		if (msg != null) {
			msgs = msg.getMessages();
			conditions = msg.getConditions();
		}
		if (msg != null) {
			String s = null;
			for (int i = 0; i < msgs.size(); i++) {
				if (conditions.size() > 0)
					continue;
				s = msgs.get(i);
				if (s.startsWith(VAR)) {
					String pattern0 = ATTRIBUTE_EQUAL;
					int pos = s.lastIndexOf(pattern0);
					if (pos > 0) {
						s = s.substring(pos + pattern0.length());
						s = s.substring(0, s.indexOf(GREATER));
						if (s.length() > 0) {
							NamedNodeMap attrs = element.getAttributes();
							String value = null;
							Node altNode = attrs.getNamedItem(s);
							value = altNode.getNodeValue();
							if (value != null && value.length() > 0) {
								if (result == null)
									result = value;
								else
									result += value;
							}
						}
					}
					pos = s.lastIndexOf(pattern1);
					if (pos > 0) {
						if (result == null)
							result = replstr1;
						else
							result += replstr1;
					}
					pos = s.lastIndexOf(pattern2);
					if (pos > 0) {
						if (result == null)
							result = replstr2;
						else
							result += replstr2;
					}
					pos = s.lastIndexOf(pattern3);
					if (pos > 0) {
						if (result == null)
							result = replstr3;
						else
							result += replstr3;
					}
				} else {
					if (result == null)
						result = s;
					else
						result += s;
				}
			}
		}
		return result;
	}

	/**
	 * Method getSiteSpecificMessage.
	 * @param mc
	 * @param element
	 * @param url
	 * @return String
	 */
	private static String getSiteSpecificMessage(
		MessageCollection mc,
		Element element,
		String url) {
		String result = null;
		Message msg = null;

		if (mc != null && mc.size() > 0) {
			for (int i = 0; i < mc.size(); i++) {
				msg = mc.get(i);
				String nodename = null;
				if (element != null
					&& element.getNodeType() == Node.ELEMENT_NODE)
					nodename = element.getNodeName().toLowerCase();

				if (nodename != null && nodename.equals(msg.getTagName())) {
					ArrayList<String> conditions = msg.getConditions();
					ArrayList<String> msgs = msg.getMessages();

					// check url condition
					if (conditions != null && conditions.size() > 0) {
						boolean found = false;
						if (url.equals(conditions.get(0))) {
							for (int k = 1; k < conditions.size(); k++) {
								String attname = null;
								String value = null;
								String a = null;
								String v = null;
								found = false;

								String s = conditions.get(k);
								attname = s.substring(0, s.indexOf("=")); //$NON-NLS-1$
								value =
									s.substring(s.indexOf("=") + 1, s.length()); //$NON-NLS-1$

								int num = element.getAttributes().getLength();
								if (num > 0) {
									for (int m = 0; m < num; m++) {
										Attr attribute =
											(Attr) element
												.getAttributes()
												.item(
												m);
										v = attribute.getNodeValue();
										a = attribute.getNodeName();
										if (attname.equals(a)
											&& value.equals(v)) {
											found = true;
											break;
										}
									}
								}
							}
							if (found == true) {
								for (int j = 0; j < msgs.size(); j++) {
									if (j == 0)
										result = msgs.get(j);
									else
										result += msgs.get(j);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
}
