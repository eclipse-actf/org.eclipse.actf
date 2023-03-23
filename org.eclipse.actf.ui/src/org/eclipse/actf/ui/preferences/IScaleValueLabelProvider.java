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

package org.eclipse.actf.ui.preferences;

import org.eclipse.swt.widgets.Scale;

/**
 * The interface to return the text for current Scale value. The text will be
 * shown as a value in the ScaleFieldEditorWithValue.
 * 
 * @see ScaleFieldEditorWithValue
 * @see Scale
 * 
 */
public interface IScaleValueLabelProvider {
	/**
	 * Return the text for current Scale value.
	 * 
	 * @param scale
	 * @return the text for current Scale value to be shown in the
	 *         ScaleFieldEditorWithValue.
	 */
	public String getScaleValueText(Scale scale);
}
