/*******************************************************************************
 * Copyright (c) 2008, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui.editor.browser;

import java.net.URL;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;

/**
 * Interface for current style information of the Element
 */
public interface ICurrentStyles {

	/**
	 * @return XPath (child path sequence) of the element
	 */
	public abstract String getXPath();

	/**
	 * @return tag name of the element
	 */
	public abstract String getTagName();

	/**
	 * @return get {@link Rectangle} of the element
	 */
	public abstract Rectangle getRectangle();

	/**
	 * @return true if the element is link
	 */
	public abstract boolean isLink();

	/**
	 * @return the destination URL of the link
	 */
	public abstract URL getLinkURL();

	/**
	 * @return background color
	 */
	public abstract String getBackgroundColor();

	/**
	 * @return background repeat
	 */
	public abstract String getBackgroundRepeat();

	/**
	 * @return background image
	 */
	public String getBackgroundImage();

	/**
	 * @return foreground color
	 */
	public abstract String getColor();

	// /**
	// * @return
	// */
	// public abstract String getCssText(); //style
	//
	// /**
	// * @return
	// */
	// public abstract String getFontWeight(); //style

	/**
	 * @return display
	 */
	public abstract String getDisplay();

	/**
	 * @return font family
	 */
	public abstract String getFontFamily();

	/**
	 * @return font size
	 */
	public abstract String getFontSize();

	/**
	 * @return font style
	 */
	public abstract String getFontStyle();

	/**
	 * @return font variant
	 */
	public abstract String getFontVariant();

	/**
	 * @return letter spacing
	 */
	public abstract String getLetterSpacing();

	/**
	 * @return line height
	 */
	public abstract String getLineHeight();

	/**
	 * @return position
	 */
	public abstract String getPosition();

	/**
	 * @return text align
	 */
	public abstract String getTextAlign();

	/**
	 * @return text decoration
	 */
	public abstract String getTextDecoration();

	/**
	 * @return visibility
	 */
	public abstract String getVisibility();

	/**
	 * @return target Element
	 */
	public abstract Element getElement();

	/**
	 * @return computed foreground color
	 */
	public String getComputedColor();

	/**
	 * @return computed background color
	 */
	public String getComputedBackgroundColor();

	/**
	 * @return computed background image (considering transparent background color)
	 */
	public String getComputedBackgroundImage();

	/**
	 * @return opacity
	 */
	public String getOpacity();

	/**
	 * @return has text directly under the element
	 */
	public boolean hasChildText();

	/**
	 * @return has text descendant that may be affected by the background image of
	 *         this style
	 */
	public boolean hasDescendantTextWithBGImage();

	/**
	 * @return texts directly under the element
	 */
	public String[] getChildTexts();

	/**
	 * @return descendant texts under the element
	 */
	public String[] getDescendantTextsWithBGImage();
	
	/**
	 * @return ZoomFactor of the borwser.
	 */
	public double getZoomFactor();

}