/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import java.net.URL;
import java.util.Map;

import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;

@SuppressWarnings("nls")
public class CurrentStylesImpl implements ICurrentStyles {

	private static final String A_VISIBILITY = "visibility";

	private static final String A_TEXT_DECORATION = "textDecoration";

	private static final String A_TEXT_ALIGN = "textAlign";

	private static final String A_POSITION = "position";

	private static final String A_LINE_HEIGHT = "lineHeight";

	private static final String A_LETTER_SPACING = "letterSpacing";

	private static final String A_FONT_VARIANT = "fontVariant";

	private static final String A_FONT_STYLE = "fontStyle";

	private static final String A_FONT_SIZE = "fontSize";

	private static final String A_FONT_FAMILY = "fontFamily";

	private static final String A_DISPLAY = "display";

	private static final String A_COLOR = "color";

	private static final String A_BACKGROUND_REPEAT = "backgroundRepeat";

	private static final String A_BACKGROUND_COLOR = "backgroundColor";

	private static final String A_BACKGROUND_IMAGE = "backgroundImage";

	private static final String A_OPACITY = "opacity";

	private final String xpath;

	private final String tagName;

	private final Rectangle rect;

	private final Rectangle orgRect;

	private final double zoomFactor;

	private final String backgroundColor;

	private final String backgroundRepeat;

	private final String backgroundImage;

	private final String color;

	private final String opacity;

	private final String fontFamily;

	private final String fontSize;

	private final String fontStyle;

	private final String fontVariant;

	private final String lineHeight;

	private final String textAlign;

	private final String visibility;

	private final String display;

	private final String position;

	private final String textDecoration;

	private final String letterSpacing;

	private boolean isLink;

	private URL target = null;

	private String cascadeColor;

	private String cascadeBackgroundColor;

	private String cascadeBackgroundImage;

	private final boolean hasChildText;

	private String[] childTexts = new String[0];

	private String[] descendantTextsWithBGImage = new String[0];

	private boolean hasDescendantTextWithBGImage = false;

	private Element element;

	/**
	 * 
	 */
	/*
	 * public CurrentStylesImpl(IElementEx element, URL baseUrl) { this.element =
	 * element; rect = element.getLocation(); xpath =
	 * XPathCreator.childPathSequence(element); tagName = element.getTagName();
	 * 
	 * isLink = false; if (TAG_A.equalsIgnoreCase(tagName)) { String href =
	 * element.getAttribute(HREF); if (null != href) { if (null != baseUrl) { try {
	 * target = new URL(baseUrl, href); isLink = true; } catch (Exception e) { //
	 * e.printStackTrace(); try { target = new URL(href); isLink = true; } catch
	 * (Exception e2) { // e2.printStackTrace(); } } } else { try { target = new
	 * URL(href); isLink = true; } catch (Exception e2) { // e2.printStackTrace(); }
	 * } } }
	 * 
	 * IStyle style = element.getStyle(); backgroundImage = (String)
	 * style.get(A_BACKGROUND_IMAGE); backgroundRepeat = (String)
	 * style.get(A_BACKGROUND_REPEAT); backgroundColor = (String)
	 * style.get(A_BACKGROUND_COLOR); color = (String) style.get(A_COLOR);
	 * cascadeColor = color; cascadeBackgroundColor = backgroundColor;
	 * cascadeBackgroundImage = backgroundImage; opacity = (String)
	 * style.get(A_OPACITY);
	 * 
	 * display = (String) style.get(A_DISPLAY); fontFamily = (String)
	 * style.get(A_FONT_FAMILY); fontSize = (String) style.get(A_FONT_SIZE);
	 * fontStyle = (String) style.get(A_FONT_STYLE); fontVariant = (String)
	 * style.get(A_FONT_VARIANT); letterSpacing = (String)
	 * style.get(A_LETTER_SPACING); lineHeight = (String) style.get(A_LINE_HEIGHT);
	 * position = (String) style.get(A_POSITION); textAlign = (String)
	 * style.get(A_TEXT_ALIGN); textDecoration = (String)
	 * style.get(A_TEXT_DECORATION); visibility = (String) style.get(A_VISIBILITY);
	 * 
	 * boolean fgFlag = "transparent".equalsIgnoreCase(cascadeColor); boolean bgFlag
	 * = "transparent".equalsIgnoreCase(cascadeBackgroundColor); boolean bgImgFlag =
	 * bgFlag && "none".equalsIgnoreCase(cascadeBackgroundImage);
	 * 
	 * Node tmpN = element.getParentNode(); while ((tmpN != null && !(tmpN
	 * instanceof Document)) && (fgFlag || bgFlag)) { try { if (tmpN instanceof
	 * IElementEx) { IElementEx tmpE = (IElementEx) tmpN; if (fgFlag) { String
	 * tmpStr = (String) tmpE.getStyle().get(A_COLOR); if (tmpStr != null &&
	 * !tmpStr.equalsIgnoreCase("transparent")) { cascadeColor = tmpStr; fgFlag =
	 * false; } } if (bgImgFlag) { String tmpStr = (String)
	 * tmpE.getStyle().get(A_BACKGROUND_IMAGE); if (tmpStr != null &&
	 * !tmpStr.equalsIgnoreCase("none")) { cascadeBackgroundImage = tmpStr;
	 * bgImgFlag = false; } } if (bgFlag) { String tmpStr = (String)
	 * tmpE.getStyle().get(A_BACKGROUND_COLOR); if (tmpStr != null &&
	 * !tmpStr.equalsIgnoreCase("transparent")) { cascadeBackgroundColor = tmpStr;
	 * bgFlag = false; bgImgFlag = false; } } } } catch (Exception e) { } tmpN =
	 * tmpN.getParentNode(); }
	 * 
	 * ArrayList<String> childs = new ArrayList<String>(); boolean flag = false;
	 * NodeList tmpNL = element.getChildNodes(); for (int i = 0; i <
	 * tmpNL.getLength(); i++) { Node tmpN2 = tmpNL.item(i); if (tmpN2 instanceof
	 * Text) { if ("#text".equals(tmpN2.getNodeName())) { if
	 * (tmpN2.getNodeValue().trim().length() > 0) { flag = true;
	 * childs.add(tmpN2.getNodeValue()); } } else {
	 * System.out.println("unkonwn node: " + tmpN2.getNodeName()); } } } if (!flag)
	 * { if ("input".equalsIgnoreCase(element.getTagName())) { String type =
	 * element.getAttribute("type"); if (type != null &&
	 * type.matches("(button)|(submit)|(reset)")) { if
	 * (element.getText().trim().length() > 0) { flag = true;
	 * childs.add(element.getText()); } } } } hasChildText = flag; childTexts = new
	 * String[childs.size()]; childs.toArray(childTexts);
	 * 
	 * if (backgroundImage != null && !"none".equalsIgnoreCase(backgroundImage)) {
	 * NodeFilter tmpNF = new NodeFilter() { public short acceptNode(Node n) { if (n
	 * instanceof Text) { if (n.getNodeValue().trim().length() > 0) { return
	 * NodeFilter.FILTER_ACCEPT; } else { return NodeFilter.FILTER_SKIP; } } if (n
	 * instanceof IElementEx) { IStyle tmpStyle = ((IElementEx) n).getStyle(); if
	 * (tmpStyle != null && (!"none".equalsIgnoreCase((String)
	 * tmpStyle.get(A_BACKGROUND_IMAGE)) || !"transparent".equalsIgnoreCase((String)
	 * tmpStyle.get(A_BACKGROUND_COLOR)))) { return NodeFilter.FILTER_REJECT; } }
	 * return NodeFilter.FILTER_SKIP; } }; ArrayList<String> descendants = new
	 * ArrayList<String>();
	 * 
	 * TreeWalkerImpl twi = new TreeWalkerImpl(element, NodeFilter.SHOW_ALL, tmpNF,
	 * false); Node tmpText = twi.nextNode(); hasDescendantTextWithBGImage =
	 * (tmpText != null); while (tmpText != null) {
	 * descendants.add(tmpText.getNodeValue()); tmpText = twi.nextNode(); }
	 * descendantTextsWithBGImage = new String[descendants.size()];
	 * descendants.toArray(descendantTextsWithBGImage);
	 * 
	 * }
	 * 
	 * }
	 */

	public CurrentStylesImpl(WebBrowserEdgeImpl edgeBrowser, int handle, String xpath, String tagName, URL target,
			Rectangle rect, double zoomFactor, Map<String, String> styles, String[] childTexts,
			String[] descendantTextsWithBGImage) {
		this.element = new EdgeElementImpl(edgeBrowser, handle);
		this.xpath = xpath;
		this.tagName = tagName;
		this.target = target;
		this.zoomFactor = zoomFactor;
		this.orgRect = rect;
		this.rect = new Rectangle(calcZoom(rect.x), calcZoom(rect.y), calcZoom(rect.width), calcZoom(rect.height));
		this.childTexts = childTexts;
		this.descendantTextsWithBGImage = descendantTextsWithBGImage;
		this.hasChildText = childTexts.length > 0;
		this.isLink = target != null;
		this.hasDescendantTextWithBGImage = descendantTextsWithBGImage.length > 0;

		backgroundImage = styles.get(A_BACKGROUND_IMAGE);
		backgroundRepeat = styles.get(A_BACKGROUND_REPEAT);
		backgroundColor = styles.get(A_BACKGROUND_COLOR);
		color = styles.get(A_COLOR);
		opacity = styles.get(A_OPACITY);

		display = styles.get(A_DISPLAY);
		fontFamily = styles.get(A_FONT_FAMILY);
		fontSize = styles.get(A_FONT_SIZE);
		fontStyle = styles.get(A_FONT_STYLE);
		fontVariant = styles.get(A_FONT_VARIANT);
		letterSpacing = styles.get(A_LETTER_SPACING);
		lineHeight = styles.get(A_LINE_HEIGHT);
		position = styles.get(A_POSITION);
		textAlign = styles.get(A_TEXT_ALIGN);
		textDecoration = styles.get(A_TEXT_DECORATION);
		visibility = styles.get(A_VISIBILITY);

		cascadeColor = styles.get("cascadeColor");
		cascadeBackgroundColor = styles.get("cascadeBackgroundColor");
		cascadeBackgroundImage = styles.get("cascadeBackgroundImage");

//		System.out.println(handle + " : " + xpath + " : " + rect + " : " + color + " / " + backgroundColor + " : "
//				+ " : " + cascadeColor + " / " + cascadeBackgroundColor + " : " + opacity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#
	 * getBackgroundColor ()
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#
	 * getBackgroundRepeat ()
	 */
	public String getBackgroundRepeat() {
		return backgroundRepeat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#
	 * getBackgroundImage ()
	 */
	public String getBackgroundImage() {
		return backgroundImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getColor()
	 */
	public String getColor() {
		return color;
	}

	// /* (non-Javadoc)
	// * @see
	// org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getCssText()
	// */
	// public String getCssText() {
	// return cssText;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getDisplay()
	 */
	public String getDisplay() {
		return display;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getFontFamily()
	 */
	public String getFontFamily() {
		return fontFamily;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getFontSize()
	 */
	public String getFontSize() {
		return fontSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getFontStyle()
	 */
	public String getFontStyle() {
		return fontStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getFontVariant()
	 */
	public String getFontVariant() {
		return fontVariant;
	}

	// /* (non-Javadoc)
	// * @see
	// org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getFontWeight()
	// */
	// public String getFontWeight() {
	// return fontWeight;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getLetterSpacing
	 * ()
	 */
	public String getLetterSpacing() {
		return letterSpacing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getLineHeight()
	 */
	public String getLineHeight() {
		return lineHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getPosition()
	 */
	public String getPosition() {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getTextAlign()
	 */
	public String getTextAlign() {
		return textAlign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getTextDecoration ()
	 */
	public String getTextDecoration() {
		return textDecoration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getVisibility()
	 */
	public String getVisibility() {
		return visibility;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getTagName()
	 */
	public String getTagName() {
		return tagName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getXPath()
	 */
	public String getXPath() {
		return xpath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getRectangle()
	 */
	public Rectangle getRectangle() {
		return rect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#getLinkURL()
	 */
	public URL getLinkURL() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.ICurrentStyles#isLink()
	 */
	public boolean isLink() {
		return isLink;
	}

	public Element getElement() {
		return element;
	}

	public String getComputedColor() {
		return cascadeColor;
	}

	public String getComputedBackgroundColor() {
		return cascadeBackgroundColor;
	}

	public String getComputedBackgroundImage() {
		return cascadeBackgroundImage;
	}

	public boolean hasChildText() {
		return hasChildText;
	}

	public boolean hasDescendantTextWithBGImage() {
		return hasDescendantTextWithBGImage;
	}

	public String[] getChildTexts() {
		return childTexts;
	}

	public String[] getDescendantTextsWithBGImage() {
		return descendantTextsWithBGImage;
	}

	public String getOpacity() {
		return opacity;
	}

	private int calcZoom(double target) {
		return (int) (target * zoomFactor + 0.5f);
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

}
