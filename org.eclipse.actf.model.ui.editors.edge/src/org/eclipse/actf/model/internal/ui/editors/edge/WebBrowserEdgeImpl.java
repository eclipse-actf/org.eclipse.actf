/*******************************************************************************
 * Copyright (c) 2007, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheet;
import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.dom.html.HTMLParserFactory;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.ModelServiceSizeInfo;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.model.ui.editor.browser.WebBrowserEventUtil;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class WebBrowserEdgeImpl implements IWebBrowserACTF {

	private static double initial_zoomFactor = 1;

	private static boolean isFreeSize = true;
	private static int browserWidth = -1;
	private static int browserHeight = -1;

	private WebBrowserToolbar toolbar;

	private WebBrowserEdgeComposite browserComposite;

	private boolean _inNavigation = false;

	private boolean _inReload = false;

	private boolean _inStop = false;

	private boolean _inJavascript = false;

	// TODO back,forw,stop,replace,etc.
	private boolean _urlExist;

	private int _navigateErrorCode;

	private IModelServiceHolder _holder = null;

//	private boolean onloadPopupBlock = true;

	// private boolean allowNewWindow = false;

	private IModelServiceScrollManager scrollManager;

//	private DomByCom domByCom;

//	private INewWiondow2EventListener newWindow2EventListener = null;
//
//	private IWindowClosedEventListener windowClosedEventListener = null;

	private OpenWindowListener _openWindowListener = null;
	private CloseWindowListener _closeWindowListener = null;

	private String errorUrl = null;
//	private int tmpErrorCode = 0;

	private Composite compositeForBrowserSize;

	public WebBrowserEdgeImpl(IModelServiceHolder holder, Composite parent, String startURL) {
		this._holder = holder;

		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		parent.setLayout(gridLayout);

		toolbar = new WebBrowserToolbar(this, parent, SWT.NONE);

		Composite dummyComp = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.marginBottom = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 1;
		dummyComp.setLayout(gridLayout);
		dummyComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		compositeForBrowserSize = new Composite(dummyComp, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginBottom = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 1;
		compositeForBrowserSize.setLayout(gridLayout);
		adjustBrowserComposizeSize();

//		browserComposite = new WebBrowserEdgeComposite(parent, SWT.NONE);
		browserComposite = new WebBrowserEdgeComposite(compositeForBrowserSize, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		browserComposite.setLayout(layout);
		browserComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		browserComposite.setZoomFactor(initial_zoomFactor); // TODO

		if (null == startURL) {
			startURL = "about:blank"; //$NON-NLS-1$
		}
		toolbar.setAddressTextString(startURL);

		setDisableScriptDebugger(true);

//		browserComposite.addBrowserEventListener(this);
		addBrowserEventListener();

		scrollManager = new WebBrowserEdgeScrollManager(this);

//		domByCom = new DomByCom(getBrowserAddress());

		navigate(startURL);
	}

	/**
	 * Set target Browser Size
	 * 
	 * @param zoomFactor target zoomFactor
	 */
	public void setBrowserSize(boolean isFree, int width, int height) {
		isFreeSize = isFree;
		browserWidth = width;
		browserHeight = height;
	}

	private void adjustBrowserComposizeSize() {
		GridData tmpData = new GridData(GridData.FILL_BOTH);
		if (isFreeSize) {
			compositeForBrowserSize.setLayoutData(tmpData);
		} else if (browserWidth > 0 && browserHeight > 0) {
			tmpData = new GridData(SWT.CENTER, SWT.TOP, false, false);
			tmpData.minimumWidth = browserWidth;
			tmpData.widthHint = browserWidth;
			tmpData.minimumHeight = browserHeight;
			tmpData.heightHint = browserHeight;
			compositeForBrowserSize.setLayoutData(tmpData);
		}
	}

	public void setFocusAddressText(boolean selectAll) {
		toolbar.setFocusToAddressText(selectAll);
	}

	public void showAddressText(boolean flag) {
		toolbar.showAddressText(flag);
	}

	/*
	 * browse commands
	 */

	public void navigate(String url) {
		if (isDisposed())
			return;

		toolbar.setAddressTextString(url);

		errorUrl = null;
		this._urlExist = true;
		this._navigateErrorCode = 200;

		browserComposite.navigate(url);

		// TODO file:// case (ReadyState = 1)
		// System.out.println("State:"+getReadyState());
//		//test();
	}

	public void goBackward() {
		if (isDisposed())
			return;

		// TODO rename?
		errorUrl = null;
		browserComposite.goBack();
	}

	public void goForward() {
		if (isDisposed())
			return;

		errorUrl = null;
		browserComposite.goForward();
	}

	public void navigateStop() {
		if (isDisposed())
			return;

		if (_inNavigation || _inReload) {
			_inStop = true;
			_inNavigation = false;
			_inReload = false;
			_inJavascript = false;
		}
		errorUrl = null;
		browserComposite.stop();
	}

	public void navigateRefresh() {
		if (isDisposed())
			return;

		if (!_inReload) {
			_inReload = true;
			_inJavascript = false;
			WebBrowserEventUtil.refreshStart(WebBrowserEdgeImpl.this);
		}
		errorUrl = null;
		browserComposite.refresh();
	}

	/**
	 * @param isWhole
	 * @return (browserSizeX, browserSizeY, pageSizeX, pageSizeY)
	 */
	ModelServiceSizeInfo getBrowserSize(boolean isWhole) {
		if (isDisposed())
			return null;

		int[] size = new int[] { 1, 1, 1, 1 };

//		System.out.println("getBrowserSize: "+ browserComposite.getWidth() + ", " + browserComposite.getHeight() + ", "
//				+ browserComposite.getWholeSize()[0] + ", " + browserComposite.getWholeSize()[1]);

		int width = browserComposite.getClientWidth();
		int height = browserComposite.getClientHeight();
		size[0] = width;
		size[1] = height;
		size[2] = width;
		size[3] = height;
		if (isWhole) {
			int[] tmpSize = browserComposite.getWholeSize();
			if (tmpSize.length == 2 && tmpSize[0] > -1 && tmpSize[1] > -1) {
				size[2] = tmpSize[0];
				size[3] = tmpSize[1];
//				if (tmpSize[0] > size[0]) {
//					size[1] -= scrollbarWidth;
//				}
			}
		}

		return (new ModelServiceSizeInfo(size[0], size[1], size[2], size[3]));
	}

	/*
	 * navigation result
	 */

	public int getReadyState() {
		if (isDisposed())
			return READYSTATE_UNINITIALIZED;

		switch (browserComposite.getReadyState()) {
		case "loading": //$NON-NLS-1$
			return READYSTATE_LOADING;
		case "interactive": //$NON-NLS-1$
			return READYSTATE_INTERACTIVE;
		case "complete": //$NON-NLS-1$
			return READYSTATE_COMPLETE;
		default:
			return READYSTATE_UNINITIALIZED;
		}
	}

	// TODO add to IWebBrowser Interface
	// Browser properties
	// "Width"
	// "Height"
	// "Left"
	// "Top"
	// "BrowserType"
	// "Silent"
	// "setSilent"

	// TODO remove?
	public boolean isReady() {
		return (getReadyState() == READYSTATE_COMPLETE);
	}

	public String getURL() {
		if (isDisposed())
			return null;

		return browserComposite.getLocationURL();
	}

	public String getLocationName() {
		if (isDisposed())
			return null;

		return browserComposite.getLocationName();
	}

	public boolean isUrlExists() {
		// TODO
		return this._urlExist;
	}

	@Deprecated
	public int getNavigateErrorCode() {
		// TODO
		return this._navigateErrorCode;
	}

	/*
	 * Scroll
	 */

	public IModelServiceScrollManager getScrollManager() {
		return scrollManager;
	}

	void scrollY(int y) {
		if (isDisposed())
			return;

		browserComposite.scrollBy(0, y);
	}

	void scrollTo(int x, int y) {
		if (isDisposed())
			return;

		browserComposite.scrollTo(x, y);
	}

	/*
	 * browser setting
	 */
	public void setHlinkStop(boolean bStop) {
		// TODO low priority
	}

	@Deprecated
	public void setWebBrowserSilent(boolean bSilent) {
	}

	@Deprecated
	public void setDisableScriptDebugger(boolean bDisable) {
	}

	public void setDisplayImage(boolean display) {
		// TODO
	}

	@Deprecated
	public boolean isDisableScriptDebugger() {
		return false;
	}

	@Deprecated
	public void setFontSize(int fontSize) {
	}

	@Deprecated
	public int getFontSize() {
		return 0;
	}

	/*
	 * highlight
	 */

	public void highlightElementById(String idVal) {
		// TODO low priority
	}

	public void hightlightElementByAttribute(String name, String value) {
		// TODO low priority
	}

	public void clearHighlight() {
		// TODO low priority
	}

	@Deprecated
	RGB getAnchorColor() {
		return getRGB("0,0,255"); //$NON-NLS-1$
	}

	@Deprecated
	RGB getVisitedAnchorColor() {
		return getRGB("128,0,128"); //$NON-NLS-1$
	}

	HashMap<String, ICurrentStyles> getCurrentStyles() {
		if (isDisposed())
			return null;

		Object[] elements = browserComposite.getCurrentStyles();
		HashMap<String, ICurrentStyles> currentStyles = new HashMap<String, ICurrentStyles>(elements.length);
		for (Object obj : elements) {
			try {
				Object[] elm = (Object[]) obj;
				int handle = ((Number) elm[0]).intValue();
				String xpath = (String) (elm[1]);
				String tagName = (String) (elm[2]);
				String href = (String) (elm[3]);
				URL target = null;
				try {
					target = href != null ? new URL(href) : null;
				} catch (Exception e) {
					System.err.println(e.getMessage() + " href=" + href); //$NON-NLS-1$
				}
				int[] r = Stream.of((Object[]) elm[4]).mapToInt(n -> ((Number) n).intValue()).toArray();
				Map<String, String> styles = Stream.of((Object[]) elm[5])
						.collect(Collectors.toMap(o -> (String) ((Object[]) o)[0], o -> (String) ((Object[]) o)[1]));
				String[] childTexts = Stream.of((Object[]) elm[6]).toArray(String[]::new);
				String[] descendantTextsWithBGImage = Stream.of((Object[]) elm[7]).toArray(String[]::new);
				currentStyles.put(xpath,
						new CurrentStylesImpl(this, handle, xpath, tagName, target,
								new Rectangle(r[0], r[1], r[2], r[3]), getZoomFactor(), styles, childTexts,
								descendantTextsWithBGImage));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return currentStyles;
	}

	private RGB getRGB(String color) {
		if (null != color) {
			try {
				String[] strArray = color.split(","); //$NON-NLS-1$
				return new RGB(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]),
						Integer.parseInt(strArray[2]));
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Deprecated
	public long getBrowserAddress() {
		System.err.println("ERROR: getBrowserAddress() is not supported"); //$NON-NLS-1$
		return 0L;
	}

	public String[] getSupportMIMETypes() {
		return MIMETYPES_HTML;
	}

	public String[] getSupportExtensions() {
		return EXTS_HTML;
	}

	public void open(String url) {
		navigate(url);
	}

	public void open(File target) {
		if (null != target) {
			// TODO test
			navigate(target.getAbsolutePath());
		}
	}

	@SuppressWarnings("nls")
	public Document getDocument() {
		try {
			File tmpF = BrowserEdge_Plugin.getDefault().createTempFile("actf", "html"); //$NON-NLS-1$ //$NON-NLS-2$
			saveOriginalDocument(tmpF.getAbsolutePath());
			IHTMLParser parser = HTMLParserFactory.createHTMLParser();
			parser.parse(new FileInputStream(tmpF));
			tmpF.delete();
			return parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public Document getLiveDocument() {
//		System.err.println("ERROR: getLiveDocument() is not supported");
//		return domByCom.getDocument();
		try {
			File tmpF = BrowserEdge_Plugin.getDefault().createTempFile("actf-live-", "html"); //$NON-NLS-1$ //$NON-NLS-2$
			saveDocumentAsHTMLFile(tmpF.getAbsolutePath());
			IHTMLParser parser = HTMLParserFactory.createHTMLParser();
			parser.parse(new FileInputStream(tmpF));
			tmpF.delete();
			return parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Composite getTargetComposite() {
		return browserComposite;
	}

	public File saveDocumentAsHTMLFile(String file) {
		if (isDisposed())
			return null;

		if (null != file) {
			// TODO replace with DomByCOM (need write as XML support)
			try (FileWriter fw = new FileWriter(file)) {
				fw.write(browserComposite.getLiveDocument());
				return new File(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("nls")
	public File saveOriginalDocument(String file) {
		if (isDisposed())
			return null;

		if (null != file) {
			String curURL = getURL();
			if (curURL.startsWith("file:///")) { //$NON-NLS-1$
				try {
					if (FileUtils.copyFile(new File(new URI(curURL)), file, true)) {
						return new File(file);
					}
				} catch (Exception e) {
				}
			}

			String orgHtmlS = browserComposite.getOriginalDocument();
			// System.out.println(orgHtmlS);
			// JapaneseEncodingDetector jed = new JapaneseEncodingDetector(inputStream);

			try (FileWriter fw = new FileWriter(file)) {
				if ("about:blank".equals(curURL)) {
					fw.write("<!DOCTYPE html><html lang=\"en\"><head><title>about:blank</title></head><body></body></html>"); //$NON-NLS-1$
				} else {
					fw.write(orgHtmlS == null ? Messages.WebBrowserEdgeImpl_HTMLforLocalFileError : orgHtmlS); //$NON-NLS-1$
				}
				return new File(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void jumpToNode(Node target) {
		// TODO impl for Runtime Dom
	}

	public String getCurrentMIMEType() {
		// TODO get info from browser
		return MIMETYPES_HTML[0];
	}

	/*
	 * SWT Browser Event Listeners
	 */

	private void addBrowserEventListener() {
		// TODO remove debug messages
		// TODO add listener features
		Browser browser = browserComposite.getBrowser();
		browser.addLocationListener(new LocationListener() {

			@Override
			public void changing(LocationEvent event) {
//				System.out.println(String.format("Location changing %s", event.location));
			}

			@Override
			public void changed(LocationEvent event) {
				System.out.println(String.format("Location changed %s", event.location)); //$NON-NLS-1$
				toolbar.setAddressTextString(browserComposite.getLocationURL());
			}
		});
		browser.addProgressListener(new ProgressListener() {

			@Override
			public void completed(ProgressEvent event) {
				System.out.println("Progress completed"); //$NON-NLS-1$
			}

			@Override
			public void changed(ProgressEvent event) {
				System.out.println("Progress changed"); //$NON-NLS-1$
			}
		});
		browser.addOpenWindowListener(new OpenWindowListener() {

			@Override
			public void open(WindowEvent event) {
				System.out.println("Window open"); //$NON-NLS-1$
				if (_openWindowListener != null) {
					_openWindowListener.open(event);
				} else {
					event.browser = browser; // Disable new window open
				}
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener() {

			@Override
			public void close(WindowEvent event) {
				System.out.println("Window close"); //$NON-NLS-1$
				if (_closeWindowListener != null) {
					_closeWindowListener.close(event);
				}
			}
		});
		browser.addTitleListener(new TitleListener() {

			@Override
			public void changed(TitleEvent event) {
				System.out.println(String.format("Title changed %s", event.title)); //$NON-NLS-1$
				_holder.setEditorTitle(event.title);
			}
		});
		browser.addVisibilityWindowListener(new VisibilityWindowListener() {

			@Override
			public void show(WindowEvent event) {
				System.out.println("Window show"); //$NON-NLS-1$
			}

			@Override
			public void hide(WindowEvent event) {
				System.out.println("Window hide"); //$NON-NLS-1$
			}
		});
	}

	/*
	 * BrowserEventListener implementations
	 */
// TODO Remove all BrowserEventListener (from beforeNavigate2 to windowClosed)
//	@SuppressWarnings("nls")
//	public void beforeNavigate2(BeforeNavigate2Parameters param) {
//		// _inNavigation = true;
//		String target = param.getUrl();
//		DebugPrintUtil.debugPrintln("BN: " + target + " "
//				+ param.getTargetFrameName());
//		if (!_inReload) {
//			if (!target.startsWith("javascript")) { // TODO //$NON-NLS-1$
//				_inJavascript = false;
//				_inNavigation = true;
//				_inReload = false;
//			} else {
//				_inJavascript = true;
//				_inNavigation = false;
//				_inReload = false;
//			}
//		}
//		WebBrowserEventUtil.beforeNavigate(this, target,
//				param.getTargetFrameName(), _inNavigation);
//
//	}
//
//	public void documentComplete(DocumentCompleteParameters param) {
//		if (param.isTopWindow()) {
//			if (errorUrl != null && errorUrl.equals(param.getUrl())) {
//				_navigateErrorCode = tmpErrorCode;
//				_urlExist = false;
//				_inNavigation = false;
//			}
//
//			WebBrowserEventUtil.rootDocumentComplete(this);
//			// System.out.println("myDocComplete");
//			_inNavigation = false;
//			_inJavascript = false;
//			_inReload = false;
//			errorUrl = null;
//		}
//		// System.out.println("Document Complete:"+param.getUrl();
//	}
//
//	public void navigateComplete2(NavigateComplete2Parameters param) {
//		WebBrowserEventUtil.navigateComplete(this, param.getUrl());
//		toolbar.setAddressTextString(browserComposite.getLocationURL()/*
//																	 * param.getUrl
//																	 * ()
//																	 */);
//		DebugPrintUtil.debugPrintln("NavigateComplete2"); //$NON-NLS-1$
//	}
//
//	@SuppressWarnings("nls")
//	public void navigateError(NavigateErrorParameters param) {
//		String tmpUrl = param.getUrl();
//		tmpErrorCode = param.getStatusCode();
//		DebugPrintUtil.debugPrintln("Navigate Error. URL:" + tmpUrl
//				+ " Status Code:" + tmpErrorCode + " "
//				+ browserComposite.getLocationURL());
//
//		if (browserComposite.getLocationURL().equals(tmpUrl)) {
//			_navigateErrorCode = tmpErrorCode;
//			_urlExist = false;
//			_inNavigation = false;
//		} else {
//			errorUrl = tmpUrl;
//		}
//	}
//
//	public void newWindow2(NewWindow2Parameters param) {
//		if (_inNavigation && onloadPopupBlock/* !browser2.READYSTATE_COMPLETE */) {
//			// TODO
//			param.setCancel(true);
//		} else if (newWindow2EventListener != null) {
//			newWindow2EventListener.newWindow2(param);
//		}
//	}
//
//	@SuppressWarnings("nls")
//	public void progressChange(ProgressChangeParameters param) {
//		int prog = param.getProgress();
//		int progMax = param.getProgressMax();
//		WebBrowserEventUtil.progressChange(this, prog, progMax);
//		DebugPrintUtil.debugPrintln("Stop: " + _inStop + " Reload: "
//				+ _inReload + " inJavaScript: " + _inJavascript
//				+ " navigation: " + _inNavigation);
//		if (_inStop) {
//			if (prog == 0 && progMax == 0) {
//				_inStop = false;
//				DebugPrintUtil.debugPrintln("stop fin");
//				WebBrowserEventUtil.navigateStop(this);
//			}
//		} else if (_inReload) {
//			if (prog == 0 && progMax == 0) {
//				_inReload = false;
//				DebugPrintUtil.debugPrintln("reload fin");
//				WebBrowserEventUtil.refreshComplete(this);
//			}
//		} else if (_inJavascript) {
//			if (prog == -1 && progMax == -1) {
//				_inJavascript = false;
//				DebugPrintUtil.debugPrintln("javascript fin");
//			}
//		} else if (!_inNavigation && !(prog == 0 && progMax == 0)) {
//			// 0/0 is complete
//			_inReload = true;
//			DebugPrintUtil.debugPrintln("reload");
//			WebBrowserEventUtil.refreshStart(this);
//		}
//	}
//
//	public void statusTextChange(StatusTextChangeParameters param) {
//		// System.out.println(param.getText());
//	}
//
//	@SuppressWarnings("nls")
//	public void titleChange(TitleChangeParameters param) {
//		try {
//			String title = param.getText();
//			WebBrowserEventUtil.titleChange(this, title);
//			DebugPrintUtil.debugPrintln("TitleChange");
//			if (!(_inNavigation || _inStop)) {
//				if (!_inReload) {
//					_inReload = true;
//					_inJavascript = false;
//					DebugPrintUtil.debugPrintln("reload");
//					WebBrowserEventUtil.refreshStart(this);
//				}
//			}
//			_holder.setEditorTitle(title);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void windowClosing(WindowClosingParameters param) {
//	}
//
//	public void windowClosed() {
//		if (windowClosedEventListener != null) {
//			windowClosedEventListener.windowClosed();
//		}
//	}

	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getID() {
		return WebBrowserEdgeImpl.class.getName() + ":" + this; //$NON-NLS-1$
	}

	public String getTitle() {
		return getLocationName();
	}

	public void setOpenWindowListener(OpenWindowListener listener) {
		this._openWindowListener = listener;
	}

	public void setCloseWindowListener(CloseWindowListener listener) {
		this._closeWindowListener = listener;
	}

	public ImagePositionInfo[] getAllImagePosition() {
//		NodeList tmpNL = getLiveDocument().getElementsByTagName("img");
//		ArrayList<ImagePositionInfo> list = new ArrayList<ImagePositionInfo>();
//		for (int i = 0; i < tmpNL.getLength(); i++) {
//			try {
//				IElementEx tmpE = ((IElementEx) tmpNL.item(i));
//				list.add(new ImagePositionInfo(tmpE.getLocation(), tmpE
//						.getAttribute("src"), tmpE));
//			} catch (Exception e) {
//			}
//		}
//		ImagePositionInfo[] result = new ImagePositionInfo[list.size()];
//		list.toArray(result);
//		return result;
//
		return Stream.of(browserComposite.getAllImagePosition()).map(obj -> {
			Object[] pos = (Object[]) obj;
			int[] r = Stream.of((Object[]) pos[1]).mapToInt(n -> ((Number) n).intValue()).toArray();
			return new ImagePositionInfo(new Rectangle(r[0], r[1], r[2], r[3]), getZoomFactor(), (String) pos[2],
					new EdgeElementImpl(this, ((Number) pos[0]).intValue()));
		}).toArray(ImagePositionInfo[]::new);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.IModelService#getModelServiceHolder()
	 */
	public IModelServiceHolder getModelServiceHolder() {
		return _holder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF#getStyleInfo()
	 */
	public IWebBrowserStyleInfo getStyleInfo() {
		// TODO obtain current style info from live DOM [233615]
		// need to wait IPZilla [2323]
		return new WebBrowserStyleInfoImpl(this);
	}

	@Deprecated
	public boolean clearInterval(int id) {
		return false;
	}

	@Deprecated
	public boolean clearTimeout(int id) {
		return false;
	}

	@Deprecated
	public int setInterval(String script, int interval) {
		return 0;
	}

	@Deprecated
	public int setTimeout(String script, int interval) {
		return 0;
	}

	private boolean isDisposed() {
		if (browserComposite.isDisposed()) {
			System.err.println("browserComposite is disposed"); //$NON-NLS-1$
			return true;
		}
		return false;
	}

	public double getZoomFactor() {
		if (isDisposed())
			return 0.0;

		return browserComposite.getZoomFactor();
	}

	public void setZoomFactor(double zoomFactor) {
		if (isDisposed())
			return;

		browserComposite.setZoomFactor(zoomFactor);
		initial_zoomFactor = zoomFactor; // TODO
	}

	public void highlight(int handle) {
		if (isDisposed())
			return;

		browserComposite.setCssText(handle, "border: 4px inset yellow;", true); //$NON-NLS-1$
	}

	public void unhighlight(int handle) {
		if (isDisposed())
			return;

		browserComposite.setCssText(handle, "border: 4px inset yellow;", false); //$NON-NLS-1$
	}

	public IStyleSheets getStyleSheets() {
		if (isDisposed())
			return null;

		return new EdgeStyleSheetsImpl(browserComposite.getStyleSheets());
	}

	/*
	 * for debug
	 */
	private void test() {
		System.out.println("getBrowserSize: " + getStyleInfo().getSizeInfo(true)); //$NON-NLS-1$
		System.out.println("getReadyState: " + getReadyState()); //$NON-NLS-1$
		System.out.println("isDisposed: " + getTargetComposite().isDisposed()); //$NON-NLS-1$

		for (ImagePositionInfo info : getAllImagePosition()) {
			((EdgeElementImpl) info.getElement()).highlight();
		}
		Map<String, ICurrentStyles> styles = getStyleInfo().getCurrentStyles();
		styles.forEach((k, v) -> {
			System.out.println(k + ": " + v.getTagName()); //$NON-NLS-1$
			if ("SPAN".equals(v.getTagName())) { //$NON-NLS-1$
				((EdgeElementImpl) v.getElement()).highlight();
			}
		});

		class Dumper {
			void dumpCssText(IStyleSheets styleSheets) {
				for (int i = 0; i < styleSheets.getLength(); i++) {
					IStyleSheet styleSheet = styleSheets.item(i);
					System.out.println("-------- Style Sheet#" + (i + 1)); //$NON-NLS-1$
					System.out.println("title: " + styleSheet.getTitle()); //$NON-NLS-1$
					System.out.println("href: " + styleSheet.getHref()); //$NON-NLS-1$
					try {
						String cssText = styleSheet.getCssText();
						System.out.println("---- cssText begin ----"); //$NON-NLS-1$
						System.out.println(cssText);
						System.out.println("---- cssText end ----"); //$NON-NLS-1$
					} catch (Exception e) {
						System.out.println("ERROR cssText: " + e.getMessage()); //$NON-NLS-1$
					}
					try {
						IStyleSheets imports = styleSheet.getImports();
						if (imports.getLength() > 0) {
							System.out.println("---- imports begin ----"); //$NON-NLS-1$
							new Dumper().dumpCssText(imports);
							System.out.println("---- imports end ----"); //$NON-NLS-1$
						}
					} catch (Exception e) {
						System.out.println("ERROR imports: " + e.getMessage()); //$NON-NLS-1$
					}
				}
			}
		}
		new Dumper().dumpCssText(getStyleSheets());
	}
}
