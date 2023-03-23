/*******************************************************************************
 * Copyright (c) 1998, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Goh KONDOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.dom.sgml;

public interface ISGMLConstants {
    int EOF = -1;
    int MDO = 1;         // <!
    int CDATA = 2; 
    int INCO = 3;      // +(
    int EXCO = 4;      // -(
    int STRING = 5;
    int DQSTR = 6;       // "....."
    int QSTR = 7;        // '.....'
    int ETAGO = 8;       // </
    int LETTER = 9;
    int DIGIT = 10;
    int NAME_CHAR = 11;
    int NUM = 12;
    int COMMENT = 13;   // <!-- .... -->
    int PCDATA = 14;
    int WHITESPACE = 15;
    int PI = 17;
    int MISC = 1000;
    
    int EQ = '=';        // =
    int TAGC = '>';      // >
    int STAGO = '<';     // <
    int DSO = '[';       // [
    int DSC = ']';       // ]
    int PERCENT = '%';   // %
    int LEFTPAR = '(';   // (
    int RIGHTPAR = ')';  // )
    int MINUS = '-';     // -
    int OMITTABLE = 'O'; // O
    int COMMA = ',';     // ,
    int BAR = '|';       // |
    int AMPA = '&';      // &
    int QUESTION = '?';  // ?
    int PLUS = '+';      // +
    int MULTI = '*';     // *
    
    int DEFAULT = 0;    
    int TAG = 1;
    int ETAG = 2;

    /**
     *  Represents org.eclipse.actf.model.dom.sgml.EndTag
     */
    public static final int ENDTAG = -1;
}
