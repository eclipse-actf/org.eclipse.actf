/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	  Shin SAITO - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html.statistics;

import org.eclipse.actf.visualization.internal.eval.XMLStringUtil;
import org.w3c.dom.Element;

/**
 * FlashData is used for flash content information
 */
@SuppressWarnings("nls")
public class FlashData implements IPageStatisticsTag {

	public static final String FLASH_IS_OBJECT = "isObject";

	public static final String FLASH_QUALITY = "quality";

	public static final String FLASH_SALIGN = "salign";

	public static final String FLASH_WMODE = "wmode";

	public static final String FLASH_MENU = "menu";

	public static final String FLASH_LOOP = "loop";

	public static final String FLASH_PLAY = "play";

	public static final String FLASH_SWLIVECONNECT = "swliveconnect";

	public static final String FLASH_WITHEMBED = "withEmbed";

	private String src = "";// movie

	// private String codebase="";
	// private String id="";

	private String width = "";

	private String height = "";

	private String quality = "";

	private String bgcolor = "";

	private String align = "";

	private String salign = "";

	private String wmode = "";

	private String base = "";

	private String menu = "";

	private String loop = "";

	private String play = "";

	private String swliveconnect = "";

	private boolean isObject = false;

	private boolean withEmbed = false;

	@SuppressWarnings("unused")
	private Element flashElement;

	/**
	 * Constructor of the class
	 * 
	 * @param target
	 *            target {@link Element} that contains the flash content
	 * 
	 * @param src
	 *            source URL for flash content
	 * @param isObject
	 *            true if the flash content is embedded by object tag
	 */
	public FlashData(Element target, String src, boolean isObject) {
		this.flashElement = target;
		this.src = src;
		this.isObject = isObject;

		// TODO
	}

	/**
	 * @return align attribute
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @return base attribute
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @return background color attribute
	 */
	public String getBgcolor() {
		return bgcolor;
	}

	/**
	 * @return height attribute
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @return loop attribute
	 */
	public String getLoop() {
		return loop;
	}

	/**
	 * @return menu attribute
	 */
	public String getMenu() {
		return menu;
	}

	/**
	 * @return play attribute
	 */
	public String getPlay() {
		return play;
	}

	/**
	 * @return quality attribute
	 */
	public String getQuality() {
		return quality;
	}

	/**
	 * @return salign attribute
	 */
	public String getSalign() {
		return salign;
	}

	/**
	 * @return source URL of the flash content
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @return swliveconnect attribute
	 */
	public String getSwliveconnect() {
		return swliveconnect;
	}

	/**
	 * @return width attribute
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @return wmode (Window Mode) attribute
	 */
	public String getWmode() {
		return wmode;
	}

	/**
	 * @return true if the flash content embedded with embed tag
	 */
	public boolean isWithEmbed() {
		return withEmbed;
	}

	/**
	 * Set if the flash content embedded with embed tag
	 * 
	 * @param withEmbed
	 *            true if the flash content embedded with embed tag
	 */
	public void setWithEmbed(boolean withEmbed) {
		this.withEmbed = withEmbed;
	}

	/**
	 * @return true if the flash content embedded by object tag
	 */
	public boolean isObject() {
		return isObject;
	}

	/**
	 * Set if the flash content embedded by object tag
	 * 
	 * @param isObject
	 *            true if the flash content embedded by object tag
	 */
	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	/**
	 * Get flash information as XML fragment
	 * 
	 * @return flash information as XML fragment
	 */
	public String getItemXML() {
		StringBuffer tmpSB = new StringBuffer("<" + FLASH + " " + SRC + "=\""
				+ XMLStringUtil.canonicalize(src) + "\" "
				+ FlashData.FLASH_IS_OBJECT + "=\"" + isObject + "\" ");
		if (isObject && withEmbed) {
			tmpSB.append(FlashData.FLASH_WITHEMBED + "=\"true\" ");
		}
		// TODO
		tmpSB.append("/>");
		return (tmpSB.toString());
	}

}
