/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.eval.problem.ILowvisionProblemSubtype;
import org.eclipse.actf.visualization.internal.engines.lowvision.checker.W3CColorChecker;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorCSS;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ColorWarning;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.FixedSizeFontProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProhibitedBackgroundColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProhibitedBothColorsProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProhibitedForegroundColorProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.SmallFontProblem;
import org.eclipse.swt.graphics.Rectangle;

/*
 * informations from HTML DOM
 */
public class PageElement {

	// IE fontsize
	static final String IE_LARGEST_FONT = "16pt"; //$NON-NLS-1$
	static final double IE_EM_SCALING = 1.33; // "1em" in largest
	// (experimental)
	static final double IE_LARGER_SCALING = 1.67; // "larger" in largest
	// (experimental)
	static final double IE_SMALLER_SCALING = 1.00; // "smaller" in largest
	// (experimental)

	// severity for color problems
	public static final double SEVERITY_PROHIBITED_FOREGROUND_COLOR = 0.5;
	public static final double SEVERITY_PROHIBITED_BACKGROUND_COLOR = 0.5;
	public static final double SEVERITY_PROHIBITED_BOTH_COLORS = SEVERITY_PROHIBITED_FOREGROUND_COLOR
			+ SEVERITY_PROHIBITED_BACKGROUND_COLOR;

	private static final String DELIM = "/"; //$NON-NLS-1$

	// text check
	@SuppressWarnings("nls")
	private static final String[] nonTextTagNames = { "area", "base", "basefont", "br", "col", "colgroup", "frame",
			"frameset", "head", "html", "hr", "img", "isindex", "link", "meta", "optgroup", "param", "script", "select",
			"style", "title" };

	// tags that change font size when that succeeded pt from <body>
	@SuppressWarnings("nls")
	private static final String[] fontSizeChangeTags = { "big", "code", "h1", "h2", "h3", "h5", "h6", "kbd", "pre",
			"samp", "small", "sub", "sup", "tt" };

	/*
	 * tags that usually uses same font size (can change by using %,em,ex) -> now we
	 * can change font size of these elements
	 */
	// @SuppressWarnings("nls")
	// private static final String[] alwaysFixedFontSizeTags = { "button",
	// "option", "textarea" };

	public static final int UNSET_POSITION = -1;

	public static final int UNSET_COLOR = -1;

	private String id = null; // eclipse-actf-id

	private ICurrentStyles style = null;

	private String tagName = null;

	// position in the image
	private int x = UNSET_POSITION;
	private int y = UNSET_POSITION;
	private int width = UNSET_POSITION;
	private int height = UNSET_POSITION;

	private int foregroundColor = UNSET_COLOR;
	private int backgroundColor = UNSET_COLOR;

	private boolean bForegroundAlpha = false;
	private boolean bBackgroundAlpha = false;
	private boolean bOpacity = false;

	public PageElement(String _key, ICurrentStyles _cs) throws ImageException {
		id = _key;
		style = _cs;

		tagName = style.getTagName().toLowerCase();
		setDimension();
		setColors();
	}

	private void setDimension() {
		// TODO confirm
		Rectangle rect = style.getRectangle();
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

	private void setColors() throws ImageException {
		if (!isTextTag()) {
			return;
		}

		String fgStr = style.getComputedColor();
		String bgStr = style.getComputedBackgroundColor();
		String opacityStr = style.getOpacity();

		try {
			if (opacityStr != null) {
				double opacity = Double.parseDouble(opacityStr);
				if (opacity < 1) {
					bOpacity = true;
				}
			}
		} catch (Exception e1) {
			DebugPrintUtil.devOrDebugPrintStackTrace(e1);
		}

		// System.out.println(style.getXPath() + ":\t" + fgStr + " / " + bgStr + " / " +
		// opacityStr);

		try {
			foregroundColor = (new ColorCSS(fgStr)).toInt();
		} catch (ColorException e) {
			if (ColorException.ALPHA_EXISTS.equals(e.getMessage())) {
				bForegroundAlpha = true;
			} else {
				throw new ImageException("Could not interpret colors."); //$NON-NLS-1$
			}
		}
		try {
			backgroundColor = (new ColorCSS(bgStr)).toInt();
		} catch (ColorException e) {
			if (ColorException.ALPHA_EXISTS.equals(e.getMessage())) {
				bBackgroundAlpha = true;
			} else {
				throw new ImageException("Could not interpret colors."); //$NON-NLS-1$
			}
		}
	}

	public int getX() {
		return (x);
	}

	public int getY() {
		return (y);
	}

	public int getWidth() {
		return (width);
	}

	public int getHeight() {
		return (height);
	}

	public String getTagName() {
		return (tagName);
	}

	public int getForegroundColor() {
		return (foregroundColor);
	}

	public int getBackgroundColor() {
		return (backgroundColor);
	}

	// _lvType: for LowVision error check
	public ILowVisionProblem[] check(LowVisionType _lvType, String[] allowedFgColors, String[] allowedBgColors) {
		Vector<ILowVisionProblem> problemVec = new Vector<ILowVisionProblem>();

		// ignore elements not in the rendered area
		if (x < 0 || y < 0) {
			return (new LowVisionProblem[0]);
		}

		try {
			problemVec.addAll(checkColors(_lvType));
		} catch (LowVisionException e) {
			DebugUtil.errMsg(this, "Error occurred in checking colors: id = " //$NON-NLS-1$
					+ this.id);
			e.printStackTrace();
		}

		/*
		 * migration from IE currentStyle to computedStyle
		 * 
		 * FixedSizeFontProblem fsfp = null; try { fsfp = checkFixedSizeFont(_lvType); }
		 * catch (LowVisionException e) { DebugUtil.errMsg(this,
		 * "Error occurred in checking fixed-size font: id = " //$NON-NLS-1$ + this.id);
		 * e.printStackTrace(); }
		 */

		SmallFontProblem sfp = null;
		try {
			sfp = checkSmallFont(_lvType);
		} catch (LowVisionException e) {
			DebugUtil.errMsg(this, "Error occurred in checking small font: id = " + this.id); //$NON-NLS-1$
			e.printStackTrace();
		}
		/*
		 * if (fsfp != null && sfp != null) { // // calc severity // double proba =
		 * Math.max( fsfp.getProbability(), // sfp.getProbability() );
		 * 
		 * int type = fsfp.getLowVisionProblemType(); String attrName = null; switch
		 * (type) { case ILowvisionProblemSubtype.LOWVISION_FIXED_SIZE_FONT_PROBLEM:
		 * attrName = fsfp.getAttrName(); try {
		 * fsfp.changeType(ILowvisionProblemSubtype.LOWVISION_FIXED_SMALL_FONT_PROBLEM);
		 * if (attrName != null && !attrName.isEmpty()) { fsfp.setAttrName(attrName); }
		 * problemVec.addElement(fsfp); } catch (LowVisionProblemException e) {
		 * e.printStackTrace(); } break; case
		 * ILowvisionProblemSubtype.LOWVISION_FIXED_SIZE_FONT_WARNING: attrName =
		 * fsfp.getAttrName(); try {
		 * fsfp.changeType(ILowvisionProblemSubtype.LOWVISION_FIXED_SMALL_FONT_WARNIG);
		 * if (attrName != null && !attrName.isEmpty()) { fsfp.setAttrName(attrName); }
		 * problemVec.addElement(fsfp); } catch (LowVisionProblemException e) {
		 * e.printStackTrace(); } break; default: DebugUtil.errMsg(this,
		 * "not fixed size font error/warning: type = " + type); //$NON-NLS-1$ } // use
		 * fixed severity } else if (fsfp != null) { problemVec.addElement(fsfp); } else
		 */
		if (sfp != null) {
			problemVec.addElement(sfp);
		}

		ProhibitedForegroundColorProblem pfcp = null;
		ProhibitedBackgroundColorProblem pbcp = null;

		if (allowedFgColors != null && allowedFgColors.length > 0 && !bForegroundAlpha) {
			try {
				pfcp = checkAllowedForegroundColors(_lvType, allowedFgColors);
			} catch (LowVisionException lve) {
				lve.printStackTrace();
			}
		}
		if (allowedBgColors != null && allowedBgColors.length > 0 && !bBackgroundAlpha) {
			try {
				pbcp = checkAllowedBackgroundColors(_lvType, allowedBgColors);
			} catch (LowVisionException lve) {
				lve.printStackTrace();
			}
		}
		if ((pfcp != null) && (pbcp != null)) {// fg/bg
			try {
				problemVec.addElement(
						new ProhibitedBothColorsProblem(this, _lvType, PageElement.SEVERITY_PROHIBITED_BOTH_COLORS));
			} catch (LowVisionProblemException lvpe) {
				lvpe.printStackTrace();
			}
			pfcp = null;
			pbcp = null;
		} else if (pfcp != null) { // fg
			problemVec.addElement(pfcp);
		} else if (pbcp != null) { // bg
			problemVec.addElement(pbcp);
		}

		int size = problemVec.size();
		ILowVisionProblem[] problemArray = new ILowVisionProblem[size];
		for (int i = 0; i < size; i++) {
			problemArray[i] = problemVec.elementAt(i);
		}
		return (problemArray);
	}

	private List<ILowVisionProblem> checkColors(LowVisionType _lvType) throws LowVisionException {

		List<ILowVisionProblem> result = new ArrayList<ILowVisionProblem>();

		if (!isTextTag()) {
			return (result);
		}
		try {
			if (style.hasDescendantTextWithBGImage()) {
				ColorProblem cp = new ColorProblem(this, _lvType, 0);
				cp.setElement(style.getElement());
				cp.setHasBackgroundImage(true);
				cp.setTargetStrings(style.getDescendantTextsWithBGImage());
				cp.setIsWarning(true);
				result.add(cp);
			}

			if (!style.hasChildText()) {
				return (result);
			}

			boolean hasBgImage = (style.getComputedBackgroundImage() != null
					&& !style.getComputedBackgroundImage().equalsIgnoreCase("none"));

			ColorWarning cw;
			if (bOpacity) {
				cw = new ColorWarning(ColorWarning.OPACITY, this, _lvType);
				result.add(cw);
				return (result);
			} else if (bForegroundAlpha) {
				if (bBackgroundAlpha) {
					cw = new ColorWarning(ColorWarning.BOTH, this, _lvType);
				} else {
					cw = new ColorWarning(ColorWarning.FONT, this, _lvType);
				}
				cw.setElement(style.getElement());
				result.add(cw);
				return (result);
			} else if (bBackgroundAlpha) {
				cw = new ColorWarning(ColorWarning.BACKGROUND, this, _lvType);
				cw.setElement(style.getElement());
				result.add(cw);
				return (result);
			}

			ColorIRGB fgOrg = new ColorIRGB(foregroundColor);
			ColorIRGB bgOrg = new ColorIRGB(backgroundColor);

			W3CColorChecker w3c = new W3CColorChecker(fgOrg, bgOrg);
			double contrast = w3c.calcContrast();
			if (contrast < 7) {
				ColorProblem cp = new ColorProblem(this, _lvType, w3c.calcSeverity());
				cp.setElement(style.getElement());
				cp.setContrast(contrast);
				cp.setTargetStrings(style.getChildTexts());
				cp.setHasBackgroundImage(hasBgImage);
				result.add(cp);
			}
		} catch (LowVisionProblemException e) {
		}
		return (result);

		/*
		 * if (!(_lvType.doChangeColors())) { //TODO ColorIRGB fgSim = null; ColorIRGB
		 * bgSim = null; try { fgSim = new
		 * ColorIRGB(_lvType.convertColor(foregroundColor)); bgSim = new
		 * ColorIRGB(_lvType.convertColor(backgroundColor)); } catch (LowVisionException
		 * e) { e.printStackTrace(); throw new
		 * LowVisionException("Could not convert colors."); //$NON-NLS-1$ }
		 * 
		 * W3CColorChecker w3c = new W3CColorChecker(fgSim, bgSim); double severity =
		 * w3c.calcSeverity(); try { if (severity <= 0.0) { if
		 * (style.getBackgroundImage() != null &&
		 * !style.getBackgroundImage().equalsIgnoreCase( "none")) { ColorProblem result
		 * = new ColorProblem(this, _lvType, 0); result.setHasBackgroundImage(true);
		 * return (result); } return (null); } else { ColorProblem result = new
		 * ColorProblem(this, _lvType, severity); if (style.getBackgroundImage() != null
		 * && !style.getBackgroundImage().equalsIgnoreCase( "none"))
		 * result.setHasBackgroundImage(true); return (result); } } catch
		 * (LowVisionProblemException e) { return (null); } } return(null);
		 */
	}

	/*
	 * fixed size font check experimental result (in IE6) (1)mm,cm,in,pt,pc,px ->
	 * fixed (2)larger,smaller, xx-small to xx-large -> variable (3)em,ex,% -> same
	 * with parent (if not specified in ancestor -> variable)
	 * 
	 * if <BODY> uses "pt" -> consider IE added this configuration
	 * 
	 * private static final Pattern regexFontSequence =
	 * Pattern.compile("^(([^\\/]+\\/)*([^\\/]+))$" );
	 */
	private static final short FONT_SIZE_UNKNOWN = 0;

	private static final short FONT_SIZE_FIXED = 1; // in, cm, mm, pc, px

	private static final short FONT_SIZE_PT = 2; // pt

	private static final short FONT_SIZE_RELATIVE = 3; // smaller, larger

	// xx-small,..., xx-large
	private static final short FONT_SIZE_ABSOLUTE = 4;

	private static final short FONT_SIZE_PERCENT = 5; // %

	private static final short FONT_SIZE_EM = 6; // em, ex

	private static final short FONT_SIZE_REM = 7; // rem

	private static final short FONT_SIZE_V = 8; // vw, vh,vmin,vmax

	private static final String[] FIXED_FONT_STRINGS = { "in", "cm", "mm", "pc", "px" };

	@SuppressWarnings("unused")
	private FixedSizeFontProblem checkFixedSizeFont(LowVisionType _lvType) throws LowVisionException {
		// if (!(_lvType.doBlur())) {
		// return (null);
		// }

		if (!isTextTag()) {
			return (null);
		}

		if (!style.hasChildText()) {
			return (null);
		}

		// difficult to change font size
		// if (isAlwaysFixedSizeFontTag(tagName)) {
		// return (null);
		// }

		String fontStr = style.getFontSize().toLowerCase();

		//System.out.println("check fixed:" + fontStr);

		// directly under the <BODY>
		if (fontStr.indexOf(DELIM) == -1) {
			fontStr = digitToFontSetting(fontStr);
			short type = fontSizeType(fontStr);

			if (type == FONT_SIZE_FIXED) {
				// not include "pt" because IE usually returns "pt"
				String typeS = null;

				for (String tmpS : FIXED_FONT_STRINGS) {
					if (fontStr.endsWith(tmpS)) {
						typeS = tmpS;
					}
				}

				try {
					FixedSizeFontProblem problem;
					problem = new FixedSizeFontProblem(ILowvisionProblemSubtype.LOWVISION_FIXED_SIZE_FONT_PROBLEM, this,
							_lvType);
					if (typeS != null) {
						problem.setAttrName(typeS);
					}
					problem.setElement(style.getElement());
					return (problem);
				} catch (LowVisionProblemException e) {
					e.printStackTrace();
					return (null);
				}
			} else if (type == FONT_SIZE_PT) {
				FixedSizeFontProblem problem;
				try {
					problem = new FixedSizeFontProblem(ILowvisionProblemSubtype.LOWVISION_FIXED_SIZE_FONT_WARNING, this,
							_lvType);
					problem.setAttrName("pt");
					problem.setElement(style.getElement());
					return (problem);
				} catch (LowVisionProblemException e) {
					e.printStackTrace();
					return (null);
				}
			} else {
				return (null);
			}
		}

		boolean fixedFlag = false;
		StringTokenizer st = new StringTokenizer(fontStr, DELIM);
		int tokenCount = st.countTokens();
		String myFont = digitToFontSetting(st.nextToken());
		short myType = fontSizeType(myFont);
		if (myType == FONT_SIZE_FIXED) {
			fixedFlag = true;
		} else if (myType == FONT_SIZE_RELATIVE || myType == FONT_SIZE_ABSOLUTE) {
			// fixedFlag = false;
		} else { // "pt", "em", "ex", "%"
			String[] fontSequence = new String[tokenCount];
			fontSequence[tokenCount - 1] = myFont;
			for (int i = tokenCount - 2; i >= 0; i--) {
				fontSequence[i] = digitToFontSetting(st.nextToken());
			}
			StringTokenizer stTag = new StringTokenizer(tagName, DELIM);
			if (stTag.countTokens() != tokenCount) {
				throw new LowVisionException("# of tagNames and fontSizes did not match."); //$NON-NLS-1$
			}
			String[] tagNameSequence = new String[tokenCount];
			for (int i = tokenCount - 1; i >= 0; i--) {
				tagNameSequence[i] = stTag.nextToken();
			}

			// fixedFlag = false;
			String curFont = fontSequence[0]; // <BODY>
			short curType = fontSizeType(curFont);
			boolean firstPtFlag = true;

			// if( curType == FONT_SIZE_PARENT ){
			// firstPtFlag = false;
			// }else if( curType == FONT_SIZE_FIXED ){
			// fixedFlag = true;
			// }
			if (curType != FONT_SIZE_PT) {
				firstPtFlag = false;
			}
			if (curType == FONT_SIZE_FIXED) {
				fixedFlag = true;
			}

			for (int i = 1; i < tokenCount; i++) {
				String tmpFont = fontSequence[i];
				String tmpTag = tagNameSequence[i];
				// <TD>,<TH> -> same initialization at <BODY>
				if (tmpTag.equals("td") || tmpTag.equals("th")) { //$NON-NLS-1$ //$NON-NLS-2$
					firstPtFlag = true;
					if (curType != FONT_SIZE_PT) {
						firstPtFlag = false;
					}
					if (curType == FONT_SIZE_FIXED) {
						fixedFlag = true;
					}
				} else {
					if (curFont.equals(tmpFont)) { // not defined by user
						continue;
					} else {
						short tmpType = fontSizeType(tmpFont);
						if (tmpType == FONT_SIZE_FIXED) {
							fixedFlag = true;
							firstPtFlag = true;
						} else if (tmpType == FONT_SIZE_RELATIVE || tmpType == FONT_SIZE_ABSOLUTE) {
							fixedFlag = false;
							firstPtFlag = true;
						} else if (tmpType == FONT_SIZE_PT) {
							if (!firstPtFlag) {
								firstPtFlag = true;
								fixedFlag = false; // need check
							} else if (curType != FONT_SIZE_PT || fixedFlag == true || !isFontSizeChangeTag(tmpTag)) {
								fixedFlag = true;
							}
							// else{
							// // "pt" & parent "pt" & variable & <PRE> etc. ->
							// variable
							// }
						}
						// else{
						// // "em", "ex", "%"-> same as parent
						// }
						curFont = tmpFont;
						curType = tmpType;
					}
				}
			}
		}

		if (fixedFlag) {
			try {
				FixedSizeFontProblem problem = new FixedSizeFontProblem(
						ILowvisionProblemSubtype.LOWVISION_FIXED_SIZE_FONT_PROBLEM, this, _lvType);
				problem.setElement(style.getElement());
				return (problem);
			} catch (LowVisionProblemException e) {
				e.printStackTrace();
				return (null);
			}
		} else {
			return (null);
		}
	}

	@SuppressWarnings("nls")
	private short fontSizeType(String _fontSize) {
		String s = _fontSize.toLowerCase();

		if (s.endsWith("vw") || s.endsWith("vh") || s.endsWith("vmin") || s.endsWith("vmax")) {
			return (FONT_SIZE_V);
		} else if (s.endsWith("mm") || s.endsWith("cm") || s.endsWith("in") || s.endsWith("pc") || s.endsWith("px")) {
			return (FONT_SIZE_FIXED);
		} else if (s.endsWith("pt")) {
			return (FONT_SIZE_PT);
		} else if (s.endsWith("%")) {
			return (FONT_SIZE_PERCENT);
		} else if (s.endsWith("rem")) {
			return (FONT_SIZE_REM);
		} else if (s.endsWith("em") || s.endsWith("ex")) {
			return (FONT_SIZE_EM);
		} else if (s.equals("smaller") || s.equals("larger")) {
			return (FONT_SIZE_RELATIVE);
		} else {
			return (FONT_SIZE_ABSOLUTE);
		}
	}

	@SuppressWarnings("nls")
	private String digitToFontSetting(String _fontStr) throws LowVisionException {

		if (_fontStr.length() == 1) {
			if (_fontStr.equals("1")) {
				return ("xx-small");
			} else if (_fontStr.equals("2")) {
				return ("x-small");
			} else if (_fontStr.equals("3")) {
				return ("small");
			} else if (_fontStr.equals("4")) {
				return ("medium");
			} else if (_fontStr.equals("5")) {
				return ("large");
			} else if (_fontStr.equals("6")) {
				return ("x-large");
			} else if (_fontStr.equals("7")) {
				return ("xx-large");
			} else {
				throw new LowVisionException("Invalid font size setting: " + _fontStr);
			}
		} else if (_fontStr.startsWith("+")) {
			if (_fontStr.equals("+1")) {
				return ("120%");
			} else if (_fontStr.equals("+2")) {
				return ("144%");
			} else if (_fontStr.equals("+3")) {
				return ("173%");
			} else if (_fontStr.equals("+4")) {
				return ("207%");
			} else if (_fontStr.equals("+5")) {
				return ("249%");
			} else if (_fontStr.equals("+6")) {
				return ("299%");
			} else if (_fontStr.equals("+0")) {
				// used in some pages
				return ("100%");
			} else {
				throw new LowVisionException("Invalid font size setting: " + _fontStr);
			}
		} else if (_fontStr.startsWith("-")) {
			if (_fontStr.equals("-1")) {
				return ("83%");
			} else if (_fontStr.equals("-2")) {
				return ("69%");
			} else if (_fontStr.equals("-3")) {
				return ("58%");
			} else if (_fontStr.equals("-4")) {
				return ("48%");
			} else if (_fontStr.equals("-5")) {
				return ("40%");
			} else if (_fontStr.equals("-6")) {
				return ("33%");
			} else if (_fontStr.equals("-0")) {
				return ("100%");
			} else {
				throw new LowVisionException("Invalid font size setting: " + _fontStr);
			}
		} else {
			return (_fontStr);
		}
	}

	private boolean isFontSizeChangeTag(String _st) {
		int len = fontSizeChangeTags.length;
		String s = _st.toLowerCase();
		for (int i = 0; i < len; i++) {
			if (s.equals(fontSizeChangeTags[i])) {
				return (true);
			}
		}
		return (false);
	}

	// private boolean isAlwaysFixedSizeFontTag(String _st) {
	// String s = _st.toLowerCase();
	// int index = s.indexOf(DELIM);
	// if (index > -1) {
	// s = s.substring(0, index);
	// }
	// int len = alwaysFixedFontSizeTags.length;
	// for (int i = 0; i < len; i++) {
	// if (s.equals(alwaysFixedFontSizeTags[i])) {
	// return (true);
	// }
	// }
	// return (false);
	// }

	/*
	 * note: reset at td/th is experimental behaviour in IE6
	 */
	@SuppressWarnings("nls")
	private SmallFontProblem checkSmallFont(LowVisionType _lvType) throws LowVisionException {
		if (!(_lvType.doBlur())) {
			return (null);
		}

		double eyesightLength = _lvType.getEyesightLength();

		if (!isTextTag()) {
			return (null);
		}

		if (!style.hasChildText()) {
			return (null);
		}

		/*
		 * TODO <OPTION>: offsetWidth, offsetHeight = 0 -> can't highlight target
		 * 
		 * need to select parent <SELECT>
		 */
		if (tagName.startsWith("option")) {
			return (null);
		}

		String fontStr = style.getFontSize().toLowerCase();

		//System.out.println("check small:" + fontStr);

		// reset at TD/TH
		StringTokenizer fontSt = new StringTokenizer(fontStr, DELIM);
		Vector<String> fontSequence = new Vector<String>();
		StringTokenizer tagSt = new StringTokenizer(tagName, DELIM);
		String curFont = fontSt.nextToken();
		String curTag = tagSt.nextToken();
		fontSequence.addElement(curFont);
		while (fontSt.hasMoreTokens()) {
			String tmpFont = fontSt.nextToken();
			curTag = tagSt.nextToken();
			if (curTag.equals("td") || curTag.equals("th")) {
				if (!(tmpFont.equals(curFont))) {
					fontSequence.addElement(tmpFont);
				}
				break;
			} else {
				if (!(tmpFont.equals(curFont))) {
					fontSequence.addElement(tmpFont);
				}
			}
			curFont = tmpFont;
		}

		int numFontSizeSettings = fontSequence.size();
		String[] fontSizeSettings = new String[numFontSizeSettings];
		for (int i = 0; i < numFontSizeSettings; i++) {
			String tmpSetting = fontSequence.elementAt(i);
			fontSizeSettings[i] = digitToFontSetting(tmpSetting);
		}
		fontSequence = null;

		/*
		 * if last value is "pt"-> consider the automatic setting by IE
		 * 
		 * define LARGEST as default to check the small size font in the LARGEST setting
		 * 
		 */
		String curFontSize = fontSizeSettings[numFontSizeSettings - 1];
		/*
		 * -> Do not replace "pt" values and evaluate it based on the value that used in
		 * IE. If the size is small, create warning.
		 */
		/*
		 * if (fontSizeType(curFontSize) == FONT_SIZE_PT) {
		 * fontSizeSettings[numFontSizeSettings - 1] = IE_LARGEST_FONT; for (int i =
		 * numFontSizeSettings - 2; i >= 0; i--) { if
		 * (fontSizeSettings[i].equals(curFontSize)) { fontSizeSettings[i] =
		 * IE_LARGEST_FONT; } else { break; } } }
		 */

		float scaling = 1.0f; // smaller, larger, em, ex, %
		short curType = FONT_SIZE_UNKNOWN;
		for (int i = 0; i < numFontSizeSettings; i++) {
			curFontSize = fontSizeSettings[i];
			curType = fontSizeType(curFontSize);
			if (curType == FONT_SIZE_FIXED || curType == FONT_SIZE_PT) {
				break;
			} else if (curType == FONT_SIZE_ABSOLUTE) {
				if (curFontSize.equals("xx-large")) {
					curFontSize = "48pt";
				} else if (curFontSize.equals("x-large")) {
					curFontSize = "32pt";
				} else if (curFontSize.equals("large")) {
					curFontSize = "24pt";
				} else if (curFontSize.equals("medium")) {
					curFontSize = "18pt";
				} else if (curFontSize.equals("small")) {
					curFontSize = "16pt";
				} else if (curFontSize.equals("x-small")) {
					curFontSize = "14pt";
				} else if (curFontSize.equals("xx-small")) {
					curFontSize = "12pt";
				}
				break;
			} else if (curType == FONT_SIZE_PERCENT) {
				double value = Double.parseDouble(curFontSize.substring(0, curFontSize.length() - 1));
				scaling *= (value / 100.0);
			} else if (curType == FONT_SIZE_REM) {
				// TODO get Root(html element) font size and evaluate size
				// (font size check for relative spec is currently not used )
			} else if (curType == FONT_SIZE_EM) {
				double value = 0.0;
				value = Double.parseDouble(curFontSize.substring(0, curFontSize.length() - 2));
				if (curFontSize.endsWith("ex")) {
					value /= 2.0;
				}
				scaling *= (value * IE_EM_SCALING);
			} else if (curType == FONT_SIZE_V) {
				// TODO get viewport and evaluate size
				// (font size check for relative spec is currently not used )
			} else if (curFontSize.equals("larger")) {
				scaling *= IE_LARGER_SCALING;
			} else if (curFontSize.equals("smaller")) {
				scaling *= IE_SMALLER_SCALING;
			} else {
				throw new LowVisionException("unknown font size setting: " + curFontSize);
			}
		}

		if (curType == FONT_SIZE_V || curType == FONT_SIZE_REM || curType == FONT_SIZE_UNKNOWN) {
			return (null);
		}

		if (curType != FONT_SIZE_FIXED && curType != FONT_SIZE_PT && curType != FONT_SIZE_ABSOLUTE) {
			curFontSize = IE_LARGEST_FONT;
		}

		float value;
		try {
			// value = Float.parseFloat(curFontSize.substring(0,
			// curFontSize.length() - 2));
			String numPart = curFontSize.replaceAll("[^0-9\\.]", "");
			value = Float.parseFloat(numPart);
		} catch (Exception e) {
			throw new LowVisionException("unknown font size unit: " + curFontSize);
		}

		float sizeInMm = 0.0f;
		if (curFontSize.endsWith("in")) {
			sizeInMm = LengthUtil.in2mm(value);
		} else if (curFontSize.endsWith("cm")) {
			sizeInMm = LengthUtil.cm2mm(value);
		} else if (curFontSize.endsWith("mm")) {
			sizeInMm = value;
		} else if (curFontSize.endsWith("pt")) {
			sizeInMm = LengthUtil.pt2mm(value);
		} else if (curFontSize.endsWith("pc")) {
			sizeInMm = LengthUtil.pc2mm(value);
		} else if (curFontSize.endsWith("px")) {
			sizeInMm = LengthUtil.px2mm(value);
		} else {
			throw new LowVisionException("unknown font size unit: " + curFontSize);
		}
		sizeInMm *= scaling;

		// can distinguish "c" and "o"?
		// size of "c" is about half of char size
		// disconnected part of "c" is about 1/5 of "c"
		double severity = 2.0 - sizeInMm / (10.0 * eyesightLength);

		//System.out.println(severity + " : " + sizeInMm + ", " + eyesightLength);

		if (severity > 1.0)
			severity = 1.0;
		else if (severity < 0.0)
			severity = 0.0;

		if (severity > 0.0) {
			try {
				// fixed severity
				SmallFontProblem problem = new SmallFontProblem(this, _lvType);
				problem.setElement(style.getElement());
				return (problem);
			} catch (LowVisionProblemException e) {
				e.printStackTrace();
				return (null);
			}
		} else {
			return (null);
		}
	}

	private ProhibitedForegroundColorProblem checkAllowedForegroundColors(LowVisionType _lvType,
			String[] _allowedColors) throws LowVisionException {
		if (_allowedColors == null) {
			return (null);
		}
		int len = _allowedColors.length;
		if (len == 0) {
			return (null);
		}

		if (!isTextTag()) {
			return (null);
		}

		// TODO check link color?
		if (tagName.startsWith("a/")) { //$NON-NLS-1$
			return (null);
		}

		// use "black" as default
		// TODO use system color
		if (foregroundColor == ColorCSS.DEFAULT_COLOR_INT) {
			return (null);
		}

		for (int i = 0; i < len; i++) {
			String curColorString = _allowedColors[i];
			ColorIRGB templateColor = null;
			try {
				templateColor = new ColorIRGB(curColorString);
			} catch (ColorException ce) {
				ce.printStackTrace();
				throw new LowVisionException("ColorException occurs while converting String \"" //$NON-NLS-1$
						+ curColorString + "\" to a color."); //$NON-NLS-1$
			}
			if (templateColor.equals(foregroundColor)) {
				return (null);
			}
		}

		try {
			return (new ProhibitedForegroundColorProblem(this, _lvType,
					PageElement.SEVERITY_PROHIBITED_FOREGROUND_COLOR));
		} catch (LowVisionProblemException lvpe) {
			lvpe.printStackTrace();
			return (null);
		}
	}

	private ProhibitedBackgroundColorProblem checkAllowedBackgroundColors(LowVisionType _lvType,
			String[] _allowedColors) throws LowVisionException {
		if (_allowedColors == null) {
			return (null);
		}
		int len = _allowedColors.length;
		if (len == 0) {
			return (null);
		}

		if (!isTextTag()) {
			return (null);
		}

		// use transparent as defaul background-color
		if (backgroundColor == ColorCSS.DEFAULT_BACKGROUND_COLOR_INT) {
			return (null);
		}

		for (int i = 0; i < len; i++) {
			String curColorString = _allowedColors[i];
			ColorIRGB templateColor = null;
			try {
				templateColor = new ColorIRGB(curColorString);
			} catch (ColorException ce) {
				ce.printStackTrace();
				throw new LowVisionException("ColorException occurs while converting String \"" //$NON-NLS-1$
						+ curColorString + "\" to a color."); //$NON-NLS-1$
			}
			if (templateColor.equals(backgroundColor)) {
				return (null);
			}
		}
		try {
			return (new ProhibitedBackgroundColorProblem(this, _lvType,
					PageElement.SEVERITY_PROHIBITED_BACKGROUND_COLOR));
		} catch (LowVisionProblemException lvpe) {
			lvpe.printStackTrace();
			return (null);
		}
	}

	private boolean isTextTag() {
		String tagName = style.getTagName().toLowerCase();
		int len = nonTextTagNames.length;
		for (int i = 0; i < len; i++) {
			if (tagName.startsWith(nonTextTagNames[i] /* +"/" */)) {
				return (false);
			}
			// if( tagName.equals( nonTextTagNames[i] ) ){
			// return( false );
			// }
		}
		return (true);
	}

}
