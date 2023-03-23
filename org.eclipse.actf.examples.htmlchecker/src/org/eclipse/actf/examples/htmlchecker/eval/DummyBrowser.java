/*******************************************************************************
 * Copyright (c) 2011, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.htmlchecker.eval;

import java.io.File;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DummyBrowser implements IWebBrowserACTF {

	private boolean isFile = true;
	private File htmlFile;

	public String[] getSupportMIMETypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSupportExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentMIMEType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void open(String url) {
		navigate(url);
	}

	public void open(File target) {
		isFile = true;
		this.htmlFile = target;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	public Document getDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	public Document getLiveDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	public Composite getTargetComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	public File saveOriginalDocument(String file) {
		if (null != file) {
			if (isFile) {
				FileUtils.copyFile(htmlFile, file, true);
				try {
					return new File(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// TODO
			}
		}
		return null;
	}

	public File saveDocumentAsHTMLFile(String file) {
		// TODO
		if (null != file) {
			if (isFile) {
				FileUtils.copyFile(htmlFile, file, true);
				try {
					return new File(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// TODO
			}
		}
		return null;
	}

	public void jumpToNode(Node target) {
		// TODO Auto-generated method stub

	}

	public IModelServiceScrollManager getScrollManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImagePositionInfo[] getAllImagePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public IModelServiceHolder getModelServiceHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFocusAddressText(boolean selectAll) {
		// TODO Auto-generated method stub

	}

	public void showAddressText(boolean flag) {
		// TODO Auto-generated method stub

	}

	public void navigate(String url) {
		isFile = false;

		// TODO
		// InputStream is;
		// URL url = new java.net.URL(url);
		// is = url.openStream();

	}

	public void goBackward() {
		// TODO Auto-generated method stub

	}

	public void goForward() {
		// TODO Auto-generated method stub

	}

	public void navigateStop() {
		// TODO Auto-generated method stub

	}

	public void navigateRefresh() {
		// TODO Auto-generated method stub

	}

	public int getReadyState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getLocationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUrlExists() {
		return true;
	}

	public int getNavigateErrorCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setWebBrowserSilent(boolean bSilent) {
		// TODO Auto-generated method stub

	}

	public void setDisableScriptDebugger(boolean bDisable) {
		// TODO Auto-generated method stub

	}

	public boolean isDisableScriptDebugger() {
		// TODO Auto-generated method stub
		return false;
	}

	public void highlightElementById(String id) {
		// TODO Auto-generated method stub

	}

	public void hightlightElementByAttribute(String name, String value) {
		// TODO Auto-generated method stub

	}

	public void clearHighlight() {
		// TODO Auto-generated method stub

	}

	public void setFontSize(int fontSize) {
		// TODO Auto-generated method stub

	}

	public int getFontSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public IWebBrowserStyleInfo getStyleInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getBrowserAddress() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int setTimeout(String script, int interval) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean clearTimeout(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public int setInterval(String script, int interval) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean clearInterval(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public double getZoomFactor() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void setZoomFactor(double zoomFactor) {
		// TODO Auto-generated method stub
	}

	@Override
	public IStyleSheets getStyleSheets() {
		// TODO Auto-generated method stub
		return null;
	}

}
