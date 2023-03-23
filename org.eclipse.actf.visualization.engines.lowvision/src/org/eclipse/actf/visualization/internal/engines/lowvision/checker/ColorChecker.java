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

package org.eclipse.actf.visualization.internal.engines.lowvision.checker;

import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;

public abstract class ColorChecker {
	ColorIRGB color1 = null;
	ColorIRGB color2 = null;

	public ColorChecker( ColorIRGB _c1, ColorIRGB _c2 ){
		color1 = _c1;
		color2 = _c2;
	}
	public ColorChecker( int _i1, int _i2 ){
		color1 = new ColorIRGB( _i1 );
		color2 = new ColorIRGB( _i2 );
	}
	//[0.0, 1.0]
	public abstract double calcSeverity();
	
	public abstract double calcContrast();
}
