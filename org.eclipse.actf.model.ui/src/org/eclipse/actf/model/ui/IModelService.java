/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui;

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Interface to provide access to the model of the content. Users can get this
 * {@link IModelService} through {@link IModelServiceHolder} that is implemented
 * with {@link IEditorPart}.
 * 
 * @see IModelServiceHolder
 */
@SuppressWarnings("nls")
public interface IModelService {

	/**
	 * HTML MIME type
	 */
	public static final String[] MIMETYPES_HTML = { "text/html",
			"application/xhtml+xml" };

	/**
	 * ODF MIME type
	 */
	public static final String[] MIMETYPES_ODF = {
			"application/vnd.oasis.opendocument.presentation",
			"application/vnd.oasis.opendocument.text",
			"application/vnd.oasis.opendocument.spreadsheet" };

	/**
	 * HTML extensions
	 */
	public static final String[] EXTS_HTML = { "html", "htm", "xhtml", "mht" };

	/**
	 * ODF extensions
	 */
	public static final String[] EXTS_ODF = { "odt", "ods", "odp" };

	/**
	 * 
	 */
	public static final String ATTR_WINDOWHANDLE = "WindowHandle";

	/**
	 * Get MIME types that are supported by this {@link IModelService}
	 * 
	 * @return array of MIME type
	 */
	String[] getSupportMIMETypes();

	// TODO use editor extension info
	/**
	 * Get file extensions that are supported by this {@link IModelService}
	 * 
	 * @return array of file extensions
	 */
	String[] getSupportExtensions();

	/**
	 * Get MIME type of the content
	 * 
	 * @return MIME type of the content
	 */
	String getCurrentMIMEType();

	/**
	 * Open specified URL
	 * 
	 * @param url
	 *            target URL
	 */
	void open(String url);

	/**
	 * Open specified {@link File}
	 * 
	 * @param target
	 *            target {@link File}
	 */
	void open(File target);

	/**
	 * Get content URL as String
	 * 
	 * @return content URL as String
	 */
	String getURL();

	/**
	 * Get title
	 * 
	 * @return title
	 */
	String getTitle();

	/**
	 * Get ID
	 * 
	 * @return ID
	 */
	String getID();

	// TODO support other model
	/**
	 * Get model of the content as {@link Document}. This method returns
	 * {@link Document} based on the original source of the content.
	 * 
	 * @return {@link Document}
	 */
	Document getDocument();

	/**
	 * Get model of the content as {@link Document}. This method returns
	 * runtime {@link Document} inside the Editor.
	 * 
	 * @return current {@link Document}
	 */
	Document getLiveDocument();

	/**
	 * Get the {@link Composite} that hold the content
	 * 
	 * @return target {@link Composite}
	 */
	Composite getTargetComposite();

	/**
	 * Save the original {@link Document} into target file
	 * 
	 * @param file
	 *            target file path
	 * @return resulting {@link File}
	 */
	File saveOriginalDocument(String file);

	/**
	 * Convert the {@link Document} into HTML and save into target file. Empty
	 * file will be generated if this method is not supported by the
	 * implementation.
	 * 
	 * @param file
	 *            target file path
	 * @return resulting {@link File}.
	 */
	File saveDocumentAsHTMLFile(String file);

	/**
	 * Move current position to the specified {@link Node}
	 * 
	 * @param target
	 *            target {@link Node}
	 */
	void jumpToNode(Node target);

	/**
	 * Get {@link IModelServiceScrollManager} to scroll the content
	 * 
	 * @return scroll manager {@link IModelServiceScrollManager}
	 */
	IModelServiceScrollManager getScrollManager();

	/**
	 * Get all image position information within the content
	 * 
	 * @return array of {@link ImagePositionInfo}
	 */
	ImagePositionInfo[] getAllImagePosition();

	/**
	 * Get {@link IModelServiceHolder} who owns this implementation
	 * 
	 * @return {@link IModelServiceHolder}
	 */
	IModelServiceHolder getModelServiceHolder();

	/**
	 * Get corresponding {@link Object} with the specified key. This method is
	 * used for implementation unique customization.
	 * 
	 * @param key
	 *            target key
	 * @return corresponding {@link Object} with the specified key
	 */
	Object getAttribute(String key);
	// clearhighlight
	// size

}
