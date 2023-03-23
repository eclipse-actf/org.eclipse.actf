/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision;

/*
 * to handle byte sequence in DOS
 */
public class DosUtil{
    public static int upsideDownInt( int _i ){
	return(
	       ((_i>>24)&0x000000ff) |
	       ((_i>>8)&0x0000ff00) |
	       ((_i<<8)&0x00ff0000) |
	       ((_i<<24)&0xff000000)
	       );
    }

    public static short upsideDownShort( short _s ){
	return(
	       (short)
	       (((_s>>8)&0x00ff) |
		((_s<<8)&0xff00))
	       );
    }
}
