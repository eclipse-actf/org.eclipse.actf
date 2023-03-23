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
package org.eclipse.actf.model.dom.odf.form;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * ODF elements representing form controls should implement this interface.
 */
public interface FormControlElement extends ODFElement {
	/**
	 * Return form:label attribute
	 * 
	 * @return String
	 */		
	public String getAttrFormLabel();

	/**
	 * Return form:tab-index attribute
	 * 
	 * @return String
	 */		
	public String getAttrFormTabIndex();

	/**
	 * Return form:tab-stop attribute
	 * 
	 * @return String
	 */		
	public boolean getAttrFormTabStop();

	/**
	 * Return form:control-implementation attribute
	 * 
	 * @return String
	 */		
	public String getAttrFormControlImplementation();
}