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

import java.net.URL;
import java.util.Vector;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.internal.eval.XMLStringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLImageElement;
import org.xml.sax.Attributes;

/**
 * ImageStatData is used for image (img) element information
 */
public class ImageStatData implements IPageStatisticsTag {

	public static final String ISMAP = "ismap"; //$NON-NLS-1$

	public static final String USEMAP = "usemap"; //$NON-NLS-1$

	public static final String LONGDESC = "longdesc"; //$NON-NLS-1$

	protected String altS = ""; //$NON-NLS-1$

	protected Element ancestorLink = null;

	protected String destUrlS = ""; //$NON-NLS-1$

	protected boolean hasAlt = false;

	protected boolean hasHeight = false;

	protected boolean hasLongDesc = false;

	protected boolean hasUseMap = false;

	protected boolean hasWidth = false;

	protected String heightS = ""; //$NON-NLS-1$

	protected boolean isInLink = false;

	protected boolean isMap = false;

	protected String longDescS = ""; //$NON-NLS-1$

	// for problem statistics
	protected Vector<IProblemItem> problemV = new Vector<IProblemItem>();

	protected String srcS = ""; //$NON-NLS-1$

	protected String urlS = ""; //$NON-NLS-1$

	protected String useMapS = ""; //$NON-NLS-1$

	protected String widthS = ""; //$NON-NLS-1$

	protected ImageStatData() {
	}

	/**
	 * Constructor of the class
	 * 
	 * @param target
	 *            target image element
	 * @param baseURL
	 *            base URL
	 */
	public ImageStatData(HTMLImageElement target, URL baseURL) {

		srcS = target.getSrc();
		urlS = srcS;
		try {
			urlS = new URL(baseURL, urlS).toString();
		} catch (Exception e) {
		}

		// System.out.println(urlS);

		if (hasAlt = target.hasAttribute(ALT)) {
			altS = target.getAlt();
		}
		if (hasLongDesc = target.hasAttribute(ImageStatData.LONGDESC)) {
			longDescS = target.getLongDesc();
		}
		if (hasUseMap = target.hasAttribute(ImageStatData.USEMAP)) {
			useMapS = target.getUseMap();
		}

		if (hasWidth = target.hasAttribute(WIDTH)) {
			widthS = target.getWidth();
		}

		if (hasHeight = target.hasAttribute(HEIGHT)) {
			heightS = target.getHeight();
		}

		isMap = target.getIsMap();
		

		if (HtmlTagUtil.hasAncestor(target, "a")) {
			Node temp = HtmlTagUtil.getAncestor(target, "a");
			if (temp != null) {
				HTMLAnchorElement tmpE = (HTMLAnchorElement) temp;
				if (isInLink = tmpE.hasAttribute(HREF)) {
					destUrlS = tmpE.getHref();
					ancestorLink = tmpE;
					try {
						destUrlS = new URL(baseURL, destUrlS).toString();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * Extract problem item information relates to the image from XML fragment
	 * and add the information into this instance.
	 * 
	 * @param atts
	 *            target {@link Attributes}
	 * @throws StatisticsDataFormatException
	 */
	public void addProblemItem(Attributes atts)
			throws StatisticsDataFormatException {
		String idS = atts.getValue(ID);
		String targetS = atts.getValue(TARGET_STRING);

		if (idS != null) {
			IProblemItem tmpItem = new ProblemItemImpl(idS);
			if (!tmpItem.getId().equals("unknown")) { //$NON-NLS-1$
				if (targetS != null) {
					tmpItem.setTargetStringForExport(targetS);
				}
				addProblemItem(tmpItem);
				return;
			}
		}
		throw new StatisticsDataFormatException();
	}

	/**
	 * Add problem item relates to image
	 * 
	 * @param problemItem
	 *            target problem item
	 */
	public void addProblemItem(IProblemItem problemItem) {
		problemV.add(problemItem);
	}

	/**
	 * 
	 * @return alt attribute
	 */
	public String getAlt() {
		return this.altS;
	}

	/**
	 * @return ancestor anchor {@link Element}, or null if not available
	 */
	public Element getAncestorLink() {
		return ancestorLink;
	}

	private String getAttr(String name, boolean value) {
		return (getAttr(name, Boolean.toString(value)));
	}

	private String getAttr(String name, String value) {
		return ((name + "=\"" + XMLStringUtil.canonicalize(value) + "\" ")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return destination URL of ancestor anchor, or empty string if not in
	 *         anchor
	 */
	public String getDestUrl() {
		return destUrlS;
	}

	/**
	 * @return heighet attribute
	 */
	public String getHeight() {
		return heightS;
	}

	/**
	 * Get image information as XML fragment
	 * 
	 * @return image information as XML fragment
	 */
	@SuppressWarnings("nls")
	public String getItemXML() {
		StringBuffer tmpSB = new StringBuffer("<" + IMAGE + " "
				+ getAttr(SRC, srcS) + getAttr(URL, urlS)
				+ getAttr(ImageStatData.ISMAP, isMap));
		if (hasAlt) {
			tmpSB.append(getAttr(ALT, altS));
		}
		if (hasWidth) {
			tmpSB.append(getAttr(WIDTH, widthS));
		}
		if (hasHeight) {
			tmpSB.append(getAttr(HEIGHT, heightS));
		}
		if (isInLink) {
			tmpSB.append(getAttr(DEST, destUrlS));
		}
		if (hasLongDesc) {
			tmpSB.append(getAttr(ImageStatData.LONGDESC, longDescS));
		}
		if (hasUseMap) {
			tmpSB.append(getAttr(ImageStatData.USEMAP, useMapS));
		}
		int size = problemV.size();
		if (size == 0) {
			tmpSB.append(" />");
		} else {
			tmpSB.append(" >" + FileUtils.LINE_SEP);
			for (int i = 0; i < size; i++) {
				IProblemItem pItem = problemV.get(i);
				tmpSB.append("<" + ERROR + " " + getAttr(ID, pItem.getId())
						+ getAttr(TARGET_STRING, pItem.getTargetStringForExport())
						+ " />" + FileUtils.LINE_SEP);
			}
			tmpSB.append("</" + IMAGE + ">");
		}
		return (tmpSB.toString());
	}

	/**
	 * @return longdesc attribute
	 */
	public String getLongDesc() {
		return longDescS;
	}

	/**
	 * @return problems relate to the image
	 */
	public Vector<IProblemItem> getProblemV() {
		return this.problemV;
	}

	/**
	 * @return src attribute
	 */
	public String getSrc() {
		return srcS;
	}

	/**
	 * @return target URL of the image (URL(base URL, src attribute value)).
	 * @see URL#URL(URL, String)
	 */
	public String getUrl() {
		return this.urlS;
	}

	/**
	 * @return usemap attribute
	 */
	public String getUseMap() {
		return useMapS;
	}

	/**
	 * @return width attribute
	 */
	public String getWidth() {
		return widthS;
	}

	/**
	 * @return true if image has alt attribute
	 */
	public boolean isHasAlt() {
		return this.hasAlt;
	}

	/**
	 * @return true if image has height attribute
	 */
	public boolean isHasHeight() {
		return hasHeight;
	}

	/**
	 * @return true if image has longdesc attribute
	 */
	public boolean isHasLongDesc() {
		return hasLongDesc;
	}

	/**
	 * @return true if image has usemap attribute
	 */
	public boolean isHasUseMap() {
		return hasUseMap;
	}

	/**
	 * @return true if image has width attribute
	 */
	public boolean isHasWidth() {
		return hasWidth;
	}

	/**
	 * @return true if image is under link (anchor element with href attribute)
	 */
	public boolean isInLink() {
		return isInLink;
	}

	/**
	 * @return true if image has ismap attribute
	 */
	public boolean isMap() {
		return isMap;
	}

}
