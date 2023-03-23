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

package org.eclipse.actf.util.xpath.jaxp;

import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;



public class XPathServiceFactoryImpl extends XPathServiceFactory {
    private static XPathService xpathServiceInstance;

    @Override
    protected XPathService getService() {
        return xpathServiceInstance;
    }

    public static XPathServiceFactory newInstance() {
        if (xpathServiceInstance == null) {
            xpathServiceInstance = XPathServiceImpl.newInstance();
            if (xpathServiceInstance == null) return null;
        }
        return new XPathServiceFactoryImpl();
    }
    
    private XPathServiceFactoryImpl() {
        
    }
}
