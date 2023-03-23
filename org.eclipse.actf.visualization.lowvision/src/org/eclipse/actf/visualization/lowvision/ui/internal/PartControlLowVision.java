/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	  Junji MAEDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.dom.dombyjs.IElementACTF;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.ModelServiceImageCreator;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.WaitForBrowserReadyHandler;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventHandler;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventListener;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.engines.lowvision.PageEvaluation;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.actf.visualization.lowvision.eval.CheckResultLowVision;
import org.eclipse.actf.visualization.lowvision.eval.SaveReportLowVision;
import org.eclipse.actf.visualization.lowvision.eval.SummaryEvaluationLV;
import org.eclipse.actf.visualization.lowvision.ui.views.LowVisionView;
import org.eclipse.actf.visualization.lowvision.util.LowVisionUtil;
import org.eclipse.actf.visualization.lowvision.util.ParamLowVision;
import org.eclipse.actf.visualization.lowvision.util.SimulateLowVision;
import org.eclipse.actf.visualization.ui.IPositionSize;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;

public class PartControlLowVision implements ISelectionListener, IVisualizationConst {

	private static final CheckResultLowVision dummyResult = new CheckResultLowVision();

	private String[] frameUrl;

	private IPageImage[] framePageImage;

	private ImagePositionInfo[][] imageInfoInHtmlArray;

	private ArrayList<Map<String, ICurrentStyles>> styleInfoArray;

	private Vector<ExtractCheckThread> checkThreads;

	private LowVisionSimulationView lowVisionView;

	private ParamLowVision paramLowVision;

	private Shell _shell;

	private String targetUrlS;

	private boolean[] isFinished;

	private SaveReportLowVision _saveReportLowVision;

	private boolean _isInSimulate;

	private boolean is1stSimulateDone = false;

	// 256M -> 7000000 400M->10000000
	private int dump_image_size = 7000000;

	private CheckResultLowVision checkResult = new CheckResultLowVision();

	private File reportFile;

	private File reportImageFile;

	private File visResultFile;

	private File dumpImgFile;

	private File sourceHtmlFile;

	private String dumpImageFile;

	private LowVisionView checker;

	private Mediator mediator = Mediator.getInstance();

	private IWebBrowserACTF webBrowser = null;

	private IModelService targetModelService;

	private Cursor waitCursor;

	private double scaleX = 1;
	private double scaleY = 1;

	private class ExtractCheckThread extends Thread {
		int frameId;
		String address;
		Document document;
		IStyleSheets styleSheets;

		PageEvaluation targetPage;

		private List<IProblemItem> lowvisionProblemList;

		ExtractCheckThread(int _frameId, String _address, Document _document, IStyleSheets _styleSheets) {
			frameId = _frameId;
			address = _address;
			document = _document;
			styleSheets = _styleSheets;
		}

		public void run() {
			try {
				targetPage = new PageEvaluation(framePageImage[frameId], document, styleSheets);
				targetPage.setInteriorImagePosition(imageInfoInHtmlArray[frameId]);
				targetPage.setCurrentStyles(styleInfoArray.get(frameId));

				_shell.getDisplay().syncExec(new Runnable() {
					public void run() {
						checker.setStatusMessage(Messages.LowVisionView_begin_to_check_problems__4);
					}
				});

				lowvisionProblemList = targetPage.check(paramLowVision.getLowVisionType(), address, frameId);

				// TODO frames
				try {
					removeTempFile(reportFile);
					reportFile = LowVisionVizPlugin.createTempFile(PREFIX_REPORT, SUFFIX_HTML);
					// TODO modelservice type
					if (webBrowser != null) {
						removeTempFile(reportImageFile);
						reportImageFile = LowVisionVizPlugin.createTempFile(PREFIX_REPORT, SUFFIX_BMP);
						targetPage.generateReport(reportFile.getParent(), reportFile.getName(),
								reportImageFile.getName(), lowvisionProblemList);
					} else {// current lv mode doesn't support ODF
						reportImageFile = null;
						targetPage.unsupportedModeReport(reportFile);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				targetPage = null;

				checkResult.addProblemItems(lowvisionProblemList);

				// ext checker here

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// TODO use event
	private class WaitExtractThread extends Thread {
		Thread simulateThread;

		WaitExtractThread(Thread _simulateThread) {
			simulateThread = _simulateThread;
		}

		public void run() {
			if (simulateThread == null && checkThreads == null)
				return;
			boolean flag = true;
			int count = 0;

			while (flag) {
				if (simulateThread == null || !simulateThread.isAlive()) {
					flag = false;
					simulateThread = null;
					for (int i = 0; i < checkThreads.size(); i++) {
						flag = flag || checkThreads.get(i).isAlive();
					}
				}
				if (count >= 200) {
					// //$NON-NLS-1$
					break;
				}
				count++;
				try {
					WaitExtractThread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			_shell.getDisplay().syncExec(new Runnable() {

				public void run() {

					if (webBrowser != null) {
						checkResult.setSummaryReportText(
								new SummaryEvaluationLV(checkResult.getProblemList()).getOverview());
					} else {
						checkResult.setSummaryReportText(SummaryEvaluationLV.notSupported());
					}
					checkResult.setLineStyleListener(SummaryEvaluationLV.getHighLightStringListener());

					checkResult.setSummaryReportUrl(reportFile.getAbsolutePath());
					mediator.setReport(checker, checkResult);

					checker.setStatusMessage(Messages.LowVisionView_simulation_of_current_page_is_over__8);
					_shell.setCursor(null);
					_isInSimulate = false;

					PlatformUIUtil.showView(IVisualizationView.ID_LOWVISIONVIEW);

					checkThreads = new Vector<ExtractCheckThread>();
				}
			});
		}
	}

	public PartControlLowVision(LowVisionView checker, Composite parent) {

		this.checker = checker;

		this._shell = parent.getShell();
		waitCursor = new Cursor(_shell.getDisplay(), SWT.CURSOR_WAIT);

		paramLowVision = ParamLowVision.getDefaultInstance();
		this._saveReportLowVision = new SaveReportLowVision(_shell);

		lowVisionView = new LowVisionSimulationView(parent);

		_isInSimulate = false;

		try {
			removeTempFile(dumpImgFile);
			dumpImgFile = LowVisionVizPlugin.createTempFile(PREFIX_SCREENSHOT, SUFFIX_BMP);
			dumpImageFile = dumpImgFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveReport() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (is1stSimulateDone && !isBInSimulate() && modelService != null) {
			this._saveReportLowVision.setScale(scaleX, scaleY);
			this._saveReportLowVision.doSave(modelService.getURL(), checkResult.getProblemList(), visResultFile,
					reportImageFile);
		}
	}

	public void saveReport(int processNo, int[] paramNo, String paramStr, String folder, String probName,
			Document probStatisticsDoc, String urlS, int depth) {

		_saveReportLowVision.doSave(processNo, paramNo, paramStr, folder, probStatisticsDoc, urlS,
				checkResult.getProblemList(), visResultFile, reportImageFile);
	}

	private void allocate(int frameSize) {
		framePageImage = new IPageImage[frameSize];
		imageInfoInHtmlArray = new ImagePositionInfo[frameSize][];
		styleInfoArray = new ArrayList<Map<String, ICurrentStyles>>(frameSize);
		for (int i = 0; i < frameSize; i++) {
			styleInfoArray.add(new HashMap<String, ICurrentStyles>());
		}
		// htmlLine2Id = new HtmlLine2Id[frameSize];
		// nodeId2Position = new HashMap[frameSize];
		isFinished = new boolean[frameSize];
		for (int i = 0; i < frameSize; i++) {
			isFinished[i] = false;
		}
	}

	private HashMap<String, WaitExecSyncEventListener> eventhandlerHolder = new HashMap<String, WaitExecSyncEventListener>();
	private static final String LISTENER_KEY = "browser"; //$NON-NLS-1$

	public void doSimulate() {
		clearHighlight();
		Display.getCurrent().timerExec(50, new Runnable() {

			public void run() {
				doSimulateInternal();
			}
		});
	}

	private void doSimulateInternal() {

		is1stSimulateDone = true;
		// TODO button: enable,disable
		if (isBInSimulate()) {
			return;
		}

		this._isInSimulate = true;
		this._shell.setCursor(waitCursor);

		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		IViewReference viewRef = activePage.findViewReference(IVisualizationView.ID_LOWVISIONVIEW);
		if (viewRef.isFastView()) {
			if (activePage.getPartState(viewRef) == IWorkbenchPage.STATE_RESTORED) {
				activePage.setPartState(viewRef, IWorkbenchPage.STATE_MINIMIZED);
			}
		}

		Mediator.getInstance().setReport(checker, dummyResult);
		checkResult = new CheckResultLowVision();

		lowVisionView.clearImage();
		_shell.getDisplay().update();

		checkThreads = new Vector<ExtractCheckThread>();

		targetModelService = ModelServiceUtils.getActiveModelService();
		if (targetModelService == null) {
			IEditorPart editor = ModelServiceUtils.reopenInACTFBrowser();
			if (editor instanceof IModelServiceHolder) {
				targetModelService = ((IModelServiceHolder) editor).getModelService();
				WaitExecSyncEventHandler handler = new WaitForBrowserReadyHandler((IWebBrowserACTF) targetModelService,
						30, false, new Runnable() {
							public void run() {
								eventhandlerHolder.remove(LISTENER_KEY);
								doSimulate();
							}
						});
				eventhandlerHolder.put(LISTENER_KEY, new WaitExecSyncEventListener(handler));
			}
			this._shell.setCursor(new Cursor(_shell.getDisplay(), SWT.CURSOR_ARROW));
			this._isInSimulate = false;
			return;
		}

		webBrowser = null;
		if (targetModelService instanceof IWebBrowserACTF) {
			webBrowser = (IWebBrowserACTF) targetModelService;
		}

		frameUrl = new String[0];
		int frameSize = 0;
		if (webBrowser != null) {

			// TODO frame support
			// if (lowVisionView.isWholepage()) {
			// frameUrl = LowVisionUtil.frameAnalyze(webBrowser);
			// }

			frameSize = frameUrl.length;
			if (frameSize == 0) {
				allocate(1);
			} else {
				allocate(frameSize);
				int tmpSize = dump_image_size / frameSize - (dump_image_size / 10) * (frameSize - 1);
				if (tmpSize < dump_image_size / 10) {
					tmpSize = dump_image_size / 10;
				}
			}
		} else {
			allocate(1);
		}

		targetUrlS = targetModelService.getURL();

		if (webBrowser != null) {
			try {
				removeTempFile(sourceHtmlFile);
				sourceHtmlFile = LowVisionVizPlugin.createTempFile("source", SUFFIX_HTML); //$NON-NLS-1$
				webBrowser.saveOriginalDocument(sourceHtmlFile.getAbsolutePath());
				checkResult.setSourceFile(sourceHtmlFile);
			} catch (Exception e) {
			}

		}

		if (frameSize == 0) {
			frameUrl = new String[1];
			frameUrl[0] = targetUrlS;
			prepareInt2Ds(targetModelService, 0, 0);
		} else {
			if (webBrowser != null) {
				// TODO replace with WaitForBrowserReadyHandler
				// webBrowser.syncNavigate(frameUrl[0]);
				// new WaitSyncNavigateThread(0, frameSize - 1).start();
			}
		}

	}

	private void prepareInt2Ds(IModelService modelService, int frameId, int lastFrame) {
		try {
			checker.setStatusMessage(Messages.LowVisionView_dump_the_image_in_the_web_browser__26);

			ModelServiceImageCreator imgCreator = new ModelServiceImageCreator(modelService);
			imgCreator.getScreenImageAsBMP(dumpImageFile, checker.isWholepage()
					&& targetModelService.getScrollManager().getScrollType() != IModelServiceScrollManager.NONE);
			scaleX = imgCreator.getScaleX();
			scaleY = imgCreator.getScaleY();
			lowVisionView.setScale(scaleX, scaleY);

			framePageImage[frameId] =
					// partLeftWebBrowser.dumpWebBrowserImg(
					PageImageFactory.createPageImage(dumpImageFile);
			// System.out.println("finish dump");

			IWebBrowserACTF browser = null;
			if (modelService instanceof IWebBrowserACTF) {
				browser = (IWebBrowserACTF) modelService;
			}

			if (framePageImage[frameId] != null) {
				checker.setStatusMessage(Messages.LowVisionView_get_information_of_all_images__25);

				Document document = null;
				IStyleSheets styleSheets = null;

				if (browser != null) {
					// double zoomFactor = browser.getZoomFactor();
					imageInfoInHtmlArray[frameId] = browser.getAllImagePosition();
					styleInfoArray.set(frameId, browser.getStyleInfo().getCurrentStyles());
					document = browser.getDocument();
					styleSheets = browser.getStyleSheets();
				} else {
					styleInfoArray.set(frameId, new HashMap<String, ICurrentStyles>());
				}

				if (lastFrame > 1) { // TODO frameURL.length?
					imageInfoInHtmlArray[frameId] = LowVisionUtil.trimInfoImageInHtml(imageInfoInHtmlArray[frameId],
							framePageImage[frameId].getHeight());
					styleInfoArray.set(frameId, LowVisionUtil.trimStyleInfoArray(styleInfoArray.get(frameId),
							framePageImage[frameId].getHeight()));
				}

				checker.setStatusMessage(Messages.LowVisionView_begin_to_make_PageImage__2);

				ExtractCheckThread checkThread = new ExtractCheckThread(frameId, frameUrl[frameId], document,
						styleSheets);
				checkThread.start();

				checkThreads.add(checkThread);

				isFinished[frameId] = true;
				if (browser != null) {
					if (frameId == lastFrame) {

						if (lastFrame > 0) {
							// browser.syncNavigate(targetUrlS);
							browser.navigate(targetUrlS);
						}
						doSimulateAfterHalf();
					} else {
						frameId++;
						// TODO replace with WaitForBrowserReadyHandler
						// browser.syncNavigate(frameUrl[frameId]);
						// new WaitSyncNavigateThread(frameId,
						// lastFrame).start();
					}
				} else {
					doSimulateAfterHalf();
				}
			} else {
				_shell.setCursor(null);
				_isInSimulate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doSimulateAfterHalf() {
		IPageImage pageImageWhole;
		if (framePageImage.length > 1) {
			pageImageWhole = PageImageFactory.joinPageImages(framePageImage);
		} else {
			pageImageWhole = framePageImage[0];
		}

		checker.setStatusMessage(Messages.LowVisionView_prepare_Simulation_Image__29);

		try {
			removeTempFile(visResultFile);
			visResultFile = LowVisionVizPlugin.createTempFile(PREFIX_VISUALIZATION, SUFFIX_BMP);
			ImageData[] imageDataArray = SimulateLowVision.doSimulate(pageImageWhole, paramLowVision,
					visResultFile.getAbsolutePath());
			if (imageDataArray.length > 0) {
				lowVisionView.displayImage(imageDataArray[0], targetModelService, checker.isWholepage());
				imageDataArray = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		checkResult.setFrameOffsetToProblems(framePageImage);

		new WaitExtractThread(null).start();
	}

	public void setHighlightPositions(List<IPositionSize> infoPositionSizeList) {
		lowVisionView.highlight(infoPositionSizeList);
	}

	/**
	 * @param vision
	 */
	public void setParamLowVision(ParamLowVision vision) {
		paramLowVision = vision;
	}

	public boolean isChildThread() {
		// TODO for HPB integration
		// return (aDesigner.isChildThread());
		return false;
	}

	public void setLVParamStatus() {
		if (!isBInSimulate()) {
			checker.setInfoMessage(paramLowVision.toString());
		}
	}

	public void simulateForMoreParameter() {
		_isInSimulate = true;

		checkResult = new CheckResultLowVision();
		// ADesignerMediator.getInstance().setCheckResult(checker, checkResult);

		lowVisionView.clearImage();
		checkThreads = new Vector<ExtractCheckThread>();

		IModelService modelService = ModelServiceUtils.getActiveModelService();

		Document document = null;
		IStyleSheets styleSheets = null;
		if (modelService instanceof IWebBrowserACTF) {
			IWebBrowserACTF browser = (IWebBrowserACTF) modelService;
			document = browser.getDocument();
			styleSheets = browser.getStyleSheets();
		}

		if (frameUrl.length == 0) {
			checker.setStatusMessage(Messages.LowVisionView_begin_to_make_PageImage__2);
			// TODO check(original is getAddressText())
			ExtractCheckThread checkThread = new ExtractCheckThread(0, modelService.getURL(), document, styleSheets);

			checkThread.start();
			checkThreads.add(checkThread);
		} else {
			for (int i = 0; i < frameUrl.length; i++) {
				ExtractCheckThread checkThread = new ExtractCheckThread(i, frameUrl[i], null, null); //TODO
				checkThread.start();
				checkThreads.add(checkThread);
			}
		}
		doSimulateAfterHalf();
	}

	/**
	 * @return
	 */
	public boolean isBInSimulate() {
		return _isInSimulate;
	}

	public boolean isInSaveReport() {
		return _saveReportLowVision.isInSaveReport();
	}

	public int getReportColorNum() {
		return _saveReportLowVision.getNumColorProblem();
	}

	public int getReportFontNum() {
		return _saveReportLowVision.getNumFontProblem();
	}

	/**
	 * @param dump_image_size The dump_image_size to set.
	 */
	public void setDump_image_size(int dump_image_size) {
		this.dump_image_size = dump_image_size;
	}

	private ArrayList<IElementACTF> highlightElements = new ArrayList<IElementACTF>();

	private void clearHighlight() {
		for (IElementACTF tmpE : highlightElements) {
			if (tmpE != null) {
				tmpE.unhighlight();
			}
		}
		highlightElements.clear();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		clearHighlight();
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			DebugPrintUtil.devOrDebugPrintln(this.getClass().getName() + ": Iselection"); //$NON-NLS-1$
			return;
		}
		ArrayList<IPositionSize> result = new ArrayList<IPositionSize>();

		// TODO check
		for (@SuppressWarnings("rawtypes")
		Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
			IProblemItem item = (IProblemItem) i.next();
			if (checkResult.getProblemList().contains(item)) {
				IPositionSize ips = (IPositionSize) item;
				result.add(ips);
			}

			if (item.getTargetNode() instanceof IElementACTF) {
				IElementACTF tmpE = (IElementACTF) item.getTargetNode();
				tmpE.highlight();
				highlightElements.add(tmpE);
			}
		}
		setHighlightPositions(result);
	}

	public void setCurrentModelService(IModelService modelService) {
		lowVisionView.setCurrentModelService(modelService);
	}

	private void removeTempFile(File target) {
		if (target != null) {
			target.delete();
		}
	}

}
