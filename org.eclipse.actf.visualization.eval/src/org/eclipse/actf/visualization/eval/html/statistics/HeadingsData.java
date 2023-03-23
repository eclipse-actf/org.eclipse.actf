/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html.statistics;

import org.eclipse.actf.util.xpath.XPathCreator;
import org.eclipse.actf.visualization.internal.eval.XMLStringUtil;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * HeadingsData is used for heading tag information
 */
public class HeadingsData implements IPageStatisticsTag {

	protected String tagName;

	protected String text;

	protected String xpath;

	// TODO orgTime/curTime?

	/**
	 * Constructor of the class
	 * 
	 * @param targetE
	 *            target heading element
	 * @param text
	 *            heading text
	 */
	public HeadingsData(Element targetE, String text) {
		super();
		this.tagName = targetE.getTagName();
		this.xpath = XPathCreator.childPathSequence(targetE);
		this.text = text;
	}

	/**
	 * Constructor of the class
	 * 
	 * @param tagName
	 *            target heading tag name
	 * @param text
	 *            heading text
	 * @param xpath
	 *            XPath of target heading
	 */
	public HeadingsData(String tagName, String text, String xpath) {
		super();
		this.tagName = tagName;
		this.text = text;
		this.xpath = xpath;
	}

	/**
	 * Get XPath of the heading
	 * 
	 * @return XPath as String
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * Set XPath of the heading
	 * 
	 * @param xpath
	 */
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	/**
	 * Get tag name of the heading
	 * 
	 * @return tag name
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Get heading text
	 * 
	 * @return heading text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set tag name of the heading
	 * 
	 * @param tagName
	 *            tag name
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Set heading text
	 * 
	 * @param text
	 *            heading text
	 */
	public void setText(String text) {
		this.text = text;
	}

	private String getAttr(String name, String value) {
		return ((name + "=\"" + XMLStringUtil.canonicalize(value) + "\" ")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Get heading tag information as XML fragment
	 * 
	 * @return heading tag information as XML fragment
	 */
	public String getItemXML() {
		return ("<" + HEADING + " " + getAttr(NAME, tagName) //$NON-NLS-1$ //$NON-NLS-2$
				+ getAttr(VALUE, text) + getAttr(XPATH, xpath) + " />"); //$NON-NLS-1$
	}

	/**
	 * Extract heading tag information from XML fragment and store information
	 * into new {@link HeadingsData} instance.
	 * 
	 * @param atts
	 *            target {@link Attributes}
	 * @return new {@link HeadingsData}
	 * @throws StatisticsDataFormatException
	 */
	public static HeadingsData parseItem(Attributes atts)
			throws StatisticsDataFormatException {
		String tmpName = atts.getValue(NAME);
		String tmpValue = atts.getValue(VALUE);
		String tmpXPath = atts.getValue(XPATH);
		if (tmpName != null && tmpValue != null) {
			return (new HeadingsData(tmpName, tmpValue, tmpXPath));
		}
		throw (new StatisticsDataFormatException(
				"Invalid item format: HeadingsData")); //$NON-NLS-1$
	}
}
