/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hisashi MIYASHITA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.xpath;

import java.lang.reflect.Method;

/**
 * Factory class to obtain XPathService utility.
 */
public abstract class XPathServiceFactory {
	@SuppressWarnings("unchecked")
	private static XPathServiceFactory enable(String name) {
		try {
			Class clazz = Class.forName(name);
			Method newInstanceMethod = clazz.getMethod("newInstance"); //$NON-NLS-1$
			Object instance = newInstanceMethod.invoke(null);
			if (instance instanceof XPathServiceFactory) {
				return (XPathServiceFactory) instance;
			}
		} catch (Exception e) {
		}
		return null;
	}

	static {
		setFactory(enable("org.eclipse.actf.util.xpath.jaxp.XPathServiceFactoryImpl")); //$NON-NLS-1$
		setFactory(enable("org.eclipse.actf.util.jxpath.XPathServiceFactoryImpl")); //$NON-NLS-1$
	}
	private static XPathServiceFactory factory;

	/**
	 * Set XPathServiceFactory
	 * 
	 * @param f
	 *            the target XPathServiceFactory
	 */
	public static void setFactory(XPathServiceFactory f) {
		if (f != null) {
			factory = f;
		}
	}

	/**
	 * Returns XPathService instance
	 * 
	 * @return XPathService instance
	 */
	public static XPathService newService() {
		return factory.getService();
	}

	protected abstract XPathService getService();
}
