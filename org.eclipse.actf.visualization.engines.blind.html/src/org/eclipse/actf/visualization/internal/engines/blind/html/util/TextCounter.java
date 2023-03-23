/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.util.StringTokenizer;

import org.eclipse.actf.visualization.engines.blind.ParamBlind;



public class TextCounter {

    private int lang;

    /**
     *  
     */
    public TextCounter(int lang) {
        this.lang = lang;
    }

    public int getWordCount(String str) {
        if (str == null) {
            return 0;
        }
        
        switch (lang) {
        case ParamBlind.JP: //japanese
            //TODO enhance
            return str.length();
        default: //english
            StringTokenizer st = new StringTokenizer(str, " \t\n\r\f,.[]():/\""); //$NON-NLS-1$
            return st.countTokens();
        }

    }

}
