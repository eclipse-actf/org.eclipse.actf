/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util.dom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.html.HTMLTitleElement;
import org.w3c.dom.traversal.NodeFilter;

/**
 * Utility class to print out DOM
 */
@SuppressWarnings("nls")
public class DomPrintUtil {

	/**
	 * Default encoding of this utility. (UTF8)
	 */
	public static final String UTF8 = "UTF8";

	private static final String LINE_SEP = System.getProperty("line.separator");
	private static final String EMPTY_STR = "";

	private static final String LT = "<";
	private static final String GT = ">";
	private static final String AMP = "&";
	private static final String QUAT = "\"";
	private static final String SINGLE_QUAT = "'";

	private static final String ESC_LT = "&lt;";
	private static final String ESC_GT = "&gt;";
	private static final String ESC_AMP = "&amp;";

	private Document document = null;
	private Node rootNode = null;
	private int whatToShow = NodeFilter.SHOW_ALL;
	private NodeFilter nodeFilter = null;
	private boolean entityReferenceExpansion = false;

	private boolean indent = true;
	private boolean escapeTagBracket = false;
	private boolean isHTML5 = false;

	private AttributeFilter attrFilter = null;

	/**
	 * AttributeFilter defines the behavior of a filter that is used for
	 * converting attributes of each Element into String.
	 */
	public interface AttributeFilter {

		/**
		 * Check whether a specified attribute is converted into String.
		 * 
		 * @param element
		 *            the target Element
		 * @param attr
		 *            the target attribute
		 * @return true to print the attribute, false to ignore the attribute
		 */
		public boolean acceptNode(Element element, Node attr);
	}

	/**
	 * Constructor of DOM print utility.
	 * 
	 * @param document
	 *            the target document
	 */
	public DomPrintUtil(Document document) {
		this.document = document;
	}

	/**
	 * Constructor of DOM print utility.
	 * 
	 * @param root
	 *            Node the target root Node
	 */
	public DomPrintUtil(Node rootNode) {
		this.rootNode = rootNode;
	}

	private String getXMLString(String targetS) {
		return targetS.replaceAll(AMP, ESC_AMP).replaceAll(LT, ESC_LT).replaceAll(GT, ESC_GT);
	}

	private String getAttributeString(Element element, Node attr) {
		if (null == attrFilter || attrFilter.acceptNode(element, attr)) {
			String value = getXMLString(attr.getNodeValue());
			String quat = QUAT;
			if (value.contains(QUAT)) {
				quat = SINGLE_QUAT;
			}
			return " " + attr.getNodeName() + "=" + quat + value + quat;
		}
		return EMPTY_STR;
	}

	private boolean checkNewLine(Node target) {
		if (indent && target.hasChildNodes()) {
			short type = target.getFirstChild().getNodeType();
			if (type == Node.TEXT_NODE || type == Node.CDATA_SECTION_NODE) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns XML text converted from the target DOM
	 * 
	 * @return String format XML converted from the target DOM
	 */
	public String toXMLString() {
		StringBuffer tmpSB = new StringBuffer(8192);

		TreeWalkerImpl treeWalker;
		Node tmpN;
		if (document != null) {
			treeWalker = new TreeWalkerImpl(document, whatToShow, nodeFilter, entityReferenceExpansion);
			tmpN = treeWalker.nextNode();
		} else {
			treeWalker = new TreeWalkerImpl(rootNode, whatToShow, nodeFilter, entityReferenceExpansion);
			tmpN = rootNode;
		}

		String lt = escapeTagBracket ? ESC_LT : LT;
		String gt = escapeTagBracket ? ESC_GT : GT;
		String line_sep = indent ? LINE_SEP : EMPTY_STR;

		boolean prevIsText = false;

		boolean isFirst = true;

		String indentS = EMPTY_STR;
		while (tmpN != null) {
			short type = tmpN.getNodeType();

			if (isFirst) {
				if (type != Node.PROCESSING_INSTRUCTION_NODE && type != Node.DOCUMENT_TYPE_NODE && isHTML5) {
					tmpSB.append("<!DOCTYPE html SYSTEM \"about:legacy-compat\">" + line_sep);
				}
				isFirst = false;
			}

			switch (type) {
			case Node.ELEMENT_NODE:
				if (prevIsText) {
					tmpSB.append(line_sep);
				}
				tmpSB.append(indentS + lt + tmpN.getNodeName());
				NamedNodeMap attrs = tmpN.getAttributes();
				int len = attrs.getLength();
				for (int i = 0; i < len; i++) {
					Node attr = attrs.item(i);
					String value = attr.getNodeValue();
					if (null != value) {
						tmpSB.append(getAttributeString((Element) tmpN, attr));
					}
				}
				if (tmpN instanceof HTMLTitleElement && !tmpN.hasChildNodes()) {
					tmpSB.append(gt + ((HTMLTitleElement) tmpN).getText());
					prevIsText = true;
				} else if (checkNewLine(tmpN)) {
					tmpSB.append(gt + line_sep);
					prevIsText = false;
				} else {
					tmpSB.append(gt);
					prevIsText = true;
				}
				break;
			case Node.TEXT_NODE:
				if (!prevIsText) {
					tmpSB.append(indentS);
				}
				tmpSB.append(getXMLString(tmpN.getNodeValue()));
				prevIsText = true;
				break;
			case Node.COMMENT_NODE:
				String comment;
				if (escapeTagBracket) {
					comment = getXMLString(tmpN.getNodeValue());
				} else {
					comment = tmpN.getNodeValue();
				}
				tmpSB.append(line_sep + indentS + lt + "!--" + comment + "--" + gt + line_sep);
				prevIsText = false;
				break;
			case Node.CDATA_SECTION_NODE:
				tmpSB.append(line_sep + indentS + lt + "!CDATA[" + tmpN.getNodeValue() + "]]" + line_sep);
				break;
			case Node.DOCUMENT_TYPE_NODE:
				if (isHTML5) {
					tmpSB.append("<!DOCTYPE html SYSTEM \"about:legacy-compat\">" + line_sep);
				} else if (tmpN instanceof DocumentType) {
					DocumentType docType = (DocumentType) tmpN;

					String pubId = docType.getPublicId();
					String sysId = docType.getSystemId();
					if (null != pubId && pubId.length() > 0) {
						if (null != sysId && sysId.length() > 0) {
							tmpSB.append(lt + "!DOCTYPE " + docType.getName() + " PUBLIC \"" + pubId + " \"" + sysId
									+ "\">" + line_sep);
						} else {
							tmpSB.append(
									lt + "!DOCTYPE " + docType.getName() + " PUBLIC \"" + pubId + "\">" + line_sep);
						}
					} else {
						tmpSB.append(lt + "!DOCTYPE " + docType.getName() + " SYSTEM \"" + docType.getSystemId() + "\">"
								+ line_sep);

					}
				} else {
					System.out.println("Document Type node does not implement DocumentType: " + tmpN);
				}
				break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				if (tmpN instanceof ProcessingInstruction) {
					String tmpS = ((ProcessingInstruction) tmpN).getData();
					if (tmpS.startsWith("?")) {
						tmpSB.append(lt);
					} else {
						tmpSB.append(lt + "?");
					}
					if (tmpS.endsWith("?")) {
						tmpSB.append(tmpS + ">" + LINE_SEP);
					} else {
						tmpSB.append(tmpS + "?>" + LINE_SEP);
					}
				}
				break;
			default:
				System.out.println(tmpN.getNodeType() + " : " + tmpN.getNodeName());
			}

			Node next = treeWalker.firstChild();
			if (null != next) {
				if (indent && type == Node.ELEMENT_NODE) {
					indentS = indentS + " ";
				}
				tmpN = next;
				continue;
			}

			if (tmpN.getNodeType() == Node.ELEMENT_NODE) {
				tmpSB.append(lt + "/" + tmpN.getNodeName() + gt + line_sep);
				prevIsText = false;
			}

			next = treeWalker.nextSibling();
			if (null != next) {
				tmpN = next;
				continue;
			}

			tmpN = null;
			next = treeWalker.parentNode();
			while (null != next) {
				if (next.getNodeType() == Node.ELEMENT_NODE) {
					if (indent) {
						if (indentS.length() > 0) {
							indentS = indentS.substring(1);
						} else {
							System.err.println("indent: " + next.getNodeName() + " " + next);
						}
					}
					tmpSB.append(line_sep + indentS + lt + "/" + next.getNodeName() + gt + line_sep);
					prevIsText = false;
				}
				next = treeWalker.nextSibling();
				if (null != next) {
					tmpN = next;
					break;
				}
				next = treeWalker.parentNode();
			}
		}
		return tmpSB.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toXMLString();
	}

	/**
	 * Set whatToShow attribute to TreeWalker used in the utility.
	 * 
	 * @param whatToShow
	 *            the attribute determines which types of node are presented via
	 *            the TreeWalker. The values are defined in the NodeFilter
	 *            interface.
	 * @see TreeWalkerImpl
	 */
	public void setWhatToShow(int whatToShow) {
		this.whatToShow = whatToShow;
	}

	/**
	 * Set NodeFilter to TreeWalker used in the utility.
	 * 
	 * @param nodeFilter
	 *            the filter used to screen nodes
	 * @see TreeWalkerImpl
	 */
	public void setNodeFilter(NodeFilter nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	/**
	 * Set the entity reference expansion flag to TreeWalker used in the
	 * utility.
	 * 
	 * @param entityReferenceExpansion
	 *            the flag to determine whether the children of entity reference
	 *            nodes are visible to TreeWalker.
	 * @see TreeWalkerImpl
	 */
	public void setEntityReferenceExpansion(boolean entityReferenceExpansion) {
		this.entityReferenceExpansion = entityReferenceExpansion;
	}

	/**
	 * Set the number of space characters used for indent
	 * 
	 * @param indent
	 *            the number of space characters used for indent
	 */
	public void setIndent(boolean indent) {
		this.indent = indent;
	}

	/**
	 * Determine to escape Tag bracket ('<','>') or not. Please set true if you
	 * want to print out DOM into &lt;pre&gt; section of HTML.
	 * 
	 * @param escapeTagBracket
	 *            if true, print Tag bracket as escaped format (
	 *            {@literal '&lt;',
	 *            '&gt;'})
	 * 
	 */
	public void setEscapeTagBracket(boolean escapeTagBracket) {
		this.escapeTagBracket = escapeTagBracket;
	}

	/**
	 * Set AttributeFilter to define the behavior for printing attributes of
	 * each Element.
	 * 
	 * @param attrFilter
	 *            the AttributeFilter to set
	 */
	public void setAttrFilter(AttributeFilter attrFilter) {
		this.attrFilter = attrFilter;
	}

	/**
	 * Set true if the document is HTML5.
	 * 
	 * @param isHTML5
	 */
	public void setHTML5(boolean isHTML5) {
		this.isHTML5 = isHTML5;
	}

	/**
	 * Print out the target Document.
	 * 
	 * @param filePath
	 *            the target file path
	 * @throws IOException
	 */
	public void writeToFile(String filePath) throws IOException {
		writeToFile(new File(filePath), UTF8);
	}

	/**
	 * Print out the target Document.
	 * 
	 * @param file
	 *            the target File
	 * @throws IOException
	 */
	public void writeToFile(File file) throws IOException {
		writeToFile(file, UTF8);
	}

	/**
	 * Print out the target Document in specified encoding
	 * 
	 * @param filePath
	 *            the target file path
	 * @param encode
	 *            the target encoding
	 * @throws IOException
	 */
	public void writeToFile(String filePath, String encode) throws IOException {
		writeToFile(new File(filePath), encode);
	}

	/**
	 * Print out the target Document in specified encoding
	 * 
	 * @param file
	 *            the target file
	 * @param encode
	 *            the target encoding
	 * @throws IOException
	 */
	public void writeToFile(File file, String encode) throws IOException {
		FileOutputStream FOS = new FileOutputStream(file);
		PrintWriter tmpPW = new PrintWriter(new OutputStreamWriter(FOS, encode));
		//BOM
		if (isHTML5 && encode.equals(UTF8)) {
			FOS.write(0xef);
			FOS.write(0xbb);
			FOS.write(0xbf);
		}
		tmpPW.println(toXMLString());
		tmpPW.flush();
		tmpPW.close();
	}

}
