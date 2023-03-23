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
package org.eclipse.actf.model.dom.odf.dr3d;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for &lt;dr3d:light&gt; element.
 */
public interface LightElement extends ODFElement {
	/**
	 * Return dr3d:diffuse-color attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dDiffuseColor();

	/**
	 * Return dr3d:direction attribute
	 * 
	 * @return String
	 */		
	public String getAttrDr3dDirection();

	/**
	 * Return dr3d:enabled attribute
	 * 
	 * @return String
	 */		
	public boolean getAttrDr3dEnabled();

	/**
	 * Return dr3d:specular attribute
	 * 
	 * @return String
	 */		
	public boolean getAttrDr3dSpecular();
}