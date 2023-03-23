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
package org.eclipse.actf.model.dom.odf.draw;

import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.form.FixedTextElement;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;

/**
 * Interface for &lt;draw:control&gt; element.
 */
public interface ControlElement extends DrawingObjectElement {
	/**
	 * Return draw:control attribute
	 * 
	 * @return String
	 */	
	public String getAttrDrawControl();

	/**
	 * Return form control element specified by draw:control attribute
	 * 
	 * @return FormControlElement
	 */	
	public FormControlElement getFormControlElement();

	/**
	 * Return &lt;form:fixed-text&gt; element bound as label by form:for attribute
	 * 
	 * @return FixedTextElement
	 */	
	public FixedTextElement getFormLabelFixedTextElement();
}