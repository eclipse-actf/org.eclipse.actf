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

package org.eclipse.actf.visualization.engines.lowvision;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.DebugUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.DecisionMaker;
import org.eclipse.actf.visualization.internal.engines.lowvision.LowVisionProblemConverter;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.internal.engines.lowvision.ScoreUtil;
import org.eclipse.actf.visualization.internal.engines.lowvision.checker.FixedFontChecker;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.io.ImageWriter;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ILowVisionProblem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemGroup;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.ProblemItemLV;
import org.w3c.dom.Document;

/**
 * Utility class to evaluate accessibility of {@link IPageImage}
 */
public class PageEvaluation {
	private static final int UNSET = -1;

	private IPageImage pageImage = null; // rendered image in browser

	private Document document;

	private IStyleSheets styleSheets;

	private ImagePositionInfo[] tmpInteriorImagePositions = null;

	private PageElement[] pageElements = null; // HTML Element info from DOM

	private String[] allowedForegroundColors = null;

	private String[] allowedBackgroundColors = null;

	private int pageWidth = UNSET;

	private int pageHeight = UNSET;

	private String overallRatingString = ""; //$NON-NLS-1$

	private String overallRatingImageString = ""; //$NON-NLS-1$

	/**
	 * Constructor of PageEvaluation utility
	 * 
	 * @param _pageImage target {@link IPageImage}
	 */
	public PageEvaluation(IPageImage _pageImage) {
		this(_pageImage, null, null);
	}

	/**
	 * Constructor of PageEvaluation utility
	 * 
	 * @param _pageImage target {@link IPageImage}
	 * @param _document target {@link Document} to check
	 * @param _styleSheets target {@link IStyleSheets} to check
	 */
	public PageEvaluation(IPageImage _pageImage, Document _document, IStyleSheets _styleSheets) {
		pageImage = _pageImage;
		if (pageImage != null) {
			if (pageImage.hasInteriorImageArraySet()) {
				try {
					pageImage.extractCharacters();
				} catch (ImageException e) {
					e.printStackTrace();
				}
			} else if (tmpInteriorImagePositions != null) {
				pageImage.setInteriorImagePosition(tmpInteriorImagePositions);
				this.tmpInteriorImagePositions = null;
				try {
					pageImage.extractCharacters();
				} catch (ImageException e) {
					e.printStackTrace();
				}
			}
			pageWidth = pageImage.getWidth();
			pageHeight = pageImage.getHeight();
		}

		document = _document;
		styleSheets = _styleSheets;
	}

	/**
	 * Get target {@link IPageImage}
	 * 
	 * @return target {@link IPageImage}
	 */
	public IPageImage getPageImage() {
		return (pageImage);
	}

	/**
	 * Get interior image position (e.g., <img> position in HTML page image)
	 * 
	 * @return array of {@link ImagePositionInfo}
	 */
	public ImagePositionInfo[] getInteriorImagePosition() {
		if (pageImage == null) {
			return (null);
		}
		return (pageImage.getInteriorImagePosition());
	}

	/**
	 * Set interior image position (e.g., <img> position in HTML page image)
	 * 
	 * @param infoArray array of {@link ImagePositionInfo}
	 */
	public void setInteriorImagePosition(ImagePositionInfo[] infoArray) {
		if (pageImage != null) {
			pageImage.setInteriorImagePosition(infoArray);
			try {
				pageImage.extractCharacters();
			} catch (ImageException e) {
				e.printStackTrace();
			}
		} else {
			this.tmpInteriorImagePositions = infoArray;
		}
	}

	/**
	 * @param _styleMap
	 */
	public void setCurrentStyles(Map<String, ICurrentStyles> _styleMap) {
		Set<String> keySet = _styleMap.keySet();
		int len = keySet.size();
		pageElements = new PageElement[len];
		int i = 0;
		for (String key : keySet) {
			try {
				pageElements[i] = new PageElement(key, _styleMap.get(key));
			} catch (ImageException e) {
				e.printStackTrace();
				pageElements[i] = null;
			}
			i++;
		}
	}

	/**
	 * Get allowed foreground colors
	 * 
	 * @return array of allowed foreground colors
	 */
	public String[] getAllowedForegroundColors() {
		return (allowedForegroundColors);
	}

	/**
	 * Get allowed background colors
	 * 
	 * @return array of allowed background colors
	 */
	public String[] getAllowedBackgroundColors() {
		return (allowedBackgroundColors);
	}

	/**
	 * Set allowed foreground/background colors
	 * 
	 * @param _fg array of allowed foreground colors
	 * @param _bg array of allowed background colors
	 */
	public void setAllowedColors(String[] _fg, String[] _bg) {
		allowedForegroundColors = _fg;
		allowedBackgroundColors = _bg;
	}

	/**
	 * Evaluate accessibility of target {@link IPageImage}
	 * 
	 * @param type    target low vision type
	 * @param urlS    target's URL
	 * @param frameId target's frame ID
	 * @return found accessibility issues as list of {@link IProblemItem}
	 * @see LowVisionType
	 */
	public List<IProblemItem> check(LowVisionType type, String urlS, int frameId) {

		List<IProblemItem> problemList = new ArrayList<IProblemItem>();

		if (pageImage != null) {
			try {
				problemList = pageImage.checkCharacters(type, urlS, frameId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (pageElements != null) {
			Vector<LowVisionProblemGroup> pageElementProblemVec = new Vector<LowVisionProblemGroup>();
			int len = pageElements.length;
			for (int i = 0; i < len; i++) {
				PageElement curElement = pageElements[i];
				if (curElement == null) {
					continue;
				}
				ILowVisionProblem[] curProblemArray = curElement.check(type, allowedForegroundColors,
						allowedBackgroundColors);
				int curLen = 0;
				if (curProblemArray != null) {
					curLen = curProblemArray.length;
				}

				// convert to LowVisionProblemGroup
				for (int j = 0; j < curLen; j++) {
					Vector<ILowVisionProblem> tmpVec = new Vector<ILowVisionProblem>();
					tmpVec.addElement(curProblemArray[j]);
					LowVisionProblemGroup lvpGroup = null;
					try {
						lvpGroup = new LowVisionProblemGroup(tmpVec);
						pageElementProblemVec.addElement(lvpGroup);
					} catch (LowVisionProblemException lvpe) {
						lvpe.printStackTrace();
					}
				}
			}

			int totalSize = pageElementProblemVec.size();
			LowVisionProblemGroup[] pageElementProblemArray = new LowVisionProblemGroup[totalSize];
			pageElementProblemVec.toArray(pageElementProblemArray);

			problemList.addAll(LowVisionProblemConverter.convert(pageElementProblemArray, urlS, frameId));

		}

		if (document != null && styleSheets != null) {
			FixedFontChecker ffc = new FixedFontChecker(document, styleSheets);
			problemList.addAll(ffc.check());
		}

		calcOverallRating(problemList);

		return (problemList);
	}

	private void calcOverallRating(List<IProblemItem> problemList) {
		int totalSeverity = 0;
		for (IProblemItem item : problemList) {
			if (item instanceof ProblemItemLV) {
				totalSeverity += ((IProblemItemImage) item).getSeverityLV();
			}
		}
		overallRatingString = ScoreUtil.getScoreString(totalSeverity);
		overallRatingImageString = ScoreUtil.getScoreImageString(totalSeverity);

	}

	// Max size of Problem Map image
	private static final int PROBLEM_MAP_LENGTH = 100;

	/**
	 * Generate report file for unsupported mode
	 * 
	 * @param targetFile target file path to save report
	 * @throws LowVisionException
	 */
	@SuppressWarnings("nls")
	// TODO
	public void unsupportedModeReport(File targetFile) throws LowVisionException {

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n<HEAD>\n<TITLE>Report from LowVision Evaulator</TITLE>\n");
		sb.append("</HEAD><BODY>");
		// TODO
		sb.append("</BODY>\n</HTML>\n");
		if (targetFile != null) {
			try {
				PrintWriter pw = new PrintWriter(targetFile);
				pw.println(sb.toString());
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new LowVisionException("Could not write to " + targetFile);
			}
		}
	}

	/**
	 * Generate report file from {@link IProblemItem} list.
	 * 
	 * @param _path              target path
	 * @param _htmlName          report file name
	 * @param _imgName           report image file name
	 * @param _problemGroupArray target {@link IProblemItem} list
	 * @throws LowVisionException
	 */
	@SuppressWarnings("nls")
	// TODO
	public void generateReport(String _path, String _htmlName, String _imgName, List<IProblemItem> _problemGroupArray)
			throws LowVisionException {
		boolean doMakeProblemMap = true;
		if (this.pageImage == null)
			doMakeProblemMap = false;

		String path = _path;
		if (!(path.endsWith(File.separator))) {
			path += File.separator;
		}
		String htmlPath = path + _htmlName;
		String imgPath = path + _imgName;

		int len = 0;
		if (_problemGroupArray != null) {
			len = _problemGroupArray.size();
		}

		Int2D scoreMap = null;

		if (doMakeProblemMap) {
			int shorter = pageWidth;
			if (pageWidth > pageHeight) {
				shorter = pageHeight;
			}
			int scale = (int) (Math.ceil(((double) shorter) / (double) (PROBLEM_MAP_LENGTH)));
			if (scale <= 0) {
				throw new LowVisionException("scale is out of range: " + scale);
			}
			int mapWidth = (int) (Math.ceil((double) (pageWidth) / (double) (scale)));
			int mapHeight = (int) (Math.ceil((double) (pageHeight) / (double) (scale)));
			scoreMap = new Int2D(mapWidth, mapHeight);

			for (int k = 0; k < len; k++) {
				if (_problemGroupArray.get(k) instanceof ProblemItemLV) {
					ProblemItemLV curProblem = (ProblemItemLV) _problemGroupArray.get(k);

					int groupX = curProblem.getX();
					if (groupX < 0)
						groupX = 0;
					int groupY = curProblem.getY();
					if (groupY < 0)
						groupY = 0;
					int groupWidth = curProblem.getWidth();
					int groupHeight = curProblem.getHeight();
					/*
					 * TODO consideration for inline element x+width or y+height might exseed the
					 * page size overlap the next block level element etc.
					 */
					int rightLimit = groupX + groupWidth;
					if (pageWidth < rightLimit)
						rightLimit = pageWidth;
					int bottomLimit = groupY + groupHeight;
					if (pageHeight < bottomLimit)
						bottomLimit = pageHeight;
					for (int j = groupY; j < bottomLimit; j++) {
						for (int i = groupX; i < rightLimit; i++) {
							// scoreMap.data[j+groupY][i+groupX] = fillingColor;
							// debug
							try {
								scoreMap.getData()[j / scale][i / scale] += curProblem.getSeverityLV();
							} catch (Exception e) {
								e.printStackTrace();
								DebugUtil.errMsg(this,
										"i=" + i + ", j=" + j + ", groupX=" + groupX + ", groupY=" + groupY
												+ ", groupWidth=" + groupWidth + ", groupHeight=" + groupHeight
												+ ", pageWidth=" + pageWidth + ", pageHeight=" + pageHeight);
								throw new LowVisionException("Error while making problem map");
							}
						}
					}
				}
			}

			double scaleDouble = (scale * scale);
			for (int j = 0; j < mapHeight; j++) {
				for (int i = 0; i < mapWidth; i++) {
					scoreMap.getData()[j][i] = DecisionMaker
							.getScoreMapColor((scoreMap.getData()[j][i]) / 100.0 / scaleDouble);
				}
			}

			try {
				ImageWriter.writeBufferedImage(scoreMap.toBufferedImage(), imgPath);
			} catch (LowVisionIOException lvioe) {
				lvioe.printStackTrace();
				throw new LowVisionException("An IO error occurred while writing the problem map file of this page.");
			}
		}
		scoreMap = null;

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n<HEAD>\n<TITLE>Report from LowVision Evaulator</TITLE>\n");
		sb.append("<STYLE type=\"text/css\">\n");
		sb.append("IMG {border:2 solid black}\n");
		sb.append("</STYLE>\n");
		sb.append("</HEAD><BODY>");

		// sb.append( "<DIV>\nOverall rating: <B>" + overallRatingString +
		// "</B></DIV>\n");

		// TODO lv report files -> result dir
		sb.append("<DIV>\nOverall rating: <IMG src=\"./img/" + overallRatingImageString + "\" alt=\""
				+ overallRatingString + "\"></DIV>\n");

		sb.append("<HR>");
		sb.append("<DIV align=\"center\">\n");
		if (doMakeProblemMap) {
			sb.append("Problem Map<BR>\n");
			sb.append("<IMG src=\"" + _imgName + "\" alt=\"score map\" ");
			if (pageWidth >= pageHeight) {
				sb.append("width=\"75%\"");
			} else {
				sb.append("height=\"75%\"");
			}
			sb.append(">\n");
		} else {
			sb.append("Problem map is not available for this page.");
		}
		sb.append("</DIV>\n");
		sb.append("</BODY>\n</HTML>\n");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(htmlPath));
			pw.println(sb.toString());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionException("Could not write to " + htmlPath);
		}
	}

}
