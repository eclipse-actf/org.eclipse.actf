/*******************************************************************************
 * Copyright (c) 2011, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.dombyjs;

/**
 * Interface to access rule information of styleSheet.
 */
public interface IRule {

	/**
	 * @return <code>String</code> that identifies which elements the
	 *         corresponding style rule applies to.
	 */
	String getSelectorText();

	/**
	 * @return {@link IStyle} of the rule.
	 */
	IStyle getStyle();
}
