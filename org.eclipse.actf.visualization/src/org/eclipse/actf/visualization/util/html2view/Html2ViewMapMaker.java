/*******************************************************************************
 * Copyright (c) 2007, 2025 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.util.html2view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.JapaneseEncodingDetector;

/**
 * Utility class to enable mapping between line number and ACTF_ID. This utility
 * embeds ACTF_ID into HTML file.
 */
@SuppressWarnings("nls")
public class Html2ViewMapMaker {
	Vector<Html2ViewMapData> html2viewV;

	int id;

	int line;

	int column;

	int startLine;

	int startColumn;

	boolean changeBase;

	boolean insertBaseNow;

	String baseUrl;

	boolean inXmlDef; // <?xml

	boolean inDoctype; // <!DOCTYPE

	boolean inSingle; // alt='

	boolean inDouble; // alt="

	boolean inTag; // <a

	boolean inEndTag; // </a

	boolean inComment; // <!--

	boolean inScript; // <script

	boolean inScriptComment;

	boolean inNoscript; // <noscript //TODO more improvement

	boolean inStyle; // <style

	boolean inStyleComment;

	boolean inTitle; // <title

	boolean inHeader; // <head

	StringBuffer resultSB;

	StringBuffer toFindHtmlSB;

	StringBuffer scriptSB;

	StringBuffer styleSB;

	String currentTargetString;

	private Html2ViewMapMaker() {
	}

	/**
	 * Create mapping information between line number and ACTF_ID. The resulting
	 * HTML file that includes ACTF_ID will be stored as a file.
	 * 
	 * @param filename       file path of target HTML
	 * @param resultFileName file name of resulting HTML file
	 * @param targetDir      path for target directory to save result file
	 * @return mapping information as vector of {@link Html2ViewMapData}
	 */
	public static Vector<Html2ViewMapData> makeMap(String filename, String resultFileName, String targetDir) {
		Html2ViewMapMaker maker = new Html2ViewMapMaker();
		maker.changeBase = false;
		return (maker.makeMapLocal(filename, resultFileName, targetDir, true));
	}

	private void init() {
		inXmlDef = false;
		inDoctype = false;
		inSingle = false;
		inDouble = false;
		inTag = false;
		inEndTag = false;
		inComment = false;
		inScript = false;
		inScriptComment = false;
		inStyle = false;
		inStyleComment = false;

		insertBaseNow = false;

		id = 0;
		line = 0;
		column = 0;
		startLine = 0;
		startColumn = 0;

		html2viewV = new Vector<Html2ViewMapData>();
		resultSB = new StringBuffer();
	}

	// TODO throw syntactic error
	private Vector<Html2ViewMapData> makeMapLocal(String filename, String resultFileName, String tmpDir,
			boolean useTmpDir) {

		PrintWriter htmlWriter = null;
		FileInputStream fis = null;

		String _tmpDir = "";

		if (useTmpDir) {
			// _tmpDir = ADesignerPlugin.getTmpDirectory();
			_tmpDir = tmpDir;
		}

		init();

		String tmpS;

		BufferedReader br = null;

		String encoding = "MS932";

		// System.out.println(tmpDir + filename);
		File target = new File(_tmpDir + filename);
		JapaneseEncodingDetector JED;
		if (target.isFile() && target.canRead()) {
			try {
				fis = new FileInputStream(target);
				JED = new JapaneseEncodingDetector(fis);
				encoding = JED.detect();
			} catch (Exception e2) {
				e2.printStackTrace();
				return (html2viewV);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			return (html2viewV);
		}

		try {
			FileOutputStream fos = new FileOutputStream(new File(_tmpDir + resultFileName));
			BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(fos, encoding));
			htmlWriter = new PrintWriter(bos);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return new Vector<Html2ViewMapData>();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return new Vector<Html2ViewMapData>();
		}

		try {
			// fis = new FileInputStream(tmpDir + filename);
			// InputStreamReader isr = new InputStreamReader(fis, encoding);
			InputStreamReader isr = new InputStreamReader(JED.getInputStream(), encoding);
			br = new BufferedReader(isr);
			while ((tmpS = br.readLine()) != null) {

				// while (!(inScript || inComment) && tmpS.length() > 512) {
				// int index = tmpS.indexOf("<");
				// int index2 = tmpS.indexOf(">");
				//
				// String tmpS2;
				// if (index2 > index) {
				// tmpS2 = tmpS.substring(0, index2 + 1);
				// doFilter(tmpS2);
				// tmpS = tmpS.substring(index2 + 1);
				// } else if (index > 0) {
				// tmpS2 = tmpS.substring(0, index);
				// doFilter(tmpS2);
				// tmpS = tmpS.substring(index);
				// } else {
				// break;
				// }
				//
				// }
				// while(doFilter(tmpS));
				doSourceLine(tmpS);
				// System.out.println(tmpS);
				line++;
				column = 0;
			}

		} catch (Exception e) {
			// e.printStackTrace();

			try {
				fis.close();
				init();
				fis = new FileInputStream(_tmpDir + filename);
				InputStreamReader isr = new InputStreamReader(fis);
				br = new BufferedReader(isr);
				while ((tmpS = br.readLine()) != null) {
					doSourceLine(tmpS);
					line++;
					column = 0;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				htmlWriter.close();
				return (new Vector<Html2ViewMapData>());
			}
		} finally {
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		htmlWriter.print(resultSB.toString());
		htmlWriter.flush();
		htmlWriter.close();
		return (html2viewV);
	}

	private void doSourceLine(String target) {
		currentTargetString = target;
		boolean flag = true;
		while (flag) {
			flag = doFilter(currentTargetString);
			// System.out.println(currentTargetString);
		}
	}

	private boolean doFilter(String targetS) {
		if (targetS == null) {
			return false;
		} else if (targetS.equals("")) {
			resultSB.append(FileUtils.LINE_SEP);
			return false;
		} else {
			if (inSingle) {
				doValueSingle(targetS);
			} else if (inDouble) {
				doValueDouble(targetS);
			} else if (inTag) {
				doTag(targetS);
			} else if (inEndTag) {
				doEndTag(targetS);
			} else if (inComment) {
				doComment(targetS);
			} else if (inDoctype) {
				doDoctype(targetS);
			} else if (inScript) {
				if (inScriptComment) {
					doScriptComment(targetS);
				} else {
					doScript(targetS);
				}
			} else if (inStyle) {
				if (inStyleComment) {
					doStyleComment(targetS);
				} else {
					doStyle(targetS);
				}
			} else if (inXmlDef) {
				doXmlDef(targetS);
			} else {
				doNormal(targetS);
			}
		}
		return true;
	}

	private void doValueSingle(String targetS) {
		doValue(targetS, "\'");
	}

	private void doValueDouble(String targetS) {
		doValue(targetS, "\"");
	}

	private void doValue(String targetS, String quatation) {
		int endIndex = targetS.indexOf(quatation);
		// System.out.println("target: "+targetS);

		// not permitted
		int startIndex = targetS.indexOf("<");
		if (startIndex > -1 && (endIndex < 0 || endIndex > startIndex)) {

			// " < or " < "

			int threshold = endIndex;
			if (endIndex < 0) {
				threshold = Integer.MAX_VALUE;
			}

			// <+alphabet? -> might be tag
			String tmpS = "";
			try {
				tmpS = targetS.substring(startIndex + 1);
			} catch (Exception e) {
			}
			int tmpLine = line + 1;
			while (startIndex > -1 && startIndex < threshold && !tmpS.matches("\\p{Alpha}.*")) {
				startIndex = targetS.indexOf("<", startIndex + 1);
				if (startIndex > -1) {
					tmpS = "";
					try {
						tmpS = targetS.substring(startIndex + 1);
					} catch (Exception e) {
					}
				}
			}

			if (startIndex > -1 && startIndex < threshold) {
				System.out.print("HTMLVMM: start tag in attribute value : line: " + tmpLine + " : " + targetS + " : ");

				int tagEndIndex = targetS.indexOf(">");
				if (tagEndIndex > -1 && tagEndIndex < startIndex) {
					targetS = targetS.substring(0, tagEndIndex) + quatation + targetS.substring(tagEndIndex);
					endIndex = targetS.indexOf(quatation);
					System.out.println("with end tag");
				} else {
					targetS = targetS.substring(0, startIndex) + quatation + ">" + targetS.substring(startIndex);
					endIndex = targetS.indexOf(quatation);
					System.out.println("without end tag");
				}

			}
		}

		if (endIndex > -1) {
			inSingle = false;
			inDouble = false;

			if (changeBase) {
				toFindHtmlSB.append(targetS.substring(0, endIndex));
			}

			doNext(targetS, endIndex);
		} else {
			if (changeBase) {
				toFindHtmlSB.append(targetS);
			}
			doNext(targetS, endIndex);
		}
	}

	private boolean checkIndex(int endIndex, int quatationIndex) {
		if (quatationIndex > -1) {
			if (endIndex > -1 && (endIndex < quatationIndex)) {
				return (false);
			}
			return (true);
		}
		return (false);
	}

	private boolean isUnexpectedStartTag(int singleIndex, int doubleIndex, int endIndex, int startTagIndex) {
		if (startTagIndex > -1) {
			return ((endIndex < 0 || endIndex > startTagIndex) && (singleIndex < 0 || singleIndex > startTagIndex)
					&& (doubleIndex < 0 || doubleIndex > startTagIndex));
		}
		return (false);
	}

	private int getAttributeIndex(String targetS, boolean isDouble, int endIndex) {
		String quatation = "\'";
		if (isDouble) {
			quatation = "\"";
		}

		int threshold = endIndex;
		if (endIndex < 0) {
			threshold = Integer.MAX_VALUE;
		}

		int index = targetS.indexOf(quatation);
		while (index > -1 && index < threshold) {
			String tmpS = targetS.substring(0, index);
			if (tmpS.matches(".*=\\p{Space}*")) {
				return (index);
			}
			// System.out.println("not attribute: "+tmpS);
			index = targetS.indexOf(quatation, index + 1);
		}
		return index;
	}

	private void doTag(String targetS) {
		int endIndex = targetS.indexOf(">");

		// int singleIndex = targetS.indexOf("\'");
		// int doubleIndex = targetS.indexOf("\"");

		int singleIndex = getAttributeIndex(targetS, false, endIndex);
		int doubleIndex = getAttributeIndex(targetS, true, endIndex);

		boolean singleC = checkIndex(endIndex, singleIndex);
		boolean doubleC = checkIndex(endIndex, doubleIndex);

		if (singleC && doubleC) {
			if (doubleIndex < singleIndex) {
				singleC = false;
			} else {
				doubleC = false;
			}
		}

		// unexpected next tag start
		int startIndex = targetS.indexOf("<");

		if (isUnexpectedStartTag(singleIndex, doubleIndex, endIndex, startIndex)) {
			int tmpLine = line + 1;
			System.out.println("HTMLVMM: unexpected start tag: line:" + tmpLine + " : " + targetS);

			targetS = targetS.substring(0, startIndex) + ">" + targetS.substring(startIndex);

			// System.out.println(targetS);
			singleC = false;
			doubleC = false;

			endIndex = targetS.indexOf(">");

		}

		if (singleC) {
			// single
			inSingle = true;
			if (changeBase) {
				toFindHtmlSB.append(targetS.substring(0, singleIndex));
			}
			doNext(targetS, singleIndex);
		} else if (doubleC) {
			// double
			inDouble = true;
			if (changeBase) {
				toFindHtmlSB.append(targetS.substring(0, doubleIndex));
			}
			doNext(targetS, doubleIndex);
		} else {
			boolean endWithSlash = false;

			if (endIndex > -1) {
				inTag = false;

				int tmpIndex = targetS.indexOf("/>");
				// System.out.println(tmpIndex+" "+endIndex);
				if (tmpIndex > -1 && (tmpIndex == endIndex - 1)) {
					endWithSlash = true;
					if (inStyle) {
						inStyle = false;
					}
				}

				if (changeBase) {
					String tmpS = toFindHtmlSB.toString() + targetS.substring(0, endIndex);
					tmpS = tmpS.toLowerCase();

					if (tmpS.startsWith("html")) {
						// System.out.println("find html");
						changeBase = false;
						insertBaseNow = true;
					}

				}

				Html2ViewMapData h2vmd = new Html2ViewMapData(new int[] { startLine, startColumn },
						new int[] { line, column + endIndex + 1 });
				// System.out.println(h2vmd.toString());
				html2viewV.add(h2vmd);
				doNext(targetS, endIndex, true, endWithSlash);
			} else {
				if (changeBase) {
					toFindHtmlSB.append(targetS);
				}
				doNext(targetS, endIndex);
			}

		}

	}

	private void doEndTag(String targetS) {
		int endIndex = targetS.indexOf(">");
		if (endIndex > -1) {
			inEndTag = false;
		}
		doNext(targetS, endIndex);
	}

	private void doComment(String targetS) {
		int endIndex = targetS.indexOf("-->");
		if (endIndex > -1) {
			inComment = false;
		}
		doNext(targetS, endIndex);
	}

	private void doScript(String targetS) {
		int commentIndex = targetS.toLowerCase().indexOf("<!--");
		int endIndex = targetS.toLowerCase().indexOf("</script>");

		inScriptComment = checkIndex(endIndex, commentIndex);

		if (inScriptComment) {
			scriptSB.append(targetS.substring(0, commentIndex + 1));
			doNext(targetS, commentIndex);
			return;
		} else if (endIndex > -1) {
			scriptSB.append(targetS.substring(0, endIndex + 8));
			String tmpScript = scriptSB.toString();
			String tmpScript2 = tmpScript.toLowerCase();

			if (changeBase && tmpScript2.indexOf(".createstylesheet") > -1) {
				resultSB.insert(resultSB.indexOf("<HEAD><BASE") + 6, tmpScript + ">");
				resultSB.append("<!-- acc_memo script move to top--");
			} else {
				resultSB.append(tmpScript);
			}
			targetS = targetS.substring(endIndex + 8);
			endIndex = 0;
			inScript = false;
		} else {
			scriptSB.append(targetS + FileUtils.LINE_SEP);
		}
		doNext(targetS, endIndex);
	}

	private void doScriptComment(String targetS) {
		int endIndex = targetS.indexOf("-->");
		if (endIndex > -1) {
			scriptSB.append(targetS.substring(0, endIndex + 1));
			inScriptComment = false;
		} else {
			scriptSB.append(targetS + FileUtils.LINE_SEP);
		}
		doNext(targetS, endIndex);
	}

	private void doStyle(String targetS) {
		// ToDo need to handle </style> used in content value, etc.

		int commentIndex = targetS.toLowerCase().indexOf("/*");
		int endIndex = targetS.toLowerCase().indexOf("</style>");

		inStyleComment = checkIndex(endIndex, commentIndex);

		if (inStyleComment) {
			styleSB.append(targetS.substring(0, commentIndex + 1));
			doNext(targetS, commentIndex);
			return;
		} else if (endIndex > -1) {
			styleSB.append(targetS.substring(0, endIndex + 7));
			resultSB.append(styleSB);
			targetS = targetS.substring(endIndex + 7);
			endIndex = 0;
			inStyle = false;
		} else {
			styleSB.append(targetS + FileUtils.LINE_SEP);
		}
		doNext(targetS, endIndex);
	}

	private void doStyleComment(String targetS) {
		int endIndex = targetS.indexOf("*/");
		if (endIndex > -1) {
			styleSB.append(targetS.substring(0, endIndex + 1));
			inStyleComment = false;
		} else {
			styleSB.append(targetS + FileUtils.LINE_SEP);
		}
		doNext(targetS, endIndex);
	}

	private void doDoctype(String targetS) {
		int endIndex = targetS.indexOf(">");
		if (endIndex > -1) {
			inDoctype = false;
		}
		// System.out.println(targetS);
		doNext(targetS, endIndex);
	}

	private void doXmlDef(String targetS) {
		int endIndex = targetS.indexOf(">");
		if (endIndex > -1) {
			inXmlDef = false;
		}
		// System.out.println(targetS);
		doNext(targetS, endIndex);
	}

	private void doNormal(String targetS) {
		int startIndex = targetS.indexOf("<");
		if (startIndex > -1) {
			startLine = line;
			startColumn = column + startIndex;

			// TODO Newline, NOSCRIPT
			String tmpS = targetS.toLowerCase();
			int endTagIndex = tmpS.indexOf("</");
			int tmpIndex = tmpS.indexOf("<!--");
			int tmpIndex2 = tmpS.indexOf("<!doctype");
			int tmpIndex3 = tmpS.indexOf("<script");
			int tmpIndex4 = tmpS.indexOf("<?xml");
			int tmpIndex5 = tmpS.indexOf("<style");
			// System.out.println(tmpS+" : "+endTagIndex+" "+tmpIndex+"
			// "+tmpIndex2+" "+tmpIndex3);
			if (startIndex == endTagIndex) {
				// System.out.println("start endTag: "+line+" "+startColumn);
				inEndTag = true;
			} else if (startIndex == tmpIndex) {
				// System.out.println("start comment: "+line+" "+startColumn);
				inComment = true;
			} else if (startIndex == tmpIndex2) {
				// System.out.println("start doctype: "+line+" "+startColumn);
				inDoctype = true;
			} else if (startIndex == tmpIndex3) {
				// System.out.println("start script: "+line+" "+startColumn);
				inScript = true;
				scriptSB = new StringBuffer("<");
				resultSB.append(targetS.substring(0, startIndex));
			} else if (startIndex == tmpIndex4) {
				inXmlDef = true;
			} else if (startIndex == tmpIndex5) {
				// System.out.println("start style: " + line + " " + startColumn);
				inTag = true;
				inStyle = true;
				styleSB = new StringBuffer();
			} else {
				// System.out.println("start tag: "+line+" "+startColumn);
				if (changeBase) {
					toFindHtmlSB = new StringBuffer();
				}
				inTag = true;
			}
		}
		doNext(targetS, startIndex);
	}

	private void doNext(String targetS, int _index) {
		doNext(targetS, _index, false, false);
	}

	// private void doNext(String targetS, int _index, boolean insertId) {
	// doNext(targetS, _index, insertId, false);
	// }

	private void doNext(String targetS, int _index, boolean insertId, boolean endWithSlash) {
		if (_index > -1) {
			int index = _index + 1;
			column = column + index;

			if (insertId) {
				if (endWithSlash) {
					resultSB.append(
							targetS.substring(0, _index - 1) + " " + Html2ViewMapData.ACTF_ID + "=\'" + id + "\'/>");
				} else {
					resultSB.append(targetS.substring(0, _index) + " " + Html2ViewMapData.ACTF_ID + "=\'" + id + "\'>");
				}
				id++;
			} else {
				if (!inScript && (!inStyle || inTag)) {
					resultSB.append(targetS.substring(0, index));
				}
			}

			if (insertBaseNow) {
				resultSB.append("<HEAD><BASE href=\"" + baseUrl + "\"></HEAD>");
				insertBaseNow = false;
			}

			// doFilter(targetS.substring(index));
			currentTargetString = targetS.substring(index);
		} else {
			if (!inScript && (!inStyle || inTag)) {
				resultSB.append(targetS + FileUtils.LINE_SEP);
			}
			currentTargetString = null;
		}

	}

}
