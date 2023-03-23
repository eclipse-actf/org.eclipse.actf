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

import org.eclipse.ui.IEditorPart;

/**
 * This interface is designed to implement together with {@link IEditorPart}.
 * Implementations are also need to implement {@link IModelService} to access to
 * model of the content within Editor.
 * 
 * @see IEditorPart
 * @see IModelService
 */
public interface IModelServiceHolder {
	/**
	 * Get {@link IModelService} to access to model of the content
	 * 
	 * @return {@link IModelService}
	 */
	IModelService getModelService();

	/**
	 * Get {@link IEditorPart} implemented together with this interface
	 * 
	 * @return {@link IEditorPart}
	 */
	IEditorPart getEditorPart();

	/**
	 * Set title to {@link IEditorPart} implemented together with this interface
	 * 
	 * @param title
	 *            target title
	 */
	void setEditorTitle(String title);
}
