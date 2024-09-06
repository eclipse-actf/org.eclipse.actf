/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.ui.internal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.WaitForBrowserReadyHandler;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.DialogSave;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventHandler;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventListener;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.blind.internal.BlindVisualizerExtension;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.eval.PageEvaluation;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IHighlightElementListener;
import org.eclipse.actf.visualization.engines.blind.html.util.VisualizeReportUtil;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ReportUtil;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.w3c.dom.Document;

public class PartControlBlind implements IHighlightElementListener {

	private static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$

	public final static String BLIND_REPORT_FILE = "report.htm"; //$NON-NLS-1$

	private IBlindVisualizer[] blindVizualizers = BlindVisualizerExtension.getVisualizers();

	private BlindVisualizationBrowser _blindBrowser;

	private boolean _canSave = false;

	private PageData _pageData;

	private String maxReachingTimeS = ""; //$NON-NLS-1$

	private PageEvaluation _pageEval;

	private Document resultDoc;

	private Shell _shell;

	private String targetUrl;

	private Mediator mediator = Mediator.getInstance();

	private IVisualizationView vizView;

	private IEvaluationResult checkResult = new EvaluationResultBlind();

	public PartControlBlind(IVisualizationView vizView, Composite parent) {
		this.vizView = vizView;
		this._shell = parent.getShell();

		// new BlindToolBar(parent, SWT.NONE, this);

		this._blindBrowser = new BlindVisualizationBrowser(parent);
		this._blindBrowser.setBrowserSilent();
	}

	public void doSave(boolean withReport) {
		String saveFile = DialogSave.open(_shell, DialogSave.CSV, targetUrl, ".csv"); //$NON-NLS-1$

		if (saveFile != null) {

			// final IViewPart curView =
			// PlatformUIUtil.showView(IVisualizationView.ID_BLINDVIEW);
			// TODO
			IEvaluationResult result = (IEvaluationResult) Mediator.getInstance().getReport(vizView);
			if (result != null) {
				try {
					FileOutputStream fos = new FileOutputStream(saveFile);
					OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(fos), "UTF-8");

					// write BOM marker
					fos.write(0xef);
					fos.write(0xbb);
					fos.write(0xbf);

					PrintWriter reportPW = new PrintWriter(osw);
					ReportUtil reportUtil = new ReportUtil();
					reportUtil.setPrintWriter(reportPW);
					reportUtil.writeFirstLine();
					result.accept(reportUtil);
					reportPW.flush();
					reportPW.close();

				} catch (Exception e) {
				}
			}

			if (withReport) {
				if (null == this.resultDoc || null == saveFile) {
					return;
				}

				if (saveFile.toLowerCase().endsWith(".csv")) {
					saveFile = saveFile.substring(0, saveFile.length() - 3);
				}
				saveFile = saveFile.concat(".html");

				String imageBriefDir = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.lastIndexOf(".")) //$NON-NLS-1$ //$NON-NLS-2$
						+ "/"; //$NON-NLS-1$
				// 2007.09.25 remove space character to include JavaScript files
				imageBriefDir = imageBriefDir.replace(' ', '_');
				saveReportFile(saveFile, imageBriefDir, true);
			}
		}
	}

	public int doVisualize() {
		return doVisualize(true);
	}

	private HashMap<String, WaitExecSyncEventListener> eventhandlerHolder = new HashMap<String, WaitExecSyncEventListener>();
	private static final String LISTENER_KEY = "browser"; //$NON-NLS-1$

	public int doVisualize(boolean isShowResult) {

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		int ret = IBlindVisualizer.ERROR;
		if (modelService == null) {
			IEditorPart editor = ModelServiceUtils.reopenInACTFBrowser();
			if (editor instanceof IModelServiceHolder) {
				modelService = ((IModelServiceHolder) editor).getModelService();
				WaitExecSyncEventHandler handler = new WaitForBrowserReadyHandler((IWebBrowserACTF) modelService, 30,
						false, new Runnable() {
							public void run() {
								eventhandlerHolder.remove(LISTENER_KEY);
								doVisualize();
								PlatformUIUtil.showView(IVisualizationView.ID_BLINDVIEW);
							}
						});
				eventhandlerHolder.put(LISTENER_KEY, new WaitExecSyncEventListener(handler));
				// TODO
				return ret = IBlindVisualizer.OK;
			} else {
				return ret;
			}
		}

		String resultFilePath = ""; //$NON-NLS-1$

		targetUrl = modelService.getURL();
		maxReachingTimeS = ""; //$NON-NLS-1$

		checkResult = new EvaluationResultBlind();
		mediator.setReport(vizView, checkResult);// clear result
		_blindBrowser.navigate(ABOUT_BLANK);

		_canSave = false;

		vizView.setStatusMessage(Messages.BlindView_Now_preparing);

		for (IBlindVisualizer bvh : blindVizualizers) {
			if (bvh.setModelService(modelService)) {
				bvh.setVisualizationView(vizView);

				ret = bvh.visualize();

				_pageData = bvh.getPageData();
				checkResult = bvh.getEvaluationResult();
				resultDoc = bvh.getResultDocument();
				maxReachingTimeS = bvh.getMaxReachingTime();

				resultFilePath = bvh.getResultFile().getAbsolutePath();

				if (ret > IBlindVisualizer.ERROR) { // OK, Frame
					vizView.setStatusMessage(Messages.BlindView_Now_rendering);
					CreateReport cr = new CreateReport(checkResult,
							new File(BlindVizResourceUtil.getTempDirectory(), BLIND_REPORT_FILE));
					if (isShowResult) {
						_blindBrowser.navigate(resultFilePath);
						_shell.getDisplay().asyncExec(cr);
					} else {
						_blindBrowser.navigate(ABOUT_BLANK);
						_shell.getDisplay().syncExec(cr);
					}

					_canSave = true;
				} else {
					checkResult.setProblemList(new ArrayList<IProblemItem>());
					_blindBrowser.navigate(ABOUT_BLANK);
					CreateReport cr = new CreateReport(checkResult);
					_shell.getDisplay().syncExec(cr);
				}
				return ret;
			}
		}

		System.out.println("not supported: " + modelService.getID() + " " //$NON-NLS-1$ //$NON-NLS-2$
				+ modelService.getCurrentMIMEType());
		return IBlindVisualizer.ERROR;
	}

	public void saveReportFile(String sFileName, String imageBriefDir, boolean bAccessory) {
		if (_canSave) {
			vizView.setStatusMessage(Messages.BlindView_saving_file); //

			// TODO encoding
			SaveReportBlind.saveReport((Document) resultDoc.cloneNode(true), mediator.getReport(vizView), sFileName,
					imageBriefDir, maxReachingTimeS, _pageEval, bAccessory);

			vizView.setStatusMessage(Messages.BlindView_end_saving_file); //
		}
	}

	private class CreateReport extends Thread {
		IEvaluationResult _checkResult;

		File targetFile;

		CreateReport(IEvaluationResult checkResult, File filePath) {
			this._checkResult = checkResult;
			this.targetFile = filePath;
		}

		// for navigation error
		CreateReport(IEvaluationResult checkResult) {
			this._checkResult = checkResult;
			this.targetFile = null;
		}

		public void run() {
			_pageEval = new PageEvaluation(_checkResult.getProblemList(), _pageData);
			if (targetFile == null) {
				// TODO clear pageData/score
				checkResult.setSummaryReportUrl(ABOUT_BLANK);
				checkResult.setSummaryReportText(""); //$NON-NLS-1$
			} else {
				VisualizeReportUtil.createReport(this.targetFile, _pageEval);
				_checkResult.setSummaryReportUrl(targetFile.getAbsolutePath());
				_checkResult.setSummaryReportText(_pageEval.getSummary());
				_checkResult.setLineStyleListener(PageEvaluation.getHighLightStringListener());
			}
			if (_checkResult instanceof EvaluationResultBlind) {
				((EvaluationResultBlind) _checkResult).setPageEvaluation(_pageEval);
			}

			_shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					// TODO through mediator
					mediator.setReport(vizView, _checkResult);
					vizView.setStatusMessage(Messages.BlindView_Done);
				}
			});
		}
	}

	private void execScript(String script) {
		_blindBrowser.execScript(script);
	}

	public void clearHighlight() {
		_blindBrowser.clearHighlight();
	}

	protected IEvaluationResult getCheckResult() {
		return checkResult;
	}

	public void highlight(List<HighlightTargetId> targetIdList) {
		if (null != targetIdList) {
			switch (targetIdList.size()) {
			case 0:
				break;
			case 1:
				execScript("setHighlight(" + targetIdList.get(0).getStartId() //$NON-NLS-1$
						+ "," + targetIdList.get(0).getEndId() + ");"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			default:
				Iterator<HighlightTargetId> ite = targetIdList.iterator();
				StringBuffer strStart = new StringBuffer(512);
				StringBuffer strEnd = new StringBuffer(512);
				HighlightTargetId tmpId = ite.next();
				strStart.append(tmpId.getStartId());
				strEnd.append(tmpId.getEndId());
				while (ite.hasNext()) {
					tmpId = ite.next();
					strStart.append("," + tmpId.getStartId()); //$NON-NLS-1$
					strEnd.append("," + tmpId.getEndId()); //$NON-NLS-1$
				}

				String highlightScript = "setHighlight2(new Array(" //$NON-NLS-1$
						+ strStart.toString() + "), new Array(" //$NON-NLS-1$
						+ strEnd.toString() + "));"; //$NON-NLS-1$
				execScript(highlightScript);
			}

		}
	}

	public boolean isBrowserModeSupported(IModelServiceHolder msh) {
		if (msh != null) {
			for (IBlindVisualizer bvh : blindVizualizers) {
				if (bvh.setModelService(msh.getModelService())) {
					// TODO add method into interface
					if (bvh.getClass().getName().startsWith("org.eclipse.actf.visualization.blind.")) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
