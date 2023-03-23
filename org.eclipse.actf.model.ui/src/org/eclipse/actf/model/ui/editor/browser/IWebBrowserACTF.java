/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.ui.editor.browser;

import org.eclipse.actf.model.dom.dombyjs.IStyleSheets;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.ui.IEditorPart;
import org.w3c.dom.Document;

/**
 * Interface to provide access to model of the Web content as {@link Document}.
 * This interface extends {@link IModelService} to enable to control Web
 * Browser.
 * 
 * Users can get this {@link IModelService} through {@link IModelServiceHolder}
 * that is implemented with {@link IEditorPart}.
 * 
 * @see IModelService
 * @see IModelServiceHolder
 */
public interface IWebBrowserACTF extends IModelService {

	public static final int READYSTATE_UNINITIALIZED = 0;

	public static final int READYSTATE_LOADING = 1;

	public static final int READYSTATE_LOADED = 2;

	public static final int READYSTATE_INTERACTIVE = 3;

	public static final int READYSTATE_COMPLETE = 4;

	/**
	 * To set {@link IWebBrowserACTFEventListener} to override
	 * {@link DefaultWebBrowserNavigationEventListener}
	 */
	public class WebBrowserNavigationEventListnerHolder {
		public static IWebBrowserNavigationEventListener LISTENER = null;
	}

	/**
	 * Focus address text area
	 * 
	 * @param selectAll if true, select all text in the address text area
	 */
	public abstract void setFocusAddressText(boolean selectAll);

	/**
	 * Show/hide address text area
	 * 
	 * @param flag true to show address text area
	 */
	void showAddressText(boolean flag);

	/*
	 * browse commands
	 */
	/**
	 * Navigate to URL
	 * 
	 * @param url target URL
	 */
	public abstract void navigate(String url);

	/**
	 * Navigate to a backward page
	 */
	public abstract void goBackward();

	/**
	 * Navigate to a forward page
	 */
	public abstract void goForward();

	/**
	 * Stop navigation
	 */
	public abstract void navigateStop();

	/**
	 * Refresh
	 */
	public abstract void navigateRefresh();

	/*
	 * navigation result
	 */

	/**
	 * Get ready state of the browser
	 * 
	 * @return ready state
	 */
	public abstract int getReadyState();

	/**
	 * Check whether the browser is ready
	 * 
	 * @return whether the browser is ready
	 */
	public abstract boolean isReady();

	/**
	 * Get current location name
	 * 
	 * @return location name
	 */
	public abstract String getLocationName();

	/**
	 * Check whether the navigated URL exists
	 * 
	 * @return whether the navigated URL exists
	 */
	public abstract boolean isUrlExists();

	/**
	 * Get navigation error code
	 * 
	 * @return error code
	 */
	public abstract int getNavigateErrorCode();

	/*
	 * browser property
	 */
	// /** TODO
	// * Disable link navigation
	// * @param bStop true to disable link navigation
	// */
	// public abstract void setHlinkStop(boolean bStop);
	/**
	 * Set silent property
	 * 
	 * @param bSilent silent property
	 */
	public abstract void setWebBrowserSilent(boolean bSilent);

	/**
	 * Disable script debugger
	 * 
	 * @param bDisable true to disable script debugger
	 */
	public abstract void setDisableScriptDebugger(boolean bDisable);

	// /** TODO
	// *
	// * Set to show/hide images in the Web page
	// * @param display true to show images
	// */
	// public abstract void setDisplayImage(boolean display);

	/**
	 * Check whether the script debugger is disabled
	 * 
	 * @return whether the script debugger is disabled
	 */
	public abstract boolean isDisableScriptDebugger();

	/*
	 * highligt element
	 * 
	 */
	/**
	 * Highlight element that has specified ID
	 * 
	 * @param id target ID
	 */
	public abstract void highlightElementById(String id);

	/**
	 * Highlight elements that have specified attribute/value set
	 * 
	 * @param name  target attribute name
	 * @param value target attribute value
	 */
	public abstract void hightlightElementByAttribute(String name, String value);

	/**
	 * Clear highlight
	 */
	public abstract void clearHighlight();

	/*
	 * font size, color, etc.
	 * 
	 */
	/**
	 * Set font size
	 * 
	 * @param fontSize font size
	 * @deprecated
	 * 
	 */
	public abstract void setFontSize(int fontSize);

	/**
	 * Get font size
	 * 
	 * @return font size
	 * @deprecated
	 */
	public abstract int getFontSize();

	/**
	 * Get current style information of the browser
	 * 
	 * @return current style information
	 * @see IWebBrowserStyleInfo
	 */
	public abstract IWebBrowserStyleInfo getStyleInfo();

	/**
	 * Get native pointer of the browser.
	 * 
	 * @return pointer
	 * @deprecated
	 */
	public long getBrowserAddress();

	/**
	 * Evaluates a script after a specified interval (msec) has elapsed.
	 * 
	 * @param script   target script
	 * @param interval interval (msec)
	 * @return id of this timer
	 */
	public int setTimeout(String script, int interval);

	/**
	 * Cancels setTimeout
	 * 
	 * @param id id of target timer
	 * @return true if succeeded
	 */
	public boolean clearTimeout(int id);

	/**
	 * Evaluates a script each time a specified interval (msec) has elapsed.
	 * 
	 * @param script   target script
	 * @param interval interval (msec)
	 * @return id of this timer
	 */
	public int setInterval(String script, int interval);

	/**
	 * Cancels setInterval
	 * 
	 * @param id id of target timer
	 * @return true if succeeded
	 */
	public boolean clearInterval(int id);

	/**
	 * Get zoom factor of the browser.
	 *
	 * @return zoom factor
	 */
	public double getZoomFactor();

	/**
	 * Set zoom factor of the browser.
	 *
	 * @return zoom factor
	 */
	public void setZoomFactor(double zoomFactor);

	/**
	 * Get StyleSheet information from live DOM
	 * 
	 * @return StyleSheet information
	 * @see IStyleSheets
	 * 
	 */
	public IStyleSheets getStyleSheets();

}
