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
package org.eclipse.actf.visualization.internal.engines.lowvision.color;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;

public class ColorSpaceSRGB{
    ICC_ColorSpace CS = (ICC_ColorSpace)(ColorSpace.getInstance( ColorSpace.CS_sRGB ));
    float[] fromCIEXYZ( float[] _xyz ){
		return( CS.fromCIEXYZ( _xyz ) );
    }
    float[] toCIEXYZ( float[] _rgb ){
		return( CS.toCIEXYZ( _rgb ) );
    }
}
