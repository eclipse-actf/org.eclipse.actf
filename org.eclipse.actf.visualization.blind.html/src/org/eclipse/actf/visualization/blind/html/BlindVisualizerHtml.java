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
package org.eclipse.actf.visualization.blind.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.DocumentTypeUtil;
import org.eclipse.actf.model.dom.html.HTMLParserFactory;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.dom.DomPrintUtil;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.blind.BlindVisualizerBase;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.blind.html.util.HtmlErrorLogListener;
import org.eclipse.actf.visualization.eval.CheckTargetFactory;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IHtmlCheckTarget;
import org.eclipse.actf.visualization.eval.IHtmlChecker;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapMaker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BlindVisualizerHtml extends BlindVisualizerBase implements IBlindVisualizer {

	public static final int FRAME = 1;

	private static final String ORIG_HTML_FILE = "origSource.html"; //$NON-NLS-1$
	private static final String IE_HTML_FILE = "ieSource.html"; //$NON-NLS-1$
	private static final String MAPPED_HTML_FILE_PRE = "MappedHTML"; //$NON-NLS-1$
	private static final String HTML_SOURCE_FILE = "source.html"; //$NON-NLS-1$

	private IWebBrowserACTF webBrowser;

	@Override
	public boolean setModelService(IModelService targetModel) {
		webBrowser = null;
		if (super.setModelService(targetModel)) {
			webBrowser = (IWebBrowserACTF) targetModel;
			return true;
		}
		return false;
	}

	public boolean isTarget(IModelService modelService) {
		if (null != modelService && modelService instanceof IWebBrowserACTF) {
			return true;
		}
		return false;
	}

	private final boolean PERFORMANCE_DEBUG = false;

	public int visualize() {
		GuidelineHolder.getInstance().setTargetMimeType("text/html"); //$NON-NLS-1$

		int frameId = 0;
		checkResult = new EvaluationResultBlind();
		File srcFile;
		File liveFile;
		File targetFile;

		boolean isHTML5 = false;

		try {
			if (PERFORMANCE_DEBUG)
				System.out.println("vizualize start\t" + (new Date()).getTime()); //$NON-NLS-1$
			srcFile = webBrowser.saveOriginalDocument(tmpDirS + ORIG_HTML_FILE);
			liveFile = webBrowser.saveDocumentAsHTMLFile(tmpDirS + IE_HTML_FILE);
			// for srcViewer
			webBrowser.saveOriginalDocument(tmpDirS + HTML_SOURCE_FILE);

			if (PERFORMANCE_DEBUG)
				System.out.println("save documents\t" + (new Date()).getTime()); //$NON-NLS-1$

			Vector<Html2ViewMapData> html2ViewMapV = new Vector<Html2ViewMapData>();
			IHTMLParser htmlParser = HTMLParserFactory.createHTMLParser();
			HtmlErrorLogListener errorLogListener = new HtmlErrorLogListener();
			htmlParser.addErrorLogListener(errorLogListener);
			String targetFileName = tmpDirS + MAPPED_HTML_FILE_PRE + frameId + ".html"; //$NON-NLS-1$

			boolean isIEhtml = false;
			if (EvaluationUtil.isOriginalDOM()) {
				html2ViewMapV = Html2ViewMapMaker.makeMap(ORIG_HTML_FILE, MAPPED_HTML_FILE_PRE + frameId + ".html", //$NON-NLS-1$
						tmpDirS);
				// decode miss
				if (html2ViewMapV.size() == 0) {
					isIEhtml = true;
				}
				if (PERFORMANCE_DEBUG)
					System.out.println("map finish\t" + (new Date()).getTime()); //$NON-NLS-1$
			} else {
				isIEhtml = true;
			}

			// for line<>id mapping
			// HtmlLine2Id htmlLine2Id = new HtmlLine2Id(html2ViewMapV);

			Document document;
			Document ieDom;
			Document originalDocument;
			if (isIEhtml) {
				ieDom = webBrowser.getLiveDocument();

				// TODO replace with DomByCom (need clone/write support)
				IHTMLParser tmpHtmlParser = HTMLParserFactory.createHTMLParser();
				tmpHtmlParser.parse(new FileInputStream(tmpDirS + IE_HTML_FILE));
				document = tmpHtmlParser.getDocument();

				tmpHtmlParser.parse(new FileInputStream(tmpDirS + ORIG_HTML_FILE));
				originalDocument = tmpHtmlParser.getDocument();

				targetFile = liveFile;

			} else {
				htmlParser.parse(new FileInputStream(targetFileName));
				document = htmlParser.getDocument();
				originalDocument = document;
				ieDom = webBrowser.getLiveDocument();

				targetFile = srcFile;
			}

			isHTML5 = DocumentTypeUtil.isOriginalHTML5(originalDocument.getDoctype());

			if (PERFORMANCE_DEBUG)
				System.out.println("parse documents\t" + (new Date()).getTime()); //$NON-NLS-1$

			// System.out.println(document+" "+ _originalDom+" "+ ieDom);

			boolean hasFrame = false;

			if (document == null) {
				return ERROR;
			} else if (hasFrameset(document, webBrowser) == true) {
				hasFrame = true;
			}

			setStatusMessage(Messages.BlindView_Now_processing); //

			pageData = new PageData();

			VisualizeEngine engine = new VisualizeEngine();
			engine.setBaseUrl(""); //$NON-NLS-1$
			// TODO "\"->"/" windows
			// engine.setBaseUrl("file:///"+RESULT_DIR);

			engine.setTargetUrl(targetUrl);
			engine.setDocument(document);
			engine.setHtml2viewMapV(html2ViewMapV);
			engine.setHTML5(isHTML5);

			// TODO invisibleElements
			// HashSet invisibleIdSet = new HashSet();
			// if (webBrowser != null) {
			// String[] invisibleIds = webBrowser.getInvisibleEleId();
			// for (int i = 0; i < invisibleIds.length; i++) {
			// invisibleIdSet.add(invisibleIds[i]);
			// }
			// }
			// engine.setInvisibleIdSet(invisibleIdSet);

			engine.setInvisibleIdSet(new HashSet<String>());
			if (PERFORMANCE_DEBUG)
				System.out.println("setInvisibleIdSet\t" + (new Date()).getTime()); //$NON-NLS-1$

			engine.setPageData(pageData);
			if (PERFORMANCE_DEBUG)
				System.out.println("setPageData\t" + (new Date()).getTime()); //$NON-NLS-1$

			engine.visualize();
			if (PERFORMANCE_DEBUG)
				System.out.println("do vizualize\t" + (new Date()).getTime()); //$NON-NLS-1$

			maxReachingTime = engine.getMaxTime();
			setInfoMessage(getMaxReachingTime());

			resultDocument = engine.getResult();
			checkResult.setProblemList(engine.getProbelems());
			checkResult.setTargetUrl(targetUrl);

			if (variantFile != null) {
				variantFile.delete();
			}
			variantFile = engine.getVariantFile();
			checkResult.addAssociateFile(variantFile);

			IVisualizeMapData mapData = engine.getVisualizeMapData();

			// TODO
			checkResult.setSourceFile(new File(tmpDirS + HTML_SOURCE_FILE));

			boolean isDBCS = false;
			if (ParamBlind.getInstance().iLanguage == ParamBlind.JP) {
				isDBCS = true;
			}

			HtmlEvalUtil edu = new HtmlEvalUtil(document, resultDocument, targetUrl, mapData.getOrig2idMap(),
					originalDocument, ieDom, pageData, isDBCS, isIEhtml);
			edu.setLiveFile(liveFile);
			edu.setSrcFile(srcFile);
			edu.setTargetFile(targetFile);
			edu.setStyleSheets(webBrowser.getStyleSheets());

			if (PERFORMANCE_DEBUG)
				System.out.println("HtmlEvalUtil\t" + (new Date()).getTime()); //$NON-NLS-1$

			ArrayList<IProblemItem> tmpResults = new ArrayList<IProblemItem>(1024);

			// TODO re-impl BrowserAndStyleInfo
			//
			// BrowserAndStyleInfo data =
			// webBrowser.getBrowserAndStyleInfo();
			IHtmlCheckTarget checkTarget = CheckTargetFactory.createHtmlCheckTarget(document, webBrowser.getURL(), null,
					edu);

			for (int i = 0; i < checkers.length; i++) {
				if (checkers[i] instanceof IHtmlChecker) {
					tmpResults.addAll(((IHtmlChecker) checkers[i]).checkHtml(checkTarget));
				} else if (checkers[i].isTargetFormat(webBrowser.getCurrentMIMEType()) && checkers[i].isEnabled()) {
					tmpResults.addAll(checkers[i].check(checkTarget));
				}
			}

			if (PERFORMANCE_DEBUG)
				System.out.println("checked\t" + (new Date()).getTime()); //$NON-NLS-1$

			boolean hasBOM = false;
			Vector<IProblemItem> tmpV = errorLogListener.getHtmlProblemVector();
			if (isHTML5) {
				int bomError = -1;
				for (int i = 0; i < tmpV.size(); i++) {
					IProblemItem item = tmpV.get(i);
					if (item.getId().equals("C_1000.8")) { //$NON-NLS-1$
						bomError = i;
					}
				}
				if (bomError > -1) {
					tmpV.remove(bomError);
					hasBOM = true;
				}
			}

			int charsetProblem = -1;
			// TODO support blind biz -> visitor
			for (int i = 0; i < tmpResults.size(); i++) {
				IProblemItem tmpItem = tmpResults.get(i);
				HighlightTargetNodeInfo nodeInfo = tmpItem.getHighlightTargetNodeInfo();
				if (nodeInfo != null) {
					if (tmpItem.getHighlightTargetIds().length == 0) {
						tmpItem.setHighlightTargetIds(nodeInfo.getHighlightTargetIds(mapData.getOrig2idMap()));
					}
					if (EvaluationUtil.isOriginalDOM()) {
						tmpItem.setHighlightTargetSourceInfo(nodeInfo.getHighlightTargetSourceInfo(html2ViewMapV));
					}
				}
				if (hasBOM && tmpItem.getId().equals("C_88.1")) { //$NON-NLS-1$
					hasBOM = false; // do not check from here
					charsetProblem = i;
				}
			}
			if (charsetProblem > -1) {
				tmpResults.remove(charsetProblem);
			}
			checkResult.addProblemItems(tmpResults);

			checkResult.addProblemItems(tmpV);
			checkResult.accept(pageData);

			if (PERFORMANCE_DEBUG)
				System.out.println("process problems\t" + (new Date()).getTime()); //$NON-NLS-1$

			// TODO move (add Icons into result doc) here

			if (resultFile != null) {
				resultFile.delete();
			}
			resultFile = BlindVizResourceUtil.createTempFile(IVisualizationConst.PREFIX_RESULT,
					IVisualizationConst.SUFFIX_HTML);
			// File tmpFile = BlindVizEnginePlugin.createTempFile("tmp",
			// IVisualizationConst.SUFFIX_HTML);

			try {
				// HtmlParserUtil.saveHtmlDocumentAsUTF8(
				// (SHDocument) resultDocument, tmpFile, resultFile);
				DomPrintUtil dpu = new DomPrintUtil(resultDocument);
				dpu.setHTML5(isHTML5);
				dpu.writeToFile(resultFile);

			} catch (Exception e3) {
				DebugPrintUtil.devOrDebugPrintln("error: saveHtmlDocumentAsUTF8"); //$NON-NLS-1$
			}

			try {
				Element[] iframes = getElementsArray(ieDom, "iframe"); //$NON-NLS-1$
				HashSet<String> urlS = new HashSet<String>();
				if (iframes.length > 0) {
					for (Element e : iframes) {
						String src = e.getAttribute("src"); //$NON-NLS-1$
						if (src != null && src.startsWith("http")) { //$NON-NLS-1$
							urlS.add(src);
						} else {
							NodeList nl = e.getChildNodes();
							if (nl.getLength() > 0 && nl.item(0).getNodeName().equalsIgnoreCase("html")) {
								try {
									File iframeDump = BlindVizResourceUtil.createTempFile("IFRAME",
											IVisualizationConst.SUFFIX_HTML);
									DomPrintUtil dpu = new DomPrintUtil(nl.item(0));
									dpu.setHTML5(true);
									dpu.writeToFile(iframeDump);
									urlS.add(iframeDump.toURI().toURL().toString());
								} catch (Exception e4) {
								}
							}
						}
					}
					if (!urlS.isEmpty() && MessageDialog.openConfirm(PlatformUIUtil.getShell(),
							Messages.BlindVisualizerHtml_15, Messages.BlindVisualizerHtml_16)) {
						IEditorPart currentE = PlatformUIUtil.getActiveEditor();
						for (String s : urlS) {
							ModelServiceUtils.launchNew(s);
						}
						PlatformUIUtil.getActivePage().activate(currentE);
					}
				}
			} catch (Exception e2) {

			}

			if (PERFORMANCE_DEBUG)
				System.out.println("vizualize end\t" + (new Date()).getTime()); //$NON-NLS-1$
			if (hasFrame) {
				pageData.setHasFrame(true);
				return FRAME;
			} else if (webBrowser != null && !webBrowser.isUrlExists()) {
				// TODO
				pageData.setError(true);
				return ERROR;
			}
			return OK;

		} catch (

		Exception e)

		{
			setStatusMessage(Messages.Visualization_Error);
			e.printStackTrace();
			return ERROR;
		}

	}

	@SuppressWarnings("nls")
	private boolean hasFrameset(Document document, IWebBrowserACTF webBrowser) {

		NodeList framesetNl = document.getElementsByTagName("frameset"); //$NON-NLS-1$

		if (framesetNl.getLength() > 0) {

			NodeList frameList = document.getElementsByTagName("frame"); //$NON-NLS-1$

			String sFileName = BlindVizResourceUtil.getTempDirectory() + "frameList.html"; //$NON-NLS-1$

			String base = webBrowser.getURL();

			try {
				URL baseURL = new URL(base);

				NodeList baseNL = document.getElementsByTagName("base"); //$NON-NLS-1$
				if (baseNL.getLength() > 0) {
					Element baseE = (Element) baseNL.item(baseNL.getLength() - 1);
					String baseUrlS = baseE.getAttribute("href"); //$NON-NLS-1$
					if (baseUrlS.length() > 0) {
						URL tmpUrl = new URL(baseURL, baseUrlS);
						base = tmpUrl.toString();
					}
				}
			} catch (Exception e) {
			}

			PrintWriter fileOutput;

			try {
				fileOutput = new PrintWriter(new OutputStreamWriter(new FileOutputStream(sFileName), "UTF-8")); //$NON-NLS-1$
			} catch (IOException e) {
				// e.printStackTrace();
				// TODO
				return true;
			}

			fileOutput.write("<html>"); //$NON-NLS-1$
			// " lang=\""+lang+\">"); //use var
			fileOutput.write("<head>" + FileUtils.LINE_SEP); //$NON-NLS-1$
			fileOutput.write(
					"<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" >" + FileUtils.LINE_SEP); //$NON-NLS-1$
			fileOutput.write("<base href=\"" + base + "\"></head>" + FileUtils.LINE_SEP + "<body><P>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileOutput.write(Messages.BlindVisualizerHtml_1); // var
			fileOutput.write(String.valueOf(frameList.getLength()));
			fileOutput.write(Messages.BlindVisualizerHtml_2); // var
			fileOutput.write("<br>" + FileUtils.LINE_SEP); //$NON-NLS-1$
			fileOutput.write(Messages.BlindVisualizerHtml_0); // var
			fileOutput.write("</P>" + FileUtils.LINE_SEP + "<ol>" + FileUtils.LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$

			String strTitle, strName;
			for (int i = 0; i < frameList.getLength(); i++) {
				Element frameEl = (Element) frameList.item(i);
				strTitle = frameEl.getAttribute("title"); //$NON-NLS-1$
				strName = frameEl.getAttribute("name"); //$NON-NLS-1$
				if (strTitle.equals("")) //$NON-NLS-1$
					strTitle.equals("none"); //$NON-NLS-1$
				if (strName.equals("")) //$NON-NLS-1$
					strName.equals("none"); //$NON-NLS-1$
				fileOutput.write("<li><a href=\"" + frameEl.getAttribute("src") + "\">Title: \"" + strTitle //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ "\".<BR> Name: \"" + strName + "\".<BR> src: \"" + frameEl.getAttribute("src") + "\".</a>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						+ FileUtils.LINE_SEP);
			}
			fileOutput.write("</ol></body></html>"); //$NON-NLS-1$

			fileOutput.flush();
			fileOutput.close();

			webBrowser.navigate(sFileName);
			return true;

		} else {
			return false;
		}
	}

	private Element[] getElementsArray(Document target, String tagName) {
		NodeList tmpNL = target.getElementsByTagName(tagName);
		int length = tmpNL.getLength();
		Element[] result = new Element[length];
		for (int i = 0; i < length; i++) {
			result[i] = (Element) tmpNL.item(i);
		}
		return (result);
	}

}
