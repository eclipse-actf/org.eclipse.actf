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
package org.eclipse.actf.model.dom.odf.style;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Interface for &lt;style:master-page&gt; element.
 */
public interface MasterPageElement extends ODFElement {
	public String getAttrStyleName();

	public String getAttrStylePageLayoutName();

	public String getAttrDrawStyleName();
}