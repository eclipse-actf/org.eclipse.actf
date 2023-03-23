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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.eclipse.actf.visualization.internal.eval.XMLStringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLImageElement;

/**
 * Utility class to store HTML page information including statistics
 */
public class PageData implements IPageStatisticsTag, IProblemItemVisitor {

	private int brokenIntraPageLinkNum = 0;

	private int brokenSkipMainNum = 0;

	private Vector<FlashData> flashV = new Vector<FlashData>();

	private int forwardIntraPageLinkNum = 0;// TODO check if the link reduce
	// reaching time?

	private Vector<HeadingsData> headingsV = new Vector<HeadingsData>();

	private Vector<ImageStatData> imageDataV = new Vector<ImageStatData>();

	private Map<HTMLImageElement, ImageStatData> imageDataMap = new HashMap<HTMLImageElement, ImageStatData>();

	private Map<Element, ImageStatData> linkImageDataMap = new HashMap<Element, ImageStatData>();

	private int imageAltErrorNum = 0;

	private double invalidLinkRatio = 0;

	private int maxTime = 0;

	private int orgMaxTime = 0;

	// category(missing/wrong/...)
	private int missingAltNum = 0;

	private int skipMainNum = 0;

	private int totalImageNumber = 0;

	private int totalLinkNum = 0;

	private int wrongAltNum = 0;

	private boolean hasJavascript = false;
	
	private boolean hasFrame = false;
	
	private boolean isError = false;

	/**
	 * Add flash content information
	 * 
	 * @param flashData
	 *            target {@link FlashData}
	 */
	public void addFlashData(FlashData flashData) {
		flashV.add(flashData);
	}

	/**
	 * @return number of broken intra page links
	 */
	public int getBrokenIntraPageLinkNum() {
		return brokenIntraPageLinkNum;
	}

	/**
	 * @return number of broken skip to main content links
	 */
	public int getBrokenSkipMainNum() {
		return brokenSkipMainNum;
	}

	/**
	 * @return flash content information
	 */
	public Vector<FlashData> getFlashData() {
		return this.flashV;
	}

	/**
	 * @return number of forward intra page link
	 */
	public int getForwardIntraPageLinkNum() {
		return forwardIntraPageLinkNum;
	}

	/**
	 * @return number of headings in the page
	 */
	public int getHeadingCount() {
		return headingsV.size();
	}

	/**
	 * @return headings information
	 */
	public Vector<HeadingsData> getHeadingsData() {
		return headingsV;
	}

	/**
	 * @return number of errors of alternative text for image
	 */
	public int getImageAltErrorNum() {
		return imageAltErrorNum;
	}

	/**
	 * @return invalid link ratio of the page. (target URL number under
	 *         invisible link/all target URL number)
	 */
	public double getInvalidLinkRatio() {
		return invalidLinkRatio;
	}

	/**
	 * @return maximum reaching time inside the page
	 */
	public int getMaxTime() {
		return maxTime;
	}

	/**
	 * @return number of missing alternative text for image
	 */
	public int getMissingAltNum() {
		return missingAltNum;
	}

	/**
	 * Get page data information as XML fragment
	 * 
	 * @return page data information as XML fragment
	 */
	@SuppressWarnings("nls")
	public String getReportFragment() {
		StringBuffer tmpSB = new StringBuffer();
		tmpSB.append("<" + IMAGES + " " + TOTAL + "=\"" + totalImageNumber
				+ "\" " + ERROR + "=\"" + imageAltErrorNum + "\" " + MISSING
				+ "=\"" + missingAltNum + "\" " + WRONG + "=\"" + wrongAltNum
				+ "\" " + ">" + FileUtils.LINE_SEP);
		for (ImageStatData imageData : imageDataV) {
			tmpSB.append(imageData.getItemXML() + FileUtils.LINE_SEP);
		}
		tmpSB.append("</" + IMAGES + ">");

		tmpSB.append("<" + SKIPMAIN + " " + VALID + "=\"" + skipMainNum + "\" "
				+ ERROR + "=\"" + brokenSkipMainNum + "\" />"
				+ FileUtils.LINE_SEP);
		tmpSB.append("<" + REACHINGTIME + " " + MAX + "=\"" + maxTime + "\" "
				+ ORG_MAX + "=\"" + orgMaxTime + "\" />");

		if (flashV.size() > 0) {
			tmpSB.append("<" + FLASH_INFO + " " + TOTAL + "=\"" + flashV.size()
					+ "\">" + FileUtils.LINE_SEP);
			for (FlashData flashData : flashV) {
				tmpSB.append(flashData.getItemXML() + FileUtils.LINE_SEP);
			}
			tmpSB.append("</" + FLASH_INFO + ">" + FileUtils.LINE_SEP);
		}
		tmpSB.append("<" + HEADINGS + " " + TOTAL + "=\"" + headingsV.size()
				+ "\">" + FileUtils.LINE_SEP);
		for (HeadingsData headingsData : headingsV) {
			tmpSB.append(headingsData.getItemXML() + FileUtils.LINE_SEP);
		}
		tmpSB.append("</" + HEADINGS + ">" + FileUtils.LINE_SEP);
		tmpSB.append("<" + JAVASCRIPT + " " + getAttr(EXISTENCE, hasJavascript)
				+ "/>");
		return (tmpSB.toString());
	}

	private String getAttr(String name, boolean value) {
		return (getAttr(name, Boolean.toString(value)));
	}

	private String getAttr(String name, String value) {
		return ((name + "=\"" + XMLStringUtil.canonicalize(value) + "\" ")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return number of valid skip to main content link
	 */
	public int getSkipMainNum() {
		return skipMainNum;
	}

	/**
	 * @return total number of images
	 */
	public int getTotalImageNumber() {
		return totalImageNumber;
	}

	/**
	 * @return total number of link (anchor with href attribute)
	 */
	public int getTotalLinkNum() {
		return totalLinkNum;
	}

	/**
	 * @return image information data
	 */
	public Vector<ImageStatData> getImageData() {
		return imageDataV;
	}

	/**
	 * Set image information data
	 * 
	 * @param imageDataV
	 *            Vector of target {@link ImageStatData}
	 */
	public void setImageData(Vector<ImageStatData> imageDataV) {
		this.imageDataV = imageDataV;
	}

	/**
	 * @return number of inappropriate alt attribute
	 */
	public int getWrongAltNum() {
		return wrongAltNum;
	}

	/**
	 * @return original reaching time (without consideration of headings, skip
	 *         links, etc.)
	 */
	public int getOrgMaxTime() {
		return orgMaxTime;
	}

	/**
	 * @return map contains {@link HTMLImageElement} and it's information pair
	 */
	public Map<HTMLImageElement, ImageStatData> getImageDataMap() {
		return imageDataMap;
	}

	/**
	 * @return map contains ancestor anchor {@link Element} of image link and
	 *         image information pair
	 */
	public Map<Element, ImageStatData> getLinkImageDataMap() {
		return linkImageDataMap;
	}

	/**
	 * @return true if page uses JavaScript
	 */
	public boolean hasJavascript() {
		return hasJavascript;
	}

	/**
	 * Set if page uses JavaScript
	 * 
	 * @param hasJavascript
	 *            true if page uses JavaScript
	 */
	public void setHasJavascript(boolean hasJavascript) {
		this.hasJavascript = hasJavascript;
	}

	/**
	 * Set map contains ancestor anchor {@link Element} of image link and image
	 * information pair
	 * 
	 * @param linkImageDataMap
	 *            target map
	 */
	public void setLinkImageDataMap(Map<Element, ImageStatData> linkImageDataMap) {
		this.linkImageDataMap = linkImageDataMap;
	}

	/**
	 * Set map contains {@link HTMLImageElement} and it's information pair
	 * 
	 * @param imageDataMap
	 *            target map
	 */
	public void setImageDataMap(
			Map<HTMLImageElement, ImageStatData> imageDataMap) {
		this.imageDataMap = imageDataMap;
	}

	/**
	 * Set original reaching time (without consideration of headings, skip
	 * links, etc.)
	 * 
	 * @param orgMaxTime
	 *            original reaching time of the page
	 */
	public void setOrgMaxTime(int orgMaxTime) {
		this.orgMaxTime = orgMaxTime;
	}

	/**
	 * Set broken intra page link number
	 * 
	 * @param brokenIntraPageLinkNum
	 *            broken intra page link number
	 */
	public void setBrokenIntraPageLinkNum(int brokenIntraPageLinkNum) {
		this.brokenIntraPageLinkNum = brokenIntraPageLinkNum;
	}

	/**
	 * Set broken skip to main content link number
	 * 
	 * @param brokenSkipMainNum
	 *            broken skip to main content link number
	 */
	public void setBrokenSkipMainNum(int brokenSkipMainNum) {
		this.brokenSkipMainNum = brokenSkipMainNum;
	}

	/**
	 * Set flash information of the page
	 * 
	 * @param flashV
	 *            Vector of flash information of the page
	 */
	public void setFlashData(Vector<FlashData> flashV) {
		this.flashV = flashV;
	}

	/**
	 * Set number of forward intra page link
	 * 
	 * @param forwardIntraPageLinkNum
	 *            number of forward intra page link
	 */
	public void setForwardIntraPageLinkNum(int forwardIntraPageLinkNum) {
		this.forwardIntraPageLinkNum = forwardIntraPageLinkNum;
	}

	/**
	 * Set headings information of the page
	 * 
	 * @param headings
	 *            Vector of headings information of the page
	 */
	public void setHeadingsData(Vector<HeadingsData> headings) {
		this.headingsV = headings;
	}

	/**
	 * Set error number of alternative text for image
	 * 
	 * @param imageAltErrorNum
	 *            errors number of alternative text for image
	 */
	public void setImageAltErrorNum(int imageAltErrorNum) {
		this.imageAltErrorNum = imageAltErrorNum;
	}

	/**
	 * Set invalid link ratio of the page. (target URL number under invisible
	 * link/all target URL number)
	 * 
	 * @param invalidLinkRatio
	 *            invalid link ratio
	 */
	public void setInvalidLinkRatio(double invalidLinkRatio) {
		this.invalidLinkRatio = invalidLinkRatio;
	}

	/**
	 * Set maximum reaching time of the page
	 * 
	 * @param maxTime
	 *            maximum reaching time of the page
	 */
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * Set number of missing alt attribute for image
	 * 
	 * @param missingAltNum
	 *            number of missing alt attribute for image
	 */
	public void setMissingAltNum(int missingAltNum) {
		this.missingAltNum = missingAltNum;
	}

	/**
	 * Set valid skip to main content link number
	 * 
	 * @param skipMainNum
	 *            valid skip to main content link number
	 */
	public void setSkipMainNum(int skipMainNum) {
		this.skipMainNum = skipMainNum;
	}

	/**
	 * Set total number of image in the page
	 * 
	 * @param totalImageNumber
	 *            total number of image
	 */
	public void setTotalImageNumber(int totalImageNumber) {
		this.totalImageNumber = totalImageNumber;
	}

	/**
	 * Set total number of link (anchor element with href attribute) in the page
	 * 
	 * @param totalLinkNum
	 */
	public void setTotalLinkNum(int totalLinkNum) {
		this.totalLinkNum = totalLinkNum;
	}

	/**
	 * Set number of inappropriate alternative text in the page
	 * 
	 * @param wrongAltNum
	 *            number of inappropriate alternative text
	 */
	public void setWrongAltNum(int wrongAltNum) {
		this.wrongAltNum = wrongAltNum;
	}

	/**
	 * @return true if page has child frame
	 */
	public boolean hasFrame() {
		return hasFrame;
	}

	/**
	 * Set if page has child frame or not
	 * @param hasFrame
	 */
	public void setHasFrame(boolean hasFrame) {
		this.hasFrame = hasFrame;
	}

	/**
	 * Set if page is error page
	 * @return
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * @param isError true if page is error page
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor#visit
	 * (org.eclipse.actf.visualization.eval.problem.IProblemItem)
	 */
	public void visit(IProblemItem item) {
		ImageStatData imageData = imageDataMap.get(item.getTargetNode());
		if (imageData != null) {
			imageData.addProblemItem(item);
		} else {
			imageData = linkImageDataMap.get(item.getTargetNode());
			if (imageData != null) {// TODO check
				imageData.addProblemItem(item);
			}
		}
	}

}
