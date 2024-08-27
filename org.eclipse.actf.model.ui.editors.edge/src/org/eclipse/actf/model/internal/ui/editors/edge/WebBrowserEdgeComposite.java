/*******************************************************************************
 * Copyright (c) 2007, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

class WebBrowserEdgeComposite extends Composite {

	private Browser browser;
	private WebBrowserEdgeHelper helper;

	WebBrowserEdgeComposite(Composite parent, int style) {
		super(parent, style);
		browser = new Browser(this, SWT.EDGE);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		helper = new WebBrowserEdgeHelper(browser);
	}

	/*
	 * functions
	 */

	Browser getBrowser() {
		return browser;
	}

	void navigate(String url) {
		browser.setUrl(url);
	}

	void goBack() {
		browser.back();
	}

	void goForward() {
		browser.forward();
	}

	void stop() {
		browser.stop();
	}

	void refresh() {
		browser.refresh();
	}

	String getLocationURL() {
		return browser.getUrl();
	}

	int getClientWidth() {
		return ((Number) browser.evaluate(
				"return (document.compatMode === 'BackCompat' ? document.body : document.documentElement).clientWidth;"))
				.intValue();
	}

	int getClientHeight() {
		return ((Number) browser.evaluate(
				"return (document.compatMode === 'BackCompat' ? document.body : document.documentElement).clientHeight;"))
				.intValue();
	}

	int[] getWholeSize() {
		Object[] size = ((Object[]) browser.evaluate("return " + loadScript("getWholeSize.js")));
		return Stream.of(size).mapToInt(n -> ((Number) n).intValue()).toArray();
	}

	String getReadyState() {
		return (String) browser.evaluate("return document.readyState");
	}

	String getLocationName() {
		return (String) browser.evaluate("return document.title;");
	}

	void scrollBy(int x, int y) {
		browser.execute(String.format("scrollBy(%d, %d);", x, y));
	}

	void scrollTo(int x, int y) {
		browser.execute(String.format("scrollTo(%d, %d);", x, y));
	}

	String getLiveDocument() {
		return (String) browser.evaluate(
				"return (document.doctype ? new XMLSerializer().serializeToString(document.doctype) : '') + document.documentElement.outerHTML;");
	}

	String getLiveCharset() {
		return (String) browser.evaluate(
				"return document.characterSet;");
	}

	
	String getOriginalDocument() {
		String urlS = getLocationURL();
		if(urlS.startsWith("file://") && urlS.contains("#")) {
			navigate(urlS.substring(0,urlS.indexOf("#")));
		}
		try {
			return (String) browser.evaluate("return " + loadScript("getOriginalDocument.js"));			
		}catch (Exception e) {
			return null;
		}
	}

	Object[] getAllImagePosition() {
		initNodeMap();
		return (Object[]) browser.evaluate("return " + loadScript("getAllImagePosition.js"));
	}

	double getZoomFactor() {
		return helper.getZoomFactor();
	}

	void setZoomFactor(double zoomFactor) {
		helper.setZoomFactor(zoomFactor);
	}

	void setCssText(int handle, String text, boolean add) {
		browser.execute(loadScript("setCssText.js", handle, text, add));
	}

	Object[] getCurrentStyles() {
		initNodeMap();
		return (Object[]) browser.evaluate("return " + loadScript("getCurrentStyles.js"));
	}

	Object[] getStyleSheets() {
		return (Object[]) browser.evaluate("return " + loadScript("getStyleSheets.js"));
	}

	/*
	 * Private functions
	 */

	private void initNodeMap() {
		if (!(Boolean) browser.evaluate("return '__ACTF_NodeManager__' in window")) {
			browser.execute(loadScript("installNodeManager.js"));
		}
	}

	private String loadScript(String name, Object... args) {
		try (InputStream is = getClass().getResourceAsStream("scripts/" + name)) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("__ARGS__", objectToString(args));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String objectToString(Object[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			Object o = args[i];
			if (o instanceof String) {
				sb.append('"' + objectToString((String) o) + '"');
			} else if (o instanceof Object[]) {
				sb.append('[' + objectToString((Object[]) o) + ']');
			} else {
				sb.append(o);
			}
			if (i < args.length - 1) {
				sb.append(", ");
			}
		}
//		System.out.println(sb.toString());
		return sb.toString();
	}

	private static String objectToString(String str) {
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray()) {
			switch (c) {
			case '\"':
			case '\'':
				sb.append('\\' + c);
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

}
