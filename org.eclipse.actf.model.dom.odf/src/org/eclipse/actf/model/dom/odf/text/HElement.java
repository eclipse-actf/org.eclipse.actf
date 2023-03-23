/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.text;

import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.content.IStylable;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;

/**
 * Interface for &lt;text:h&gt; element.
 */
public interface HElement extends ODFElement, IStylable, IEditable,
		ITextElementContainer {
	public int getAttrTextOutlineLevel();

	public boolean getAttrTextRestartNumbering();

	public int getAttrTextStartValue();

	public boolean getAttrTextIsListHeader();
}