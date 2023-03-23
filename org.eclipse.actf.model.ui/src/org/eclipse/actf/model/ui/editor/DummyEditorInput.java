/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui.editor;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Dummy Editor Input for Editors of ACTF {@link IModelService} implementation.
 * This class is used for open external resources, such as Web site, etc.
 * 
 * @see IModelService
 */
public class DummyEditorInput implements IEditorInput {

	private String name = ""; //$NON-NLS-1$

	private String url = ""; //$NON-NLS-1$

	/**
	 * 
	 * @param url
	 *            target content URL
	 * @param name
	 *            the name of this Editor input
	 */
	public DummyEditorInput(String url, String name) {
		setUrl(url);
		if (name != null) {
			this.name = name;
		}

	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return ""; //$NON-NLS-1$
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * Get target content URL as String. ACTF {@link IModelService}
	 * implementations will open the URL.
	 * 
	 * @return target content URL as String
	 */
	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		if (url != null) {
			this.url = url;
		}
	}

}
