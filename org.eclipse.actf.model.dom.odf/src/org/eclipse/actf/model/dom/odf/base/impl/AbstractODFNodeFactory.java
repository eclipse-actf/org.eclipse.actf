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
package org.eclipse.actf.model.dom.odf.base.impl;

import java.lang.reflect.Constructor;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.w3c.dom.Element;


public abstract class AbstractODFNodeFactory {

	@SuppressWarnings({ "unchecked", "cast" })
	protected static Constructor<? extends ODFElement> findElementConstractor(Class<? extends ODFElement> cs) {
		Constructor<? extends ODFElement>[] constructors = (Constructor<? extends ODFElement>[]) cs.getDeclaredConstructors();
		for (int i = 0; i < constructors.length; i++) {
			Class<?>[] parms = constructors[i].getParameterTypes();
			if (parms.length == 2 && parms[0].equals(ODFDocument.class)
					&& parms[1].equals(Element.class)) {
				return constructors[i];
			}
		}
		return null;
	}

	public static Element createElement(ODFDocument odfDoc, String tagName,
			Element element) {
		return null;
	}
}
