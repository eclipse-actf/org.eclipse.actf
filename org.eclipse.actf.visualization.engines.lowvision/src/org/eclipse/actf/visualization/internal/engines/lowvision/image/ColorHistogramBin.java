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

package org.eclipse.actf.visualization.internal.engines.lowvision.image;

public class ColorHistogramBin {
	int color;
	int occurrence = 0;

	ColorHistogramBin( int _c ){
		color = _c;
	}

	int getR(){
		return( color>>16 & 0xff );
	}
	int getG(){
		return( color>>8 & 0xff );
	}
	int getB(){
		return( color & 0xff );
	}

	
}
